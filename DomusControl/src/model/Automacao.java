package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Um cenário com nome ("Sair de Casa", "Ver Cinema", ...) que guarda um
 * conjunto de comandos: para cada dispositivo, se deve ficar ligado (true) ou
 * desligado (false) quando a automação é executada.
 */
public class Automacao implements Serializable {

    private String nome;
    private Map<String, Boolean> comandos;  // id do dispositivo -> ligar/desligar

    public Automacao(String nome) {
        this.nome = nome;
        this.comandos = new HashMap<>();
    }

    public Automacao(Automacao outro) {
        this.nome = outro.nome;
        this.comandos = new HashMap<>(outro.comandos);
    }

    public void adicionarComando(String idDispositivo, boolean ligar) {
        this.comandos.put(idDispositivo, ligar);
    }

    public String getNome() {
        return this.nome;
    }

    public Map<String, Boolean> getComandos() {
        return new HashMap<>(this.comandos);
    }

    @Override
    public Automacao clone() {
        return new Automacao(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Automacao that = (Automacao) o;
        return Objects.equals(this.nome, that.nome)
            && Objects.equals(this.comandos, that.comandos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, comandos);
    }

    @Override
    public String toString() {
        return String.format("Automação '%s' (%d comando(s))", nome, comandos.size());
    }
}
