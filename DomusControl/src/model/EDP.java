package model;

import java.util.Objects;

/**
 * Tarifário da EDP: uma taxa fixa mensal mais um preço por kWh.
 */
public class EDP implements FornecedorEnergia {

    private double precoKWh;
    private double taxaFixa;

    public EDP() {
        this.precoKWh = 0.16;
        this.taxaFixa = 5.00;
    }

    public EDP(EDP outro) {
        this.precoKWh = outro.precoKWh;
        this.taxaFixa = outro.taxaFixa;
    }

    @Override
    public String getNomeFornecedor() {
        return "EDP Comercial";
    }

    @Override
    public double calcularFatura(double energiaConsumidaKWh) {
        return this.taxaFixa + (energiaConsumidaKWh * this.precoKWh);
    }

    @Override
    public EDP clone() {
        return new EDP(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EDP that = (EDP) o;
        return Double.compare(this.precoKWh, that.precoKWh) == 0
            && Double.compare(this.taxaFixa, that.taxaFixa) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(precoKWh, taxaFixa);
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f €/kWh + %.2f € fixos)", getNomeFornecedor(), precoKWh, taxaFixa);
    }
}
