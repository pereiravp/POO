package dispositivos;

import java.util.Objects;

/**
 * Coluna de som com volume, reforço de graves (bass) e luzes LED. Cada extra
 * pesa no consumo: o volume escala o consumo base, o bass soma por nível e as
 * LEDs têm um custo fixo.
 */
public class Coluna extends Dispositivo {

    private int volume;
    private boolean modoBass;
    private int intensidadeBass;
    private boolean modoLeds;
    private String corLeds;

    public Coluna(String marca, String modelo, double consumoHora) {
        super(marca, modelo, consumoHora);
        this.volume = 0;
        this.modoBass = false;
        this.intensidadeBass = 0;
        this.modoLeds = false;
        this.corLeds = "Azul";
    }

    public Coluna(Coluna outro) {
        super(outro);
        this.volume = outro.volume;
        this.modoBass = outro.modoBass;
        this.intensidadeBass = outro.intensidadeBass;
        this.modoLeds = outro.modoLeds;
        this.corLeds = outro.corLeds;
    }

    public void selecionarVolume(int novoVolume) {
        if (novoVolume < 0 || novoVolume > 100) {
            throw new IllegalArgumentException("escolhe um volume entre 0 e 100.");
        }
        this.volume = novoVolume;
    }

    public int getVolume() {
        return this.volume;
    }

    /** Desligar o bass volta a pôr a intensidade a zero. */
    public void setBass(boolean estadoBass) {
        this.modoBass = estadoBass;
        if (!this.modoBass) {
            this.intensidadeBass = 0;
        }
    }

    public boolean getEstadoBass() {
        return this.modoBass;
    }

    public void setIntensidadeBass(int novaIntensidade) {
        if (novaIntensidade < 0 || novaIntensidade > 10) {
            throw new IllegalArgumentException("intensidade de bass inválida! escolhe uma intensidade entre 0 e 10!");
        }
        this.intensidadeBass = novaIntensidade;
    }

    public int getIntensidadeBass() {
        return this.intensidadeBass;
    }

    public void setLeds(boolean estadoLeds) {
        this.modoLeds = estadoLeds;
    }

    public boolean getEstadoLeds() {
        return this.modoLeds;
    }

    public void setCorLED(String novaCor) {
        if (novaCor.equalsIgnoreCase("Verde")
         || novaCor.equalsIgnoreCase("Vermelho")
         || novaCor.equalsIgnoreCase("Azul")
         || novaCor.equalsIgnoreCase("Multicolor")) {
            this.corLeds = novaCor;
        } else {
            throw new IllegalArgumentException("cor inválida! escolhe entre Verde, Vermelho, Azul ou Multicolor.");
        }
    }

    public String getCorLeds() {
        return this.corLeds;
    }

    @Override
    public double getConsumoInstantaneo() {
        double consumoBase = super.getConsumoInstantaneo();
        if (consumoBase == 0.0) return 0.0;

        double consumoFinal = consumoBase * (this.volume / 100.0);
        if (this.modoBass) {
            consumoFinal += this.intensidadeBass * 2.0;  // 2 W por nível de bass
        }
        if (this.modoLeds) {
            consumoFinal += 5.0;  // custo fixo das LEDs
        }
        return consumoFinal;
    }

    @Override
    public Coluna clone() {
        return new Coluna(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Coluna that = (Coluna) o;
        return this.volume == that.volume
            && this.modoBass == that.modoBass
            && this.intensidadeBass == that.intensidadeBass
            && this.modoLeds == that.modoLeds
            && Objects.equals(this.corLeds, that.corLeds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), volume, modoBass, intensidadeBass, modoLeds, corLeds);
    }

    @Override
    public String toString() {
        return super.toString()
             + String.format(" | volume %d%%, bass %s, leds %s",
                     volume,
                     modoBass ? ("nível " + intensidadeBass) : "off",
                     modoLeds ? corLeds : "off");
    }
}
