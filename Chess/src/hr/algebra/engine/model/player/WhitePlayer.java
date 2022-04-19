/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.engine.model.player;

import hr.algebra.engine.PieceColor;
import hr.algebra.engine.model.board.Board;
import hr.algebra.engine.model.board.ChessField;
import hr.algebra.engine.model.board.Move;
import hr.algebra.engine.model.board.Move.*;
import hr.algebra.engine.model.pieces.Piece;
import hr.algebra.engine.model.pieces.Rook;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author fran
 */
public class WhitePlayer extends Player {

    public WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public PieceColor getPieceColor() {
        return PieceColor.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
        
        final List<Move> kingCastles = new ArrayList<>(); 
        
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            //White king side castle
            if(!this.board.getChessField(61).isFieldOccupied() && 
                    !this.board.getChessField(62).isFieldOccupied()) {
                final ChessField rookChessField = this.board.getChessField(63);
                if (rookChessField.isFieldOccupied() && 
                        rookChessField.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() && 
                            Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty() && 
                            rookChessField.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new KingSideCastleMove(this.board, 
                                                                    this.playerKing, 
                                                                    62, 
                                                                    (Rook)rookChessField.getPiece(), 
                                                                    rookChessField.getFieldPosition(), 
                                                                    61)
                        );
                    }
                }
            }
            //White queen side castle
            if(!this.board.getChessField(59).isFieldOccupied() && 
                    !this.board.getChessField(58).isFieldOccupied() && 
                    !this.board.getChessField(57).isFieldOccupied()) {
                final ChessField rookChessField = this.board.getChessField(56);
                if (rookChessField.isFieldOccupied() && 
                        rookChessField.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty() && 
                            Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() && 
                            Player.calculateAttacksOnTile(57, opponentsLegals).isEmpty() && 
                            rookChessField.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new QueenSideCastleMove(this.board, 
                                                                    this.playerKing, 
                                                                    58, 
                                                                    (Rook)rookChessField.getPiece(), 
                                                                    rookChessField.getFieldPosition(), 
                                                                    59)
                        );
                    }
                }
            }
        }
        
        return Collections.unmodifiableList(kingCastles);
        
    }
    
}
