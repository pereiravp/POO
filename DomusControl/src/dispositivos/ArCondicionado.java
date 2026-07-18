package dispositivos;

import java.util.Objects;

/**
 * Ar condicionado com temperatura, modo (Frio, Quente, Ventoinha,
 * Desumidificador), velocidade da ventoinha e rotação. O modo e a velocidade
 * mexem bastante no consumo - a ventoinha sozinha gasta pouco, o frio no
 * máximo gasta tudo.
 */
public class ArCondicionado extends Dispositivo {

    private double temperatura;
    private String modo;
    private int velocidadeVentoinha;
    private boolean rotacao;

    public ArCondicionado(String marca, String modelo, double consumoHora) {
        super(marca, modelo, consumoHora);
        this.temperatura = 22.0;
        this.modo = "Frio";
        this.velocidadeVentoinha = 1;
        this.rotacao = false;
    }

    public ArCondicionado(ArCondicionado outro) {
        super(outro);
        this.temperatura = outro.temperatura;
        this.modo = outro.modo;
        this.velocidadeVentoinha = outro.velocidadeVentoinha;
        this.rotacao = outro.rotacao;
    }

    public void setModo(String novoModo) {
        if (novoModo.equalsIgnoreCase("Frio") || novoModo.equalsIgnoreCase("Ventoinha")
         || novoModo.equalsIgnoreCase("Quente") || novoModo.equalsIgnoreCase("Desumidificador")) {
            this.modo = novoModo;
        } else {
            throw new IllegalArgumentException("modo inválido! escolhe entre \"Frio\", \"Quente\", \"Ventoinha\" ou \"Desumidificador\".");
        }
    }

    public String getModo() {
        return this.modo;
    }

    public void setVelocidade(int novaVelocidade) {
        if (novaVelocidade < 1 || novaVelocidade > 5) {
            throw new IllegalArgumentException("velocidade inválida! escolhe uma velocidade entre 1 a 5.");
        }
        this.velocidadeVentoinha = novaVelocidade;
    }

    public int getVelocidade() {
        return this.velocidadeVentoinha;
    }

    public void setTemperatura(double novaTemperatura) {
        if (novaTemperatura < 16 || novaTemperatura > 30) {
            throw new IllegalArgumentException("temperatura inválida! escolhe uma temperatura entre 16 e 30 graus celsius.");
        }
        this.temperatura = novaTemperatura;
    }

    public double getTemperatura() {
        return this.temperatura;
    }

    public void setRotacao(boolean estadoRotacao) {
        this.rotacao = estadoRotacao;
    }

    public boolean getRotacao() {
        return this.rotacao;
    }

    @Override
    public double getConsumoInstantaneo() {
        double consumoBase = super.getConsumoInstantaneo();
        if (consumoBase == 0.0) return 0.0;

        double consumoFinal = consumoBase;
        if (this.modo.equals("Ventoinha")) {
            consumoFinal = consumoBase * 0.10;
        } else if (this.modo.equals("Desumidificador")) {
            consumoFinal = consumoBase * 0.80;
        }
        consumoFinal += this.velocidadeVentoinha * 50;  // 50 W por nível de velocidade
        if (this.rotacao) {
            consumoFinal += 20;
        }
        return consumoFinal;
    }

    @Override
    public ArCondicionado clone() {
        return new ArCondicionado(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ArCondicionado that = (ArCondicionado) o;
        return Double.compare(this.temperatura, that.temperatura) == 0
            && this.velocidadeVentoinha == that.velocidadeVentoinha
            && this.rotacao == that.rotacao
            && Objects.equals(this.modo, that.modo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), temperatura, modo, velocidadeVentoinha, rotacao);
    }

    @Override
    public String toString() {
        return super.toString()
             + String.format(" | %s %.1f°C, vel %d%s",
                     modo, temperatura, velocidadeVentoinha, rotacao ? ", rotação" : "");
    }
}
