package me.distsys.client;

import me.distsys.common.ClientInterface;
import me.distsys.common.ServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implementação da interface ClientInterface. Métodos documentados com JavaDoc na interface.
 * @see ClientInterface
 */
public class ClientInterfaceImpl extends UnicastRemoteObject implements ClientInterface {

    /**
     * guarda uma referência do servidor.
     */
    ServerInterface serverInterface;

    /**
     * construtor da implementação, recebe um Registry (servidor de nomes), e o usa para procurar o servidor.
     * @param registry
     * @throws RemoteException
     * @throws NotBoundException
     */
    ClientInterfaceImpl(Registry registry) throws RemoteException, NotBoundException {
        serverInterface = (ServerInterface) registry.lookup("server");
    }

    /**
     * método de notificação do cliente, imprime a saída no canal err para diferenciar dos outros textos impressos na tela
     * @param message
     * @throws RemoteException
     */
    @Override
    public void notifyClient(String message) throws RemoteException {
        System.err.println(message);
    }
}
