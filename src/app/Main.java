package app;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import model.Casa;
import model.Utilizador;
import persistence.EstadoInicial;
import persistence.GestorDados;
import ui.MenuUI;

/**
 * Ponto de entrada da aplicação. Só coordena: carrega o estado do disco (ou
 * gera um de teste se for o primeiro arranque), entrega o controlo ao menu e,
 * no fim, grava tudo. A leitura e a gravação em si vivem no GestorDados.
 */
public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GestorDados dados = new GestorDados();

        Map<String, Utilizador> utilizadores = dados.carregarUtilizadores();
        Map<String, Casa> casas = dados.carregarCasas();

        boolean sistemaGeradoAgora = false;
        if (utilizadores.isEmpty()) {
            EstadoInicial.gerar(utilizadores, casas);
            sistemaGeradoAgora = true;
        }

        MenuUI.menuInicial(input, utilizadores, casas, sistemaGeradoAgora);

        try {
            dados.guardarUtilizadores(utilizadores);
            dados.guardarCasas(casas);
            System.out.println("Sistema encerrado e dados salvaguardados.");
        } catch (IOException e) {
            System.out.println("Erro ao guardar os dados: " + e.getMessage());
        }

        input.close();
    }
}
