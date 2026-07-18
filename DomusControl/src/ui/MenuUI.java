package ui;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import model.Casa;
import model.Utilizador;
import persistence.GestorDados;

public class MenuUI {

    // gestor de dados partilhado, para gravar o estado a partir dos menus
    static final GestorDados GESTOR = new GestorDados();

    // Um "gravar" que pode falhar com IOException. Envolve as chamadas ao
    // GestorDados para que os menus não tenham de repetir o try/catch.
    @FunctionalInterface
    interface GravadorComErro<T> { void gravar(T dados) throws IOException; }

    static <T> void guardarSeguro(GravadorComErro<T> gravador, T dados) {
        try {
            gravador.gravar(dados);
        } catch (IOException e) {
            System.out.println("Aviso: nao foi possivel gravar os dados (" + e.getMessage() + ").");
        }
    }

    // menu de entrada que gere login e registo
    public static void menuInicial(Scanner input, Map<String, Utilizador> utilizadores, Map<String, Casa> casas, boolean ajuda) {

        int opcao = -1;

        do {
            // primeiro limpamos o ecrã
            Leitor.limparConsola();

            if (ajuda) {
                System.out.println("\n[GUIA DE AVALIAÇÃO]");
                System.out.println("O sistema detetou que não existem dados. Foram gerados dados de teste.");
                System.out.println("Pode entrar com: admin@uminho.pt / poo2026");
            }
            
            System.out.println("\n---------------------------");
            System.out.println(" Bem-Vindo ao DomusControl");
            System.out.println("---------------------------");
            System.out.println("1 - Iniciar Sessão");
            System.out.println("2 - Criar Conta");
            System.out.println("0 - Sair");
            
            opcao = Leitor.lerInteiro(input, "Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    Leitor.limparConsola();
                    System.out.println("--- INICIAR SESSÃO ---");
                    int tentativas = 0;
                    boolean sucesso = false;
                    Utilizador utilizadorLogado = null;

                    // sistema de login com 3 tentativas e password protegida
                    while (tentativas < 3 && !sucesso) {
                        System.out.print("Email ou Nome de Utilizador: ");
                        String loginInput = input.nextLine();
                        
                        // utiliza o novo leitor para proteger a password no terminal
                        String passInput = Leitor.lerPassword(input, "Password: ");

                        utilizadorLogado = utilizadores.get(loginInput);

                        if (utilizadorLogado != null && utilizadorLogado.getPassword().equals(passInput)) {
                            sucesso = true;
                        } else {
                            tentativas++;
                            if (tentativas < 3) {
                                System.out.println("\nErro: Dados incorretos. Tentativa " + tentativas + " de 3.");
                            }
                        }
                    }

                    if (sucesso) {
                        System.out.println("\nLogin efetuado com sucesso!");
                        System.out.println("Bem-vindo, " + utilizadorLogado.getNome());
                        
                        // entra no hub principal do utilizador
                        menuUtilizador(input, utilizadorLogado, utilizadores, casas);
                    } else {
                        System.out.println("\nLimite de tentativas excedido.");
                        
                        // sistema de recuperação de password "profissional"
                        System.out.println("\n--- RECUPERAÇÃO DE CONTA ---");
                        System.out.print("Introduza o email da conta: ");
                        String emailRecuperacao = input.nextLine();

                        Utilizador contaRecuperar = utilizadores.get(emailRecuperacao);

                        if (contaRecuperar != null) {
                            System.out.println("Pergunta de Segurança: " + contaRecuperar.getPerguntaSeguranca());
                            System.out.print("Resposta: ");
                            String resp = input.nextLine();

                            if (contaRecuperar.getRespostaSeguranca().equalsIgnoreCase(resp)) {
                                String novaPass = Leitor.lerPassword(input, "Resposta Correta! Introduza a nova password: ");
                                contaRecuperar.setPassword(novaPass);
                                guardarSeguro(GESTOR::guardarUtilizadores, utilizadores);
                                System.out.println("\nSucesso! A password foi alterada. Prima Enter para continuar...");
                                input.nextLine();
                            } else {
                                System.out.println("Erro: Resposta incorreta. Prima Enter para voltar ao menu.");
                                input.nextLine();
                            }
                        } else {
                            System.out.println("Erro: Conta não encontrada. Prima Enter para voltar.");
                            input.nextLine();
                        }
                    }
                    break;

                case 2:
                    Leitor.limparConsola();
                    System.out.println("--- REGISTO DE NOVA CONTA ---");
                    
                    System.out.print("Nome de utilizador: ");
                    String nome = input.nextLine();

                    // validação real de formato de email
                    String email = Leitor.lerEmail(input, "Email: ");

                    if (utilizadores.containsKey(email) || utilizadores.containsKey(nome)) {
                        System.out.println("\nErro: Este utilizador ou email já se encontra registado no sistema.");
                        System.out.println("Prima Enter para tentar novamente...");
                        input.nextLine();
                        break;
                    }

                    String pass = Leitor.lerPassword(input, "Defina a sua Password: ");

                    // configuração da pergunta de segurança para recuperação
                    System.out.println("\nEscolha uma pergunta de segurança:");
                    System.out.println("1. Qual o nome do seu primeiro animal de estimação?");
                    System.out.println("2. Qual a sua cor favorita?");
                    System.out.println("3. Qual o nome da sua rua de infância?");
                    
                    int pOpcao = Leitor.lerInteiro(input, "Opção (1-3): ");

                    String pergunta = switch (pOpcao) {
                        case 1 -> "Qual o nome do seu primeiro animal de estimação?";
                        case 2 -> "Qual a sua cor favorita?";
                        case 3 -> "Qual o nome da sua rua de infância?";
                        default -> "Pergunta padrão de segurança";
                    };

                    System.out.print("Resposta à pergunta: ");
                    String resposta = input.nextLine();

                    // criação do objeto utilizador com id único gerado automaticamente
                    Utilizador novo = new Utilizador(nome, email, pass, pergunta, resposta, new java.util.HashMap<>());
                    
                    // indexamos por email e por nome para facilitar o login
                    utilizadores.put(email, novo);
                    utilizadores.put(nome, novo);
                    
                    // guarda os dados imediatamente no disco
                    guardarSeguro(GESTOR::guardarUtilizadores, utilizadores);
                    System.out.println("\nConta criada com sucesso! O seu ID de partilha é: " + novo.getId());
                    System.out.println("Prima Enter para continuar...");
                    input.nextLine();
                    break;

                case 0:
                    System.out.println("Obrigado por usar o DomusControl. Até breve!");
                    break;

                default:
                    System.out.println("Erro: Opção inválida.");
                    break;
            }
        } while (opcao != 0);
    }

    // hub principal apos login
    public static void menuUtilizador(Scanner input, Utilizador logado, Map<String, Utilizador> mapaUtils, Map<String, Casa> mapaCasas) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n========= DOMUSCONTROL - HUB [" + logado.getNome() + "] =========");
            System.out.println("1 - Ver Perfil");
            System.out.println("2 - Gerir Casas (Adicionar/Remover)");
            System.out.println("3 - Ver Minhas Permissões");
            System.out.println("4 - Convidar Amigo (Dar acesso via ID)");
            System.out.println("0 - Logout");
            
            opcao = Leitor.lerInteiro(input, "Escolha: ");

            switch (opcao) {
                case 1: verPerfil(logado, mapaCasas); break; 
                case 2: GestaoHabitacaoUI.menuGerirCasas(input, logado, mapaUtils, mapaCasas); break;
                case 3: GestaoHabitacaoUI.consultarCasas(input, logado, mapaCasas); break;
                case 4: convidarAmigo(input, logado, mapaUtils, mapaCasas); break;
                case 0: System.out.println("Sessão terminada."); break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

 public static void verPerfil(Utilizador u, Map<String, Casa> mapaCasas) {
        System.out.println("\n--- O TEU PERFIL ---");
        System.out.println("Nome: " + u.getNome());
        System.out.println("Email: " + u.getEmail());
        System.out.println("ID de Partilha: " + u.getId());
        System.out.println("--------------------");
        System.out.println("AS TUAS PROPRIEDADES:");
        
        boolean temCasas = false;
        // procurar as casas onde o utilizador logado é o dono
        for (Casa c : mapaCasas.values()) {
            if (c.getIdDono().equals(u.getId())) {
                System.out.println("- " + c.getNomeCasa() + " [ID: " + c.getIdCasa() + "]");
                temCasas = true;
            }
        }
        
        if (!temCasas) {
            System.out.println("Ainda não tens casas registadas.");
        }
        System.out.println("--------------------");
    }

    public static void convidarAmigo(Scanner input, Utilizador logado, Map<String, Utilizador> mapaUtils, Map<String, Casa> mapaCasas) {
        System.out.println("\n--- CONVIDAR AMIGO ---");
        
        System.out.println("As Tuas Casas:");
        boolean temCasas = false;
        for (Casa c : mapaCasas.values()) {
            if (c.getIdDono().equals(logado.getId())) {
                System.out.println("- " + c.getNomeCasa() + " [ID: " + c.getIdCasa() + "]");
                temCasas = true;
            }
        }

        if (!temCasas) {
            System.out.println("Erro: Não tens casas registadas para partilhar.");
            return; // sai do metodo imediatamente se nao tiver casas
        }

        System.out.print("\nIntroduz o ID da Casa a partilhar: ");
        String idCasa = input.nextLine().toUpperCase();

        if (mapaCasas.containsKey(idCasa) && mapaCasas.get(idCasa).getIdDono().equals(logado.getId())) {
            
            // mostrar a lista de utilizadores sem duplicados visuais
            System.out.println("\nUtilizadores disponíveis no sistema:");
            java.util.Set<String> idsImpressos = new java.util.HashSet<>();
            
            for (Utilizador u : mapaUtils.values()) {
                if (!u.getId().equals(logado.getId())) { 
                    if (!idsImpressos.contains(u.getId())) {
                        System.out.println("- " + u.getNome() + " [ID: " + u.getId() + "]");
                        idsImpressos.add(u.getId());
                    }
                }
            }

            System.out.print("\nIntroduz o ID de Partilha do Amigo: ");
            String idAmigo = input.nextLine();

            boolean existe = false;
            String idRealDoAmigo = ""; // variável para guardar o ID com as maiúsculas/minúsculas exatas

            for (Utilizador u : mapaUtils.values()) {
                // equalsIgnoreCase em vez de equals
                if (u.getId().equalsIgnoreCase(idAmigo)) {
                    existe = true;
                    idRealDoAmigo = u.getId(); // captura o ID original gravado no sistema
                    break;
                }
            }

            // lógica final para adicionar o amigo
            if (existe) {
                Casa casa = mapaCasas.get(idCasa);
                
                // importante: usar o método darAcesso da classe Casa
                // este método adiciona à lista real e não a uma cópia
                casa.darAcesso(idRealDoAmigo); 
                
                // gravar imediatamente para o ficheiro casas.dat
                guardarSeguro(GESTOR::guardarCasas, mapaCasas); 
                
                System.out.println("Sucesso: Acesso concedido a " + idRealDoAmigo + " para a casa '" + casa.getNomeCasa() + "'!");
                System.out.println("Enter para continuar...");
                input.nextLine();
            } else {
                System.out.println("Erro: Utilizador com ID '" + idAmigo + "' não encontrado.");
            }

        } else {
            System.out.println("Erro: Casa não encontrada ou não és o dono desta casa.");
        }
    }
}