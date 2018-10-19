package me.distsys.client;

import me.distsys.common.ClientSubscription;
import me.distsys.common.SearchParams;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

// classe que implementa a UI do programa. Todos os menus e submenus, sao exibidos aqui.
public class ClientMain {
    private ClientInterfaceImpl clientImpl;
    private Scanner scanner;

    public ClientMain() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1100);
        clientImpl = new ClientInterfaceImpl(registry);
        scanner = new Scanner(System.in);
    }

    /**
     * desaloca todas os recursos inscritos pelo cliente
     */
    private void clearResourcesOnServer() {
//        TODO CLEAR NOTICATION SUBSCRIPTIONS
//            // recupera a lista de arquivos inscritos pelo cliente
//            //String[] strings = client.listClientSubscribedFiles();
//            // um a um se desinscreve
//            for (String string : strings) client.unsubscribe(string);
    }


    //<editor-fold desc="UI Methods">
    // menu principal
    public void mainMenu() throws RemoteException {
        int choice, choice2, comprar;
        int[] id;
        int idHotel;
        boolean buyed, result;
        ClientSubscription subscription;
        do {
            System.out.println("1 - Consultar e comprar \n2 - Inscrever-se em notificações\n3 - Cancelar inscrição\n4 - Sair");
            choice = scanner.nextInt();
            // escolha das opções
            switch (choice) {
                case 1: {
                    System.out.println("1 - Passagens\n2 - Hospedagem\n3 - Pacotes\n4 - Retornar a menu principal");
                    choice2 = scanner.nextInt();
                    switch (choice2) {
                        case 1: {
                            SearchParams searchParams = showFlightSearchForm(1);
                            id = clientImpl.serverInterface.consultPlaneTickets(searchParams);
                            if (id[0] == -1) {//Ida nao encontrada
                                System.out.println("\nPassagem com os seguintes parâmetros não encontrada!\n");
                            }
                            else if(searchParams.idaEVolta == true && id[1] == -1)
                                System.out.println("\nPassagem com os seguintes parâmetros não encontrada!\n");
                            else {
                                System.out.println("\nPassagem com os seguintes parâmetros encontrada!\nComprar?\n1 - Sim\n2 - Não");
                                comprar = scanner.nextInt();
                                if(comprar == 1) {
                                    buyed = clientImpl.serverInterface.buyPlaneTickets(id, searchParams.numeroPessoas);
                                    if (buyed == true)
                                        System.out.println("\nCompra bem sucedida\n");
                                    else
                                        System.out.println("\nErro na compra!\n");

                                }
                            }
                            break;
                        }
                        case 2: {
                            SearchParams searchParams = showHotelSearchForm();
                            idHotel = clientImpl.serverInterface.consultAccomodation(searchParams);
                            if (idHotel == -1) {//Hospedagem nao encontrada
                                System.out.println("\nHospedagem com os seguintes parâmetros não encontrado!\n");
                            }
                            else {
                                System.out.println("\nHospedagem com os seguintes parâmetros encontrada!\nComprar?\n1 - Sim\n2 - Não");
                                comprar = scanner.nextInt();
                                if(comprar == 1) {
                                    buyed = clientImpl.serverInterface.buyAccomodation(idHotel, searchParams.numeroQuartos, searchParams.numeroPessoas);
                                    if (buyed == true)
                                        System.out.println("\nCompra bem sucedida\n");
                                    else
                                        System.out.println("\nErro na compra. Hospedagem sem vagas!\n");
                                }
                            }
                            break;
                        }
                        case 3: {
                            SearchParams searchParams = showPackageForm();
                            id = clientImpl.serverInterface.consultPackages(searchParams); //realiza consulta
                            if (id[0] == -1) {//Ida nao encontrada
                                System.out.println("\nPacote com os seguintes parâmetros não encontrado!\n");
                            }
                            else if(searchParams.idaEVolta == true && id[1] == -1)
                                System.out.println("\nPacote com os seguintes parâmetros não encontrado!\n");
                            else if (id[2] == -1) {//Hospedagem nao encontrada
                                System.out.println("\nPacote com os seguintes parâmetros não encontrado!\n");
                            }
                            else {
                                System.out.println("\nPacote com os seguintes parâmetros encontrada!\nComprar?\n1 - Sim\n2 - Não");
                                comprar = scanner.nextInt();
                                if(comprar == 1) {
                                    buyed = clientImpl.serverInterface.buyPackage(id, searchParams.numeroQuartos, searchParams.numeroPessoas);
                                    if (buyed == true)
                                        System.out.println("\nCompra bem sucedida\n");
                                    else
                                        System.out.println("\nErro na compra. Hospedagem sem vagas!\n");
                                }
                            }
                            //clientImpl.serverInterface.buyPackage(origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos);
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                }
                case 2:
                    System.out.println("1 - Passagens\n2 - Hospedagem\n3 - Pacotes\n4 - Retornar a menu principal");
                    choice2 = scanner.nextInt();
                    switch (choice2) {
                        case 1: {
                            SearchParams searchParams = showFlightSearchForm(2);
                            System.out.println("Preço máximo por pessoa: ");
                            searchParams.preço = scanner.nextInt();
                            subscription = new ClientSubscription(clientImpl);
                            result = clientImpl.serverInterface.subscribe(searchParams, subscription);
                            System.out.println(result ? "Inscrição em passagem feita com sucesso!" : "Inscrição em passagem falhou!");
                            break;
                        }
                        case 2:{
                            SearchParams searchParams = showHotelSearchForm();
                            System.out.println("Preço máximo por pessoa: ");
                            searchParams.preço = scanner.nextInt();
                            subscription = new ClientSubscription(clientImpl);
                            result = clientImpl.serverInterface.subscribe(searchParams, subscription);
                            System.out.println(result ? "Inscrição em hospedagem feita com sucesso!" : "Inscrição em hospedagem falhou!");
                            break;
                        }
                        case 3: {
                            SearchParams searchParams = showPackageForm();
                            subscription = new ClientSubscription(clientImpl);
                            result = clientImpl.serverInterface.subscribe(searchParams, subscription);
                            System.out.println(result ? "Inscrição em pacote feita com sucesso!" : "Inscrição em pacote falhou!");
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                case 3:
                    System.out.println("1 - Passagens\n2 - Hospedagem\n3 - Pacotes\n4 - Retornar a menu principal");
                    choice2 = scanner.nextInt();
                    switch (choice2) {
                        case 1: {
                            SearchParams searchParams = showFlightSearchForm(3);
                            subscription = new ClientSubscription(clientImpl);
                            result = clientImpl.serverInterface.unsubscribe(searchParams, subscription);
                            System.out.println(result ? "Cancelamento de Inscrição em passagem feita com sucesso!" : "Cancelamento de Inscrição em passagem falhou!");
                            break;
                        }
                        case 2:{
                            SearchParams searchParams = showHotelSearchForm();
                            subscription = new ClientSubscription(clientImpl);
                            result = clientImpl.serverInterface.unsubscribe(searchParams, subscription);
                            System.out.println(result ? "Cancelamento de Inscrição em hospedagem feita com sucesso!" : "Cancelamento de Inscrição em hospedagem falhou!");
                            break;
                        }
                        case 3: {
                            SearchParams searchParams = showPackageForm();
                            subscription = new ClientSubscription(clientImpl);
                            result = clientImpl.serverInterface.unsubscribe(searchParams, subscription);
                            System.out.println(result ? "Cancelamento de Inscrição em pacote feita com sucesso!" : "Cancelamento de Inscrição em pacote falhou!");
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                case 4:
                    //clearResourcesOnServer();
                    break;
            }
        } while (choice != 4);
    }

    private SearchParams showPackageForm(){
        SearchParams searchParams = new SearchParams();

        System.out.println("\n1 - Ida e volta\n2 - Somente ida");
        int choice = scanner.nextInt();
        scanner.nextLine();
        searchParams.idaEVolta = choice == 1;
        System.out.println("Origem: ");
        searchParams.origem = scanner.nextLine();
        System.out.println("Destino: ");
        searchParams.destino = scanner.nextLine();
        System.out.println("Data de ida (dd/mm/aaaa): ");
        searchParams.dataIda = scanner.nextLine();
        if (searchParams.idaEVolta) {
            System.out.println("Data de volta (dd/mm/aaaa): ");
            searchParams.dataVolta = scanner.nextLine();
        } else {
            searchParams.dataVolta = null;
        }

        System.out.println("Número de pessoas: ");
        searchParams.numeroPessoas = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\nInforme o nome do hotel: ");
        searchParams.hotel = scanner.nextLine();

        System.out.println("Informe a data de entrada (dd/mm/aaaa): ");
        searchParams.dataEntrada = scanner.nextLine();

        System.out.println("Informe a data de saída (dd/mm/aaaa): ");
        searchParams.dataSaida = scanner.nextLine();

        System.out.println("Informe o número de quartos: ");
        searchParams.numeroQuartos = scanner.nextInt();
        scanner.nextLine();

        return searchParams;
    }

    private SearchParams showFlightSearchForm(int menuNumber) {
        SearchParams searchParams = new SearchParams();
        searchParams.idaEVolta = false;

        if(menuNumber == 1) { //Somente compra permite ida e volta
            System.out.println("\n1 - Ida e volta\n2 - Somente ida");
            int choice = scanner.nextInt();
            searchParams.idaEVolta = choice == 1;
        }
        scanner.nextLine();
        System.out.println("Origem: ");
        searchParams.origem = scanner.nextLine();
        System.out.println("Destino: ");
        searchParams.destino = scanner.nextLine();
        System.out.println("Data de ida (dd/mm/aaaa): ");
        searchParams.dataIda = scanner.nextLine();
        if (searchParams.idaEVolta) {
            System.out.println("Data de volta (dd/mm/aaaa): ");
            searchParams.dataVolta = scanner.nextLine();
        } else {
            searchParams.dataVolta = null;
        }

        System.out.println("Número de pessoas: ");
        searchParams.numeroPessoas = scanner.nextInt();
        scanner.nextLine();

        return searchParams;
    }

    private SearchParams showHotelSearchForm() {
        SearchParams searchParams = new SearchParams();

        System.out.println("\nInforme o nome do hotel: ");
        scanner.nextLine();
        searchParams.hotel = scanner.nextLine();

        System.out.println("Informe a data de entrada (dd/mm/aaaa): ");
        searchParams.dataEntrada = scanner.nextLine();

        System.out.println("Informe a data de saída (dd/mm/aaaa): ");
        searchParams.dataSaida = scanner.nextLine();

        System.out.println("Informe o número de quartos: ");
        searchParams.numeroQuartos = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Informe o número de pessoas: ");
        searchParams.numeroPessoas = scanner.nextInt();
        scanner.nextLine();

        return searchParams;
    }



    //</editor-fold>

    //<editor-fold desc="Event notification methods">
/*
    // submenu de desinscrição de interesse em um arquivo
    private void unsubscribeSection() {
            scanner.nextLine();
            System.out.println("Currently subscribed items");
            String[] strings = client.listClientSubscribedFiles();
            Arrays.stream(strings).forEach(System.out::println);
            System.out.println("Enter a file name: ");
            String fileName = scanner.nextLine();

            boolean result = client.unsubscribe(fileName);
            System.out.println(result ? "File unsubscribed successfully!" : "File unsubscribe failed!");
    }


    /**
     * Main do cliente
     *
     * @param args the command line arguments
     */

    //TODO fazer o subscribe notify e unsubscribe certo pra tudo

        public static void main(String[] args) {
        try {
            ClientMain m = new ClientMain();
            m.mainMenu();
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        // System.exit é usado porque o cliente RMI inicializa um Daemon que não encerra com o fim da execução principal.
        System.exit(0);
    }
}
