/* Ficha 1: ex1

1. Escrever um programa que, dada um data em dia (1..31), mês (1..12) e ano,
calcule o dia da semana dessa data. O dia da semana de datas entre Março
de 1900 e Fevereiro de 2100 pode ser calculado do seguinte modo:
(a) Calcule o número total de dias entre 01/01/1900 e a data dada usando o
seguinte algoritmo:

i. Subtraia 1900 do ano dado e multiplique por 365
ii. Adicione (ano−1900)/4 (os anos bissextos)
iii. Se o ano dado for ele próprio bissexto, e o mês Janeiro ou Fevereiro,
subtraia um ao resultado anterior.
iv. Adicione os dias já passados no corrente ano (considere 28 dias para
Fevereiro)

(b) Calcule a d  // --- FUNÇÃO AUXILIAR (Contar dias dos meses anteriores) ---ivisão inteira desse número por 7
(c) O resto é o dia da semana: 0 – Domingo .. 6 – Sábado
*/

import java.util.Scanner;

public class ex1 { 

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        // pedir os dados ao utilizador
        System.out.print("Dia: ");
        int dia = input.nextInt();
        
        System.out.print("Mês: ");
        int mes = input.nextInt();
        
        System.out.print("Ano: ");
        int ano = input.nextInt();
        
        // chamar a função
        int resultado = calculaDiaSemana(dia, mes, ano);
        
        // resultado
        System.out.println("O dia da semana é: " + resultado);
        System.out.println("(0=Dom, 1=Seg, 2=Ter, 3=Qua, 4=Qui, 5=Sex, 6=Sab)");
        
        input.close();
    }
    
    public static int calculaDiaSemana(int dia, int mes, int ano) {
        // passo i: dias dos anos completos desde 1900
        int totalDias = (ano - 1900) * 365;
        
        // passo ii: adicionar anos bissextos (divisão inteira por 4)
        totalDias += (ano - 1900) / 4;
        
        // passo iii: se o ano for bissexto e o mês for Jan ou Fev, subtrai 1 (pedido pelo enunciado)
        if ((ano % 4 == 0) && (mes == 1 || mes == 2)) {
            totalDias -= 1;
        }
        
        // passo iv: somar dias deste ano (com Fevereiro a 28)
        totalDias += diasPassadosNoAno(dia, mes);
        
        // o resto de divisão vai ser por 7 (dias da semana)
        return totalDias % 7;
    }

        // função auxiliar
    public static int diasPassadosNoAno(int dia, int mes) {
        int total = 0;
        // array com os dias 
        int[] diasPorMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        
        // somar os meses completos que ficaram para trás
        for (int i = 0; i < mes - 1; i++) {
            total += diasPorMes[i];
        }
        
        // somar os dias do mês atual
        total += dia;
        
        return total;
    }
}

// tenho de pedir ao utilizador dados
// criar funçao auxiliar que some os dias
// criar função auxiliar que cumpra as fórmulas dadas no enunciado para descobrir o dia da semana
// chamar a função que descobre o dia da semana na main para me dar o resultado