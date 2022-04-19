/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.engine.controller.ChessController;
import hr.algebra.packets.RemovePlayerPacket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author fran
 */
public class Client implements Runnable{

    //Client variables
    private String host; //IP of server
    private int port;   // Port of server
    
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = false;
    private EventListener listener;

    private final ChessController controler;

    public Client(String host, int port, ChessController controler) {
        this.host = host;
        this.port = port;
        this.controler = controler;
    }
    
    //connect to the server
    public void connect() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            listener = new EventListener(controler);
            new Thread(this).start();
//            Platform.runLater(this);
        } catch (ConnectException ex) {
            System.out.println("Unable to connect to the server");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //close the connection
    public void close() {
        try {
            running = false;
            RemovePlayerPacket packet = new RemovePlayerPacket();
            sendObject(packet);
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //send data to the server
    public void sendObject(Object packet) {
        try {
            out.writeObject(packet);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isRunning() {
        return running;
    }
    
    @Override
    public void run() {
        try {
            running = true; 
            while (running) {
                try {
                     Object data = in.readObject();
                     listener.received(data);
                     System.out.println(Thread.currentThread());
                } catch (SocketException ex) {
                    close();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}
