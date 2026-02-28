//ex1
public class Ex1 {

    private int[] numeros;
    private int dimensao;
    
    // ex1(a)
    public void recebeArray (int[] vector, int dimensao ) {
        this.numeros = new int[dimensao];
        for (int j = 0; j < dimensao; j++){
            this.numeros[j] = vector[j]; 
        }

        this.dimensao = dimensao;
    }
    public void printNumeros (){
            for (int i = 0; i < this.dimensao; i++){
                System.out.println(this.numeros[i]);
            }
    }

    // ex1(b)
    public int minNum (){
        
        int min = this.numeros[0];
        for (int i = 1; i < this.dimensao;i++ ){
            if (this.numeros[i] < min){
                min = this.numeros[i];
            }
        }
        return min;
    }
    
    //ex1(c)
    public int[] intervalo(int iMin, int iMax){
        
        int j = 0;
        int [] lista = new int [(iMax-iMin)-1]; 
        for (int i = iMin+1; i <iMax; i++, j++) {
            lista [j] = numeros [i];
        }
        return lista;
    }

    //ex1(d)
    public int [] digitoComum(int quantDigitos1,int quantDigitos2, int [] array1, int [] array2){
    
    int quantMaior; int quantMenor;
        if (quantDigitos1 > quantDigitos2){
            quantMaior = quantDigitos1;
            quantMenor = quantDigitos2;
            return checkCommon(quantMenor,quantMaior,array2,array1);
        } 
        else {
            quantMaior = quantDigitos2;
            quantMenor = quantDigitos1;
            return checkCommon(quantMenor, quantMaior, array1, array2);
        } 
    }
    private int[] checkCommon(int quantMenor,int quantMaior,int [] array1,int [] array2){
        int [] comum = new int[quantMenor];
        int k = 0;
        for (int i = 0; i < quantMenor; i++){
            for (int j = 0; j < quantMaior; j++){
                if (array1 [i] == array2[j]){
                    comum [k] = array1[i];
                    k++;
                }
            }
        }
                int [] comum2 = new int[k];
        for (int i = 0; i < k; i++)
            comum2 [i] = comum [i];
        return comum2;
    }
}