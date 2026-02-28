import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String [] args){

        // printEx1();
    
        // printEx2();
        
        // printEx3();

        // printEx4();

        // printEx5();

        // printEx6();

        // printEx7();
    }

// exercicio 1
    private static void printEx1(){
        Ex1 exercicio1 = new Ex1();
        
        // ex1a)
        int[] vector = {4,2,3,1,5};
        int dimensao = vector.length;
        System.out.println("Exercício 1 (a):\n");

        exercicio1.recebeArray(vector, dimensao);
        exercicio1.printNumeros();

        // ex1b)
        System.out.println("\n\nExercício 1 (b): " + exercicio1.minNum());      
        
        //ex1c)
        int imin = 2;
        int imax = 4;
        int [] lista = exercicio1.intervalo(imin, imax);
        System.out.print("\nExercício 1 (c): ");
        for (int i = 0; i < (imax-imin)-1; i++){
                System.out.println(lista[i]);
            }

        //ex1d)
        Scanner input = new Scanner(System.in);

        System.out.print("Quantos digitos pretende por no primeiro array: ");
        int quantDigitos1 = input.nextInt();
        System.out.print("Quantos digitos pretende por no segundo array: ");
        int quantDigitos2 = input.nextInt();
        
        int array1[] = new int [quantDigitos1];
        int array2[] = new int [quantDigitos2];

        for (int i = 0; i < quantDigitos1; i++){
            System.out.print("Que digito quer por no primeiro array: ");
            int n1 = input.nextInt();
            array1 [i] = n1; 
        }
        for (int i = 0; i < quantDigitos2; i++){
            System.out.print("Que digito quer por no segundo array: ");
            int n2 = input.nextInt();
            array2 [i] = n2; 
        } 
        int[] comum = exercicio1.digitoComum(quantDigitos1, quantDigitos2, array1, array2);
        System.out.println("\nExercício 1 (d): ");
        for (int i = 0; i < comum.length; i++){
                System.out.println(comum[i]);
            }
    }



// exercicio 2
    private static void printEx2(){
        Scanner input = new Scanner(System.in);

        Ex2 pauta = new Ex2(5);
        
        //ex2a)

        System.out.println("Quantas datas quer inserir? (Max: 5)");
        int quantidade = input.nextInt();

        for (int i = 0; i < quantidade; i++) {
            System.out.println("--- Inserir Data " + (i + 1) + " ---");
            
            System.out.print("Dia: ");
            int dia = input.nextInt();
            
            System.out.print("Mês: ");
            int mes = input.nextInt();
            
            System.out.print("Ano: ");
            int ano = input.nextInt();

            LocalDate dataFabricada = LocalDate.of(ano, mes, dia);

            pauta.insereData(dataFabricada);
        }

        System.out.println("--- Datas Guardadas ---");
        pauta.mostrarTudo();

        // ex2b)

        System.out.println("\n--- Teste da Data Mais Próxima ---");
        
        LocalDate alvo = LocalDate.of(2025, 12, 25);
        System.out.println("Data Alvo: " + alvo);

        LocalDate resultado = pauta.dataMaisProxima(alvo);

        if (resultado != null) {
            System.out.println("A data mais próxima encontrada foi: " + resultado);
        } else {
            System.out.println("O array estava vazio.");
        }
        input.close();  

        // ex2c)
        String relatorio = pauta.toString();

        System.out.println(relatorio);
    }

// exercicio 3
    private static void printEx3(){
        Ex3 exercicio3 = new Ex3();
        Scanner input = new Scanner(System.in);


        //ex3a)
        System.out.print("Quantos números queres meter na tua lista: ");
        int tamanho = input.nextInt();
        int array[] = new int [tamanho]; 
        
        for (int i = 0; i < tamanho; i++){
        System.out.print("Dá me um número para a lista:");
        int numero3 = input.nextInt(); 
        array[i] = numero3;
        }
        exercicio3.ordena(array, tamanho);
        System.out.println("Array ordenado: " + java.util.Arrays.toString(array));   

        //ex3b)
        System.out.println("Dentro da lista que número pretende encontrar? ");
        int numeroPretendido = input.nextInt();

        System.out.println("A posição do seu número na lista é: " + exercicio3.procuraBinario(array, numeroPretendido, tamanho));
        
    }   

// exercicio 4
    private static void printEx4(){

        Scanner input = new Scanner(System.in);
        Ex4 exercicio4 = new Ex4(); 

        System.out.print("Quantas strings pretende inserir no array: ");
        int tamanho = input.nextInt();
        
        String[] arrayOriginal = new String[tamanho];
        for (int i = 0; i < tamanho; i++) {
            System.out.print("Qual a sua string: ");
            arrayOriginal[i] = input.next();
        }

        System.out.println("\n--- RELATÓRIO FINAL ---");
        System.out.println("Array Original: " + java.util.Arrays.toString(arrayOriginal));
        
        // (a) Sem repetições
        String[] arrayUnicas = exercicio4.removerRepeticoes(arrayOriginal);
        System.out.println("(a) Array sem repetições: " + java.util.Arrays.toString(arrayUnicas));

        // (b) Maiores Strings
        String[] arrayMaiores = exercicio4.maioresStrings(arrayOriginal);
        System.out.println("(b) Maior(es) string(s): " + java.util.Arrays.toString(arrayMaiores));

        // (c) Strings repetidas
        String[] arrayRepetidas = exercicio4.stringsRepetidas(arrayOriginal);
        System.out.println("(c) Strings repetidas: " + java.util.Arrays.toString(arrayRepetidas));

        // (d) Contagem específica
        System.out.print("\nQual a string que pretende contar? ");
        String stringAlvo = input.next();
        
        int ocorrencias = exercicio4.contarOcorrencias(arrayOriginal, stringAlvo);
        System.out.println("(d) A string '" + stringAlvo + "' aparece " + ocorrencias + " vez(es) no array original.");
    }

    // exercicio 5
    private static void printEx5(){

        Scanner input = new Scanner(System.in);
        Ex5 exercicio5 = new Ex5();

        //ex5a)
        int[][] grelhaMain = new int[5][5];
        
        for (int i = 0; i < 5; i++){
            System.out.println("--- A Inserir notas do Aluno " + (i + 1) + " ---");
            
            for (int j = 0; j < 5 ; j++){
                System.out.print("Insira a nota da UC " + (j + 1) + ": ");
                
                grelhaMain[i][j] = input.nextInt();
            }
        }

        // ex5a) 
        exercicio5.recebePauta(grelhaMain);
        System.out.println("\n Pauta enviada e guardada com sucesso!\n");


        // ex5(b)
        System.out.print("(b) Que UC deseja calcular a soma (1 a 5)? ");
        int ucDesejada = input.nextInt() - 1;
        System.out.println("A soma da UC é: " + exercicio5.notasUcs(ucDesejada) + "\n");


        // ex5(c)
        System.out.print("(c) Qual o número do aluno que quer calcular a média (1 a 5)? ");
        int alunoDesejado = input.nextInt() - 1;
        System.out.println("A média desse aluno é de: " + exercicio5.calcularMedia(alunoDesejado) + "\n");


        // ex5(d)
        System.out.print("(d) Quer calcular a média de qual UC (1 a 5)? ");
        int ucMediaDesejada = input.nextInt() - 1;
        System.out.println("A média da UC é: " + exercicio5.calcularMediaUC(ucMediaDesejada) + "\n");

        // ex5(e)
        System.out.println("(e) A nota mais alta é: " +  exercicio5.notaMaisAlta());

        //ex5(f)
        System.out.println("(f) A nota mais baixa é: " + exercicio5.notaMaisBaixa());

        //ex5(g)
        System.out.print("Dizer as notas acima do valor: ");
        int valor = input.nextInt();
        System.out.println("(g) Array com notas acima do valor pedido: " +  java.util.Arrays.toString(exercicio5.notasAcimaValor(valor)));
    
        //ex5(h)
        System.out.println("(h) Pauta Completa da Turma: \n");
        System.out.println(exercicio5.pautaCompleta());

        //ex5(i)
        System.out.println("(i) UC com Media mais Elevada: " +  exercicio5.ucMediaElevado());

    }

    // exercicio 6
    private static void printEx6() {
        
        Scanner input = new Scanner(System.in);
        Ex6 exercicio6 = new Ex6(); 


        //ex6(a)
        System.out.println("\n(a) Vamos criar a primeira matriz (Matriz A):");
        int[][] matrizA = lerArray();
        
        System.out.println("\n(a) Vamos criar a segunda matriz (Matriz B):");
        int[][] matrizB = lerArray();

        System.out.println("\n Matrizes construídas! Aqui estão elas:");
        System.out.println("Matriz A:");
        imprimirMatriz(matrizA);
        System.out.println("Matriz B:");
        imprimirMatriz(matrizB);

        //ex6(b)
        System.out.println("\n(b) Soma das Matrizes (A + B): ");        
        int[][] matrizResultado = exercicio6.somaMatrizes(matrizA, matrizB);
        if (matrizResultado != null) {
            imprimirMatriz(matrizResultado);
        }

        //ex6(c)
        System.out.print("\n(c) As matrizes A e B são iguais? ");
        System.out.print("As matrizes são iguais: " + exercicio6.matrizesIguais(matrizA, matrizB));
        
        //ex6(d)
        System.out.println("\n(d) Matriz Oposta da Matriz A (-A): ");
        
        int[][] matrizComSinaisTrocados = exercicio6.matrizOposta(matrizA);
        
        imprimirMatriz(matrizComSinaisTrocados);
    }



    // ex6(a) - método dentro do main como foi pedido no enunciado
    private static int[][] lerArray() {
        Scanner input = new Scanner(System.in);

        System.out.print("Quantas linhas vai ter a sua matriz: ");
        int linhas = input.nextInt();

        System.out.print("Quantas colunas vai ter a sua matriz: ");
        int colunas = input.nextInt();

        int[][] matriz = new int[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                System.out.print("Digite um número para a posição (" + i + "," + j + "): ");
                matriz[i][j] = input.nextInt();
            }
        }
        return matriz; 
    }

    private static void imprimirMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println(); 
        }
    }

    // exercicio 7:
    private static void printEx7(){

        Ex7 exercicio7 = new Ex7();
        int [] numeros = new int [5];
        int [] estrelas = new int [2];
        Scanner input = new Scanner (System.in);

        for (int i = 0; i < 5; i++){
            System.out.print("Digite um número (1-50): ");
            int numeroEuro = input.nextInt(); 
            if (numeroEuro < 51 && numeroEuro > 0){
                numeros[i] = numeroEuro;
            }
            else{
                System.out.println("Número Inválido, tente outra vez.");
                i--;
            }
        }
        for (int i = 0; i < 2; i++){
            System.out.print("Digite uma estrela (1-9): ");
            int estrelaEuro = input.nextInt();
            if(estrelaEuro < 10 && estrelaEuro > 0){
                estrelas[i] = estrelaEuro;
            }
            else {
                System.out.println("Estrela inválida, tente outra vez.");
                i--;
            } 
        }
        exercicio7.euroMilhoes(estrelas, numeros);
    }
}

