/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.packets;

import hr.algebra.engine.PieceColor;
import hr.algebra.engine.model.board.Board.Builder;
import java.io.Serializable;

/**
 *
 * @author fran
 */
public class MovePacket implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    public int gameKey;
    public PieceColor previousPlayerColor;
    public Builder builder;

    public MovePacket(int gameKey, PieceColor previousPlayerColor, Builder builder) {
        this.gameKey = gameKey;
        this.previousPlayerColor = previousPlayerColor;
        this.builder = builder;
    }
}
