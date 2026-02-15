/* Ficha 1: ex3

Escrever um programa que aceite n classificações (números reais) de uma UC,
e indique o número de classificações em cada um dos intervalos: [0, 5[, [5, 10[,
[10, 15[ e [15, 20].

 */

import java.util.Scanner;

public class ex3 {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        //perguntar ao utilizador quantas notas pretender meter
        System.out.print("Quantas notas pretende inserir: ");
        int qn = input.nextInt();
        // criar as variaveis para as contagens de quanta notas vão estar inseridas no intervalo em si
        int intervalo1 = 0;
        int intervalo2 = 0;
        int intervalo3 = 0;
        int intervalo4 = 0;
        for (int i = 0; i < qn; i++){
        System.out.print("Insira a classificação do aluno na UC: ");
        double nota = input.nextDouble();
        // em java não dá para fazer tipo 0 <= nota <= 20 ainda não percebi bem o porque mas fiz com ||
        if (nota < 0 || nota > 20){
            System.out.println("Nota Inválida.");
            }
            else if (nota < 5){
                intervalo1++;
            }
            else if (nota < 10){ // nao vale a pena meter que é <= 5, isto porque já fez a verificação disso na anterior
                intervalo2++; //contagem
            }
            else if (nota < 15){
                intervalo3++;
            }
            else {
                intervalo4++;
            }
        }
        System.out.println("Notas classificadas de [0,5[ : " + intervalo1);
        System.out.println("Notas classificadas de [5,10[ : " + intervalo2);
        System.out.println("Notas classificadas de [10,15[ : " + intervalo3);
        System.out.println("Notas classificadas de [15,20] : " + intervalo4);
    }
    
}

// criar variaveis para fazer contagem de quantas notas aparecem no intervalo
// perguntar ao utilizador quantas notas pretende inserir
// ver se a determinada nota pertence ao intervalo
// e meter opção inválida se o número nem sequer pertencer ao intervalo de [0,20]