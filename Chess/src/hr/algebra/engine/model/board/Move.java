/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.engine.model.board;

import hr.algebra.engine.model.board.Board.*;
import hr.algebra.engine.model.pieces.*;
import java.util.Objects;

/**
 *
 * @author fran
 */
public abstract class Move {
    
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;
    
    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destinationCoordinates) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinates;
        this.isFirstMove = movedPiece.isFirstMove();
    }
    
    private Move(final Board board, final int destinationCoordinates) {
        this.board = board;
        this.movedPiece = null;
        this.destinationCoordinate = destinationCoordinates;
        this.isFirstMove = false;
    }

    public Integer getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }
    
    public boolean isAttack() {
        return false;
    }
    
    public boolean isCastlingMove() {
        return false;
    }
    
    public Piece getAttackedPiece() {
        return null;
    }

    public Board getBoard() {
        return board;
    }
   

    public Board execute() {
        final Builder builder = new Builder();
        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getPieceColor());
        return builder.build();
    }

    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }


    public static final class NormalMove extends Move {
        
        public NormalMove(final Board board, final Piece movedPiece, final int destinationCoordinates) {
            super(board, movedPiece, destinationCoordinates);
        }   

    }
    
    public static class AttackMove extends Move {
        
        final Piece attackedPiece;
        
        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinates, final Piece AttackedPiece) {
            super(board, movedPiece, destinationCoordinates);
            this.attackedPiece = AttackedPiece;
        }   

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public boolean isAttack() {
            return true;
        }   
    }
    
    public static class PawnPromotion extends Move {
        
        final Move decoratedMove;
        final Pawn promotedPawn;
    
        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }

        @Override
        public Board execute() {
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Board.Builder builder = new Builder();
            for (final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()) {
                if (!this.promotedPawn.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()) {
                    builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getPieceColor());
            return builder.build();
        }

        @Override
        public Piece getAttackedPiece() {
            return this.decoratedMove.getAttackedPiece();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }
        
        
    
    }
    
    public static final class PawnMove extends Move {
        
        public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinates) {
            super(board, movedPiece, destinationCoordinates);
        }   
    }
    
    public static class PawnAttackMove extends AttackMove {
        
        public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinates, final Piece AttackedPiece) {
            super(board, movedPiece, destinationCoordinates, AttackedPiece);
        }   
    }
    
    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        
        public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinates, final Piece AttackedPiece) {
            super(board, movedPiece, destinationCoordinates, AttackedPiece);
        }   

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                if (!piece.equals(this.getAttackedPiece())) {
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getPieceColor());
            return builder.build();
        }
        
        
    }
    
    public static final class PawnJump extends Move {
        
        public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinates) {
            super(board, movedPiece, destinationCoordinates);
        }  

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                    builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getPieceColor());
            
            return builder.build();
        }
        
        
    }
    
    static abstract class CastleMove extends Move {
        
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;
        
        public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinates, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinates);
            this.castleRook  = castleRook;
            this.castleRookStart  = castleRookStart;
            this.castleRookDestination  = castleRookDestination;
        }   

        public Rook getCastleRook() {
            return castleRook;
        }
        @Override
        public boolean isCastlingMove () {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                    builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceColor()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getPieceColor());
            
            return builder.build();
        }
        
    }
    
    public static final class KingSideCastleMove extends CastleMove {
        
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinates, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinates, castleRook, castleRookStart, castleRookDestination);
        }   

        @Override
        public String toString() {
            return "0-0";
        }
        
        
    }
    
    public static final class QueenSideCastleMove extends CastleMove {
        
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinates, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinates, castleRook, castleRookStart, castleRookDestination);
        }  

        @Override
        public String toString() {
            return "0-0-0";
        }
        
    }
    
    public static final class NullMove extends Move {
        
        public NullMove() {
            super(null, 65);
        } 

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute the null move!");
        }

        @Override
        public int getCurrentCoordinate() {
            return -1; 
        }
        
        
        
    }
    
    
    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("Not instantiable!");
        }
        
        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate)  {
            for(final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
        
    }
}
