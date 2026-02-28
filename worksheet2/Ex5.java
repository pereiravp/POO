import java.util.Arrays;

//ex5
public class Ex5 {

    int[][] notasTurma = new int[5][5];

    //ex5a)
    public void recebePauta(int[][] grelhaMain) {
        notasTurma = grelhaMain; 
    }

    //ex5b)
    public double notasUcs (int ucDesejada){

        int ucSoma = 0;
        for (int i = 0; i < 5; i++){
            ucSoma = ucSoma + notasTurma[i][ucDesejada]; 
        }
        return ucSoma;
    }

    //ex5c)
    public double calcularMedia(int alunoDesejado) {
        int somaAlunoUcs = 0;
        for (int i = 0; i < 5; i++) {
            somaAlunoUcs = somaAlunoUcs + notasTurma[alunoDesejado][i];
        }
        double media = (somaAlunoUcs / 5.0); 
        return media;
    }

    //ex5d)
    public double calcularMediaUC(int ucMediaDesejada) {
        double somaDaUC = notasUcs(ucMediaDesejada);   
        
        double mediaUc = (somaDaUC / 5.0);
        return mediaUc;
    }

    //ex5e)
    public int notaMaisAlta(){
        
        int notaAlta = 0;
        
        for (int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                if (notasTurma[i][j] > notaAlta) {
                    notaAlta = notasTurma[i][j];
                }
            }
        }
        return notaAlta;
    }

    //ex5f)
    public int notaMaisBaixa(){
         int notaBaixa = notasTurma[0][0];
         for ( int i = 0; i < 5; i++){
            for (int j = 0 ; j <5; j++){
                if (notasTurma[i][j] < notaBaixa){
                    notaBaixa = notasTurma[i][j];
                }
            }
         }
         return notaBaixa;
    }

    //ex5(g)
    public int []notasAcimaValor (int valor){
        int [] arrayValor = new int[25];
        int posicaoArrayValor = 0; 
        for(int i = 0; i < 5; i++){
            for (int j = 0; j < 5 ; j++) {
                if (notasTurma[i][j] > valor){
                    arrayValor[posicaoArrayValor] = notasTurma[i][j];
                    posicaoArrayValor++;
                }
            }
        }
        return Arrays.copyOf(arrayValor, posicaoArrayValor);
    }

    //ex5(h)
    public String pautaCompleta() {
        
        String relatorio = "--- RELATÓRIO DE NOTAS DA TURMA ---\n";
        
        for (int i = 0; i < 5; i++) {
            relatorio = relatorio + "Aluno " + (i + 1) + ": ";
            
            for (int j = 0; j < 5; j++) {
                relatorio = relatorio + notasTurma[i][j] + " ";
            }
            relatorio = relatorio + "\n"; 
        }        
        return relatorio;
    }

    //ex5(i)
    public int ucMediaElevado(){
        double mediaElevada = 0.0;
        int indice = 0;
        for (int i = 0; i < 5; i++){
            double somaDaUC = notasUcs(i);
            double ucMedia = (somaDaUC / 5.0);
            if (ucMedia > mediaElevada) {
                mediaElevada = ucMedia;
                indice = i;
            }
        }
        return indice;
    }
}