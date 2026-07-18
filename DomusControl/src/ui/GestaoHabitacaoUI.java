package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.*;
import dispositivos.*;

public class GestaoHabitacaoUI {

    public static void consultarCasas(Scanner input, Utilizador logado, Map<String, Casa> mapaCasas) {
        System.out.println("--- GESTÃO DE ACESSOS ---");
                        
        // lista de casas como administrador
        System.out.println("[ ADMIN ] Minhas Casas:");
        for (Casa c : mapaCasas.values()) {
            if (c.getIdDono().equals(logado.getId())) {
                 System.out.println("  - " + c.getNomeCasa() + " [ID: " + c.getIdCasa() + "]");
            }
        }
        // lista de casas como convidado
        System.out.println("[ GUEST ] Casas Partilhadas:");
        for (Casa c : mapaCasas.values()) {
            if (c.getAutorizados().contains(logado.getId())) {
                System.out.println("  - " + c.getNomeCasa() + " [ID: " + c.getIdCasa() + "]");
            }
        }
    }

    public static void consultarDivisoes(Casa casa) {
        // mostrar sempre a lista de divisoes no topo do menu
        System.out.println("Divisões Atuais:");
        
        if (casa.getDivisoes().isEmpty()) {
            System.out.println("  (Nenhuma divisão criada)");
        } else {
            for (Divisao d : casa.getDivisoes().values()) {
                System.out.println("  - " + d.getNome() + " [ID: " + d.getIdDivisao() + "]");
            }
        }
    }

    public static void consultarDispositivos(Divisao divisao) {
        // mostrar sempre a lista de dispositivos no topo do menu
        System.out.println("Dispositivos Atuais:");
        
        if (divisao.getDispositivos().isEmpty()) {
            System.out.println("  (Nenhum dispositivo criado)");
        } else {
            for (Dispositivo d : divisao.getDispositivos().values()) {
                // d.getClass().getSimpleName() vai buscar o nome do dispositivo
                System.out.println("  - " + d.getClass().getSimpleName() + " [ID: " + d.getIdentificador() + "]");
            }
        }
    }

    public static void menuGerirCasas(Scanner input, Utilizador logado, Map<String, Utilizador> mapaUtils, Map<String, Casa> mapaCasas) {        
        int opcao = -1;
        while (opcao != 0) {
            Leitor.limparConsola();
            System.out.println("--- GESTÃO DE CASAS ---");
            System.out.println("1 - Adicionar Nova Casa");
            System.out.println("2 - Remover Minha Casa");
            System.out.println("3 - Selecionar Casa para Gerir"); 
            System.out.println("0 - Voltar");
            
            // layout melhorado: lista aparece depois das opções
            System.out.println("\n-----------------------------------------");
            consultarCasas(input, logado, mapaCasas);
            System.out.println("-----------------------------------------");

            opcao = Leitor.lerInteiro(input, "Opção: ");

            switch (opcao) {
                case 1:
                    // lógica de adicionar casa
                    System.out.print("Nome da Casa: ");
                    String nome = input.nextLine();
                    System.out.print("Morada: ");
                    String morada = input.nextLine();
                    Casa nova = new Casa(nome, morada, logado.getId());
                    mapaCasas.put(nova.getIdCasa(), nova);
                    MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                    System.out.println("Casa criada com sucesso! ID: " + nova.getIdCasa());
                    break;
                case 2:
                    // lógica de remover casa
                    System.out.print("ID da Casa a remover: ");
                    String idR = input.nextLine().toUpperCase();
                    if (mapaCasas.containsKey(idR) && mapaCasas.get(idR).getIdDono().equals(logado.getId())) {
                        mapaCasas.remove(idR);
                        MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                        System.out.println("Casa removida com sucesso.");
                    } else {
                        System.out.println("Erro: Não tens permissão ou ID inválido.");
                    }
                    break;
                case 3:
                    System.out.print("ID da Casa que desejas gerir: ");
                    String idG = input.nextLine().toUpperCase();
                    Casa casaG = mapaCasas.get(idG);
                    
                    if (casaG != null && (casaG.getIdDono().equals(logado.getId()) || casaG.getAutorizados().contains(logado.getId()))) {
                        // como o menu principal agora calcula as permissões (co-admin, etc) sozinho
                        // já não precisamos de lhe passar o boolean 'eAdmin' aqui
                        menuPrincipalCasa(input, casaG, mapaUtils, mapaCasas, logado);
                        } else {
                        System.out.println("Erro: Casa inexistente ou sem acesso.");
                    }
                    break;
            }
        }
    }

    // submenu para gestao de propriedades
    public static void menuEstatisticasESimulacao(Scanner input, Utilizador logado, Map<String, Casa> mapaCasas) {
            int opcao = -1;
            while (opcao != 0) {
                consultarCasas(input, logado, mapaCasas);

                System.out.println("\n--- PAINEL DE CONTROLO E ESTATÍSTICAS ---");
                System.out.println("1 - Consumo e Simulação (Casa Específica)");
                System.out.println("2 - Estatística: Casa com maior consumo");
                System.out.println("3 - Estatística: Top 3 Divisões com mais dispositivos");
                System.out.println("4 - Ver Histórico de Atividades (Logs)");
                System.out.println("5 - Emitir Fatura e Efetuar Pagamento");
                System.out.println("6 - Mudar Fornecedor de Energia"); 
                System.out.println("0 - Voltar");
                
                opcao = Leitor.lerInteiro(input, "Opção: ");
                
                switch (opcao) {
                    case 1:
                        // chama o método auxiliar para não sobrecarregar o menu
                        visualizarConsumoESimulacao(input, logado, mapaCasas);
                        break;

                    case 2:
                        // estatística global do sistema
                        mostrarCasaMaiorConsumo(mapaCasas);
                        break;

                    case 3:
                        // estatística de densidade de dispositivos
                        mostrarTopDivisoes(mapaCasas);
                        break;

                    case 4:
                        // mostra os logs de uma casa específica
                        mostrarLogsCasa(input, logado, mapaCasas);
                        break;

                    case 5:
                        // lógica de faturação e reset de contador
                        processarFaturaEPagamento(input, logado, mapaCasas);
                        break;

                    case 6:
                        // gestão de contratos (edp/galp)
                        System.out.print("\nIntroduza o ID da Casa: ");
                        String idF = input.nextLine().toUpperCase();
                        Casa casaF = mapaCasas.get(idF);
                        if (casaF != null) menuMudarFornecedor(input, casaF, mapaCasas);
                        else System.out.println("Erro: Casa não encontrada.");
                        break;

                    case 0:
                        break;

                    default:
                        System.out.println("Erro: Opção inválida.");
                }
            }
        }

    // submenu dedicado as divisoes de uma casa especifica
    public static void menuGerirDivisoes(Scanner input, Casa casa, Map<String, Casa> mapaCasas, boolean eAdmin) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- GESTÃO DE DIVISÕES: " + casa.getNomeCasa() + " ---");
            
            // mostrar sempre a lista de divisoes no topo do menu
            consultarDivisoes(casa);
            
            System.out.println("-----------------------------------------");
            System.out.println("1 - Adicionar Divisão");
            System.out.println("2 - Remover Divisão");
            System.out.println("3 - Consultar Consumo da Divisão");            
            System.out.println("4 - Gerir Dispositivos");
            System.out.println("0 - Voltar");
            
            // leitura segura
            opcao = Leitor.lerInteiro(input, "Opção: ");

            switch (opcao) {
                case 1:
                    if (!eAdmin) { System.out.println("Erro: Apenas o dono pode criar divisões."); break; }
                    System.out.print("Nome da nova divisão: ");
                    String nomeDiv = input.nextLine();
                    
                    Divisao novaDiv = new Divisao(nomeDiv, casa.getIdCasa());
                    
                    casa.adicionarDivisao(novaDiv);
                    
                    MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                    System.out.println("Divisão criada com sucesso! ID: " + novaDiv.getIdDivisao());
                    break;
                    
                case 2:
                    if (!eAdmin) { System.out.println("Erro: Apenas o dono pode remover divisões."); break; }
                    System.out.print("Introduza o ID da divisão a remover: ");
                    String idRemover = input.nextLine().toUpperCase();
                    
                    if (casa.getDivisoes().containsKey(idRemover)) {
                        casa.removerDivisao(idRemover);
                        MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                        System.out.println("Divisão removida com sucesso.");
                    } else {
                        System.out.println("Erro: O ID da divisão não existe nesta casa.");
                    }
                    break;

                case 3:   
                    if (!eAdmin) { System.out.println("Erro: Apenas o dono pode consultar o consumo da divisão."); break; }
                    System.out.print("Introduza o ID da divisão que quer consultar o consumo:");
                    String idDigitado=input.nextLine().toUpperCase();
                    Divisao DivisaoAtual=casa.getDivisoes().get(idDigitado); // ir buscar a divisão com o ID recebido
                    
                    if(DivisaoAtual!=null){  // se existir vou calcular o consumo da divisão
                            System.out.println("O consumo da divisão " + DivisaoAtual.getNome() + " é de: " + DivisaoAtual.getConsumoTotalDivisao() + "Watts!" );
                    } break;

                case 4:
                    // trava de seguranca caso nao haja divisoes
                    if (casa.getDivisoes().isEmpty()) {
                        System.out.println("Erro: Primeiro tens de adicionar uma divisão.");
                        break; 
                    } 
                    
                    // como a lista ja esta visivel em cima basta pedir o id
                    System.out.print("Introduza o ID da divisão que quer gerir: ");
                    String idDivGerir = input.nextLine().toUpperCase();
                    
                    // validar e entrar na divisao para gerir dispositivos
                    if (casa.getDivisoes().containsKey(idDivGerir)) {
                        // vai buscar a divisão certa
                        Divisao divSelecionada = casa.getDivisoes().get(idDivGerir);
                        
                        // abre o menu dos dispositivos enviando a casa e a divisão
                        menuGerirDispositivos(input, casa, divSelecionada, mapaCasas, eAdmin);
                        
                    } else {
                        System.out.println("Erro: ID de divisão inválido.");
                    }
                    break;
                    
                case 0:
                    break;
                    
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // submenu para gerir os dispositivos dentro de uma divisao especifica
    public static void menuGerirDispositivos(Scanner input, Casa casaAtual, Divisao divSelecionada, Map<String, Casa> mapaCasas, boolean eAdmin){
        int opcao = -1;
            while (opcao != 0) {
                System.out.println("\n--- GESTÃO DE DISPOSITIVOS: " + divSelecionada.getNome() + " ---");
                
                // mostrar os dispositivos atuais na divisão
                consultarDispositivos(divSelecionada);
                
                System.out.println("-----------------------------------------");
                // apenas mostra as opções de adicionar e remover se o utilizador for o dono (eAdmin = true)
                if (eAdmin) {
                    System.out.println("1 - Adicionar Dispositivo");
                    System.out.println("2 - Remover Dispositivo");
                }
                System.out.println("3 - Controlar Dispositivo (Ligar/Desligar/Configurar)");
                System.out.println("4 - Agendar Tarefa (Escalonamento)"); 
                System.out.println("5 - Gerir Cenários (Automações)");
                System.out.println("0 - Voltar");
                
                opcao = Leitor.lerInteiro(input, "Opção: ");

                switch (opcao) {
                    case 1:
                        if (!eAdmin) {
                            System.out.println("Erro: Apenas o dono pode adicionar dispositivos.");
                            break;
                        }

                        System.out.println("\n--- CATÁLOGO DE DISPOSITIVOS ---");
                        System.out.println("1 - Ar Condicionado");
                        System.out.println("2 - Coluna");
                        System.out.println("3 - Robô Aspirador");
                        System.out.println("4 - Lâmpada");
                        System.out.println("5 - Persiana");
                        System.out.println("6 - Regador");
                        System.out.println("7 - Relé");

                        int tipo = Leitor.lerInteiro(input, "Digite a opção do aparelho que pretende instalar: ");

                        System.out.print("Marca: ");
                        String marca = input.nextLine();

                        System.out.print("Modelo: ");
                        String modelo = input.nextLine();

                        double consumo = Leitor.lerDouble(input, "Consumo Base (Watts/Hora): ");

                        Dispositivo novoAparelho = null;

                        if (tipo == 1){
                            novoAparelho = new ArCondicionado(marca, modelo, consumo);
                        } else if (tipo == 2){
                            novoAparelho = new Coluna(marca, modelo, consumo);
                        } else if (tipo == 3){
                            novoAparelho = new RoboAspirador(marca, modelo, consumo);
                        } else if (tipo == 4){
                            novoAparelho = new Lampada(marca, modelo, consumo);
                        } else if (tipo == 5){
                            novoAparelho = new Persiana(marca, modelo, consumo);
                        } else if (tipo == 6){
                            novoAparelho = new Regador(marca, modelo, consumo);
                        } 
                        else if (tipo == 7){
                            novoAparelho = new Rele(marca, modelo, consumo);
                        }

                        // guarda na divisão e verifica se foi escolhida uma opção válida
                        if (novoAparelho != null) {
                            divSelecionada.addDispositivo(novoAparelho);
                            System.out.println("Aparelho [" + novoAparelho.getIdentificador() + "] instalado com sucesso na " + divSelecionada.getNome() + "!");
                        } else {
                            System.out.println("Opção de dispositivo inválida. Instalação cancelada.");
                        }
                        MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                        break;
                        
                    case 2:
                        if (!eAdmin) { 
                            System.out.println("Erro: Apenas o administrador da casa pode remover dispositivos."); 
                            break; 
                        }
                        System.out.print("Introduza o ID do dispositivo a remover: ");
                        String idRemover = input.nextLine().trim();
                        
                        // verificar se o dispositivo existe antes de apagar
                        if (divSelecionada.getDispositivos().containsKey(idRemover)) {
                            divSelecionada.removerDispositivo(idRemover);

                            // garante que o dispositivo também sai de todas as automações
                            casaAtual.removerDispositivoDasAutomacoes(idRemover);

                            MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                            System.out.println("Dispositivo [" + idRemover + "] removido com sucesso!");
                        } else {
                            System.out.println("Erro: Não existe nenhum dispositivo com o ID '" + idRemover + "' nesta divisão.");
                        }
                        break;

                    case 3:
                        if (divSelecionada.getDispositivos().isEmpty()) {
                            System.out.println("Aviso: Não há dispositivos nesta divisão para controlar.");
                            break;
                        }

                        System.out.print("Escreve o ID do dispositivo a controlar (Digite 0 para cancelar): ");
                        String idCtrl = input.nextLine().trim();

                        if (idCtrl.equals("0")) {
                            break;
                        }

                        // vai buscar o dispositivo ao mapa da divisão selecionada
                        Dispositivo disp = divSelecionada.getDispositivos().get(idCtrl);

                        if (disp != null) {
                            // se encontrar entra na oficina
                            oficinaControlo(input, disp);
                            MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas); // grava qualquer alteração feita no dispositivo
                        } else {
                            System.out.println("Erro: O dispositivo [" + idCtrl + "] não foi encontrado nesta divisão!");
                        }
                        break;
                        case 4: // agendar automação
                        System.out.println("\n--- APARELHOS NA DIVISÃO ---");
                        
                        // primeiro mostramos os aparelhos todos que estão nesta divisão
                        if (divSelecionada.getDispositivos().isEmpty()) {
                            System.out.println("Não existe aparelhos para automatizar nesta divisão.");
                        } else {
                            for (Dispositivo d : divSelecionada.getDispositivos().values()) {
                                System.out.println("- [" + d.getIdentificador() + "] " + d.getMarca() + " " + d.getModelo());
                            }
                            
                            System.out.print("\nDigite o ID do aparelho a automatizar: ");
                            String idBusca = input.nextLine().toUpperCase();
                            
                            // verifica se o utilizador escreveu um id válido que está na lista
                            if (divSelecionada.getDispositivos().containsKey(idBusca)) {
                                int hora = Leitor.lerInteiro(input, "hora (0-23): ");
                                int min = Leitor.lerInteiro(input, "minuto (0-59): ");
                                
                                System.out.println("Ação pretendida:\n1 - ligar\n2 - desligar");
                                int acao = Leitor.lerInteiro(input, "opção: ");
                                boolean deveLigar = (acao == 1);
                                
                                // agenda na casa
                                casaAtual.agendarTarefa(idBusca, hora, min, deveLigar);
                                System.out.println("Sucesso! Tarefa agendada para as " + String.format("%02d:%02d", hora, min));
                                MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                            } else {
                                System.out.println("Erro: aparelho não encontrado nesta divisão.");
                            }
                        }
                        break;
                    case 5:
                        menuAutomacoes(input, casaAtual, mapaCasas);
                    break;
                        
                    case 0:
                        break;
                        
                    default:
                        System.out.println("Erro: Opção inválida.");
                }
            }
        }

public static void oficinaControlo(Scanner input, Dispositivo d) {
        int opcao = -1;
        while (opcao != 0) {
            // limpa o terminal para manter a interface limpa e profissional
            Leitor.limparConsola();
            
            System.out.println("\n--- CONTROLAR: " + d.getIdentificador() + " (" + d.getClass().getSimpleName() + ") ---");
            
            // mostra o estado geral (comum a todos os aparelhos)
            System.out.println("Estado Atual: " + (d.isEstado() ? "LIGADO" : "DESLIGADO"));
            
            // mostra os detalhes específicos de cada aparelho no topo
            if (d instanceof ArCondicionado) {
                ArCondicionado ac = (ArCondicionado) d;
                System.out.println("Modo Atual: " + ac.getModo() + " | Temperatura: " + ac.getTemperatura() + "ºC");
            } else if (d instanceof Lampada) {
                System.out.println("Luminosidade: " + ((Lampada) d).getLuminosidade() + "% | Cor: " + ((Lampada) d).getTemperaturaCor() + "K");
            } else if (d instanceof Coluna) {
                System.out.println("Volume: " + ((Coluna) d).getVolume() + "% | Bass: " + (((Coluna) d).getEstadoBass() ? "Nível "+((Coluna) d).getIntensidadeBass() : "Off") + " | LEDs: " + (((Coluna) d).getEstadoLeds() ? ((Coluna) d).getCorLeds() : "Off"));
            } else if (d instanceof RoboAspirador) {
                System.out.println("Modo: " + ((RoboAspirador) d).getModoLimpeza() + " | Na Base: " + (((RoboAspirador) d).isNaBase() ? "Sim" : "Não"));
            } else if (d instanceof Persiana) {
                System.out.println("Altura: " + ((Persiana) d).getAlturaPersiana() + "% | Lâminas: " + ((Persiana) d).getAnguloLaminas() + "º | Sensor: " + (((Persiana) d).getModoSensorSolar() ? "On" : "Off"));
            } else if (d instanceof Regador) {
                System.out.println("Rega: " + ((Regador) d).getModoRega() + " | Sensor Chuva: " + (((Regador) d).getModoSensorChuva() ? "On" : "Off"));
            }

            System.out.println("\n1 - Ligar");
            System.out.println("2 - Desligar");
            
            // mostra as opções dinâmicas consoante o tipo de dispositivo
            if (d instanceof ArCondicionado) {
                System.out.println("3 - Ajustar Temperatura");
                System.out.println("4 - Mudar Modo (Frio/Quente...)");
            } else if (d instanceof Coluna) {
                System.out.println("3 - Ajustar Volume (0-100)");
                System.out.println("4 - Ligar / Desligar Bass");
                System.out.println("5 - Ajustar Intensidade do Bass (0-10)");
                System.out.println("6 - Ligar / Desligar LEDs");
                System.out.println("7 - Mudar Cor dos LEDs");
            } else if (d instanceof RoboAspirador) {
                System.out.println("3 - Mudar Modo de Limpeza");
                System.out.println("4 - Colocar / Retirar da Base de Carregamento");
            } else if (d instanceof Lampada) {
                System.out.println("3 - Ajustar Luminosidade (0-100)");
                System.out.println("4 - Mudar Cor da Luz");
            } else if (d instanceof Persiana) {
                System.out.println("3 - Ajustar Altura da Persiana (0-100)");
                System.out.println("4 - Ajustar Ângulo das Lâminas (0-90)");
                System.out.println("5 - Ligar / Desligar Sensor Solar");
            } else if (d instanceof Regador) {
                System.out.println("3 - Mudar Modo de Rega");
                System.out.println("4 - Ligar / Desligar Sensor de Chuva");
            }
            // o relé como só liga e desliga não precisa de botões extra
            
            System.out.println("0 - Voltar");
            opcao = Leitor.lerInteiro(input, "Escolha uma ação: ");  

            try {
                switch (opcao) {
                    case 1: d.setEstado(true); System.out.println("Aparelho ligado!"); break;
                    case 2: d.setEstado(false); System.out.println("Aparelho desligado!"); break;
                    
                    case 3:
                        if (d instanceof ArCondicionado) {
                            double temp = Leitor.lerDouble(input, "Nova Temperatura (16-30): "); ((ArCondicionado) d).setTemperatura(temp);
                        } else if (d instanceof Coluna) {
                            int vol = Leitor.lerInteiro(input, "Novo Volume (0-100): "); ((Coluna) d).selecionarVolume(vol); System.out.println();
                        } else if (d instanceof RoboAspirador) {
                            System.out.print("Novo Modo (Silencioso/Esfregona/Turbo/Normal): "); ((RoboAspirador) d).setModoLimpeza(input.nextLine());
                        } else if (d instanceof Lampada) {
                            int lum = Leitor.lerInteiro(input, "Nova Luminosidade (0 a 100): "); ((Lampada) d).setLuminosidade(lum);
                        } else if (d instanceof Persiana) {
                            int alt = Leitor.lerInteiro(input, "Nova Altura (0 a 100): "); ((Persiana) d).setAlturaPersiana(alt);
                        } else if (d instanceof Regador) {
                            System.out.print("Novo Modo (Jato/Dispersão/Névoa/Goteamento): "); ((Regador) d).setModoRega(input.nextLine());
                        }
                        break;
                        
                    case 4:
                        if (d instanceof ArCondicionado) {
                            System.out.print("Novo Modo: "); ((ArCondicionado) d).setModo(input.nextLine());
                        } else if (d instanceof Coluna) {
                            boolean estadoBass = ((Coluna) d).getEstadoBass();
                            ((Coluna) d).setBass(!estadoBass); // inverte o estado atual
                        } else if (d instanceof RoboAspirador) {
                            boolean estadoBase = ((RoboAspirador) d).isNaBase();
                            ((RoboAspirador) d).setBaseCarregamento(!estadoBase);
                            System.out.println(((RoboAspirador) d).isNaBase() ? "Robô está na zona de carregamento" : " Robô saiu da zona de carregamento");
                        } else if (d instanceof Lampada) {
                            System.out.println("Cores disponíveis: Branco Quente | Branco Neutro | Branco Frio");
                            System.out.print("Temperatura da cor (2700 a 4000): "); ((Lampada) d).setTemperaturaCor(Integer.parseInt(input.nextLine().trim()));
                        } else if (d instanceof Persiana) {
                            int ang = Leitor.lerInteiro(input, "Novo Ângulo (0 a 90): "); ((Persiana) d).setAnguloLaminas(ang);
                        } else if (d instanceof Regador) {
                            boolean estadoChuva = ((Regador) d).getModoSensorChuva();
                            ((Regador) d).setModoSensorChuva(!estadoChuva);
                        }
                        break;

                    case 5:
                        if (d instanceof Coluna) {
                            int intBass = Leitor.lerInteiro(input, "Nova Intensidade Bass (0-10): "); ((Coluna) d).setIntensidadeBass(intBass);
                        } else if (d instanceof Persiana) {
                            boolean estadoSolar = ((Persiana) d).getModoSensorSolar();
                            ((Persiana) d).setModoSensorSolar(!estadoSolar);
                        }
                        break;
                    
                    case 6:
                        if (d instanceof Coluna) {
                            boolean estadoLeds = ((Coluna) d).getEstadoLeds();
                            ((Coluna) d).setLeds(!estadoLeds);
                        }
                        break;

                    case 7:
                        if (d instanceof Coluna) {
                            System.out.println("Cores: Verde | Vermelho | Azul | Multicolor");
                            System.out.print("Cor desejada: "); ((Coluna) d).setCorLED(input.nextLine());
                        }
                        break;
                    case 0: 
                        break;
                default: 
                    System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                // apanha as regras que definiste nas classes dos aparelhos
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    public static void subMenuControlarAparelho(Scanner input, Dispositivo d) {
        
        if (d instanceof ArCondicionado) {
            ArCondicionado ac = (ArCondicionado) d; 
            
            System.out.println("\n[ Painel do Ar Condicionado: " + ac.getIdentificador() + " ]");
            System.out.println("Estado atual -> Temp: " + ac.getTemperatura() + " | Modo: " + ac.getModo());
            
            double novaTemp = Leitor.lerDouble(input, "Introduz a nova temperatura (16 a 30): ");
            
            try {
                ac.setTemperatura(novaTemp);
                System.out.println("Temperatura alterada com sucesso para " + ac.getTemperatura() + " graus.");
            } catch (IllegalArgumentException e) {
                System.out.println("Erro do Sistema: " + e.getMessage());
            }
        }
    }
    public static void menuAutomacoes(Scanner input, Casa casaAtual, Map<String, Casa> mapaCasas) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- GESTÃO DE AUTOMAÇÕES: " + casaAtual.getNomeCasa() + " ---");
            System.out.println("1 - Criar Nova Automação");
            System.out.println("2 - Ativar Automação");
            System.out.println("3 - Remover Automação");
            System.out.println("4 - Obter Sugestões Inteligentes");
            System.out.println("0 - Voltar");
            
            opcao = Leitor.lerInteiro(input, "Opção: ");

            switch (opcao) {
                case 1:
                    System.out.println("\nDispositivos disponíveis na casa:");
                        for(Divisao div : casaAtual.getDivisoes().values()) {
                            for(Dispositivo disp : div.getDispositivos().values()) {
                                System.out.println("  - [" + disp.getIdentificador() + "] " + disp.getClass().getSimpleName() + " em " + div.getNome());
                            }
                        }
                    // cria uma nova automação personalizada
                    System.out.print("\nNome da automação: ");
                    String nome = input.nextLine();
                    Automacao nova = new Automacao(nome);

                    boolean mais = true;
                    while (mais) {
                        System.out.print("ID do aparelho: ");
                        String id = input.nextLine().toUpperCase();
                        
                        if (casaAtual.procurarDispositivoPorId(id) != null) {
                            int acao = Leitor.lerInteiro(input, "Ação (1-Ligar / 2-Desligar): ");
                            nova.adicionarComando(id, (acao == 1));
                        } else {
                            System.out.println("Erro: Aparelho não encontrado.");
                        }

                        System.out.print("Adicionar mais? (S/N): ");
                        if (!input.nextLine().trim().equalsIgnoreCase("S")) mais = false;
                    }
                    casaAtual.adicionarAutomacao(nova);
                    MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                    break;

                case 2:
                    // lista e executa automações existentes
                    if (casaAtual.getAutomacoes().isEmpty()) {
                        System.out.println("Aviso: Sem automações guardadas.");
                    } else {
                        casaAtual.getAutomacoes().keySet().forEach(n -> System.out.println("- " + n));
                        System.out.print("Nome da automação a ativar: ");
                        String ativar = input.nextLine();
                        casaAtual.executarAutomacao(ativar);
                        MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                    }
                    break;
                
                case 3:
                    if (casaAtual.getAutomacoes().isEmpty()) {
                        System.out.println("\nAviso: Não existem automações para remover.");
                    } else {
                        System.out.println("\n--- AUTOMAÇÕES DISPONÍVEIS ---");
                        casaAtual.getAutomacoes().keySet().forEach(n -> System.out.println("- " + n));
                        
                        System.out.print("\nDigite o Nome da automação a apagar: ");
                        String nomeApagar = input.nextLine();
                        
                        if (casaAtual.getAutomacoes().containsKey(nomeApagar.toUpperCase())) {
                            casaAtual.removerAutomacao(nomeApagar);
                            MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                            System.out.println("Sucesso: Automação '" + nomeApagar + "' removida!");
                        } else {
                            System.out.println("Erro: Automação não encontrada.");
                        }
                    }
                    break;
                case 4:
                    sugerirAutomacoesInteligentes(input, casaAtual, mapaCasas);
                    break;

                case 0: 
                    break;
                    
                default: 
                    System.out.println("Erro: Opção inválida.");
            }
        }
    }

    public static void menuMudarFornecedor(Scanner input, Casa casaAtual, Map<String, Casa> mapaCasas) {
        System.out.println("\n--- GESTÃO DE CONTRATO DE ENERGIA ---");
        // mostra o fornecedor que está ativo no momento
        System.out.println("Fornecedor atual: " + casaAtual.getFornecedor().getNomeFornecedor());
        System.out.println("1 - Mudar para EDP (0.15€/kWh)");
        System.out.println("2 - Mudar para Galp (0.13€/kWh + 5€ taxa fixa)");
        System.out.println("0 - Voltar");

        int escolha = Leitor.lerInteiro(input, "Escolha a sua opção: ");

        switch (escolha) {
            case 1:
                // define a edp como novo fornecedor
                casaAtual.setFornecedor(new EDP());
                System.out.println("sucesso: Contrato alterado para a edp!");
                break;
            case 2:
                // define a galp como novo fornecedor
                casaAtual.setFornecedor(new Galp());
                System.out.println("Sucesso: Contrato alterado para a galp!");
                break;
            case 0:
                return;
            default:
                System.out.println("Erro: opção inválida.");
                return;
        }
        
        // grava a alteração no ficheiro casas.dat para não perder o novo contrato
        MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
    }

    // funções auxiliares para o menu de estatísticas
    private static void visualizarConsumoESimulacao(Scanner input, Utilizador logado, Map<String, Casa> mapaCasas) {
        System.out.print("\nIntroduza o ID da Casa: ");
        String idLido = input.nextLine().toUpperCase();
        Casa casaAtual = mapaCasas.get(idLido);
        
        if (casaAtual != null && (casaAtual.getIdDono().equals(logado.getId()) || casaAtual.getAutorizados().contains(logado.getId()))) {
            System.out.println("\n[DADOS DE CONSUMO]");
            System.out.println("Potência Instantânea: " + casaAtual.getConsumoTotalCasa() + " Watts");
            System.out.println("Energia Acumulada: " + String.format("%.2f", casaAtual.getEnergiaTotalConsumidaWh() / 1000.0) + " kWh");
            
            // lógica do top 3 dispositivos
            List<Dispositivo> todosDisp = new ArrayList<>();
            for (Divisao d : casaAtual.getDivisoes().values()) {
                todosDisp.addAll(d.getDispositivos().values());
            }
            
            if (!todosDisp.isEmpty()) {
                System.out.println("\n-> Top 3 Dispositivos (Uso):");
                todosDisp.sort((d1, d2) -> Double.compare(d2.getTempoLigado(), d1.getTempoLigado()));
                for (int i = 0; i < Math.min(3, todosDisp.size()); i++) {
                    System.out.println("   " + (i+1) + "º: " + todosDisp.get(i).getIdentificador() + " (" + String.format("%.2f", todosDisp.get(i).getTempoLigado()) + "h)");
                }
            }

            System.out.print("\nPretende simular o avanço do tempo? (S/N): ");
            if (input.nextLine().trim().equalsIgnoreCase("S")) {
                int dias = Leitor.lerInteiro(input, "Dias a simular: ");
                if (dias > 0) {
                    double gastoWh = casaAtual.avancarTempo(dias * 24 * 60); 
                    MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas); 
                    
                    // cálculo usando o fornecedor real da casa e não um valor fixo
                    double custo = casaAtual.getFornecedor().calcularFatura(gastoWh / 1000.0);
                    System.out.println("Simulação concluída. Gasto: " + String.format("%.2f", gastoWh / 1000.0) + " kWh");
                    System.out.println("Custo estimado (" + casaAtual.getFornecedor().getNomeFornecedor() + "): " + String.format("%.2f", custo) + "€");
                }
            }
        } else {
            System.out.println("Erro: Acesso negado ou casa inexistente.");
        }
    }

    private static void processarFaturaEPagamento(Scanner input, Utilizador logado, Map<String, Casa> mapaCasas) {
        System.out.print("\nIntroduza o ID da Casa para faturação: ");
        String idF = input.nextLine().toUpperCase();
        Casa casaF = mapaCasas.get(idF);

        // verifica se a casa existe e se o utilizador tem permissão
        if (casaF != null && (casaF.getIdDono().equals(logado.getId()) || casaF.getAutorizados().contains(logado.getId()))) {
            double valorFinal = casaF.emitirFaturaEletricidade();

            System.out.println("\n--- EMISSÃO DE FATURA: " + casaF.getNomeCasa() + " ---");
            System.out.println("Fornecedor: " + casaF.getFornecedor().getNomeFornecedor());
            System.out.println("Consumo acumulado: " + String.format("%.2f", casaF.getEnergiaTotalConsumidaWh() / 1000.0) + " kWh");
            System.out.println("Total a pagar: " + String.format("%.2f", valorFinal) + " €");

            if (valorFinal > 0) {
                System.out.print("\nDeseja pagar agora e reiniciar o contador? (S/N): ");
                if (input.nextLine().trim().equalsIgnoreCase("S")) {
                    // faz o reset do contador na classe casa
                    casaF.pagarFatura();
                    MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                    System.out.println("Sucesso: Contador reiniciado!");
                }
            }
        } else {
            System.out.println("Erro: Sem permissão ou casa inexistente.");
        }
    }

    private static void mostrarCasaMaiorConsumo(Map<String, Casa> mapaCasas) {
        if (mapaCasas.isEmpty()) {
            System.out.println("Não existem casas registadas.");
            return;
        }

        Casa vencedora = null;
        double max = -1;

        // percorre todas as casas do sistema para encontrar a mais gastadora
        for (Casa c : mapaCasas.values()) {
            if (c.getConsumoTotalCasa() > max) {
                max = c.getConsumoTotalCasa();
                vencedora = c;
            }
        }

        if (vencedora != null) {
            System.out.println("\nCasa com maior consumo instantâneo: " + vencedora.getNomeCasa());
            System.out.println("Consumo atual: " + String.format("%.2f", max) + " W");
        }
    }

    private static void mostrarTopDivisoes(Map<String, Casa> mapaCasas) {
        // cria uma lista com todas as divisões de todas as casas
        List<Divisao> todas = new ArrayList<>();
        for (Casa c : mapaCasas.values()) {
            todas.addAll(c.getDivisoes().values());
        }

        if (todas.isEmpty()) {
            System.out.println("Não existem divisões registadas.");
            return;
        }

        // ordena as divisões pelo número de dispositivos (maior para menor)
        todas.sort((d1, d2) -> Integer.compare(d2.getDispositivos().size(), d1.getDispositivos().size()));

        System.out.println("\n--- TOP 3 DIVISÕES COM MAIS DISPOSITIVOS ---");
        for (int i = 0; i < Math.min(3, todas.size()); i++) {
            Divisao d = todas.get(i);
        System.out.println((i + 1) + "º Lugar: " + d.getNome() + " (" + d.getDispositivos().size() + " dispositivos)");        
        }
    }

    private static void mostrarLogsCasa(Scanner input, Utilizador logado, Map<String, Casa> mapaCasas) {
        System.out.print("\nIntroduza o ID da Casa: ");
        String id = input.nextLine().toUpperCase();
        Casa c = mapaCasas.get(id);

        if (c != null && (c.getIdDono().equals(logado.getId()) || c.getAutorizados().contains(logado.getId()))) {
            System.out.println("\n--- HISTÓRICO DE ATIVIDADES ---");
            if (c.getLogAtividades().isEmpty()) {
                System.out.println("Sem atividades registadas.");
            } else {
                // imprime cada linha do histórico guardado na casa
                for (String log : c.getLogAtividades()) {
                    System.out.println(log);
                }
            }
        } else {
            System.out.println("Erro: Acesso negado.");
        }
    }

   public static void menuPrincipalCasa(Scanner input, Casa casa, Map<String, Utilizador> mapaUtils, Map<String, Casa> mapaCasas, Utilizador logado) {
        int opcao = -1;
        // verifica permissões administrativas (dono ou co-admin)
        boolean eAdmin = casa.temPermissaoAdministrativa(logado.getId());
        boolean eDonoReal = casa.getIdDono().equals(logado.getId());

        while (opcao != 0) {
            Leitor.limparConsola();
            System.out.println(" HUB DA CASA: " + casa.getNomeCasa());
            System.out.println(" Perfil: " + (eDonoReal ? "Proprietário" : (eAdmin ? "Co-Administrador" : "Convidado")));
            System.out.println("-----------------------------------------");
            System.out.println("1 - Gerir Divisões e Dispositivos");
            System.out.println("2 - Estatísticas, Simulação e Fatura");
            System.out.println("3 - Automações e Cenários");
            
            if (eDonoReal) {
                System.out.println("4 - Gerir Permissões (Promover/Remover)");
            }
            System.out.println("0 - Sair desta Casa");

            opcao = Leitor.lerInteiro(input, "Escolha: ");

            switch (opcao) {
                case 1: menuGerirDivisoes(input, casa, mapaCasas, eAdmin); break;
                case 2: menuEstatisticasESimulacao(input, logado, mapaCasas); break;
                case 3: menuAutomacoes(input, casa, mapaCasas); break;
                case 4: 
                    if (eDonoReal) menuGestaoPermissoes(input, casa, mapaUtils, mapaCasas); 
                    else System.out.println("Erro: Apenas o proprietário pode gerir acessos.");
                    break;
            }
        }
    }
    public static void menuGestaoPermissoes(Scanner input, Casa casa, Map<String, Utilizador> mapaUtils, Map<String, Casa> mapaCasas) {
        int opcao = -1;
        while (opcao != 0) {
            Leitor.limparConsola();
            System.out.println("--- GESTÃO DE ACESSOS: " + casa.getNomeCasa() + " ---");
            System.out.println("1 - Promover a Co-Administrador (Controlo Total)");
            System.out.println("2 - Despromover a Convidado (Apenas Leitura)");
            System.out.println("3 - Remover Acesso Total (Expulsar da Casa)");
            System.out.println("0 - Voltar");

            // listagem profissional com nomes associados aos ids
            System.out.println("\nUTILIZADORES COM ACESSO:");
            if (casa.getAutorizados().isEmpty()) {
                System.out.println("  (Não existem convidados nesta casa)");
            } else {
                for (String id : casa.getAutorizados()) {
                    // procura o nome do utilizador no mapa global através do id
                    String nomeUser = "Desconhecido";
                    for (Utilizador u : mapaUtils.values()) {
                        if (u.getId().equalsIgnoreCase(id)) {
                            nomeUser = u.getNome();
                            break;
                        }
                    }
                    
                    String cargo = casa.getCoAdmins().contains(id) ? "[CO-ADMIN]" : "[GUEST]";
                    System.out.println("  - [" + id + "] " + nomeUser + " -> " + cargo);
                }
            }

            opcao = Leitor.lerInteiro(input, "\nOpção: ");
            
            if (opcao >= 1 && opcao <= 3) {
                System.out.print("Introduz o ID do utilizador: ");
                String idAlvo = input.nextLine().toUpperCase();
                
                if (casa.getAutorizados().contains(idAlvo)) {
                    switch (opcao) {
                        case 1:
                            casa.adicionarCoAdmin(idAlvo);
                            System.out.println("Sucesso: " + idAlvo + " agora é Co-Administrador!");
                            break;
                        case 2:
                            casa.removerCoAdmin(idAlvo);
                            System.out.println("Sucesso: Privilégios administrativos de " + idAlvo + " removidos.");
                            break;
                        case 3:
                            // remove de ambas as listas para garantir limpeza total
                            casa.getAutorizados().remove(idAlvo);
                            casa.removerCoAdmin(idAlvo);
                            System.out.println("Sucesso: O utilizador " + idAlvo + " foi expulso da casa.");
                            break;
                    }
                    MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                } else {
                    System.out.println("Erro: Esse ID não consta na lista de autorizados.");
                }
                System.out.println("Prima Enter para continuar...");
                input.nextLine();
            }
        }
    }

    private static void sugerirAutomacoesInteligentes(Scanner input, Casa casaAtual, Map<String, Casa> mapaCasas) {
        System.out.println("\n--- MOTOR DE SUGESTÕES INTELIGENTES ---");
        System.out.println("A analisar o histórico de interações da casa...");

        Dispositivo maisUsado = null;
        int maxInteracoes = 0;

        // vê em toda a casa à procura do dispositivo mais utilizado pelo utilizador
        for (Divisao d : casaAtual.getDivisoes().values()) {
            for (Dispositivo disp : d.getDispositivos().values()) {
                if (disp.getNumeroAtivacoes() > maxInteracoes) {
                    maxInteracoes = disp.getNumeroAtivacoes();
                    maisUsado = disp;
                }
            }
        }

        // se encontrou um aparelho com uso significativo
        if (maisUsado != null && maxInteracoes > 3) {
            String nomeCenarioSugerido = "Rotina " + maisUsado.getClass().getSimpleName();
            
            // verifica se a sugestão já existe para não chatear o utilizador
            if (casaAtual.getAutomacoes().containsKey(nomeCenarioSugerido.toUpperCase())) {
                System.out.println("O sistema está otimizado. Não há novas sugestões de momento.");
                return;
            }

            System.out.println("\n[PADRÃO DETETADO]");
            System.out.println("O dispositivo '" + maisUsado.getIdentificador() + "' tem sido muito utilizado (" + maxInteracoes + " ativações).");
            System.out.print("Deseja que o sistema crie um cenário '" + nomeCenarioSugerido + "' para o ligar rapidamente? (S/N): ");
            
            if (input.nextLine().trim().equalsIgnoreCase("S")) {
                Automacao autoSugerida = new Automacao(nomeCenarioSugerido);
                
                // dizemos à automação para LIGAR (true) o aparelho mais usado
                autoSugerida.adicionarComando(maisUsado.getIdentificador(), true);
                
                // guarda a nova automação na casa usando o teu método
                casaAtual.adicionarAutomacao(autoSugerida); 
                MenuUI.guardarSeguro(MenuUI.GESTOR::guardarCasas, mapaCasas);
                
                System.out.println("Sucesso: A Inteligência da casa gerou e guardou o cenário '" + nomeCenarioSugerido + "'!");
            } else {
                System.out.println("Sugestão ignorada pelo utilizador.");
            }
        } else {
            System.out.println("Ainda não há dados suficientes de uso para gerar sugestões fiáveis. Utilize mais os aparelhos.");
        }
    }
}
