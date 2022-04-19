/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.engine.model.pieces;

import hr.algebra.engine.PieceColor;
import hr.algebra.engine.model.board.Board;
import static hr.algebra.engine.model.board.BoardUtils.getCoordinateAtPosition;
import hr.algebra.engine.model.board.ChessField;
import hr.algebra.engine.model.board.Move;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author fran
 */
public class Bishop extends Piece {

    public Bishop(final int pieceCoordinates, final PieceColor pieceColor) {
        super(PieceType.BISHOP, pieceCoordinates, pieceColor, true);
    }
    
    public Bishop(final int pieceCoordinates, final PieceColor pieceColor, final boolean isFirstMove) {
        super(PieceType.BISHOP, pieceCoordinates, pieceColor, isFirstMove);
    }

    @Override
    public String toString() {
        return Piece.PieceType.BISHOP.toString();
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        //Converting position to octal in order to get the piece coorinates on board
        final int[] pieceCoordinates =  getCoordinateAtPosition(this.getPiecePosition());
        
        final List<Move> legalMoves = new ArrayList<>();
        
        boolean isEndLeft = false;
        boolean isEndRight = false;
        
        for (int i = 1; i <= pieceCoordinates[0]; i++) {
            if (pieceCoordinates[1] - i >= 0) {
                if (!isEndLeft) {                    
                    String newOctalString = String.format("%s%s", pieceCoordinates[0] - i, pieceCoordinates[1] - i);
                    isEndLeft = addMove(newOctalString, board, legalMoves);
                }
            }
            if (pieceCoordinates[1] + i <= 7) {
                if (!isEndRight) {                    
                    String newOctalString = String.format("%s%s", pieceCoordinates[0] - i, pieceCoordinates[1] + i);
                    isEndRight = addMove(newOctalString, board, legalMoves);
                }
            }
        }
        isEndLeft = false;
        isEndRight = false;
        for (int i = 1; i <= 7 - pieceCoordinates[0]; i++) {
            if (pieceCoordinates[1] - i >= 0) {
                if (!isEndLeft) {
                    String newOctalString = String.format("%s%s", pieceCoordinates[0] + i, pieceCoordinates[1] - i);
                    isEndLeft = addMove(newOctalString, board, legalMoves);
                }
            }
            if (pieceCoordinates[1] + i <= 7) {
                if (!isEndRight) {                    
                    String newOctalString = String.format("%s%s", pieceCoordinates[0] + i, pieceCoordinates[1] + i);
                    isEndRight = addMove(newOctalString, board, legalMoves);
                }
            }
        }
        
        return Collections.unmodifiableList(legalMoves);
    }    


    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }
}
