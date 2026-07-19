package model;

import java.util.Objects;

/**
 * Tarifário da Galp: preço por kWh, sem taxa fixa, com 10% de desconto para
 * quem consome mais de 100 kWh.
 */
public class Galp implements FornecedorEnergia {

    private double precoKWh;

    public Galp() {
        this.precoKWh = 0.18;
    }

    public Galp(Galp outro) {
        this.precoKWh = outro.precoKWh;
    }

    @Override
    public String getNomeFornecedor() {
        return "Galp Energia";
    }

    @Override
    public double calcularFatura(double energiaConsumidaKWh) {
        double custoBase = energiaConsumidaKWh * this.precoKWh;
        if (energiaConsumidaKWh > 100) {
            return custoBase * 0.90;  // desconto de grande consumidor
        }
        return custoBase;
    }

    @Override
    public Galp clone() {
        return new Galp(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Galp that = (Galp) o;
        return Double.compare(this.precoKWh, that.precoKWh) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(precoKWh);
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f €/kWh, -10%% acima de 100 kWh)", getNomeFornecedor(), precoKWh);
    }
}
