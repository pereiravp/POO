package dispositivos;

import java.util.Objects;

/**
 * Robô aspirador. Enquanto está na base de carregamento é a casa que paga a
 * energia; quando sai a limpar corre à bateria, e nesse tempo não conta para o
 * consumo da casa.
 */
public class RoboAspirador extends Dispositivo {

    private int nivelLixo;
    private String modoLimpeza;
    private boolean baseCarregamento;

    public RoboAspirador(String marca, String modelo, double consumoHora) {
        super(marca, modelo, consumoHora);
        this.nivelLixo = 0;
        this.modoLimpeza = "Normal";
        this.baseCarregamento = true;
    }

    public RoboAspirador(RoboAspirador outro) {
        super(outro);
        this.nivelLixo = outro.nivelLixo;
        this.modoLimpeza = outro.modoLimpeza;
        this.baseCarregamento = outro.baseCarregamento;
    }

    public int getNivelLixo() {
        return this.nivelLixo;
    }

    public void setModoLimpeza(String novoModo) {
        if (novoModo.equalsIgnoreCase("Silencioso") || novoModo.equalsIgnoreCase("Esfregona")
         || novoModo.equalsIgnoreCase("Turbo") || novoModo.equalsIgnoreCase("Normal")) {
            this.modoLimpeza = novoModo;
        } else {
            throw new IllegalArgumentException("Modo Limpeza inválido! Escolhe entre: Silencioso, Esfregona, Turbo ou Normal.");
        }
    }

    public String getModoLimpeza() {
        return this.modoLimpeza;
    }

    public void setBaseCarregamento(boolean estadoBase) {
        this.baseCarregamento = estadoBase;
    }

    public boolean isNaBase() {
        return this.baseCarregamento;
    }

    @Override
    public double getConsumoInstantaneo() {
        // desligado, ou fora da base a gastar a própria bateria: não pesa na casa
        if (!super.isEstado() || !this.baseCarregamento) {
            return 0.0;
        }
        return super.getConsumoHora();
    }

    @Override
    public RoboAspirador clone() {
        return new RoboAspirador(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RoboAspirador that = (RoboAspirador) o;
        return this.nivelLixo == that.nivelLixo
            && this.baseCarregamento == that.baseCarregamento
            && Objects.equals(this.modoLimpeza, that.modoLimpeza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nivelLixo, modoLimpeza, baseCarregamento);
    }

    @Override
    public String toString() {
        return super.toString()
             + String.format(" | modo %s, %s", modoLimpeza, baseCarregamento ? "na base" : "a limpar");
    }
}
