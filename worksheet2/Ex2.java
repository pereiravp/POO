import java.time.LocalDate;

//ex2
public class Ex2 {
    private LocalDate[] datas;
    private int indiceAtual;

    public Ex2(int tamanho) {
        this.datas = new LocalDate[tamanho];
        this.indiceAtual = 0;
    }
    
    //ex2(a)
    public void insereData(LocalDate data) {
        if (this.indiceAtual < this.datas.length) {
            this.datas[this.indiceAtual] = data;
            this.indiceAtual++;
            System.out.println("Data guardada na posição " + (this.indiceAtual - 1));
        } else {
            System.out.println("Erro: O array está cheio!");
        }
    }
    
    public void mostrarTudo() {
        for (int i = 0; i < this.indiceAtual; i++) {
            System.out.println("Data " + i + ": " + this.datas[i]);
        }
    }

    // ex2(b)
    public LocalDate dataMaisProxima(LocalDate dataAlvo) {
        if (this.indiceAtual == 0) {
            return null;
        }
        LocalDate maisProxima = this.datas[0];
        
        long menorDistancia = Math.abs(dataAlvo.toEpochDay() - maisProxima.toEpochDay());

        for (int i = 1; i < this.indiceAtual; i++) {
            
            long distanciaAtual = Math.abs(dataAlvo.toEpochDay() - this.datas[i].toEpochDay());

            if (distanciaAtual < menorDistancia) {
                menorDistancia = distanciaAtual;
                maisProxima = this.datas[i]; 
            }
        }
        return maisProxima;
    }
    
    //ex2(c)
    public String toString(){   
        String resultado = "\nLista de Datas:\n";
        for (int i = 0; i < this.indiceAtual; i++) {
            resultado = resultado + datas[i] + "\n";
        }
        return resultado;   
    }
}