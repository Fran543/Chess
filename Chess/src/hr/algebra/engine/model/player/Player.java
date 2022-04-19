/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.engine.model.player;

import hr.algebra.engine.PieceColor;
import hr.algebra.engine.model.board.Board;
import hr.algebra.engine.model.board.Move;
import hr.algebra.engine.model.pieces.King;
import hr.algebra.engine.model.pieces.Piece;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author fran
 */
public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    public Player(final Board board, final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {
        this.board = board;
        this.playerKing = establishKing();
        playerLegals.addAll(calculateKingCastles(playerLegals, opponentLegals));
        this.legalMoves = Collections.unmodifiableCollection(playerLegals);        
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegals).isEmpty();
    }

    public King getPlayerKing() {
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return legalMoves;
    }

    protected static Collection<Move> calculateAttacksOnTile(Integer piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (final Move move : moves) {
            if (piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(attackMoves);
    }
    
    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Should not reach here! Not a valid board!");
    }
    
    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }
    
    public boolean isInCheck() {
        return this.isInCheck;
    }
    
    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }
    
    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }

    protected boolean hasEscapeMoves() {
        for(final Move move : this.legalMoves)  {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isCastled() {
        return false;
    }
    
    public MoveTransition makeMove(final Move move) {
        
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();
        
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(), transitionBoard.currentPlayer().getLegalMoves());
        
        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }
    
    public abstract Collection<Piece> getActivePieces();
    public abstract PieceColor getPieceColor();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);

}
