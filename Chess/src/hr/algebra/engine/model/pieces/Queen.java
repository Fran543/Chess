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
public class Queen extends Piece{

    public Queen(final int pieceCoordinates, final PieceColor pieceColor) {
        super(PieceType.QUEEN, pieceCoordinates, pieceColor, true);
    }
    
    public Queen(final int pieceCoordinates, final PieceColor pieceColor, final boolean isFirstMove) {
        super(PieceType.QUEEN, pieceCoordinates, pieceColor, isFirstMove);
    }

    @Override
    public String toString() {
        return Piece.PieceType.QUEEN.toString();
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        //Converting position to octal in order to get the piece coorinates on board
        final int[] pieceCoordinates = getCoordinateAtPosition(this.getPiecePosition());
        
        final List<Move> legalMoves = new ArrayList<>();

        boolean isEndLeft = false;
        boolean isEndRight = false;
        boolean isEndMiddle = false;

        
        for (int i = 1; i <= pieceCoordinates[0]; i++) {
            if (!isEndMiddle) {                
                String newOctalString = String.format("%s%s", pieceCoordinates[0] - i, pieceCoordinates[1]);
                isEndMiddle = addMove(newOctalString, board, legalMoves);
            }

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
        isEndMiddle = false;
        for (int i = 1; i <= 7 - pieceCoordinates[0]; i++) {
            if (!isEndMiddle) {              
                String newOctalString = String.format("%s%s", pieceCoordinates[0] + i, pieceCoordinates[1]);
                isEndMiddle = addMove(newOctalString, board, legalMoves);
            }

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
        isEndLeft = false;
        isEndRight = false;
        for (int i = 1; i <= pieceCoordinates[1]; i++) {
            if (!isEndLeft) {                
                String newOctalString = String.format("%s%s", pieceCoordinates[0], pieceCoordinates[1] - i);
                isEndLeft = addMove(newOctalString, board, legalMoves);
            }
        }
        for (int i = 1; i <= 7 - pieceCoordinates[1]; i++) {
            if (!isEndRight) {                
                String newOctalString = String.format("%s%s", pieceCoordinates[0], pieceCoordinates[1] + i);
                isEndRight = addMove(newOctalString, board, legalMoves);
            }
        }
        
        return Collections.unmodifiableList(legalMoves);
    }
   

    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }

}
