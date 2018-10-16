package me.distsys.server;

import me.distsys.common.Accommodation;
import me.distsys.common.ClientSubscription;
import me.distsys.common.Flight;
import me.distsys.common.SearchParams;
import me.distsys.common.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implementação da interface ServerInterface. Métodos documentados com JavaDoc na interface.
 * @see ServerInterface
 */
public class ServerInterfaceImpl extends UnicastRemoteObject implements ServerInterface {
    // HashMap onde a chave é o arquivo, e o valor é a lista de clientes interessados
    private HashMap<String, List<ClientSubscription>> subscribedUsersHashMap;

    private List<Flight> flights = new ArrayList<>();
    private List<Accommodation> accommodations = new ArrayList<>();

    ServerInterfaceImpl() throws RemoteException {
        subscribedUsersHashMap = new HashMap<>();

        flights.add(new Flight("CWB", "GRU", "10/02/2019", 150, 100));
        flights.add(new Flight("GRU", "CWB", "11/02/2019", 150, 100));
        flights.add(new Flight("CWB", "REC", "10/02/2019", 150, 100));
        flights.add(new Flight("REC", "CWB", "11/02/2019", 150, 100));
        flights.add(new Flight("GRU", "YYZ", "10/02/2019", 150, 100));
        flights.add(new Flight("YYZ", "GRU", "11/02/2019", 150, 100));
        flights.add(new Flight("JFK", "GRU", "10/02/2019", 150, 100));
        flights.add(new Flight("GRU", "JFK", "11/02/2019", 150, 100));

        //TODO MOCK DATASET FOR ACCOMMODATION
    }

    @Override
    public int consultPlaneTickets(SearchParams searchParams) throws RemoteException {
        return 0;
    }

    @Override
    public boolean buyPlaneTickets(int id, int numeroPessoas) throws RemoteException {
        int vagas = flights.get(id).getVagas();
        boolean b = vagas >= numeroPessoas;
        if (b) {
            flights.get(id).setVagas(vagas - numeroPessoas);
        }
        return b;
    }

    @Override
    public int consultAccomodation(SearchParams searchParams) throws RemoteException {
        return 0;
    }

    @Override
    public boolean buyAccomodation(int id, int numeroQuartos, int numeroPessoas) throws RemoteException {
        int vagas = flights.get(id).getVagas();
        boolean b = vagas >= numeroPessoas;
        if (b) {
            flights.get(id).setVagas(vagas - numeroPessoas);
        }
        return b;
    }
}
