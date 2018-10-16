package me.distsys.client;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Duration;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

// classe que implementa a UI do programa. Todos os menus e submenus, sao exibidos aqui.
public class ClientMain {
    // instância do modelo de cliente da aplicação
    ClientInterfaceImpl clientImpl;

    // instância única do scanner
    private Scanner scanner;
    // array é mantido para referência posterior de outros métodos
    private String[] files;
    int choice, choice2, choice3, numeroPessoas = 0, numeroQuartos;
    boolean idaEVolta;
    String origem, destino, dataIda, dataVolta, dataEntrada, dataSaida, hotel;

    // construtor
    public ClientMain() {
        try {
            Registry registry = LocateRegistry.getRegistry(1100);
            clientImpl = new ClientInterfaceImpl(registry);
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        scanner = new Scanner(System.in);
    }

    // menu principal
    public void mainMenu() {
        do {
            System.out.println("1 - Comprar\n2 - Consultar\n3 - Inscrever em evento\n4 - Cancelar inscrição\n5 - Sair");
            choice = scanner.nextInt();
            // escolha das opções
            switch (choice) {
                case 1:
                    System.out.println("1 - Hospedagem\n2 - Pacotes\n3 - Passagens\n4 - Retornar a menu principal");
                    choice2 = scanner.nextInt();
                    switch (choice2) {
                        case 1:
                            menuDadosHospedagem();
                            buyAccomodation(hotel, dataEntrada, dataSaida, numeroQuartos, numeroPessoas);
                            break;
                        case 2:
                            menuDadosTickets();
                            menuDadosHospedagem();
                            buyPackage(origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos);
                            break;
                        case 3:
                            menuDadosTickets();
                            buyPlaneTickets(origem, destino, dataIda, dataVolta, numeroPessoas);
                            break;
                        case 4:
                            break;
                    }
                    break;
                case 2:
                    System.out.println("1 - Hospedagem\n2 - Pacotes\n3 - Passagens\n4 - Retornar a menu principal");
                    choice2 = scanner.nextInt();
                    switch (choice2) {
                        case 1:
                            menuDadosHospedagem();
                            consultAccomodation(hotel, dataEntrada, dataSaida, numeroQuartos, numeroPessoas);
                            break;
                        case 2:
                            menuDadosTickets();
                            menuDadosHospedagem();
                            consultPackages(origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos);
                            break;
                        case 3:
                            menuDadosTickets();
                            consultPlaneTickets(origem, destino, dataIda, dataVolta, numeroPessoas);
                            break;
                        case 4:
                            break;
                    }
                    break;
//                case 3:
//                    subscribeSection();
//                    break;
//                case 4:
//                    unsubscribeSection();
//                    break;
                case 5:
                    clearResourcesOnServer();
                    // System.exit é usado porque o cliente RMI inicializa um Daemon que não encerra com o fim da execução principal.
                    System.exit(0);
                    break;
            }
        } while (true);
    }

    private void menuDadosTickets() {
        System.out.println("\n1 - Ida e volta\n2 - Somente ida");
        choice3 = scanner.nextInt();
        switch (choice3) {
            case 1:
                idaEVolta = Boolean.TRUE;
                break;
            case 2:
                idaEVolta = Boolean.FALSE;
                break;
            default:
                System.out.println("Erro");
                break;
        }
        System.out.println("Origem: ");
        scanner.nextLine();
        origem = scanner.nextLine();
        System.out.println("Destino: ");
        destino = scanner.nextLine();
        System.out.println("Data de ida (dd/mm/aaaa): ");
        dataIda = scanner.nextLine();
        if (idaEVolta) {
            System.out.println("Data de volta (dd/mm/aaaa): ");
            dataVolta = scanner.nextLine();
        } else {
            dataVolta = null;
        }

        System.out.println("Número de pessoas: ");
        numeroPessoas = scanner.nextInt();
    }

    private void menuDadosHospedagem() {
        System.out.println("\nInforme o nome do hotel: ");
        scanner.nextLine();
        hotel = scanner.nextLine();

        System.out.println("Informe a data de entrada (dd/mm/aaaa): ");
        dataEntrada = scanner.nextLine();

        System.out.println("Informe a data de saída (dd/mm/aaaa): ");
        dataSaida = scanner.nextLine();

        System.out.println("Informe o número de quartos: ");
        numeroQuartos = scanner.nextInt();
        if (numeroPessoas == 0) {
            System.out.println("Informe o número de pessoas: ");
            numeroPessoas = scanner.nextInt();
        }
    }

    /**
     * método de consulta de tickets
     *
     * @param origem, destino, dataIda, dataVolta, numeroPessoas
     * @throws RemoteException
     */
    public String consultPlaneTickets(String origem, String destino, String dataIda, String dataVolta, int numeroPessoas) throws RemoteException {
        return clientImpl.serverInterface.consultPlaneTickets(origem, destino, dataIda, dataVolta, numeroPessoas);
    }

    /**
     * método de compra de tickets
     *
     * @param origem, destino, dataIda, dataVolta, numeroPessoas
     * @throws RemoteException
     */
    public void buyPlaneTickets(String origem, String destino, String dataIda, String dataVolta, int numeroPessoas) throws RemoteException {
        clientImpl.serverInterface.buyPlaneTickets(origem, destino, dataIda, dataVolta, numeroPessoas);
    }

    /**
     * método de consulta de pacotes
     *
     * @param origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos
     * @throws RemoteException
     */
    void consultPackages(String origem, String destino, String dataIda, String dataVolta, int numeroPessoas, String hotel, String dataEntrada, String dataSaida, int numeroQuartos) throws RemoteException {
        clientImpl.serverInterface.consultPackages(origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos);
    }

    /**
     * método de compra de pacotes
     *
     * @param origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos
     * @throws RemoteException
     */
    void buyPackage(String origem, String destino, String dataIda, String dataVolta, int numeroPessoas, String hotel, String dataEntrada, String dataSaida, int numeroQuartos) throws RemoteException {
        clientImpl.serverInterface.buyPackage(origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos);
    }

    /**
     * método de consulta de hospedagem
     *
     * @param hotel, dataEntrada, dataSaida, numeroQuartos, numeroPessoas
     * @throws RemoteException
     */
    void consultAccomodation(String hotel, String dataEntrada, String dataSaida, int numeroQuartos, int numeroPessoas) throws RemoteException {
        clientImpl.serverInterface.consultAccomodation(hotel, dataEntrada, dataSaida, numeroQuartos, numeroPessoas);
    }

    /**
     * método de compra de hospedagem
     *
     * @param hotel, dataEntrada, dataSaida, numeroQuartos, numeroPessoas
     * @throws RemoteException
     */
    void buyAccomodation(String hotel, String dataEntrada, String dataSaida, int numeroQuartos, int numeroPessoas) throws RemoteException {
        clientImpl.serverInterface.buyAccomodation(hotel, dataEntrada, dataSaida, numeroQuartos, numeroPessoas);
    }

    // desaloca todas os recursos inscritos pelo cliente
    private void clearResourcesOnServer() {
//        try {
//            // recupera a lista de arquivos inscritos pelo cliente
//            //String[] strings = client.listClientSubscribedFiles();
//            // um a um se desinscreve
//            for (String string : strings) client.unsubscribe(string);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    //<editor-fold desc="Event notification methods">
/*


    // lista de arquivos contidos no servidor
    private void listFilesSection() {
        try {
            files = client.listFiles();
            for (int i = 0, filesLength = files.length; i < filesLength; i++) {
                String file = files[i];
                System.out.printf("File [%d]: %s%n", i, file);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // submenu de download de um arquivo
    private void downloadSection() {
        listFilesSection();
        System.out.println("Select a file to download From ServerMain: ");
        int choice = scanner.nextInt();
        try {
            boolean result = client.downloadFromServer(files[choice]);
            if (result) {
                client.unsubscribe(files[choice]);
            }
            System.out.println(result ? "File downloaded successfully!" : "File download failed!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // submenu de upload de um arquivo
    private void uploadSection() {
        System.out.println("Enter a file path for upload To ServerMain: ");
        scanner.nextLine();
        String path = scanner.nextLine();
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("The file doesn't exist!");
            return;
        }
        try {
            boolean result = client.uploadToServer(file);
            System.out.println(result ? "File uploaded successfully!" : "File upload failed!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // submenu de desinscrição de interesse em um arquivo
    private void unsubscribeSection() {
        try {
            scanner.nextLine();
            System.out.println("Currently subscribed items");
            String[] strings = client.listClientSubscribedFiles();
            Arrays.stream(strings).forEach(System.out::println);
            System.out.println("Enter a file name: ");
            String fileName = scanner.nextLine();

            boolean result = client.unsubscribe(fileName);
            System.out.println(result ? "File unsubscribed successfully!" : "File unsubscribe failed!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // submenu de inscrição de interesse em um arquivo
    private void subscribeSection() {
        try {
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
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }*/
    //</editor-fold>

    // main do cliente
    public static void main(String[] args) {
        ClientMain m = new ClientMain();
        m.mainMenu();
    }
}
