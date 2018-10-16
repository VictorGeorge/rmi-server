/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.distsys.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static me.distsys.common.Configuration.PORT;

public class ServerMain {
    private ServerInterfaceImpl serverImpl;

    public ServerMain() throws RemoteException {
        serverImpl = new ServerInterfaceImpl();
        // instancia serviço de nomes e implementacao do servidor
        Registry registry = LocateRegistry.createRegistry(PORT);
        registry.rebind("serverImpl", serverImpl);
        System.err.println("Running serverImpl on port " + PORT);
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
