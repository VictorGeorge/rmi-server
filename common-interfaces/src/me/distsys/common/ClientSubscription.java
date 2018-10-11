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

    /**
     * duração do interesse e quando ele começou (inicializado com o tempo em que o objeto é construído)
     */
    public long subscriptionDuration, startTime;

    public ClientSubscription(ClientInterface clientInterface, long subscriptionDuration) {
        this.clientInterface = clientInterface;
        this.subscriptionDuration = subscriptionDuration;
        this.startTime = System.currentTimeMillis();
    }
}