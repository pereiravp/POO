package dispositivos;

import java.util.Objects;

/**
 * Lâmpada regulável: tem intensidade de luz (0 a 100%) e temperatura de cor
 * (2700K a 4000K). O consumo acompanha a luminosidade - a meio gás gasta
 * metade.
 */
public class Lampada extends Dispositivo {

    private int luminosidade;
    private int temperaturaCor;

    public Lampada(String marca, String modelo, double consumoHora) {
        super(marca, modelo, consumoHora);
        this.luminosidade = 0;
        this.temperaturaCor = 2700;
    }

    public Lampada(Lampada outro) {
        super(outro);
        this.luminosidade = outro.luminosidade;
        this.temperaturaCor = outro.temperaturaCor;
    }

    /** Ao ligar, a lâmpada acende no máximo. */
    @Override
    public void ligar() {
        super.ligar();
        this.luminosidade = 100;
    }

    public void setLuminosidade(int novaLuminosidade) {
        if (novaLuminosidade < 0 || novaLuminosidade > 100) {
            throw new IllegalArgumentException("luminosidade inválida! escolhe um valor entre 0 e 100.");
        }
        this.luminosidade = novaLuminosidade;
    }

    public int getLuminosidade() {
        return this.luminosidade;
    }

    public void setTemperaturaCor(int novaTemperatura) {
        if (novaTemperatura < 2700 || novaTemperatura > 4000) {
            throw new IllegalArgumentException("cor inválida! escolhe um valor na escala entre 2700 e 4000.");
        }
        this.temperaturaCor = novaTemperatura;
    }

    public int getTemperaturaCor() {
        return this.temperaturaCor;
    }

    /** O consumo é proporcional à luminosidade: 50% de luz, 50% do consumo. */
    @Override
    public double getConsumoInstantaneo() {
        double consumoBase = super.getConsumoInstantaneo();
        if (consumoBase == 0.0) return 0.0;
        return consumoBase * (this.luminosidade / 100.0);
    }

    @Override
    public Lampada clone() {
        return new Lampada(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Lampada that = (Lampada) o;
        return this.luminosidade == that.luminosidade
            && this.temperaturaCor == that.temperaturaCor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), luminosidade, temperaturaCor);
    }

    @Override
    public String toString() {
        return super.toString()
             + String.format(" | luz %d%%, %dK", luminosidade, temperaturaCor);
    }
}
