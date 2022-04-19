/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.engine.controller.ChessController;
import hr.algebra.engine.model.board.Board.Builder;
import hr.algebra.utilities.ByteUtils;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author fran
 */
public class ClientThread extends Thread{

    private static final String PROPS_FILE = "socket.properties";
    private static final String CLIENT_PORT = "CLIENT_PORT";
    private static final String GROUP = "GROUP";
    private static final Properties PROPERTIES = new Properties();
    private final String groupAddres;
    
    static {
        try {
            PROPERTIES.load(new FileInputStream(PROPS_FILE));
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private final ChessController controler;

    public ClientThread(ChessController controler, String groupAddres) {
        this.controler = controler;
        this.groupAddres = groupAddres;
    }
    
    @Override
    public void run() {
        try(MulticastSocket client = new MulticastSocket(Integer.valueOf(PROPERTIES.getProperty(CLIENT_PORT)))){
            InetAddress groupAddress = InetAddress.getByName(groupAddres);
            int clientPort = Integer.valueOf(PROPERTIES.getProperty(CLIENT_PORT));
            System.err.println(controler.hashCode() + "... joining group");
            client.joinGroup(groupAddress);
            
            while(true) {
                
                byte[] numberOfBulderBytes = new byte[4];
                DatagramPacket packet = new DatagramPacket(numberOfBulderBytes, numberOfBulderBytes.length);
                client.receive(packet);
                int length = ByteUtils.byteArrayToInt(numberOfBulderBytes);
                
                byte[] builderBytes = new byte[length];
                packet = new DatagramPacket(builderBytes, numberOfBulderBytes.length);
                client.receive(packet);
                
                try (ByteArrayInputStream bais = new ByteArrayInputStream(builderBytes);
                        ObjectInputStream ois = new ObjectInputStream(bais)) {
                    Builder builder = (Builder) ois.readObject();
                    Platform.runLater(() -> controler.updateBoard(builder));
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
