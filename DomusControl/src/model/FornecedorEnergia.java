package model;

import java.io.Serializable;

/**
 * Um fornecedor de energia sabe dizer o seu nome e calcular a fatura a partir
 * da energia consumida. Cada empresa tem a sua tabela de preços e as suas
 * regras (descontos, taxas fixas), escondidas atrás desta interface para que a
 * casa não precise de saber com quem está a lidar.
 */
public interface FornecedorEnergia extends Serializable {

    /** Nome comercial da empresa. */
    String getNomeFornecedor();

    /** Fatura correspondente à energia gasta, em euros. */
    double calcularFatura(double energiaConsumidaKWh);

    /** Cópia independente deste fornecedor. */
    FornecedorEnergia clone();
}
