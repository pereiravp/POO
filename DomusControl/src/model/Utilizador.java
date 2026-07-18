package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Um utilizador do sistema. Além dos dados de conta (nome, email, password e
 * pergunta/resposta de segurança), guarda as suas permissões: para cada casa a
 * que tem acesso, o papel que desempenha lá.
 */
public class Utilizador implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String password;
    private String perguntaSeguranca;
    private String respostaSeguranca;
    private Map<String, String> permissoes;  // id da casa -> papel

    /** Construtor vazio, útil para preencher passo a passo na interface. */
    public Utilizador() {
        this.id = gerarId();
        this.nome = "";
        this.email = "";
        this.password = "";
        this.perguntaSeguranca = "";
        this.respostaSeguranca = "";
        this.permissoes = new HashMap<>();
    }

    public Utilizador(String nome, String email, String password,
                      String pergunta, String resposta, Map<String, String> permissoes) {
        this.id = gerarId();
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.perguntaSeguranca = pergunta;
        this.respostaSeguranca = resposta;
        this.permissoes = new HashMap<>(permissoes);
    }

    /**
     * Construtor de cópia. Copia também o id - antes ficava de fora, e o
     * resultado era um "clone" com id nulo que já não representava a mesma
     * pessoa.
     */
    public Utilizador(Utilizador u) {
        this.id = u.id;
        this.nome = u.nome;
        this.email = u.email;
        this.password = u.password;
        this.perguntaSeguranca = u.perguntaSeguranca;
        this.respostaSeguranca = u.respostaSeguranca;
        this.permissoes = new HashMap<>(u.permissoes);
    }

    private static String gerarId() {
        return UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    // --- getters e setters ----------------------------------------------

    public String getId()                { return this.id; }
    public String getNome()              { return this.nome; }
    public String getEmail()             { return this.email; }
    public String getPassword()          { return this.password; }
    public String getPerguntaSeguranca() { return this.perguntaSeguranca; }
    public String getRespostaSeguranca() { return this.respostaSeguranca; }

    public void setNome(String nome)         { this.nome = nome; }
    public void setEmail(String email)       { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPerguntaSeguranca(String p) { this.perguntaSeguranca = p; }
    public void setRespostaSeguranca(String r) { this.respostaSeguranca = r; }

    public Map<String, String> getPermissoes() {
        return new HashMap<>(this.permissoes);
    }

    public void adicionarPermissao(String idCasa, String papel) {
        this.permissoes.put(idCasa, papel);
    }

    // --- métodos canónicos ----------------------------------------------

    @Override
    public Utilizador clone() {
        return new Utilizador(this);
    }

    /**
     * Dois utilizadores são o mesmo se tiverem o mesmo email - é o que
     * identifica uma conta de forma única no sistema.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilizador that = (Utilizador) o;
        return Objects.equals(this.email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", id, nome, email);
    }
}
