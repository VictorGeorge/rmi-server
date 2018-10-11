package me.distsys.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

// interface de métodos do servidor
public interface ServerInterface extends Remote {


    void consultPlaneTickets() throws RemoteException;
    void consultPackages() throws RemoteException;
    void consultAccomodation() throws RemoteException;
    void buyPlaneTickets() throws RemoteException;
    void buyPackage() throws RemoteException;
    void buyAccomodation() throws RemoteException;

    /**
     * Método de upload de um arquivo para o servidor.
     * @param buffer array de bytes contendo os dados do arquivo
     * @param fileName string do nome do arquivo
     * @return true para sucesso no envio ou false para falha.
     * @throws RemoteException exceção padrão de métodos remotos.
     */
    boolean uploadFile(byte[] buffer, String fileName) throws RemoteException;
    /**
     * Método de download de um arquivo do servidor.
     * @param fileName string do nome do arquivo a ser baixado
     * @return array dos bytes do arquivo
     * @throws RemoteException exceção padrão de métodos remotos.
     */
    byte[] downloadFile(String fileName) throws RemoteException;


    /**
     * Método de listar os arquivos
     * @return array de strings com o nome dos arquivos
     * @throws RemoteException
     */
    String[] listFiles() throws RemoteException;

    /**
     * Método de registro de interesse no arquivo
     * @param fileName string do nome do arquivo a ser baixado
     * @param clientSubscription objeto contendo a referencia do cliente e o tempo pretendido
     * @return true ou false representando sucesso ou falha no processo
     * @throws RemoteException
     */
    boolean subscribeToFile(String fileName, ClientSubscription clientSubscription) throws RemoteException;

    /**
     * Método de desregistro de interesse no arquivo
     * @param fileName string do nome do arquivo a ser baixado
     * @param clientRef referencia do cliente
     * @return true ou false representando sucesso ou falha no processo
     * @throws RemoteException
     */
    boolean unsubscribeToFile(String fileName, ClientInterface clientRef) throws RemoteException;

    /**
     * Método de listagem arquivos de interesse do cliente
     * @param clientRef referencia do cliente
     * @return lista de strings dos nomes dos arquivos
     * @throws RemoteException
     */
    String[] listClientSubscribedFiles(ClientInterface clientRef) throws RemoteException;
}
