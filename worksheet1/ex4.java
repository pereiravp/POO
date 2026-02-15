/* Ficha 1 - ex4

Escrever um programa que aceite n temperaturas inteiras (pelo menos duas) e
determine a média das temperaturas, o dia (2,3, ...) em que se registou a maior
variação em valor absoluto relativamente ao dia anterior e qual o valor efectivo
(positivo ou negativo) dessa variação. Os resultados devem ser apresentados
sob a forma:
A média das _n_ temperaturas foi de ____ graus.
A maior variação registou-se entre os dias __ e __, tendo a temperatura subido/des-
cido4 ___ graus.

*/

import java.util.Scanner;

public class ex4 {

    public static void main (String [] args){
        
        // criação de scanner
        Scanner input = new Scanner(System.in);

        System.out.print("Quantos dias quer para registrar a temperatura: ");
        int dias = input.nextInt();
        
        if (dias < 2){
            System.out.println("Número Inválido. Obrigatório pelo menos 2 seleções.");
        }

        double media = 0; 
        int total = 0;
        
        int valoref = 0;      // preciso de guardar o valor real (-5, +3)
        int maiorvalor = -1;  // e o valor absoluto, que tem de começar a -1 para o primeiro ganhar logo
        
        int diamaior1 = 0;
        int diamaior2 = 0;

        System.out.print("Insira a temperatura do dia 1: ");
        int base = input.nextInt(); // a base vai ser o dia anterior
        int firstday = base;        // preciso do primeiro dia para fora do for, então tenho de regista-lo aqui para a media final

        // o ciclo começa no índice 1 (que corresponde ao dia 2 logo tenho de meter no texto do print i + 1 para aparecer o dia certo)
        for (int i = 1; i < dias ; i++){
            System.out.print("Insira a temperatura do dia " + (i + 1) + ": ");
            int temp = input.nextInt(); // o temp vai ser o dia atual
            
            total = total + temp; // acumulador para contar para a média
            
            int variacaoAtual = temp - base; // conta da variação dos dois dias para termos um número
            int variacaoAbsoluta = Math.abs(variacaoAtual); // transforma -5 em 5 para compararmos a magnitude no if

            // se a variação absoluta for maior vamos estabelecer então um novo valor recorde
            if (variacaoAbsoluta > maiorvalor){
                maiorvalor = variacaoAbsoluta; // registo do recorde
                valoref = variacaoAtual;       // guardamos o valor real
                
                // ajuste dos dias para bater certo com a realidade
                diamaior1 = i;     
                diamaior2 = i + 1; 
            }

            // o dia de atual passa a ser o dia anterior para a próxima volta do ciclo (demorei a perceber ...)
            base = temp; 
        }

        // cálculo da média
        media = (double)(total + firstday) / dias;
        // texto pedido pelo enunciado com os valores inseridos
        System.out.println("A média das " + dias + " temperaturas foi de " + media + " graus.");
        System.out.println("A maior variação registou-se entre os dias " + diamaior1 + " e " + diamaior2 + 
                           ", tendo a temperatura subido/descido " + valoref + " graus.");
    }
}

// muitas variáveis que vão ter de ser criadas (dias,media,total,valoref,maiorvalor,diamaior1,diamaior2,base,firstday,temp,varabsoluta,varatual)
// ciclo for para o utilizador meter as temperaturas que pretender
// compração de variações para ver se uma é maior que a outra
// calculo da média a ter em conta o primeiro dia que vai ter de ficar fora do ciclo for (isto porque o primeiro dia não tem dia anterior)