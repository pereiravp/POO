package logic;

import java.io.Serializable;
import java.util.Objects;

/**
 * Uma tarefa agendada: a uma dada hora e minuto, ligar ou desligar um
 * dispositivo. O motor de tempo da casa percorre a agenda a cada minuto
 * virtual e executa as que casam com a hora atual.
 */
public class Agendamento implements Serializable {

    private String idDispositivo;
    private int hora;
    private int minuto;
    private boolean ligar;

    public Agendamento(String idDispositivo, int hora, int minuto, boolean ligar) {
        this.idDispositivo = idDispositivo;
        this.hora = hora;
        this.minuto = minuto;
        this.ligar = ligar;
    }

    public Agendamento(Agendamento outro) {
        this.idDispositivo = outro.idDispositivo;
        this.hora = outro.hora;
        this.minuto = outro.minuto;
        this.ligar = outro.ligar;
    }

    public String getIdDispositivo() { return this.idDispositivo; }
    public int getHora()             { return this.hora; }
    public int getMinuto()           { return this.minuto; }
    public boolean deveLigar()       { return this.ligar; }

    @Override
    public Agendamento clone() {
        return new Agendamento(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agendamento that = (Agendamento) o;
        return this.hora == that.hora
            && this.minuto == that.minuto
            && this.ligar == that.ligar
            && Objects.equals(this.idDispositivo, that.idDispositivo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDispositivo, hora, minuto, ligar);
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d %s dispositivo %s",
                hora, minuto, ligar ? "ligar" : "desligar", idDispositivo);
    }
}
