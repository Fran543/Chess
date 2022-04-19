/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javafx.scene.paint.Color;

/**
 *
 * @author fran
 */
public interface ChatService extends Remote{
    int getRoomKey() throws RemoteException;
    Color getColor() throws RemoteException;
    String getName() throws RemoteException; 
    void send(String message, int gameKey) throws RemoteException;
}
