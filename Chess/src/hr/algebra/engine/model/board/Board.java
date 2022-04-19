/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.engine.model.board;

import hr.algebra.engine.PieceColor;
import hr.algebra.engine.model.player.BlackPlayer;
import hr.algebra.engine.model.player.Player;
import hr.algebra.engine.model.player.WhitePlayer;
import hr.algebra.engine.model.pieces.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author fran
 */
public class Board {
    
    private final List<ChessField> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;

    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, PieceColor.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, PieceColor.BLACK);
        this.enPassantPawn = builder.enPassantPawn;
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
        
        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
        
    }
    
    public ChessField getChessField(final int ChessFieldCoordinate) {
        return gameBoard.get(ChessFieldCoordinate);
    }
    
    public Player currentPlayer() {
        return this.currentPlayer;
    }
    
    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        
        final List<Move> legalMoves = new ArrayList<>();
        
        for (final Piece piece : pieces) {
            
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        
        return legalMoves;
    }

    private static Collection<Piece> calculateActivePieces(final List<ChessField> gameBoard, final PieceColor pieceColor) {
        final List<Piece> activePieces = new ArrayList<>();
        
        for (final ChessField chessField : gameBoard) {
            if (chessField.isFieldOccupied()) {
                final Piece piece = chessField.getPiece();
                if (piece.getPieceColor() == pieceColor) {
                    activePieces.add(piece);
                }
            }
        }
        
        return Collections.unmodifiableList(activePieces);        
    }

    private List<ChessField> createGameBoard(final Builder builder) {
        final ChessField[] chessFields = new ChessField[64];
        for (int i = 0; i < BoardUtils.TILE_NUMBER; i++) {
            chessFields[i] = ChessField.createChessField(i, builder.boardConfig.get(i));
        }
        return Collections.unmodifiableList(Arrays.asList(chessFields));
    }
    
    public static Board createStandardBoard() {
        final Builder builder = new Builder();
        //BLACK
        builder.setPiece(new Rook(0, PieceColor.BLACK));
        builder.setPiece(new Knight(1, PieceColor.BLACK));
        builder.setPiece(new Bishop(2, PieceColor.BLACK));
        builder.setPiece(new Queen(3, PieceColor.BLACK));
        builder.setPiece(new King(4, PieceColor.BLACK));
        builder.setPiece(new Bishop(5, PieceColor.BLACK));
        builder.setPiece(new Knight(6, PieceColor.BLACK));
        builder.setPiece(new Rook(7, PieceColor.BLACK));
        builder.setPiece(new Pawn(8, PieceColor.BLACK));
        builder.setPiece(new Pawn(9, PieceColor.BLACK));
        builder.setPiece(new Pawn(10, PieceColor.BLACK));
        builder.setPiece(new Pawn(11, PieceColor.BLACK));
        builder.setPiece(new Pawn(12, PieceColor.BLACK));
        builder.setPiece(new Pawn(13, PieceColor.BLACK));
        builder.setPiece(new Pawn(14, PieceColor.BLACK));
        builder.setPiece(new Pawn(15, PieceColor.BLACK));
        //WHITE
        builder.setPiece(new Pawn(48, PieceColor.WHITE));
        builder.setPiece(new Pawn(49, PieceColor.WHITE));
        builder.setPiece(new Pawn(50, PieceColor.WHITE));
        builder.setPiece(new Pawn(51, PieceColor.WHITE));
        builder.setPiece(new Pawn(52, PieceColor.WHITE));
        builder.setPiece(new Pawn(53, PieceColor.WHITE));
        builder.setPiece(new Pawn(54, PieceColor.WHITE));
        builder.setPiece(new Pawn(55, PieceColor.WHITE));
        builder.setPiece(new Rook(56, PieceColor.WHITE));
        builder.setPiece(new Knight(57, PieceColor.WHITE));
        builder.setPiece(new Bishop(58, PieceColor.WHITE));
        builder.setPiece(new Queen(59, PieceColor.WHITE));
        builder.setPiece(new King(60, PieceColor.WHITE));
        builder.setPiece(new Bishop(61, PieceColor.WHITE));
        builder.setPiece(new Knight(62, PieceColor.WHITE));
        builder.setPiece(new Rook(63, PieceColor.WHITE));
        
        builder.setMoveMaker(PieceColor.WHITE);
        
        return builder.build();
    }
    
    public Builder makeBuilder() {
        Builder builder = new Builder();
        
        for (ChessField chessField : gameBoard) {
            if (chessField.isFieldOccupied()) {
                builder.setPiece(chessField.getPiece());
            }
        }
        builder.setMoveMaker(currentPlayer.getPieceColor());
        builder.setEnPassantPawn(getEnPassantPawn());
        
        return builder;
    }

    @Override
    public String toString() {
        
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.TILE_NUMBER; i++) {
            final String chessFieldText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", chessFieldText));
            if ((i + 1) % BoardUtils.TILE_NUMBER_PER_ROW == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Player whitePlayer() {
        return this.whitePlayer;
    }
    
    public Player blackPlayer() {
        return this.blackPlayer;
    }

    public Iterable<Move> getAllLegalMoves() {
        List<Move> allLegalMoves = new ArrayList<>();
        allLegalMoves.addAll(this.whitePlayer.getLegalMoves());
        allLegalMoves.addAll(this.blackPlayer.getLegalMoves());
        return Collections.unmodifiableList(allLegalMoves);
    }

    public Pawn getEnPassantPawn() {
        return enPassantPawn;
    }

    public static class Builder implements Serializable{
        
        private static final long serialVersionUID = 1l;
        
        public Map<Integer, Piece> boardConfig;
        PieceColor nextMoveMaker; //Color of next player
        Pawn enPassantPawn;
        
        public Builder() {
            this.boardConfig = new HashMap<>();
        }
        
        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final PieceColor color) {
            this.nextMoveMaker = color;
            return this;
        }

        public PieceColor getNextMoveMaker() {
            return nextMoveMaker;
        }
        
        public Board build() {
            return new Board(this);
        }


        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }

        public Pawn getEnPassantPawn() {
            return enPassantPawn;
        }
        
    }
    
}
