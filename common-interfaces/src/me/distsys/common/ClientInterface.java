package me.distsys.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface de métodos do cliente.
 */
public interface ClientInterface extends Remote {
    /**
     * Método de notificação do cliente
     * @param message
     * @throws RemoteException
     */
    void notifyClient(String message) throws RemoteException;
}
