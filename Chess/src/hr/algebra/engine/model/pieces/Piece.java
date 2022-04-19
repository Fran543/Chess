/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.engine.model.pieces;

import hr.algebra.engine.PieceColor;
import hr.algebra.engine.model.board.Board;
import hr.algebra.engine.model.board.ChessField;
import hr.algebra.engine.model.board.Move;
import hr.algebra.engine.model.board.Move.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author fran
 */
public abstract class Piece implements Serializable{
    
    private static final long serialVersionUID = 1l;
    
    private final PieceType pieceType;
    private final int piecePosition;
    private final PieceColor pieceColor;
    private final boolean isFirstMove;
    private final int cachedHashCode;


    public Piece(final PieceType pieceType, final int pieceCoordinates, final PieceColor pieceColor, final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.piecePosition = pieceCoordinates;
        this.pieceColor = pieceColor;
        this.cachedHashCode = computeHashCode();
        this.isFirstMove = isFirstMove;
    }

    public boolean isIsFirstMove() {
        return isFirstMove;
    }

    public PieceType getPieceType() {
        return pieceType;
    }
    
    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public Integer getPiecePosition() {
        return piecePosition;
    }
    
    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    private int computeHashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.pieceType);
        hash = 47 * hash + this.piecePosition;
        hash = 47 * hash + Objects.hashCode(this.pieceColor);
        return hash;
    }
    
    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Piece other = (Piece) obj;
        if (this.piecePosition != other.piecePosition) {
            return false;
        }
        if (this.pieceType != other.pieceType) {
            return false;
        }
        if (this.pieceColor != other.pieceColor) {
            return false;
        }
        return true;
    }

    public abstract Collection<Move> calculateLegalMoves (final Board board);
    
    public abstract Piece movePiece(Move move);
    
    protected boolean addMove(String newOctalString, Board board, final List<Move> legalMoves) throws NumberFormatException {
        final int candidateDestinationCoordinate = Integer.parseInt(newOctalString, 8);
        final boolean isEnd;
        
        final ChessField candidateDestinationField = board.getChessField(candidateDestinationCoordinate);
        if (!candidateDestinationField.isFieldOccupied()) {
            legalMoves.add(new Move.NormalMove(board, this, candidateDestinationCoordinate));
            isEnd = false;
        } else {
            final Piece pieceAtDestination = candidateDestinationField.getPiece();
            final PieceColor color = pieceAtDestination.getPieceColor();
            isEnd = true;
            if (this.pieceColor != color) {
                legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
            }
        }
        return isEnd;
    }


    public enum PieceType {
        
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };
        
        private final String pieceName;
        
        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public abstract boolean isKing();
        public abstract boolean isRook();
    }
}
