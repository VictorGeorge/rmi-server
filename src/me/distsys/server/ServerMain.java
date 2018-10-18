/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.distsys.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import static me.distsys.common.Configuration.PORT;

public class ServerMain {
    private ServerInterfaceImpl serverImpl;

    public ServerMain() throws RemoteException {
        Scanner scanner = new Scanner(System.in);;
        int choice, numeroPessoas = 0, numeroQuartos, vagas, preço, preçoQuarto, preçoPessoa;
        String origem, destino, dataIda, dataVolta, dataEntrada, dataSaida, hotel, data;

        serverImpl = new ServerInterfaceImpl();
        // instancia serviço de nomes e implementacao do servidor
        Registry registry = LocateRegistry.createRegistry(PORT);
        registry.rebind("serverImpl", serverImpl);
        System.err.println("Running serverImpl on port " + PORT);
        do {
            System.out.println("\n1 - Cadastrar voos\n2 - Cadastrar hospedagem");
            choice = scanner.nextInt();
            // escolha das opções
            switch (choice) {
                case 1:
                    System.out.println("Origem: ");
                    scanner.nextLine();
                    origem = scanner.nextLine();
                    System.out.println("Destino: ");
                    destino = scanner.nextLine();
                    System.out.println("Data (dd/mm/aaaa): ");
                    data = scanner.nextLine();
                    System.out.println("Número de vagas: ");
                    vagas = scanner.nextInt();
                    System.out.println("Preço Unitário: ");
                    preço = scanner.nextInt();
                    serverImpl.addFlight(origem, destino, data, vagas, preço);
                    break;
                case 2:
                    System.out.println("\nNome do hotel: ");
                    scanner.nextLine();
                    hotel = scanner.nextLine();
                    System.out.println("Data de entrada (dd/mm/aaaa): ");
                    dataEntrada = scanner.nextLine();
                    System.out.println("Data de saída (dd/mm/aaaa): ");
                    dataSaida = scanner.nextLine();
                    System.out.println("Número de quartos: ");
                    numeroQuartos = scanner.nextInt();
                    System.out.println("Número de pessoas: ");
                    numeroPessoas = scanner.nextInt();
                    System.out.println("Preço por quarto: ");
                    preçoQuarto = scanner.nextInt();
                    System.out.println("Preço por pessoa: ");
                    preçoPessoa = scanner.nextInt();
                    serverImpl.addAccommodation(hotel, dataEntrada, dataSaida, numeroQuartos, numeroPessoas, preçoQuarto, preçoPessoa);
                    break;
            }
        } while (true);
    }

    /**
     * Main do servidor
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServerMain m = new ServerMain();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
