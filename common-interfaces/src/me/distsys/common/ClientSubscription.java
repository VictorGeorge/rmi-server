package me.distsys.common;

import java.io.Serializable;

/**
 * Classe usada para encapsular o interesse de um cliente
 */
public class ClientSubscription implements Serializable {
    /**
     * referência do cliente
     */
    public ClientInterface clientInterface;

    public ClientSubscription(ClientInterface clientInterface) {
        this.clientInterface = clientInterface;
    }
}