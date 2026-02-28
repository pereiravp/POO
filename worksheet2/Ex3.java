//ex3
public class Ex3 {

    //ex3(a)
    public void ordena(int[] array, int tamanho) {

        for (int i = 0; i < tamanho - 1; i++) {
            for (int j = 0; j < tamanho - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    int copoVazio = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = copoVazio;
                }
            }
        }
    }

    //ex3(b)
    public int procuraBinario(int[] array,int numeroPretendido, int tamanho) {
        for (int i = 0; i < tamanho; i++ ){
            if (array [i] == numeroPretendido){
                return i;
            }
            else{
            }
        }
        return -1;
    }
}