/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.packets;

import hr.algebra.engine.PieceColor;
import java.io.Serializable;

/**
 *
 * @author fran
 */
public class NewGamePacket implements Serializable{
        
    private static final long serialVersionUID = 1L;
    
    public int gameKey;
    public PieceColor PlayerColor;

    public NewGamePacket(int gameKey, PieceColor PlayerColor) {
        this.gameKey = gameKey;
        this.PlayerColor = PlayerColor;
    }

    public NewGamePacket(int roomKey) {
        this.gameKey = roomKey;
    }
}
