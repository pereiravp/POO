import java.util.Arrays;

//ex7:
public class Ex7 {
    
    public void euroMilhoes(int[] estrelasUser, int[] numerosUser){
        
        int[] listaNumerosRandom = new int[5]; 
        int[] listaEstrelasRandom = new int[2];
        
        int numerosCertos = 0;
        int estrelasCertas = 0;
        
        for (int i = 0; i < 5; i++){
            listaNumerosRandom[i] = (int) (Math.random() * 50) + 1;
        }
        for (int i = 0; i < 2; i++){
            listaEstrelasRandom[i] = (int) (Math.random() * 9) + 1;
        }

        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                if (numerosUser[i] == listaNumerosRandom[j]) {
                    numerosCertos++;
                }
            }
        }

        for (int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                if (estrelasUser[i] == listaEstrelasRandom[j]){
                    estrelasCertas++;
                }
            }
        }
        
        System.out.println("A sua aposta: Números " + Arrays.toString(numerosUser) + " | Estrelas " + Arrays.toString(estrelasUser));
        System.out.println("Chave Sorteada: Números " + Arrays.toString(listaNumerosRandom) + " | Estrelas " + Arrays.toString(listaEstrelasRandom));
        System.out.println("Acertou em " + numerosCertos + " números e " + estrelasCertas + " estrelas!\n");
        
        if (numerosCertos == 5 && estrelasCertas == 2) {
            System.out.println("GANHOU O EUROMILHÕES!\n");
            
            String chaveVencedora = Arrays.toString(listaNumerosRandom) + " + " + Arrays.toString(listaEstrelasRandom);
            String espacamento = "";
            
            for (int i = 0; i < 50; i++) {
                System.out.println(espacamento + chaveVencedora);
                espacamento = espacamento + "  "; 
            }
        }
    }
}

