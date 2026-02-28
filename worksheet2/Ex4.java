import java.util.Arrays; 

public class Ex4 {
    
    //ex4(a)
    public String[] removerRepeticoes(String[] arrayOriginal) {
        String[] unicas = new String[arrayOriginal.length];
        int contagem = 0;

        for (int i = 0; i < arrayOriginal.length; i++) {
            boolean jaExiste = false;
            for (int j = 0; j < contagem; j++) {
                if (arrayOriginal[i].equals(unicas[j])) {
                    jaExiste = true;
                    break;
                }
            }
            if (jaExiste == false) {
                unicas[contagem] = arrayOriginal[i];
                contagem++;
            }
        }
        return Arrays.copyOf(unicas, contagem);
    }

    //ex4(b)
    public String[] maioresStrings(String[] arrayOriginal) {
        String[] maiores = new String[arrayOriginal.length];
        int tamanhoMaximo = 0;
        int contagem = 0;

        for (int i = 0; i < arrayOriginal.length; i++) {
            int tamanhoAtual = arrayOriginal[i].length();

            if (tamanhoAtual > tamanhoMaximo) {
                tamanhoMaximo = tamanhoAtual;
                contagem = 0;
                maiores[contagem] = arrayOriginal[i];
                contagem++;
            } 
            else if (tamanhoAtual == tamanhoMaximo) {
                boolean jaExiste = false;
                for (int j = 0; j < contagem; j++) {
                    if (arrayOriginal[i].equals(maiores[j])) {
                        jaExiste = true; break;
                    }
                }
                if (jaExiste == false) {
                    maiores[contagem] = arrayOriginal[i];
                    contagem++;
                }
            }
        }
        return Arrays.copyOf(maiores, contagem);
    }

    //ex4(c)
    public String[] stringsRepetidas(String[] arrayOriginal) {
        String[] repetidas = new String[arrayOriginal.length];
        int contagemRepetidas = 0;

        for (int i = 0; i < arrayOriginal.length; i++) {
            int ocorrencias = 0;
            for (int j = 0; j < arrayOriginal.length; j++) {
                if (arrayOriginal[i].equals(arrayOriginal[j])) {
                    ocorrencias++;
                }
            }

            if (ocorrencias > 1) {
                boolean jaGuardada = false;
                for (int k = 0; k < contagemRepetidas; k++) {
                    if (arrayOriginal[i].equals(repetidas[k])) {
                        jaGuardada = true;
                        break;
                    }
                }
                if (jaGuardada == false) {
                    repetidas[contagemRepetidas] = arrayOriginal[i];
                    contagemRepetidas++;
                }
            }
        }
        return Arrays.copyOf(repetidas, contagemRepetidas);
    }

    //ex4(d)
    public int contarOcorrencias(String[] arrayOriginal, String palavraAlvo) {
        int contador = 0;
        for (int i = 0; i < arrayOriginal.length; i++) {
            if (arrayOriginal[i].equals(palavraAlvo)) {
                contador++;
            }
        }
        return contador; 
    }
}