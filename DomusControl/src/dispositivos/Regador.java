package dispositivos;

import java.util.Objects;

/**
 * Regador automático com vários modos de rega e um sensor de chuva opcional. O
 * modo de rega define a força - e portanto o consumo -, do jato a toda a força
 * ao goteamento quase sem esforço.
 */
public class Regador extends Dispositivo {

    private boolean modoSensorChuva;
    private String modoRega;

    public Regador(String marca, String modelo, double consumoHora) {
        super(marca, modelo, consumoHora);
        this.modoSensorChuva = false;
        this.modoRega = "Jato";
    }

    public Regador(Regador outro) {
        super(outro);
        this.modoSensorChuva = outro.modoSensorChuva;
        this.modoRega = outro.modoRega;
    }

    public void setModoSensorChuva(boolean estado) {
        this.modoSensorChuva = estado;
    }

    public boolean getModoSensorChuva() {
        return this.modoSensorChuva;
    }

    public void setModoRega(String novoModo) {
        if (novoModo.equals("Jato") || novoModo.equals("Dispersão")
         || novoModo.equals("Névoa") || novoModo.equals("Goteamento")) {
            this.modoRega = novoModo;
        } else {
            throw new IllegalArgumentException("modo de rega inválido! escolhe entre \"Jato\", \"Dispersão\", \"Névoa\", \"Goteamento\".");
        }
    }

    public String getModoRega() {
        return this.modoRega;
    }

    @Override
    public double getConsumoInstantaneo() {
        double consumoBase = super.getConsumoInstantaneo();
        if (consumoBase == 0.0) return 0.0;

        double consumoFinal;
        switch (this.modoRega) {
            case "Dispersão":   consumoFinal = consumoBase * 0.8; break;
            case "Névoa":       consumoFinal = consumoBase * 0.5; break;
            case "Goteamento":  consumoFinal = consumoBase * 0.2; break;
            default:            consumoFinal = consumoBase;        break;  // Jato: força toda
        }
        if (this.modoSensorChuva) {
            consumoFinal += 2.0;  // sensor de chuva gasta mais 2 W
        }
        return consumoFinal;
    }

    @Override
    public Regador clone() {
        return new Regador(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Regador that = (Regador) o;
        return this.modoSensorChuva == that.modoSensorChuva
            && Objects.equals(this.modoRega, that.modoRega);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), modoSensorChuva, modoRega);
    }

    @Override
    public String toString() {
        return super.toString()
             + String.format(" | rega %s%s", modoRega, modoSensorChuva ? ", sensor chuva" : "");
    }
}
