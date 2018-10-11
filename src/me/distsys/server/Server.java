/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.distsys.server;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import static me.distsys.common.Configuration.PORT;
import static me.distsys.common.Configuration.SERVER_DEFAULT_FOLDER;

public class Server {
    /**
     * Main do servidor
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // cria pasta padrão se ela não existir
            File defaultFolder = new File(SERVER_DEFAULT_FOLDER);
            if (!defaultFolder.exists()) {
                boolean b = defaultFolder.mkdirs();
                System.err.println(b ? "Default folder created successfully!" : "Failure creating default folder");
            }
            ServerInterfaceImpl server = new ServerInterfaceImpl();
            // instancia serviço de nomes e implementacao do servidor
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.rebind("server", server);
            System.out.println("Running server on port " + PORT);
        } catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
