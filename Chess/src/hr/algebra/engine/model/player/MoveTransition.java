/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.engine.model.player;

import hr.algebra.engine.model.board.Board;
import hr.algebra.engine.model.board.Move;

/**
 *
 * @author fran
 */
public class MoveTransition {
    
    private final Board trasitionBoard;
    final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(Board trasitionBoard, Move move, MoveStatus moveStatus) {
        this.trasitionBoard = trasitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getTrasitionBoard() {
        return trasitionBoard;
    }

    @Override
    public String toString() {
        switch (this.getTrasitionBoard().currentPlayer().getPieceColor()) {
            case WHITE:
                if (this.getTrasitionBoard().currentPlayer().isInCheckMate()) {
                    return "White player is in checkmate, black player wins";
                }else if (this.getTrasitionBoard().currentPlayer().isInCheck()) {
                    return "White player is in check";
                }else if (this.getTrasitionBoard().currentPlayer().isInStaleMate()) {
                    return "White player is in stalemate, draw";
                } else {
                    return "White player moves";
                }
            case BLACK:
                if (this.getTrasitionBoard().currentPlayer().isInCheckMate()) {
                    return "Black player is in checkmate, white player wins";
                }else if (this.getTrasitionBoard().currentPlayer().isInCheck()) {
                    return "Black player is in check";
                }else if (this.getTrasitionBoard().currentPlayer().isInStaleMate()) {
                    return "Black player is in stalemate, draw";
                } else {
                    return "Black player moves";
                }
            default:
                return "Invalid board";
        }
    }
    
    
    
    
}
