/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.rmi;

import com.sun.jndi.rmi.registry.RegistryContextFactory;
import hr.algebra.engine.controller.ChessController;
import hr.algebra.jndi.InitialDirContextCloseable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 *
 * @author fran
 */
public class ChatClient {
    
    private static final String CLIENT_NAME = "Client";
    private static final String RMI_CLIENT = "client";
    private static final String RMI_SERVER = "server";
    
    private static final String RMI_URL = "rmi://localhost:1099";
    private static final int REMOTE_PORT = 1099;
    private static final int RANDOM_PORT_HINT = 0;
        
    private ChatService server;
    private ChatService client;
    private Registry registry;
    
    private final ChessController chessController;

    public ChatClient(ChessController chessController) {
        this.chessController = chessController;
        publishClient();
        fetchServer();
    }

    private void publishClient() {
        client = new ChatService() {
            @Override
            public Color getColor() throws RemoteException {
                return Color.BLACK;
            }

            @Override
            public String getName() throws RemoteException {
                return chessController.getColor().name();
            }

            @Override
            public void send(String message, int gameKey) throws RemoteException {
                System.out.println("Trying to read message");
                chessController.postMessage(message, getName(), getColor());
                System.out.println("message send");
            }

            @Override
            public int getRoomKey() throws RemoteException {
                return chessController.getRoomKey();
            }
        };
        
        try {
            registry = LocateRegistry.getRegistry(REMOTE_PORT);
            ChatService stub = (ChatService)UnicastRemoteObject.exportObject(client, RANDOM_PORT_HINT);
            registry.rebind(RMI_CLIENT, stub);
        } catch (RemoteException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fetchServer() {
        Hashtable<String, String> properties = new Hashtable<>();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, RegistryContextFactory.class.getName());
        properties.put(Context.PROVIDER_URL, RMI_URL);
        
        try(InitialDirContextCloseable context = new InitialDirContextCloseable(properties)) {
            
            server = (ChatService) context.lookup(RMI_SERVER);
            
        } catch (NamingException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void sendMessage(String message, int gameKey) {
        try {
            server.send(message,gameKey);
        } catch (RemoteException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
