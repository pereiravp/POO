package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import dispositivos.Dispositivo;

/**
 * Uma divisão da casa (sala, cozinha, quarto...) e os dispositivos que lá estão
 * instalados, indexados pelo seu identificador.
 *
 * A versão original tinha aqui um método que imprimia os dispositivos no ecrã.
 * Foi removido: a divisão devolve os dados (getDispositivos) e é a interface
 * que decide como os mostrar.
 */
public class Divisao implements Serializable {

    private String idDivisao;
    private String nome;
    private String idCasa;
    private Map<String, Dispositivo> mapaDispositivos;

    public Divisao(String nome, String idCasa) {
        this.idDivisao = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        this.nome = nome;
        this.idCasa = idCasa;
        this.mapaDispositivos = new HashMap<>();
    }

    /**
     * Construtor de cópia com cópia profunda: cada dispositivo é clonado, para
     * que mexer na divisão copiada não afete a original.
     */
    public Divisao(Divisao outra) {
        this.idDivisao = outra.idDivisao;
        this.nome = outra.nome;
        this.idCasa = outra.idCasa;
        this.mapaDispositivos = new HashMap<>();
        for (Map.Entry<String, Dispositivo> e : outra.mapaDispositivos.entrySet()) {
            this.mapaDispositivos.put(e.getKey(), e.getValue().clone());
        }
    }

    public String getIdDivisao() { return this.idDivisao; }
    public String getNome()      { return this.nome; }
    public String getIdCasa()    { return this.idCasa; }

    public Dispositivo getDispositivo(String id) {
        return this.mapaDispositivos.get(id);
    }

    public void addDispositivo(Dispositivo d) {
        this.mapaDispositivos.put(d.getIdentificador(), d);
    }

    public void removerDispositivo(String id) {
        this.mapaDispositivos.remove(id);
    }

    /** Cópia do mapa de dispositivos (referências partilhadas, não clones). */
    public Map<String, Dispositivo> getDispositivos() {
        return new HashMap<>(this.mapaDispositivos);
    }

    public int getNumeroDispositivos() {
        return this.mapaDispositivos.size();
    }

    /** Soma o consumo instantâneo de todos os dispositivos da divisão. */
    public double getConsumoTotalDivisao() {
        double total = 0.0;
        for (Dispositivo d : this.mapaDispositivos.values()) {
            total += d.getConsumoInstantaneo();
        }
        return total;
    }

    // --- métodos canónicos ----------------------------------------------

    @Override
    public Divisao clone() {
        return new Divisao(this);
    }

    /** Cada divisão tem um id único, que é o que a identifica. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Divisao that = (Divisao) o;
        return Objects.equals(this.idDivisao, that.idDivisao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDivisao);
    }

    @Override
    public String toString() {
        return String.format("Divisão: %s [ID: %s] - %d dispositivo(s)",
                nome, idDivisao, mapaDispositivos.size());
    }
}
