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
public class Rook extends Piece{

    public Rook(int pieceCoordinates, PieceColor pieceColor) {
        super(PieceType.ROOK, pieceCoordinates, pieceColor, true);
    }

    public Rook(int pieceCoordinates, PieceColor pieceColor, final boolean isFirstMove) {
        super(PieceType.ROOK, pieceCoordinates, pieceColor, isFirstMove);
    }

    @Override
    public String toString() {
        return Piece.PieceType.ROOK.toString();
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        //Converting position to octal in order to get the piece coorinates on board
        final int[] pieceCoordintes =  getCoordinateAtPosition(this.getPiecePosition());
        
        final List<Move> legalMoves = new ArrayList<>();
                
        boolean isEnd = false;

        for (int i = 1; i <= pieceCoordintes[0]; i++) {
            if (!isEnd) {                
                String newOctalString = String.format("%s%s", pieceCoordintes[0] - i, pieceCoordintes[1]);
                isEnd = addMove(newOctalString, board, legalMoves);
            }
        }
        isEnd = false;
        for (int i = 1; i <= 7 - pieceCoordintes[0]; i++) {
            if (!isEnd) {                
                String newOctalString = String.format("%s%s", pieceCoordintes[0] + i, pieceCoordintes[1]);
                isEnd = addMove(newOctalString, board, legalMoves);
            }
        }
        isEnd = false;
        for (int i = 1; i <= pieceCoordintes[1]; i++) {
            if (!isEnd) {                
                String newOctalString = String.format("%s%s", pieceCoordintes[0], pieceCoordintes[1] - i);
                isEnd = addMove(newOctalString, board, legalMoves);
            }

        }
        isEnd = false;
        for (int i = 1; i <= 7 - pieceCoordintes[1]; i++) {
            if (!isEnd) {                
                String newOctalString = String.format("%s%s", pieceCoordintes[0], pieceCoordintes[1] + i);
                isEnd = addMove(newOctalString, board, legalMoves);
            }
        }
        
        return Collections.unmodifiableList(legalMoves);
    }   

    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor(), false);
    }

}
