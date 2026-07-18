package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import dispositivos.ArCondicionado;
import dispositivos.Dispositivo;
import dispositivos.Regador;
import logic.Agendamento;

/**
 * Uma casa e todo o seu estado: divisões e dispositivos, quem lá tem acesso, o
 * fornecedor de energia, a agenda de tarefas, as automações e um relógio
 * virtual que faz o tempo (e o consumo) andar.
 *
 * A casa nunca imprime nada. Tudo o que "acontece" durante a simulação é
 * registado no log de atividades, que a interface pode depois mostrar. Assim a
 * lógica de negócio fica separada da apresentação.
 */
public class Casa implements Serializable {

    private String idCasa;
    private String nomeCasa;
    private String morada;
    private String idDono;
    private List<String> coAdmins;

    // estado do sistema e sensores
    private int tempoVirtualMinutos;
    private double energiaTotalConsumidaWh;
    private boolean estaAChover;

    // coleções de dados
    private List<Agendamento> agenda;
    private List<String> logAtividades;
    private List<String> autorizados;
    private Map<String, Divisao> divisoes;
    private Map<String, Dispositivo> dispositivosCasa;
    private FornecedorEnergia fornecedor;
    private Map<String, Automacao> automacoesCriadas;

    public Casa(String nomeCasa, String morada, String idDono) {
        this.idCasa = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        this.nomeCasa = nomeCasa;
        this.morada = morada;
        this.idDono = idDono;

        this.divisoes = new HashMap<>();
        this.dispositivosCasa = new HashMap<>();
        this.autorizados = new ArrayList<>();
        this.agenda = new ArrayList<>();
        this.logAtividades = new ArrayList<>();
        this.automacoesCriadas = new HashMap<>();
        this.coAdmins = new ArrayList<>();

        this.energiaTotalConsumidaWh = 0.0;
        this.tempoVirtualMinutos = 0;
        this.estaAChover = false;
        this.fornecedor = new EDP();
    }

    /**
     * Construtor de cópia com cópia profunda. As divisões (e por arrasto os
     * dispositivos), as automações e o fornecedor são todos clonados, para que
     * a casa copiada seja completamente independente da original.
     */
    public Casa(Casa outra) {
        this.idCasa = outra.idCasa;
        this.nomeCasa = outra.nomeCasa;
        this.morada = outra.morada;
        this.idDono = outra.idDono;
        this.tempoVirtualMinutos = outra.tempoVirtualMinutos;
        this.energiaTotalConsumidaWh = outra.energiaTotalConsumidaWh;
        this.estaAChover = outra.estaAChover;

        this.coAdmins = new ArrayList<>(outra.coAdmins);
        this.autorizados = new ArrayList<>(outra.autorizados);
        this.logAtividades = new ArrayList<>(outra.logAtividades);

        // Agendamento é imutável na prática, mas clonamos à mesma para ser coerente
        this.agenda = new ArrayList<>();
        for (Agendamento a : outra.agenda) {
            this.agenda.add(a.clone());
        }

        this.divisoes = new HashMap<>();
        for (Map.Entry<String, Divisao> e : outra.divisoes.entrySet()) {
            this.divisoes.put(e.getKey(), e.getValue().clone());
        }

        this.automacoesCriadas = new HashMap<>();
        for (Map.Entry<String, Automacao> e : outra.automacoesCriadas.entrySet()) {
            this.automacoesCriadas.put(e.getKey(), e.getValue().clone());
        }

        // mapa auxiliar: reaponta para os dispositivos já clonados nas divisões
        this.dispositivosCasa = new HashMap<>();
        for (Divisao div : this.divisoes.values()) {
            for (Dispositivo d : div.getDispositivos().values()) {
                this.dispositivosCasa.put(d.getIdentificador(), d);
            }
        }

        this.fornecedor = outra.fornecedor.clone();
    }

    // --- permissões ------------------------------------------------------

    public boolean temPermissaoAdministrativa(String idUtilizador) {
        return this.idDono.equals(idUtilizador) || this.coAdmins.contains(idUtilizador);
    }

    public List<String> getCoAdmins() {
        return new ArrayList<>(coAdmins);
    }

    public void adicionarCoAdmin(String id) {
        if (!coAdmins.contains(id)) coAdmins.add(id);
    }

    public void removerCoAdmin(String id) {
        coAdmins.remove(id);
    }

    // --- motor de tempo --------------------------------------------------

    /**
     * Avança o relógio virtual minuto a minuto. Em cada minuto processa a
     * agenda, aplica as regras inteligentes, soma o consumo e, de vez em
     * quando, muda o tempo. Devolve a energia gasta neste salto, em Wh.
     */
    public double avancarTempo(int minutosParaAvancar) {
        double consumoDesteSalto = 0.0;
        int tempoObjetivo = this.tempoVirtualMinutos + minutosParaAvancar;

        while (this.tempoVirtualMinutos < tempoObjetivo) {
            this.tempoVirtualMinutos++;

            int horaAtual = (this.tempoVirtualMinutos / 60) % 24;
            int minutoAtual = this.tempoVirtualMinutos % 60;

            processarAgenda(horaAtual, minutoAtual);
            verificarRegrasInteligentes();

            double gastoDesteMinuto = getConsumoTotalInstantaneo() / 60.0;
            this.energiaTotalConsumidaWh += gastoDesteMinuto;
            consumoDesteSalto += gastoDesteMinuto;

            if (Math.random() < 0.01) {
                this.estaAChover = !this.estaAChover;
            }
        }
        return consumoDesteSalto;
    }

    private void processarAgenda(int horaAtual, int minutoAtual) {
        for (Agendamento tarefa : this.agenda) {
            if (tarefa.getHora() == horaAtual && tarefa.getMinuto() == minutoAtual) {
                Dispositivo d = procurarDispositivoPorId(tarefa.getIdDispositivo());
                if (d != null) {
                    if (tarefa.deveLigar()) d.ligar();
                    else d.desligar();
                    adicionarLog("agendamento: [" + d.getIdentificador() + "] executado com sucesso.");
                }
            }
        }
    }

    private void verificarRegrasInteligentes() {
        // regadores com sensor de chuva desligam-se quando chove
        for (Divisao div : this.divisoes.values()) {
            for (Dispositivo d : div.getDispositivos().values()) {
                if (d instanceof Regador) {
                    Regador r = (Regador) d;
                    if (r.getModoSensorChuva() && r.isEstado() && this.estaAChover) {
                        r.desligar();
                        adicionarLog("automacao: regador [" + r.getIdentificador() + "] desligado por chuva.");
                    }
                }
            }
        }

        // proteção contra sobrecarga: acima de 10 kW desliga os ares condicionados
        if (getConsumoTotalInstantaneo() > 10000) {
            for (Divisao div : this.divisoes.values()) {
                for (Dispositivo d : div.getDispositivos().values()) {
                    if (d instanceof ArCondicionado && d.isEstado()) {
                        d.desligar();
                        adicionarLog("seguranca: ar condicionado [" + d.getIdentificador() + "] desligado por consumo excessivo.");
                    }
                }
            }
        }
    }

    // --- automações ------------------------------------------------------

    public void adicionarAutomacao(Automacao a) {
        this.automacoesCriadas.put(a.getNome().toUpperCase(), a);
        adicionarLog("sistema: nova automacao '" + a.getNome() + "' adicionada.");
    }

    public void executarAutomacao(String nomeAutomacao) {
        Automacao a = this.automacoesCriadas.get(nomeAutomacao.toUpperCase());
        if (a != null) {
            adicionarLog("sistema: a iniciar automacao '" + a.getNome() + "'.");
            for (Map.Entry<String, Boolean> comando : a.getComandos().entrySet()) {
                Dispositivo d = procurarDispositivoPorId(comando.getKey());
                if (d != null) {
                    if (comando.getValue()) d.ligar();
                    else d.desligar();
                }
            }
        }
    }

    public void removerDispositivoDasAutomacoes(String idAparelho) {
        for (Automacao a : this.automacoesCriadas.values()) {
            if (a.getComandos().containsKey(idAparelho)) {
                a.getComandos().remove(idAparelho);
                adicionarLog("Sistema: Aparelho [" + idAparelho + "] removido da automação " + a.getNome());
            }
        }
    }

    public void removerAutomacao(String nome) {
        this.automacoesCriadas.remove(nome.toUpperCase());
        adicionarLog("Sistema: Automação '" + nome + "' foi eliminada.");
    }

    // --- logs ------------------------------------------------------------

    public void adicionarLog(String mensagem) {
        int dia = (this.tempoVirtualMinutos / (24 * 60)) + 1;
        int hora = (this.tempoVirtualMinutos / 60) % 24;
        int minuto = this.tempoVirtualMinutos % 60;

        String timestamp = "[Dia " + dia + " - " + String.format("%02d:%02d", hora, minuto) + "] ";
        this.logAtividades.add(timestamp + mensagem);
    }

    // --- energia e faturas ----------------------------------------------

    public void setFornecedor(FornecedorEnergia novoFornecedor) {
        this.fornecedor = novoFornecedor;
        adicionarLog("energia: fornecedor alterado para " + novoFornecedor.getNomeFornecedor());
    }

    public double emitirFaturaEletricidade() {
        double consumoKWh = this.energiaTotalConsumidaWh / 1000.0;
        return this.fornecedor.calcularFatura(consumoKWh);
    }

    public void pagarFatura() {
        this.energiaTotalConsumidaWh = 0;
        adicionarLog("Financeiro: Fatura paga e contador de consumo reiniciado.");
    }

    public double getConsumoTotalInstantaneo() {
        double total = 0;
        for (Divisao div : this.divisoes.values()) {
            for (Dispositivo d : div.getDispositivos().values()) {
                total += d.getConsumoInstantaneo();
            }
        }
        return total;
    }

    public double getConsumoTotalCasa() {
        double total = 0.0;
        for (Divisao d : this.divisoes.values()) {
            total += d.getConsumoTotalDivisao();
        }
        return total;
    }

    // --- dispositivos e divisões ----------------------------------------

    public Dispositivo procurarDispositivoPorId(String id) {
        for (Divisao div : this.divisoes.values()) {
            Dispositivo d = div.getDispositivo(id);
            if (d != null) return d;
        }
        return null;
    }

    public void agendarTarefa(String idAparelho, int hora, int minuto, boolean ligar) {
        this.agenda.add(new Agendamento(idAparelho, hora, minuto, ligar));
    }

    public void adicionarDivisao(Divisao d) {
        this.divisoes.put(d.getIdDivisao(), d);
    }

    public void removerDivisao(String idDiv) {
        this.divisoes.remove(idDiv);
    }

    public void darAcesso(String idUtilizador) {
        if (!this.autorizados.contains(idUtilizador)) this.autorizados.add(idUtilizador);
    }

    public void addDispositivo(Dispositivo d) {
        this.dispositivosCasa.put(d.getIdentificador(), d);
    }

    /** Número total de dispositivos instalados nas divisões da casa. */
    public int getNumeroDispositivos() {
        int total = 0;
        for (Divisao div : this.divisoes.values()) {
            total += div.getNumeroDispositivos();
        }
        return total;
    }

    /**
     * Os dispositivos mais usados da casa, ordenados por número de ativações
     * (do maior para o menor). Devolve no máximo `quantos` dispositivos - o
     * enunciado pede os três mais utilizados, mas o método serve para qualquer
     * número.
     */
    public List<Dispositivo> dispositivosMaisUsados(int quantos) {
        List<Dispositivo> todos = new ArrayList<>();
        for (Divisao div : this.divisoes.values()) {
            todos.addAll(div.getDispositivos().values());
        }
        todos.sort(Comparator.comparingInt(Dispositivo::getNumeroAtivacoes).reversed());
        return todos.subList(0, Math.min(quantos, todos.size()));
    }

    // --- getters ---------------------------------------------------------

    public String getIdCasa()                  { return idCasa; }
    public String getNomeCasa()                { return nomeCasa; }
    public String getMorada()                  { return morada; }
    public String getIdDono()                  { return idDono; }
    public int getTempoVirtualMinutos()        { return tempoVirtualMinutos; }
    public double getEnergiaTotalConsumidaWh() { return energiaTotalConsumidaWh; }
    public boolean isEstaAChover()             { return estaAChover; }
    public FornecedorEnergia getFornecedor()   { return fornecedor; }

    public List<String> getLogAtividades()      { return new ArrayList<>(logAtividades); }
    public Map<String, Divisao> getDivisoes()   { return new HashMap<>(divisoes); }
    public Map<String, Automacao> getAutomacoes() { return new HashMap<>(automacoesCriadas); }
    public List<String> getAutorizados()        { return new ArrayList<>(autorizados); }

    // --- métodos canónicos ----------------------------------------------

    @Override
    public Casa clone() {
        return new Casa(this);
    }

    /** Cada casa tem um id único, que é o que a identifica. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Casa that = (Casa) o;
        return Objects.equals(this.idCasa, that.idCasa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCasa);
    }

    @Override
    public String toString() {
        return String.format("Casa: %s (%s) [ID: %s] - %d divisão(ões), %d dispositivo(s)",
                nomeCasa, morada, idCasa, divisoes.size(), getNumeroDispositivos());
    }
}
