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
import java.util.logging.Level;
import java.util.logging.Logger;

import static me.distsys.common.Configuration.PORT;

public class ServerMain {
    /**
     * Main do servidor
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServerInterfaceImpl server = new ServerInterfaceImpl();
            // instancia serviço de nomes e implementacao do servidor
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.rebind("server", server);
            System.out.println("Running server on port " + PORT);

            //TODO MOCK DATASET FOR ACCOMMODATION
            server.addFlight("CWB", "GRU", "10/02/2019", 150, 100);
            server.addFlight("GRU", "CWB", "11/02/2019", 150, 100);
            server.addFlight("CWB", "REC", "10/02/2019", 150, 100);
            server.addFlight("REC", "CWB", "11/02/2019", 150, 100);
            server.addFlight("GRU", "YYZ", "10/02/2019", 150, 100);
            server.addFlight("YYZ", "GRU", "11/02/2019", 150, 100);
            server.addFlight("JFK", "GRU", "10/02/2019", 150, 100);
            server.addFlight("GRU", "JFK", "11/02/2019", 150, 100);

        } catch (RemoteException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
