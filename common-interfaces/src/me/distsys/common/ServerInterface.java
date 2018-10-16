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
//    TODO
//    void consultPackages(SearchParams searchParams) throws RemoteException;

    /**
     * método de compra de pacotes
     * @param origem, destino, dataIda, dataVolta, numeroPessoas, hotel, dataEntrada, dataSaida, numeroQuartos
     * @throws RemoteException
     */
//    TODO
//    boolean buyPackage(String origem, String destino, String dataIda, String dataVolta, int numeroPessoas, String hotel, String dataEntrada, String dataSaida, int numeroQuartos) throws RemoteException;
}
