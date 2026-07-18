package dispositivos;

/**
 * O dispositivo mais simples: só liga e desliga, deixando passar corrente ou
 * não. Não acrescenta estado nenhum à base, por isso herda tudo tal como está.
 */
public class Rele extends Dispositivo {

    public Rele(String marca, String modelo, double consumoHora) {
        super(marca, modelo, consumoHora);
    }

    public Rele(Rele outro) {
        super(outro);
    }

    @Override
    public Rele clone() {
        return new Rele(this);
    }
}
