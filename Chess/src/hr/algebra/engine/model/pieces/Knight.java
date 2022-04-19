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
public class Knight extends Piece{

    
    
    public Knight(final int pieceCoordinates, final PieceColor pieceColor) {
        super(PieceType.KNIGHT, pieceCoordinates, pieceColor, true);
    }
    
    public Knight(final int pieceCoordinates, final PieceColor pieceColor, final boolean isFirstMove) {
        super(PieceType.KNIGHT, pieceCoordinates, pieceColor, isFirstMove);
    }

    @Override
    public String toString() {
        return Piece.PieceType.KNIGHT.toString();
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        //Converting position to octal in order to get the piece coorinates on board
        final int[] pieceCoordinates = getCoordinateAtPosition(this.getPiecePosition());
        
        final List<Move> legalMoves = new ArrayList<>();
           
        
        
        if (pieceCoordinates[0] - 2 >= 0) {
            if (pieceCoordinates[1] - 1 >= 0) {
                String newOctalString = String.format("%s%s", pieceCoordinates[0] - 2, pieceCoordinates[1] - 1);
                addMove(newOctalString, board, legalMoves );
            }
            if (pieceCoordinates[1] + 1 <= 7) {
                
                String newOctalString = String.format("%s%s", pieceCoordinates[0] - 2, pieceCoordinates[1] + 1);
                addMove(newOctalString, board, legalMoves );
            }
        }
        if (pieceCoordinates[0] - 1 >= 0) {
            if (pieceCoordinates[1] - 2 >= 0) {
                
                String newOctalString = String.format("%s%s", pieceCoordinates[0] - 1, pieceCoordinates[1] - 2);
                addMove(newOctalString, board, legalMoves );
            }
            if (pieceCoordinates[1] + 2 <= 7) {
                
                String newOctalString = String.format("%s%s", pieceCoordinates[0] - 1, pieceCoordinates[1] + 2);
                addMove(newOctalString, board, legalMoves );
            }
        }
        if (pieceCoordinates[0] + 1 <= 7) {
            if (pieceCoordinates[1] - 2 >= 0) {
                
                String newOctalString = String.format("%s%s", pieceCoordinates[0] + 1, pieceCoordinates[1] - 2);
                addMove(newOctalString, board, legalMoves );
            }
            if (pieceCoordinates[1] + 2 <= 7) {
                
                String newOctalString = String.format("%s%s", pieceCoordinates[0] + 1, pieceCoordinates[1] + 2);
                addMove(newOctalString, board, legalMoves );
            }
        }
        if (pieceCoordinates[0] + 2 <= 7) {
            if (pieceCoordinates[1] - 1 >= 0) {
                
                String newOctalString = String.format("%s%s", pieceCoordinates[0] + 2, pieceCoordinates[1] - 1);
                addMove(newOctalString, board, legalMoves );
            }
            if (pieceCoordinates[1] + 1 <= 7) {
                
                String newOctalString = String.format("%s%s", pieceCoordinates[0] + 2, pieceCoordinates[1] + 1);
                addMove(newOctalString, board, legalMoves );
            }
        }
        
//        for (int i = 1; i < 3; i++) {
//            
//            
//            if (pieceCoordinates[0] - i >= 0) {
//                if (pieceCoordinates[1] - i >= 0) {
//                //addPosition
//                }
//                if (pieceCoordinates[1] + i <= 7) {
//                    //addPosition
//                }
//            }
//            if (pieceCoordinates[0] + i <= 7) {
//                if (pieceCoordinates[1] - i >= 0) {
//                //addPosition
//                }
//                if (pieceCoordinates[1] + i <= 7) {
//                    //addPosition
//                }
//            }
//        }
        return Collections.unmodifiableList(legalMoves);
    }    

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }
}
