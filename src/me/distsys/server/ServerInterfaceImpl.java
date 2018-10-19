package me.distsys.server;

import me.distsys.common.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Stream;

/**
 * Implementação da interface ServerInterface. Métodos documentados com JavaDoc na interface.
 *
 * @see ServerInterface
 */
public class ServerInterfaceImpl extends UnicastRemoteObject implements ServerInterface {
    // HashMap onde a chave é o evento, e o valor é a lista de clientes interessados
    private HashMap<SearchParams, List<ClientSubscription>> subscribedFlights;
    private HashMap<SearchParams, List<ClientSubscription>> subscribedAccommodations;
    private HashMap<SearchParams, List<ClientSubscription>> subscribedPackages;

    private List<ClientSubscription> clientSubscriptions;

    //Listas de voos e hoteis
    private List<Flight> flights = new ArrayList<>();
    private List<Accommodation> accommodations = new ArrayList<>();

    int idHotel = 2;

    ServerInterfaceImpl() throws RemoteException {
        subscribedFlights = new HashMap<>();
        subscribedAccommodations = new HashMap<>();
        subscribedPackages = new HashMap<>();

        flights.add(new Flight("CWB", "GRU", "10/02/2019", 150, 100));
        flights.add(new Flight("GRU", "CWB", "11/02/2019", 150, 100));
        flights.add(new Flight("CWB", "REC", "10/02/2019", 150, 100));
        flights.add(new Flight("REC", "CWB", "11/02/2019", 150, 100));
        flights.add(new Flight("GRU", "YYZ", "10/02/2019", 150, 100));
        flights.add(new Flight("YYZ", "GRU", "11/02/2019", 150, 100));
        flights.add(new Flight("JFK", "GRU", "10/02/2019", 150, 100));
        flights.add(new Flight("GRU", "JFK", "11/02/2019", 150, 100));
        flights.add(new Flight("q", "w", "e", 150, 100));

        accommodations.add(new Accommodation(0, "Copacabana", "10/02/2019", "14/02/2019", 100, 400, 320, 100, 420));
        accommodations.add(new Accommodation(1, "b", "11/02/2019", "11/02/2019", 120, 420, 320, 100, 420));

    }

    @Override
    public int[] consultPlaneTickets(SearchParams searchParams) throws RemoteException {
        int[] idsTuple = new int[2];
        idsTuple[0] = -1;
        idsTuple[1] = -1;

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
    public synchronized boolean buyPlaneTickets(int[] idsTuple, int numeroPessoas) throws RemoteException {
        if (idsTuple[0] == -1)//error in buying
            return false;
        Flight departureFlight = flights.get(idsTuple[0]);
        int vagas = departureFlight.getVagas();
        boolean b = vagas >= numeroPessoas;
        if (b) {
            departureFlight.setVagas(vagas - numeroPessoas);
        }
        if (idsTuple[1] == -1)
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
        int[] idsTuple = new int[2];
        idsTuple[0] = -1;
        Stream<Accommodation> availableAccomodations = accommodations.stream().filter(accommodation ->
                accommodation.getHotel().equals(searchParams.hotel) &&
                        accommodation.getDataEntrada().equals(searchParams.dataEntrada) &&
                        accommodation.getDataSaida().equals(searchParams.dataSaida));

        Optional<Accommodation> optionalAccommodation = availableAccomodations.findFirst();
        optionalAccommodation.ifPresent(accommodation -> idsTuple[0] = accommodations.indexOf(accommodation));
        return idsTuple[0];
    }

    @Override
    public synchronized boolean buyAccomodation(int id, int numeroQuartos, int numeroPessoas) throws RemoteException {
        if (id == -1)//error in buying
            return false;
        int vagas = accommodations.get(id).getNumeroPessoas();
        int quartos = accommodations.get(id).getNumeroQuartos();
        boolean b = (vagas >= numeroPessoas) && (quartos >= numeroQuartos);
        if (b) {
            accommodations.get(id).setNumeroPessoas(vagas - numeroPessoas);
            accommodations.get(id).setNumeroQuartos(quartos - numeroQuartos);
        }
        return b;
    }

    @Override
    public int[] consultPackages(SearchParams searchParams) throws RemoteException{
        int[] idsTuple = new int[3];
        int[] auxTuple = new int[2];
        int idHospedagem;
        idsTuple[0] = -1; //ida
        idsTuple[1] = -1; //volta
        idsTuple[2] = -1; //hospedagem
        //primeira consulta as passagens
        auxTuple = consultPlaneTickets(searchParams);
        idsTuple[0] = auxTuple[0]; //ida
        idsTuple[1] = auxTuple[1]; //volta
        idHospedagem = consultAccomodation(searchParams);
        idsTuple[2] = idHospedagem;
        return idsTuple;
    }

    @Override
    public synchronized boolean buyPackage(int[] idsTuple, int numeroQuartos, int numeroPessoas) throws RemoteException{
        boolean b = buyPlaneTickets(idsTuple, numeroPessoas);
        if(b)
            b = buyAccomodation(idsTuple[2], numeroQuartos, numeroPessoas);
        return b;
    }

    @Override
    public boolean subscribe(SearchParams searchParams, ClientSubscription clientSubscription) throws RemoteException{
        boolean b = true;
        if (searchParams.hotel == null) {//É passagem de aviao
            clientSubscriptions = subscribedFlights.computeIfAbsent(searchParams, k -> new LinkedList<>()); //insere no hashmap
            clientSubscriptions.add(clientSubscription);
        }
        else if (searchParams.origem == null) {//É Hotel
            clientSubscriptions = subscribedAccommodations.computeIfAbsent(searchParams, k -> new LinkedList<>()); //insere no hashmap
            clientSubscriptions.add(clientSubscription);
        }
        else{// É Pacote
            clientSubscriptions = subscribedPackages.computeIfAbsent(searchParams, k -> new LinkedList<>()); //insere no hashmap
            clientSubscriptions.add(clientSubscription);
        }
        return b;
    }

    @Override
    public boolean unsubscribe(SearchParams searchParams, ClientSubscription clientSubscription) throws RemoteException{
        for(Map.Entry<SearchParams, List<ClientSubscription>> entry : subscribedFlights.entrySet()) {
            SearchParams key = entry.getKey();
            List<ClientSubscription> value = entry.getValue();
            if(key.origem.equals(searchParams.origem) && key.destino.equals(searchParams.destino) && key.dataIda.equals(searchParams.dataIda)) { //Se esse é o voo a se cancelar a inscrição
                if(value != null) {
                    for (ClientSubscription subscription : value) { //procura o cliente a se retirar
                        if (subscription.clientInterface.equals(clientSubscription.clientInterface)) {
                            value.remove(subscription);
                            return true; //unsubscribe feito
                        }
                    }
                }
            }
        }
        return false;  //erro
    }

    @Override
    public void notifySubscribedClients(SearchParams searchParams) throws RemoteException{
        if (searchParams.hotel == null) {//É passagem de aviao
            for(Map.Entry<SearchParams, List<ClientSubscription>> entry : subscribedFlights.entrySet()) {
                SearchParams key = entry.getKey();
                List<ClientSubscription> value = entry.getValue();
                if(searchParams.dataIda.equals(key.dataIda) && searchParams.destino.equals(key.destino) && searchParams.origem.equals(key.origem) && searchParams.preço <= key.preço){
                    if(searchParams.dataVolta == null || searchParams.dataVolta == key.dataVolta) {
                        for (ClientSubscription clientSubscription : value) {
                            clientSubscription.clientInterface.notifyClient(String.format("O voo de %s para %s agora tá disponível!", searchParams.origem, searchParams.destino));
                        }
                    }
                }
            }
        }
        else if (searchParams.origem == null) {//É Hotel
            for(Map.Entry<SearchParams, List<ClientSubscription>> entry : subscribedAccommodations.entrySet()) {
                SearchParams key = entry.getKey();
                List<ClientSubscription> value = entry.getValue();
                if(searchParams.hotel.equals(key.hotel) && searchParams.dataEntrada.equals(key.dataEntrada) && searchParams.dataSaida.equals(key.dataSaida) && searchParams.preço <= key.preço){
                    for (ClientSubscription clientSubscription : value) {
                        clientSubscription.clientInterface.notifyClient(String.format("O hotel %s com entrada em %s e saída em %s agora está disponível!", searchParams.hotel, searchParams.dataEntrada, searchParams.dataSaida));
                    }
                }
            }
        }
        if (clientSubscriptions == null)
            return;
    }

    @Override
    public void addFlight(String origem,String destino,String data, int vagas, int preço) throws RemoteException{
        flights.add(new Flight(origem, destino, data, vagas, preço));
        SearchParams parameters = new SearchParams();
        parameters.origem = origem;
        parameters.destino = destino;
        parameters.dataIda = data;
        parameters.preço = preço;
        notifySubscribedClients(parameters);
    }

    @Override
    public void addAccommodation(String hotel,String dataEntrada,String dataSaida,int numeroQuartos,int numeroPessoas,int preçoQuarto, int preçoPessoa) throws RemoteException {
        accommodations.add(new Accommodation(idHotel, hotel, dataEntrada, dataSaida, numeroQuartos, numeroPessoas, preçoQuarto, preçoPessoa, preçoQuarto+preçoPessoa));
        SearchParams parameters = new SearchParams();
        parameters.hotel = hotel;
        parameters.dataEntrada = dataEntrada;
        parameters.dataSaida = dataSaida;
        parameters.preço = preçoPessoa;
        notifySubscribedClients(parameters);
    }
}
