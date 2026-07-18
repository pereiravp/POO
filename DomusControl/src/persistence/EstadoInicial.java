package persistence;

import java.util.HashMap;
import java.util.Map;

import dispositivos.ArCondicionado;
import dispositivos.Lampada;
import dispositivos.RoboAspirador;
import model.Casa;
import model.Divisao;
import model.Utilizador;

/**
 * Cria um estado de teste com que dá para experimentar o programa sem ter de
 * introduzir tudo à mão. É usado no primeiro arranque, quando ainda não há
 * dados gravados em ficheiro.
 */
public class EstadoInicial {

    /** Preenche os mapas recebidos com utilizadores e casas de exemplo. */
    public static void gerar(Map<String, Utilizador> utilizadores, Map<String, Casa> casas) {
        Utilizador prof = new Utilizador("Professor", "admin@uminho.pt", "poo2026",
                "Melhor UC?", "POO", new HashMap<>());
        Utilizador aluno = new Utilizador("Aluno", "aluno@domus.pt", "1234",
                "Cor favorita?", "Azul", new HashMap<>());

        utilizadores.put(prof.getEmail(), prof);
        utilizadores.put(aluno.getEmail(), aluno);

        Casa casaProf = new Casa("Apartamento de Gualtar", "Rua da Universidade, Braga", prof.getId());
        casaProf.darAcesso(aluno.getId());

        Divisao sala = new Divisao("Sala de Estar", casaProf.getIdCasa());
        Divisao cozinha = new Divisao("Cozinha", casaProf.getIdCasa());

        ArCondicionado ac = new ArCondicionado("Daikin", "Serie 1", 1800.0);
        ac.ligar();
        ac.setModo("Frio");

        Lampada luz = new Lampada("Philips", "Serie 6", 12.0);
        luz.ligar();
        luz.setLuminosidade(100);

        RoboAspirador robo = new RoboAspirador("Kobold", "Serie 4", 50.0);
        robo.ligar();

        sala.addDispositivo(ac);
        sala.addDispositivo(luz);
        cozinha.addDispositivo(robo);

        casaProf.adicionarDivisao(sala);
        casaProf.adicionarDivisao(cozinha);

        casas.put(casaProf.getIdCasa(), casaProf);
    }
}
