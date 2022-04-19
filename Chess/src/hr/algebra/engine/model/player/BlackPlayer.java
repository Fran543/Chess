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
public class BlackPlayer extends Player {

    public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public PieceColor getPieceColor() {
        return PieceColor.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>(); 
        
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            //black king side castle
            if(!this.board.getChessField(5).isFieldOccupied() && !this.board.getChessField(6).isFieldOccupied()) {
                final ChessField rookChessField = this.board.getChessField(7);
                if (rookChessField.isFieldOccupied() && rookChessField.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(5, opponentsLegals).isEmpty() && 
                            Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty() && 
                            rookChessField.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new KingSideCastleMove(this.board, 
                                                                    this.playerKing, 
                                                                    6, 
                                                                    (Rook)rookChessField.getPiece(), 
                                                                    rookChessField.getFieldPosition(), 
                                                                    5)
                        );
                    }
                }
            }
            //black queen side castle
            if(!this.board.getChessField(1).isFieldOccupied() && !this.board.getChessField(2).isFieldOccupied() && !this.board.getChessField(3).isFieldOccupied()) {
                final ChessField rookChessField = this.board.getChessField(0);
                if (rookChessField.isFieldOccupied() && rookChessField.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(1, opponentsLegals).isEmpty() && 
                            Player.calculateAttacksOnTile(2, opponentsLegals).isEmpty() && 
                            Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty() && 
                            rookChessField.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new QueenSideCastleMove(this.board, 
                                                                    this.playerKing, 
                                                                    2, 
                                                                    (Rook)rookChessField.getPiece(), 
                                                                    rookChessField.getFieldPosition(), 
                                                                    3)
                        );
                    }
                }
            }
        }
        
        return Collections.unmodifiableList(kingCastles);
    }
    
}
