package persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import model.Casa;
import model.Utilizador;

/**
 * Trata de guardar e carregar o estado do programa em ficheiros de objetos.
 *
 * Antes isto estava tudo no Main, misturado com o arranque da aplicação.
 * Passou para aqui para que a leitura e a gravação fiquem num sítio só, com uma
 * responsabilidade clara, e o Main apenas as invoque.
 */
public class GestorDados {

    private final String ficheiroUtilizadores;
    private final String ficheiroCasas;

    public GestorDados() {
        this("utilizadores.dat", "casas.dat");
    }

    public GestorDados(String ficheiroUtilizadores, String ficheiroCasas) {
        this.ficheiroUtilizadores = ficheiroUtilizadores;
        this.ficheiroCasas = ficheiroCasas;
    }

    public void guardarUtilizadores(Map<String, Utilizador> mapa) throws IOException {
        guardar(mapa, this.ficheiroUtilizadores);
    }

    public void guardarCasas(Map<String, Casa> mapa) throws IOException {
        guardar(mapa, this.ficheiroCasas);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Utilizador> carregarUtilizadores() {
        Object dados = carregar(this.ficheiroUtilizadores);
        return dados != null ? (Map<String, Utilizador>) dados : new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Casa> carregarCasas() {
        Object dados = carregar(this.ficheiroCasas);
        return dados != null ? (Map<String, Casa>) dados : new HashMap<>();
    }

    private void guardar(Object objeto, String ficheiro) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ficheiro))) {
            out.writeObject(objeto);
        }
    }

    /** Lê um objeto do ficheiro, ou devolve null se não existir ou falhar. */
    private Object carregar(String ficheiro) {
        File f = new File(ficheiro);
        if (!f.exists()) return null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
