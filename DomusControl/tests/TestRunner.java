import java.io.File;
import java.util.Map;

import dispositivos.*;
import logic.Agendamento;
import model.*;
import persistence.GestorDados;

/**
 * Testes do DomusControl sem framework externo - só Java puro, para não
 * obrigar a instalar nada. Cada teste imprime "ok" ou "FALHOU" e no fim há um
 * resumo. Corre com o script tests/run_tests.sh.
 *
 * O foco é o que o refactor tinha de garantir: os métodos canónicos (equals,
 * hashCode, clone, construtor de cópia), a cópia profunda das estruturas, o
 * polimorfismo do consumo e a persistência em ficheiro.
 */
public class TestRunner {

    static int passou = 0;
    static int falhou = 0;

    static void check(String nome, boolean condicao) {
        if (condicao) {
            System.out.println("  ok   " + nome);
            passou++;
        } else {
            System.out.println("  FALHOU " + nome);
            falhou++;
        }
    }

    public static void main(String[] args) {
        System.out.println("Dispositivos - canónicos");
        testDispositivoEqualsHashCode();
        testDispositivoCloneIndependente();
        testCloneMantemTipo();
        testConsumoPolimorfico();

        System.out.println("Utilizador");
        testUtilizadorCloneCopiaId();
        testUtilizadorEquals();

        System.out.println("Divisão e Casa - cópia profunda");
        testDivisaoCopiaProfunda();
        testCasaCopiaProfunda();

        System.out.println("Automação e Agendamento");
        testAutomacaoClone();
        testAgendamentoEquals();

        System.out.println("Fornecedores");
        testFaturas();

        System.out.println("Persistência");
        testGuardarCarregar();

        System.out.println();
        System.out.println(passou + " passou, " + falhou + " falhou");
        if (falhou > 0) System.exit(1);
    }

    // --- dispositivos ---------------------------------------------------

    static void testDispositivoEqualsHashCode() {
        Lampada a = new Lampada("Philips", "L1", 10);
        Lampada copia = a.clone();
        // mesmo id -> iguais e mesmo hashCode
        check("clone é igual ao original", a.equals(copia));
        check("hashCode coerente com equals", a.hashCode() == copia.hashCode());

        Lampada outra = new Lampada("Philips", "L1", 10);
        check("dispositivos diferentes têm ids diferentes", !a.equals(outra));
    }

    static void testDispositivoCloneIndependente() {
        Coluna original = new Coluna("Bose", "C1", 30);
        original.selecionarVolume(50);
        Coluna copia = original.clone();
        copia.selecionarVolume(90);
        // mexer na cópia não altera o original
        check("cópia é independente do original", original.getVolume() == 50);
        check("cópia guardou o seu próprio valor", copia.getVolume() == 90);
    }

    static void testCloneMantemTipo() {
        Dispositivo d = new ArCondicionado("Daikin", "A1", 1800);
        Dispositivo copia = d.clone();
        check("clone de ArCondicionado continua ArCondicionado", copia instanceof ArCondicionado);
    }

    static void testConsumoPolimorfico() {
        // uma lâmpada a 50% gasta metade; desligada gasta zero
        Lampada l = new Lampada("Philips", "L1", 100);
        l.ligar();
        l.setLuminosidade(50);
        check("lâmpada a 50% gasta metade", Math.abs(l.getConsumoInstantaneo() - 50.0) < 0.001);
        l.desligar();
        check("dispositivo desligado gasta zero", l.getConsumoInstantaneo() == 0.0);
    }

    // --- utilizador -----------------------------------------------------

    static void testUtilizadorCloneCopiaId() {
        Utilizador u = new Utilizador("Ana", "ana@mail.pt", "pw", "p", "r", new java.util.HashMap<>());
        Utilizador copia = u.clone();
        // este era o bug: o clone perdia o id
        check("clone do utilizador mantém o id", u.getId().equals(copia.getId()));
    }

    static void testUtilizadorEquals() {
        Utilizador a = new Utilizador("Ana", "ana@mail.pt", "pw", "p", "r", new java.util.HashMap<>());
        Utilizador b = new Utilizador("Ana Maria", "ana@mail.pt", "outra", "p", "r", new java.util.HashMap<>());
        check("utilizadores com o mesmo email são iguais", a.equals(b));
        check("hashCode coerente para o mesmo email", a.hashCode() == b.hashCode());
    }

    // --- cópia profunda -------------------------------------------------

    static void testDivisaoCopiaProfunda() {
        Divisao d = new Divisao("Sala", "CASA1");
        Lampada l = new Lampada("Philips", "L1", 10);
        d.addDispositivo(l);

        Divisao copia = d.clone();
        // o dispositivo dentro da cópia deve ser outro objeto
        Dispositivo original = d.getDispositivos().values().iterator().next();
        Dispositivo clonado = copia.getDispositivos().values().iterator().next();
        check("divisão clonada tem o mesmo número de dispositivos", copia.getNumeroDispositivos() == 1);
        check("dispositivo da cópia é outro objeto", original != clonado);
        check("mas representa o mesmo dispositivo (id igual)", original.equals(clonado));
    }

    static void testCasaCopiaProfunda() {
        Casa casa = new Casa("Minha Casa", "Braga", "DONO1");
        Divisao sala = new Divisao("Sala", casa.getIdCasa());
        sala.addDispositivo(new Lampada("Philips", "L1", 10));
        casa.adicionarDivisao(sala);

        Casa copia = casa.clone();
        check("casa clonada é igual (mesmo id)", casa.equals(copia));

        // alterar a cópia não deve mexer na original
        Divisao salaCopia = copia.getDivisoes().values().iterator().next();
        salaCopia.addDispositivo(new Rele("Xiaomi", "R1", 5));
        Divisao salaOriginal = casa.getDivisoes().values().iterator().next();
        check("mexer na casa copiada não afeta a original",
              salaOriginal.getNumeroDispositivos() == 1);
    }

    // --- automação e agendamento ----------------------------------------

    static void testAutomacaoClone() {
        Automacao a = new Automacao("Sair de Casa");
        a.adicionarComando("D1", false);
        Automacao copia = a.clone();
        check("automação clonada é igual", a.equals(copia));
        copia.adicionarComando("D2", true);
        // a original tinha 1 comando, a cópia agora tem 2
        check("comandos da cópia são independentes", a.getComandos().size() == 1);
    }

    static void testAgendamentoEquals() {
        Agendamento a = new Agendamento("D1", 8, 30, true);
        Agendamento b = new Agendamento("D1", 8, 30, true);
        Agendamento c = new Agendamento("D1", 9, 0, true);
        check("agendamentos iguais são iguais", a.equals(b));
        check("agendamentos diferentes não são iguais", !a.equals(c));
    }

    // --- fornecedores ---------------------------------------------------

    static void testFaturas() {
        // Galp: >100 kWh leva 10% desconto
        Galp galp = new Galp();
        double semDesconto = 50 * 0.18;
        double comDesconto = 200 * 0.18 * 0.90;
        check("Galp cobra preço cheio abaixo de 100 kWh",
              Math.abs(galp.calcularFatura(50) - semDesconto) < 0.001);
        check("Galp aplica desconto acima de 100 kWh",
              Math.abs(galp.calcularFatura(200) - comDesconto) < 0.001);

        // EDP: taxa fixa + preço
        EDP edp = new EDP();
        check("EDP soma taxa fixa",
              Math.abs(edp.calcularFatura(100) - (5.0 + 100 * 0.16)) < 0.001);

        check("fornecedores clonam-se", galp.equals(galp.clone()) && edp.equals(edp.clone()));
    }

    // --- persistência ---------------------------------------------------

    static void testGuardarCarregar() {
        String fUtil = "test_utils.dat";
        String fCasas = "test_casas.dat";
        GestorDados gestor = new GestorDados(fUtil, fCasas);

        java.util.Map<String, Casa> casas = new java.util.HashMap<>();
        Casa casa = new Casa("Casa Teste", "Braga", "DONO1");
        casas.put(casa.getIdCasa(), casa);

        try {
            gestor.guardarCasas(casas);
            Map<String, Casa> lidas = gestor.carregarCasas();
            check("casa gravada é recuperada do ficheiro", lidas.containsKey(casa.getIdCasa()));
            check("casa recuperada é igual à original", lidas.get(casa.getIdCasa()).equals(casa));
        } catch (Exception e) {
            check("gravar/carregar sem excepção", false);
        } finally {
            new File(fUtil).delete();
            new File(fCasas).delete();
        }
    }
}
