package dispositivos;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Base de todos os dispositivos da casa.
 *
 * Guarda o que qualquer aparelho tem em comum - identificador, marca, modelo,
 * consumo e estado - e deixa para as subclasses o comportamento próprio de
 * cada tipo. O consumo instantâneo é o exemplo: aqui devolve-se o consumo base
 * quando ligado, e cada subclasse ajusta-o à sua maneira (a lâmpada pela
 * luminosidade, o ar condicionado pelo modo, etc.).
 */
public abstract class Dispositivo implements Serializable {

    private String identificador;
    private String marca;
    private String modelo;
    private double consumoHora;   // em Wh
    private boolean estado;       // true = ligado
    private int numeroAtivacoes;
    private double tempoLigado;   // em horas

    /** Cria um dispositivo novo, desligado, com um id gerado automaticamente. */
    public Dispositivo(String marca, String modelo, double consumoHora) {
        this.identificador = gerarId();
        this.marca = marca;
        this.modelo = modelo;
        this.consumoHora = consumoHora;
        this.estado = false;
        this.numeroAtivacoes = 0;
        this.tempoLigado = 0.0;
    }

    /**
     * Construtor de cópia. Copia tudo, incluindo o identificador - um clone
     * representa o mesmo aparelho, por isso mantém o mesmo id, o histórico de
     * ativações e o tempo ligado.
     */
    public Dispositivo(Dispositivo outro) {
        this.identificador = outro.identificador;
        this.marca = outro.marca;
        this.modelo = outro.modelo;
        this.consumoHora = outro.consumoHora;
        this.estado = outro.estado;
        this.numeroAtivacoes = outro.numeroAtivacoes;
        this.tempoLigado = outro.tempoLigado;
    }

    private static String gerarId() {
        return UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    // --- comportamento ---------------------------------------------------

    /** Liga o dispositivo e conta mais uma ativação. */
    public void ligar() {
        this.estado = true;
        this.numeroAtivacoes++;
    }

    public void desligar() {
        this.estado = false;
    }

    /**
     * Consumo neste instante, em Wh. Desligado gasta zero; ligado gasta o
     * consumo base. As subclasses reescrevem isto para refletir os seus modos.
     */
    public double getConsumoInstantaneo() {
        return this.estado ? this.consumoHora : 0.0;
    }

    // --- getters e setters ----------------------------------------------

    public String getIdentificador() { return this.identificador; }
    public String getMarca()         { return this.marca; }
    public String getModelo()        { return this.modelo; }
    public double getConsumoHora()   { return this.consumoHora; }
    public boolean isEstado()        { return this.estado; }
    public int getNumeroAtivacoes()  { return this.numeroAtivacoes; }
    public double getTempoLigado()   { return this.tempoLigado; }

    public void setEstado(boolean novoEstado) { this.estado = novoEstado; }
    public void addTempoLigado(double horas)  { this.tempoLigado += horas; }

    // --- métodos canónicos ----------------------------------------------

    /**
     * Cada tipo de dispositivo sabe copiar-se a si próprio. Declarado aqui
     * como abstracto para obrigar as subclasses a devolver o seu próprio tipo,
     * em vez de deixarmos alguém esquecer-se e ficar com uma cópia incompleta.
     */
    @Override
    public abstract Dispositivo clone();

    /**
     * Dois dispositivos são o mesmo se tiverem o mesmo identificador. O id é
     * único e não muda, por isso é a chave natural - comparar marca, estado e
     * companhia seria frágil e nem faria sentido (o mesmo aparelho continua o
     * mesmo depois de ser ligado).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dispositivo that = (Dispositivo) o;
        return Objects.equals(this.identificador, that.identificador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identificador);
    }

    @Override
    public String toString() {
        return String.format("%s [%s] %s %s - %s (%.0f Wh)",
                getClass().getSimpleName(),
                identificador,
                marca,
                modelo,
                estado ? "ligado" : "desligado",
                consumoHora);
    }
}
