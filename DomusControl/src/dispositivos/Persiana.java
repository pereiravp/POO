package dispositivos;

import java.util.Objects;

/**
 * Persiana motorizada: abre e fecha por altura (0 a 100%), inclina as lâminas
 * (0 a 90°) e pode seguir um sensor solar.
 *
 * Ao contrário da versão original, esta classe já não imprime mensagens para o
 * ecrã - o modelo não deve saber que existe um ecrã. Os setters validam e
 * lançam exceção em caso de valor inválido, deixando a decisão de o que
 * mostrar ao utilizador para a camada de interface.
 */
public class Persiana extends Dispositivo {

    private int alturaPersiana;   // 0 = fechada, 100 = aberta
    private int anguloLaminas;    // 0 = abertas, 90 = fechadas
    private boolean modoSensorSolar;

    public Persiana(String marca, String modelo, double consumoHora) {
        super(marca, modelo, consumoHora);
        this.alturaPersiana = 0;
        this.anguloLaminas = 90;
        this.modoSensorSolar = false;
    }

    public Persiana(Persiana outro) {
        super(outro);
        this.alturaPersiana = outro.alturaPersiana;
        this.anguloLaminas = outro.anguloLaminas;
        this.modoSensorSolar = outro.modoSensorSolar;
    }

    /** Ligar abre totalmente. */
    @Override
    public void ligar() {
        super.ligar();
        this.alturaPersiana = 100;
    }

    /** Desligar fecha totalmente. */
    @Override
    public void desligar() {
        super.desligar();
        this.alturaPersiana = 0;
    }

    public void setAlturaPersiana(int novaAltura) {
        if (novaAltura < 0 || novaAltura > 100) {
            throw new IllegalArgumentException("altura inválida! escolhe uma altura entre 0 e 100.");
        }
        this.alturaPersiana = novaAltura;
    }

    public int getAlturaPersiana() {
        return this.alturaPersiana;
    }

    public void setAnguloLaminas(int novoAngulo) {
        if (novoAngulo < 0 || novoAngulo > 90) {
            throw new IllegalArgumentException("ângulo inválido! escolhe um ângulo entre 0 e 90.");
        }
        this.anguloLaminas = novoAngulo;
    }

    public int getAnguloLaminas() {
        return this.anguloLaminas;
    }

    public void setModoSensorSolar(boolean estado) {
        this.modoSensorSolar = estado;
    }

    public boolean getModoSensorSolar() {
        return this.modoSensorSolar;
    }

    @Override
    public double getConsumoInstantaneo() {
        if (!super.isEstado()) return 0.0;
        double consumoFinal = super.getConsumoInstantaneo();
        if (this.modoSensorSolar) {
            consumoFinal += 3.0;  // sensor solar gasta mais 3 W
        }
        return consumoFinal;
    }

    @Override
    public Persiana clone() {
        return new Persiana(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Persiana that = (Persiana) o;
        return this.alturaPersiana == that.alturaPersiana
            && this.anguloLaminas == that.anguloLaminas
            && this.modoSensorSolar == that.modoSensorSolar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), alturaPersiana, anguloLaminas, modoSensorSolar);
    }

    @Override
    public String toString() {
        return super.toString()
             + String.format(" | altura %d%%, lâminas %d°%s",
                     alturaPersiana, anguloLaminas, modoSensorSolar ? ", sensor solar" : "");
    }
}
