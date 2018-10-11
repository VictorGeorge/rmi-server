package me.distsys.client;

import java.io.File;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

// classe que implementa a UI do programa. Todos os menus e submenus, sao exibidos aqui.
public class MainClient {
    // instância do modelo de cliente da aplicação
    Client client;
    // instância única do scanner
    private Scanner scanner;
    // array é mantido para referência posterior de outros métodos
    private String[] files;

    // construtor
    public MainClient() {
        client = new Client();
        scanner = new Scanner(System.in);
    }

    // menu principal
    public void mainMenu() {
        int choice, subchoice;
        do {
            System.out.println("1 - Buy\n2 - Consult\n3 - Subscribe to event\n4 - Cancel Subscription\n5 - Exit");
            choice = scanner.nextInt();
// escolha das opções
            switch (choice) {
                case 1:
                    System.out.println("1 - Accommodation\n2 - Packages\n3 - Plane tickets\n4 - Return to main menu");
                    subchoice = scanner.nextInt();
                    switch (subchoice) {
                        case 1:
                            buyAccomodation();
                            break;
                        case 2:
                            buyPackage();
                            break;
                        case 3:
                            buyPlaneTickets();
                            break;
                        case 4:
                            break;
                    }
                    break;
                case 2:
                    System.out.println("1 - Accommodation\n2 - Packages\n3 - Plane tickets\n4 - Return to main menu");
                    subchoice = scanner.nextInt();
                    switch (subchoice) {
                        case 1:
                            consultAccomodation();
                            break;
                        case 2:
                            consultPackages();
                            break;
                        case 3:
                            consultPlaneTickets();
                            break;
                        case 4:
                            break;
                    }
                    break;
                case 3:
                    subscribeSection();
                    break;
                case 4:
                    unsubscribeSection();
                    break;
                case 5:
                    clearResourcesOnServer();
                    // System.exit é usado porque o cliente RMI inicializa um Daemon que não encerra com o fim da execução principal.
                    System.exit(0);
                    break;
            }
        } while (true);
    }

    private void consultPlaneTickets() {
        // TODO: 11/10/2018
    }

    private void consultPackages() {
        // TODO: 11/10/2018
    }

    private void consultAccomodation() {
        // TODO: 11/10/2018
    }

    private void buyPlaneTickets() {
        // TODO: 11/10/2018
    }

    private void buyPackage() {
        // TODO: 11/10/2018
    }

    private void buyAccomodation() {
        // TODO: 11/10/2018
    }

    // desaloca todas os arquivos inscritos pelo cliente
    private void clearResourcesOnServer() {
        try {
            // recupera a lista de arquivos inscritos pelo cliente
            String[] strings = client.listClientSubscribedFiles();
            // um a um se desinscreve
            for (String string : strings) client.unsubscribe(string);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

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
        System.out.println("Select a file to download From Server: ");
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
        System.out.println("Enter a file path for upload To Server: ");
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
    }

    // main do cliente
    public static void main(String[] args) {
        MainClient m = new MainClient();
        m.mainMenu();
    }
}
