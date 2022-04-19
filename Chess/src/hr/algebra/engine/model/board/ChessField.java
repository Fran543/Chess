/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.engine.model.board;

import hr.algebra.engine.model.pieces.Piece;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fran
 */
public abstract class ChessField {

    private final int fieldPosition;

    private static final Map<Integer, EmptyField> EMPTY_FIELDS_CACHE = createAllPosibleEmptyFields();

    private static Map<Integer, EmptyField> createAllPosibleEmptyFields() {

        final Map<Integer, EmptyField> emptyFieldMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.TILE_NUMBER; i++) {
            emptyFieldMap.put(i, new EmptyField(i));
        }

        return Collections.unmodifiableMap(emptyFieldMap);
    }

    public static ChessField createChessField(final int fieldCoordinate, final Piece piece) {
        return piece != null ? new OccupiedField(piece, fieldCoordinate) : EMPTY_FIELDS_CACHE.get(fieldCoordinate);
    }

    private ChessField(int fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public abstract boolean isFieldOccupied();

    public abstract Piece getPiece();

    public int getFieldPosition() {
        return fieldPosition;
    }
    
    

    public static final class EmptyField extends ChessField {

        private EmptyField(final int fieldPosition) {
            super(fieldPosition);
        }

        @Override
        public String toString() {
            return "-";
        }

        @Override
        public boolean isFieldOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    public static final class OccupiedField extends ChessField {

        private final Piece pieceOnField;

        private OccupiedField(Piece pieceOnTile, int fieldPosition) {
            super(fieldPosition);
            this.pieceOnField = pieceOnTile;
        }

        @Override
        public String toString() {
            return getPiece().getPieceColor().isWhite() ? getPiece().toString() : getPiece().toString().toLowerCase();
        }

        @Override
        public boolean isFieldOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return pieceOnField;
        }
    }

}
