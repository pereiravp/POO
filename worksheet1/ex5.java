/* Ficha 1 - ex5

Escrever um programa que leia sucessivas vezes a base e altura de um triân-
gulo retângulo (valores reais) e calcule a área e o perímetro respectivos. Usar
printf() para apresentar os resultados com uma precisão de 5 casas decimais.
O programa apenas deverá terminar com a leitura de uma base = 0.0.

*/


import java.util.Scanner;

public class ex5 {
    public static void main(String [] args){
            
        Scanner input = new Scanner(System.in);

        double base, altura, area, perimetro, hipotenusa;

        System.out.println("Calculadora de Triângulos");
        System.out.println("Sair - (Base = 0)");


        // enquanto o while for true cria um loop infinito que só para quando mandamos (break)
        while (true) {
            System.out.print("Base do seu triângulo: ");
            base = input.nextDouble();

            // se base = 0, o programa vai fechar como foi pedido no enunciado
            if (base == 0.0) {
                break; 
            }

            System.out.print("Altura do triângulo: ");
            altura = input.nextDouble();

            // calcular hipotenusa
            // Math.hypot - comando novo que descobri agora
            // também dava para fazer: Math.sqrt(Math.pow(base, 2) + Math.pow(altura, 2));
            hipotenusa = Math.hypot(base, altura);

            // calcular o perimetro
            perimetro = base + altura + hipotenusa;

            // calcular a área
            area = (base * altura) / 2;

            // output formatado com 5 casas decimais (%.5f) como o enunciado pede
            System.out.printf("Área: %.5f \n", area);
            System.out.printf("Perímetro: %.5f \n", perimetro);
        }
        input.close();
    }
}

// tenho de calcular hipotenusa para saber o perimetro visto que só me dão a base e a altura
// criar um loop com o while com a condição que se b = 0 dá break no ciclo
// nas respostas dar com 5 casas decimais (%.5f)