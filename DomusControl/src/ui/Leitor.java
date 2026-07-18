package ui;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Leitor {

// limpa o terminal de forma nativa para manter a interface organizada
    public static void limparConsola() {
        try {
            // descobre qual é o sistema operativo do utilizador
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("win")) {
                // se for windows, executa o 'cls'
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // se for linux ou mac, executa o 'clear'
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // plano b: usar os codigos ansi caso falhe algo no sistema
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    public static int lerInteiro(Scanner input, String mensagem) {
        int valor = -1;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensagem);
            String texto = input.nextLine();
            try {
                valor = Integer.parseInt(texto);
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Erro: Introduza um número válido.");
            }
        }
        return valor;
    }

    public static double lerDouble(Scanner input, String mensagem) {
        while (true) { 
            System.out.print(mensagem);
            try {
                return Double.parseDouble(input.nextLine()); 
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, introduz apenas números (ex: 22.5).");
            }
        }
    }

    // valida se o email tem um formato real (exemplo@dominio.com)
    public static String lerEmail(Scanner input, String mensagem) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pat = Pattern.compile(emailRegex);
        while (true) {
            System.out.print(mensagem);
            String email = input.nextLine();
            if (pat.matcher(email).matches()) {
                return email;
            }
            System.out.println("Erro: O formato do email é inválido.");
        }
    }

    // tenta ler a password de forma oculta no terminal
    public static String lerPassword(Scanner input, String mensagem) {
        // o system.console() pode ser null se correres dentro de certos ides
        if (System.console() != null) {
            System.out.print(mensagem);
            return new String(System.console().readPassword());
        } else {
            // fallback para scanner normal caso o console falhe
            System.out.print(mensagem + " (Nota: Visível no IDE): ");
            return input.nextLine();
        }
    }
}