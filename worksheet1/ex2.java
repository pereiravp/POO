/* Ficha 1 - ex2

Escrever um programa que determine a soma de duas datas em dias, horas,
minutos e segundos, utilizando um método auxiliar para o efeito. O método
deverá aceitar as duas datas e devolver uma string no formato “ddD hhH mmM
ssS”. 

*/

import java.util.Scanner;

public class ex2 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // inputs da primeira data
        System.out.println("Data 1");
        System.out.print("Dias: ");     int d1 = input.nextInt();
        System.out.print("Horas: ");    int h1 = input.nextInt();
        System.out.print("Minutos: ");  int m1 = input.nextInt();
        System.out.print("Segundos: "); int s1 = input.nextInt();

        // inputs da segunda data
        System.out.println("\nData 2");
        System.out.print("Dias: ");     int d2 = input.nextInt();
        System.out.print("Horas: ");    int h2 = input.nextInt();
        System.out.print("Minutos: ");  int m2 = input.nextInt();
        System.out.print("Segundos: "); int s2 = input.nextInt();

        // chamamos a função e guardamos o texto que ela devolve
        String resultado = calcularSoma(d1, h1, m1, s1, d2, h2, m2, s2);

        System.out.println("\nResultado da Soma: " + resultado);
        
        input.close();
    }

    // função auxiliar
    public static String calcularSoma(int d1, int h1, int m1, int s1, int d2, int h2, int m2, int s2) {
        
        // conversão para segundos
        int totalD = (d1 + d2) * 86400;
        int totalH = (h1 + h2) * 3600;
        int totalM = (m1 + m2) * 60;
        int totalS = (s1 + s2);
        // fazer a soma total para sabermos o resultado em segundos dos dois dias
        int segundosTotais = totalD + totalH + totalM + totalS;

        // quantos segundos cabem num dia
        int diasFinais = segundosTotais / 86400;
        // precisamos de saber o que sobrou para conseguir fazer as horas e isso simultaneamente até os segundos
        int resto = segundosTotais % 86400;

        int horasFinais = resto / 3600;
        resto = resto % 3600;

        int minutosFinais = resto / 60;
        
        int segundosFinais = resto % 60;

        // resposta dada como o professor pediu: "ddD hhH mmM ssS"
        return diasFinais + "Dias " + horasFinais + "Horas " + minutosFinais + "Minutos " + segundosFinais + "Segundos";
    }

}

// conversão total para segundos 
// soma dos segundos
// conversão inversa com sistema de resto
// resposta em forma como o enunciado pede