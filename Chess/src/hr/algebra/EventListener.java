/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.engine.controller.ChessController;
import hr.algebra.engine.model.board.Board;
import hr.algebra.packets.AddPlayerPacket;
import hr.algebra.packets.CreateGamePacket;
import hr.algebra.packets.ErrorPacket;
import hr.algebra.packets.GamePacket;
import hr.algebra.packets.MovePacket;
import hr.algebra.packets.NewGamePacket;
import hr.algebra.packets.RemovePlayerPacket;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author fran
 */
public class EventListener {

    private final ChessController controler;
    
    EventListener(ChessController controler) {
        this.controler = controler;
    }
    
    public void received(Object p) {
        
        if (p instanceof CreateGamePacket) {
            CreateGamePacket packet = (CreateGamePacket)p;
        } else if(p instanceof MovePacket) {
            System.out.println("Trying to read move from server");
            MovePacket packet = (MovePacket)p;
            Platform.runLater(() -> controler.updateBoard(packet.builder));
            System.out.println("move was read from server");
        } else if(p instanceof RemovePlayerPacket) {
            RemovePlayerPacket packet = (RemovePlayerPacket)p;
        } else if(p instanceof NewGamePacket) {
            NewGamePacket packet = (NewGamePacket)p;
            Platform.runLater(() -> controler.setBoardDirection(packet.PlayerColor));
            Platform.runLater(() -> controler.setRoomKey(packet.gameKey));
            Platform.runLater(() -> controler.setColor(packet.PlayerColor));
            Platform.runLater(() -> controler.updateBoard(Board.createStandardBoard()));
            Platform.runLater(() -> controler.handleConnect());
            System.out.println(packet.PlayerColor);
        } else if(p instanceof GamePacket) {
            GamePacket packet = (GamePacket)p;
            Platform.runLater(() -> controler.setRoomKey(packet.gameKey));
            Platform.runLater(() -> controler.showInfoMessage("Kreirana je nova igra pod ključem: " + packet.gameKey));
            System.out.println(packet.gameKey);
        } else if(p instanceof ErrorPacket) {
            ErrorPacket packet = (ErrorPacket)p;
            Platform.runLater(() -> controler.showErrorMessage(packet.message));
            System.out.println(packet.message);
        } 
    }
    
}
