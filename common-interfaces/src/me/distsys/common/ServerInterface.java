package me.distsys.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

// interface de métodos do servidor
public interface ServerInterface extends Remote {
    /**
     * método de consulta de tickets
     * @param searchParams
     * @throws RemoteException
     */
    int[] consultPlaneTickets(SearchParams searchParams) throws RemoteException;

    /**
     * método de compra de tickets
     * @param idsTuple, numeroPessoas
     * @throws RemoteException
     */
    boolean buyPlaneTickets(int[] idsTuple, int numeroPessoas) throws RemoteException;

    /**
     * método de consulta de hospedagem
     * @param searchParams
     * @throws RemoteException
     */
    int consultAccomodation(SearchParams searchParams) throws RemoteException;

    /**
     * método de compra de hospedagem
     * @param numeroQuartos, numeroPessoas
     * @throws RemoteException
     */
    boolean buyAccomodation(int id, int numeroQuartos, int numeroPessoas) throws RemoteException;

    /**
     * método de consulta de pacotes
     * @param searchParams
     * @throws RemoteException
     */
    int[] consultPackages(SearchParams searchParams) throws RemoteException;

    /**
     * método de compra de pacotes
     * @param idsTuple,  numeroQuartos, numeroPessoas
     * @throws RemoteException
     */
    boolean buyPackage(int[] idsTuple, int numeroQuartos, int numeroPessoas) throws RemoteException;

    /**
     * método de inscrição em eventos
     * @param searchParams
     * @throws RemoteException
     */
    boolean subscribe(SearchParams searchParams, ClientSubscription clientSubscription) throws RemoteException;

    boolean unsubscribe(SearchParams searchParams, ClientSubscription clientSubscription) throws RemoteException;

    /**
     * método de notificação de eventos
     * @param searchParams
     * @throws RemoteException
     */
    void notifySubscribedClients(SearchParams searchParams) throws RemoteException;

    void addFlight(String origem,String destino,String data, int vagas, int preço) throws RemoteException;

    void addAccommodation(String hotel,String dataEntrada,String dataSaida,int numeroQuartos,int numeroPessoas,int preçoQuarto, int preçoPessoa) throws RemoteException;
}
