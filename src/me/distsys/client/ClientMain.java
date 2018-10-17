package me.distsys.client;

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
        int choice, choice2;
        boolean buyed;
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
                            SearchParams searchParams = showFlightSearchForm();
                            int[] id = clientImpl.serverInterface.consultPlaneTickets(searchParams);
                            buyed = clientImpl.serverInterface.buyPlaneTickets(id, searchParams.numeroPessoas);
                            if(buyed == true)
                                System.out.println("\nCompra bem sucedida\n");
                            else
                                System.out.println("\nPassagem com os seguintes parâmetros não encontrada!\n");
                            break;
                        }
                        case 2: {
                            SearchParams searchParams = showHotelSearchForm();
                            int id = clientImpl.serverInterface.consultAccomodation(searchParams);
                            buyed = clientImpl.serverInterface.buyAccomodation(id, searchParams.numeroQuartos, searchParams.numeroPessoas);
                            if(buyed == true)
                                System.out.println("\nCompra bem sucedida\n");
                            else
                                System.out.println("\nHotel com os seguintes parâmetros não encontrada!\n");
                            break;
                        }
                        case 3: {
                            System.err.println("Not implemented yet!");
//                            showFlightSearchForm();
//                            showHotelSearchForm();
//                            clientImpl.serverInterface.consultPackages(origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos);
//                            clientImpl.serverInterface.buyPackage(origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos);
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                }
                case 2:
                    System.err.println("Not implemented yet!");
                    break;
                case 3:
                    System.err.println("Not implemented yet!");
                    break;
                case 4:
                    clearResourcesOnServer();
                    break;
            }
        } while (choice != 4);
    }

    private SearchParams showFlightSearchForm() {
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

    // submenu de inscrição de interesse em um arquivo
    private void subscribeSection() {
            scanner.nextLine();
            System.out.println("Currently subscribed items");
            String[] strings = client.listClientSubscribedFiles();
            Arrays.stream(strings).forEach(System.out::println);
            System.out.println("Enter a file name: ");
            String fileName = scanner.nextLine();
            System.out.println("For how long to be subscribed: ");
            String s = scanner.next(Pattern.compile("\\d{2}:\\d{2}"));
            String[] split = s.split(":");
            Duration duration = Duration.ofHours(Long.valueOf(split[0])).plusMinutes(Long.valueOf(split[1]));

            boolean result = client.subscribe(fileName, duration.toMillis());
            System.out.println(result ? "File subscribed successfully!" : "File subscribe failed!");
    }*/
    //</editor-fold>

    /**
     * Main do cliente
     *
     * @param args the command line arguments
     */
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
