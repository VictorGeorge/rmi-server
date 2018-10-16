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
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Implementação da interface ServerInterface. Métodos documentados com JavaDoc na interface.
 *
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
    public int[] consultPlaneTickets(SearchParams searchParams) throws RemoteException {
        int[] idsTuple = new int[2];

        Stream<Flight> departureFlightStream = flights.stream().filter(flight ->
                flight.getOrigem().equals(searchParams.origem) &&
                        flight.getDestino().equals(searchParams.destino) &&
                        flight.getData().equals(searchParams.dataIda) &&
                        flight.getVagas() >= searchParams.numeroPessoas
        );
        Optional<Flight> optionalDeparture = departureFlightStream.findFirst();
        optionalDeparture.ifPresent(flight -> idsTuple[0] = flights.indexOf(flight));

        if (!searchParams.idaEVolta) return idsTuple;
        Stream<Flight> returnFlightStream = flights.stream().filter(flight ->
                flight.getOrigem().equals(searchParams.destino) &&
                        flight.getDestino().equals(searchParams.origem) &&
                        flight.getData().equals(searchParams.dataVolta) &&
                        flight.getVagas() >= searchParams.numeroPessoas
        );
        Optional<Flight> optionalReturn = returnFlightStream.findFirst();
        optionalReturn.ifPresent(flight -> idsTuple[1] = flights.indexOf(flight));

        return idsTuple;
    }

    @Override
    public boolean buyPlaneTickets(int[] idsTuple, int numeroPessoas) throws RemoteException {
        Flight departureFlight = flights.get(idsTuple[0]);
        int vagas = departureFlight.getVagas();
        boolean b = vagas >= numeroPessoas;
        if (b) {
            departureFlight.setVagas(vagas - numeroPessoas);
        }
        if (idsTuple.length < 2)
            return b;

        Flight returnFlight = flights.get(idsTuple[1]);
        vagas = returnFlight.getVagas();
        b = vagas >= numeroPessoas;
        if (b) {
            returnFlight.setVagas(vagas - numeroPessoas);
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
