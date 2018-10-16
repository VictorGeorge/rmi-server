package me.distsys.client;

import me.distsys.common.ClientSubscription;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import static me.distsys.common.Configuration.CLIENT_DEFAULT_FOLDER;


/**
 * Classe que encapsula o estado da aplicação do cliente. Através da instância da implementação da interface.
 * No geral, invoca os métodos remotos do ServerInterface. Manipula a leitura e escrita de arquivos.
 */
class Client {
    private ClientInterfaceImpl clientImpl;

    /**
     * Se registra no servidor de nomes e instancia a implementacao da interface do cliente.
     */
    Client() {
        try {
            Registry registry = LocateRegistry.getRegistry(1100);
            clientImpl = new ClientInterfaceImpl(registry);
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Retorna a lista de arquivos do servidor.
     * @return
     * @throws RemoteException
     */
    String[] listFiles() throws RemoteException {
        return clientImpl.serverInterface.listFiles();
    }


    /**
     * Invoca o metodo remoto de download de arquivo do servidor. Usa os bytes para criar um arquivo no cliente com o conteudo do array.
     * @param fileName
     * @return
     * @throws RemoteException
     */
    boolean downloadFromServer(String fileName) throws RemoteException {
        byte[] buffer = clientImpl.serverInterface.downloadFile(fileName);
        if(buffer.length == 0)
            return false;

        File file = new File(CLIENT_DEFAULT_FOLDER + File.separator + fileName);
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            bufferedOutputStream.write(buffer, 0, buffer.length);
            return file.length() == buffer.length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Invoca o metodo remoto de download de arquivo do servidor. Usa os bytes para criar um arquivo no cliente com o conteudo do array.
     * @param file
     * @return
     * @throws RemoteException
     */
    boolean uploadToServer(File file) throws RemoteException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[(int) file.length()];
            int read = bufferedInputStream.read(buffer, 0, buffer.length);
            return clientImpl.serverInterface.uploadFile(buffer, file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cria objeto que encapsula o interesse do cliente num arquivo remoto. Formaliza o interesse atraves do metodo remoto.
     * @param fileName
     * @param subscriptionDuration
     * @return
     * @throws RemoteException
     */
    boolean subscribe(String fileName, long subscriptionDuration) throws RemoteException {
        ClientSubscription subscription = new ClientSubscription(clientImpl, subscriptionDuration);
        return clientImpl.serverInterface.subscribeToFile(fileName, subscription);
    }

    /**
     *
     * @param fileName
     * @return
     * @throws RemoteException
     */
    boolean unsubscribe(String fileName) throws RemoteException {
        return clientImpl.serverInterface.unsubscribeToFile(fileName, clientImpl);
    }

    String[] listClientSubscribedFiles() throws RemoteException {
        String[] strings = clientImpl.serverInterface.listClientSubscribedFiles(clientImpl);
        return strings;
    }

    /**
     * método de consulta de tickets
     * @param origem, destino, dataIda, dataVolta, numeroPessoas
     * @throws RemoteException
     */
    public String consultPlaneTickets(String origem, String destino, String dataIda, String dataVolta, int numeroPessoas) throws RemoteException {
        return clientImpl.serverInterface.consultPlaneTickets(origem, destino, dataIda, dataVolta, numeroPessoas);
    }

    /**
     * método de compra de tickets
     * @param origem, destino, dataIda, dataVolta, numeroPessoas
     * @throws RemoteException
     */
    public void buyPlaneTickets(String origem, String destino, String dataIda, String dataVolta, int numeroPessoas) throws RemoteException{
        clientImpl.serverInterface.buyPlaneTickets(origem, destino, dataIda, dataVolta, numeroPessoas);
    }

    /**
     * método de consulta de pacotes
     * @param origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos
     * @throws RemoteException
     */
    void consultPackages(String origem, String destino, String dataIda, String dataVolta, int numeroPessoas, String hotel, String dataEntrada, String dataSaida, int numeroQuartos) throws RemoteException{
        clientImpl.serverInterface.consultPackages(origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos);
    }

    /**
     * método de compra de pacotes
     * @param origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos
     * @throws RemoteException
     */
    void buyPackage(String origem, String destino, String dataIda, String dataVolta, int numeroPessoas, String hotel, String dataEntrada, String dataSaida, int numeroQuartos) throws RemoteException{
        clientImpl.serverInterface.buyPackage(origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos);
    }

    /**
     * método de consulta de hospedagem
     * @param hotel, dataEntrada, dataSaida, numeroQuartos, numeroPessoas
     * @throws RemoteException
     */
    void consultAccomodation(String hotel, String dataEntrada, String dataSaida, int numeroQuartos, int numeroPessoas) throws RemoteException{
        clientImpl.serverInterface.consultAccomodation(hotel, dataEntrada, dataSaida, numeroQuartos, numeroPessoas);
    }

    /**
     * método de compra de hospedagem
     * @param hotel, dataEntrada, dataSaida, numeroQuartos, numeroPessoas
     * @throws RemoteException
     */
    void buyAccomodation(String hotel, String dataEntrada, String dataSaida, int numeroQuartos, int numeroPessoas) throws RemoteException{
        clientImpl.serverInterface.buyAccomodation(hotel, dataEntrada, dataSaida, numeroQuartos, numeroPessoas);
    }
}
