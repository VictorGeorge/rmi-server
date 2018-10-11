package me.distsys.server;

import me.distsys.common.ClientInterface;
import me.distsys.common.ClientSubscription;
import me.distsys.common.ServerInterface;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static me.distsys.common.Configuration.SERVER_DEFAULT_FOLDER;

/**
 * Implementação da interface ServerInterface. Métodos documentados com JavaDoc na interface.
 * @see ServerInterface
 */
public class ServerInterfaceImpl extends UnicastRemoteObject implements ServerInterface {
    // HashMap onde a chave é o arquivo, e o valor é a lista de clientes interessados
    private HashMap<String, List<ClientSubscription>> subscribedUsersHashMap;

    ServerInterfaceImpl() throws RemoteException {
        subscribedUsersHashMap = new HashMap<>();
    }

    public void consultPlaneTickets() throws RemoteException{
        // TODO: 11/10/2018
    }
    public void consultPackages() throws RemoteException{
        // TODO: 11/10/2018
    }
    public void consultAccomodation() throws RemoteException{
        // TODO: 11/10/2018
    }
    public void buyPlaneTickets() throws RemoteException{
        // TODO: 11/10/2018
    }
    public void buyPackage() throws RemoteException{
        // TODO: 11/10/2018
    }
    public void buyAccomodation() throws RemoteException{
        // TODO: 11/10/2018
    }

    public byte[] downloadFile(String fileName) throws RemoteException {
        File file = new File(SERVER_DEFAULT_FOLDER + File.separator + fileName);
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buffer = new byte[(int) file.length()];
            int read = bufferedInputStream.read(buffer, 0, buffer.length);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public boolean uploadFile(byte[] buffer, String fileName) throws RemoteException {
        File file = new File(SERVER_DEFAULT_FOLDER + File.separator + fileName);
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            bufferedOutputStream.write(buffer, 0, buffer.length);
            boolean b = file.length() == buffer.length;
            if (b)
                notifySubscribedClients(fileName);
            return b;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Notifica todos os clientes interessados num arquivo.
    private void notifySubscribedClients(String fileName) throws RemoteException {
        List<ClientSubscription> clientSubscriptions = subscribedUsersHashMap.get(fileName);
        if (clientSubscriptions == null)
            return;
        // Para cada subscription, verifica se tempo do interesse do cliente nao expirou e envia a notificacao.
        for (ClientSubscription clientSubscription : clientSubscriptions) {
            long now = System.currentTimeMillis();
            long timeDifference = now - clientSubscription.startTime;
            if (timeDifference <= clientSubscription.subscriptionDuration)
                clientSubscription.clientInterface.notifyClient(String.format("The file %s is now available.", fileName));
        }
    }

    public String[] listFiles() throws RemoteException {
        File defaultFolder = new File(SERVER_DEFAULT_FOLDER);
        return defaultFolder.list();
    }

    public boolean subscribeToFile(String fileName, ClientSubscription clientSubscription) throws RemoteException {
        List<ClientSubscription> clientSubscriptions = subscribedUsersHashMap.computeIfAbsent(fileName, k -> new LinkedList<>());
        clientSubscriptions.add(clientSubscription);

        return true;
    }

    public boolean unsubscribeToFile(String fileName, ClientInterface clientRef) throws RemoteException {
        List<ClientSubscription> clientSubscriptionList = subscribedUsersHashMap.get(fileName);
        if (clientSubscriptionList != null) {
            for (ClientSubscription subscription : clientSubscriptionList)
                if (subscription.clientInterface.equals(clientRef))
                    clientSubscriptionList.remove(subscription);
        }
        return true;
    }

    public String[] listClientSubscribedFiles(ClientInterface clientRef) throws RemoteException {
        List<String> filesList = new LinkedList<>();
        subscribedUsersHashMap.forEach((s, clientSubscriptions) -> {
            clientSubscriptions.forEach(clientSubscription -> {
                if (clientSubscription.clientInterface.equals(clientRef)) {
                    filesList.add(s);
                }
            });
        });
        return filesList.toArray(new String[0]);
    }

}
