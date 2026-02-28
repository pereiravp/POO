public class Ex6 {
    
   //ex6(b)
    public int[][] somaMatrizes(int[][] matrizA, int[][] matrizB) {
        
        if (matrizA.length != matrizB.length || matrizA[0].length != matrizB[0].length) {
            System.out.println("As matrizes têm tamanhos diferentes.");
            return null; 
        }
        int linhas = matrizA.length;
        int colunas = matrizA[0].length;
        int[][] matrizSoma = new int[linhas][colunas];

        for (int i = 0; i < linhas; i++) {
            for(int j = 0; j < colunas; j++) {
                matrizSoma[i][j] = matrizA[i][j] + matrizB[i][j];
            }
        }
        return matrizSoma;
    }

    //ex6(c)
    public boolean matrizesIguais(int[][] matrizA, int[][] matrizB) {
        
        if (matrizA.length != matrizB.length || matrizA[0].length != matrizB[0].length) {
            return false;
        }

        int linhas = matrizA.length;
        int colunas = matrizA[0].length;

        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                
                if (matrizA[i][j] != matrizB[i][j]) {
                    return false; 
                }
            }
        }
        return true; 
    }
    
    //ex6d)
    public int[][] matrizOposta(int[][] matrizA) {
        
        int linhas = matrizA.length;
        int colunas = matrizA[0].length;
        
        int[][] oposta = new int[linhas][colunas];

        for(int i = 0; i < linhas; i++) {
            for(int j = 0; j < colunas; j++) {
                oposta[i][j] = -matrizA[i][j]; 
            }
        }
        return oposta;
    }
}
