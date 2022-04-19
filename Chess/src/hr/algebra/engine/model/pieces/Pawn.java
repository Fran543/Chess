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
public class Pawn extends Piece {

    private Piece PromotionPiece;

    public void setPromotionPiece(Piece PromotionPiece) {
        this.PromotionPiece = PromotionPiece;
    }
    
    public Piece getPromotionPiece() {
        if (PromotionPiece == null) {
            return new Queen(this.getPiecePosition(), this.getPieceColor());
        }
        return PromotionPiece;
    }
    
    public Pawn(int pieceCoordinates, PieceColor pieceColor) {
        super(PieceType.PAWN, pieceCoordinates, pieceColor, true);
    }
    public Pawn(int pieceCoordinates, PieceColor pieceColor, final boolean isFirstMove) {
        super(PieceType.PAWN, pieceCoordinates, pieceColor, isFirstMove);
    }

    @Override
    public String toString() {
        return Piece.PieceType.PAWN.toString();
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        //Converting position to octal in order to get the piece coorinates on board
        final int[] pieceCoordinates = getCoordinateAtPosition(this.getPiecePosition());

        final List<Move> legalMoves = new ArrayList<>();
        
        if (this.getPieceColor().isWhite()) {
            if (pieceCoordinates[0] - 1 >= 0) {
                String newOctalString = String.format("%s%s", pieceCoordinates[0] - 1, pieceCoordinates[1]);
                int candidateDestinationCoordinate = Integer.parseInt(newOctalString, 8);

                final ChessField candidateDestinationField = board.getChessField(candidateDestinationCoordinate);
                if (!candidateDestinationField.isFieldOccupied()) {
                    if (pieceCoordinates[0] - 1 == 0) {
                        legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board, this, candidateDestinationCoordinate)));
                    } else {
                        legalMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));
                    }
                    if (pieceCoordinates[0] == 6) {
                        newOctalString = String.format("%s%s", pieceCoordinates[0] - 2, pieceCoordinates[1]);
                        candidateDestinationCoordinate = Integer.parseInt(newOctalString, 8);

                        final ChessField JumpCandidateDestinationField = board.getChessField(candidateDestinationCoordinate);
                        if (!JumpCandidateDestinationField.isFieldOccupied()) {
                            legalMoves.add(new Move.PawnJump(board, this, candidateDestinationCoordinate));
                        }
                    }
                }
                if (pieceCoordinates[1] - 1 >= 0) {
                    newOctalString = String.format("%s%s", pieceCoordinates[0] - 1, pieceCoordinates[1] - 1);
                    candidateDestinationCoordinate = Integer.parseInt(newOctalString, 8);

                    final ChessField AttackCandidateDestinationField = board.getChessField(candidateDestinationCoordinate);
                    if (AttackCandidateDestinationField.isFieldOccupied()) {
                        final Piece pieceAtDestination = AttackCandidateDestinationField.getPiece();
                        final PieceColor color = pieceAtDestination.getPieceColor();
                        if (this.getPieceColor() != color) {
                            if (pieceCoordinates[0] - 1 == 0) {
                                legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)));
                            } else {
                                legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                            }
                        }
                    }
                    else if (board.getEnPassantPawn() != null) {
                        final String enPassantPawnToOctalString = Integer.toOctalString(board.getEnPassantPawn().getPiecePosition());   
                        final int[] enPassantPiecePosition =  {enPassantPawnToOctalString.length() == 1 ? 0 : Character.getNumericValue(enPassantPawnToOctalString.charAt(0)), enPassantPawnToOctalString.length() == 1 ? Character.getNumericValue(enPassantPawnToOctalString.charAt(0)) : Character.getNumericValue(enPassantPawnToOctalString.charAt(1))};
                        if (enPassantPiecePosition[0] == pieceCoordinates[0] && enPassantPiecePosition[1] == pieceCoordinates[1] - 1) {
                            final Piece pieceOnTile = board.getEnPassantPawn();
                            if (this.getPieceColor() != pieceOnTile.getPieceColor()) {
                                legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnTile));
                            }
                        }
                    }
                }
                if (pieceCoordinates[1] + 1 <= 7) {
                    newOctalString = String.format("%s%s", pieceCoordinates[0] - 1, pieceCoordinates[1] + 1);
                    candidateDestinationCoordinate = Integer.parseInt(newOctalString, 8);

                    final ChessField AttackCandidateDestinationField = board.getChessField(candidateDestinationCoordinate);
                    if (AttackCandidateDestinationField.isFieldOccupied()) {
                        final Piece pieceAtDestination = AttackCandidateDestinationField.getPiece();
                        final PieceColor color = pieceAtDestination.getPieceColor();
                        if (this.getPieceColor() != color) {
                            if (pieceCoordinates[0] - 1 == 0) {
                                legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)));
                            } else {
                                legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                            }
                        }
                    }
                    else if (board.getEnPassantPawn() != null) {
                        final String enPassantPawnToOctalString = Integer.toOctalString(board.getEnPassantPawn().getPiecePosition());   
                        final int[] enPassantPiecePosition =  {enPassantPawnToOctalString.length() == 1 ? 0 : Character.getNumericValue(enPassantPawnToOctalString.charAt(0)), enPassantPawnToOctalString.length() == 1 ? Character.getNumericValue(enPassantPawnToOctalString.charAt(0)) : Character.getNumericValue(enPassantPawnToOctalString.charAt(1))};
                        if (enPassantPiecePosition[0] == pieceCoordinates[0] && enPassantPiecePosition[1] == pieceCoordinates[1] + 1) {
                            final Piece pieceOnTile = board.getEnPassantPawn();
                            if (this.getPieceColor() != pieceOnTile.getPieceColor()) {
                                legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnTile));
                            }
                        }
                    }
                }
                
            }
        } 
        else {
            if (pieceCoordinates[0] + 1 <= 7) {
                String newOctalString = String.format("%s%s", pieceCoordinates[0] + 1, pieceCoordinates[1]);
                int candidateDestinationCoordinate = Integer.parseInt(newOctalString, 8);

                final ChessField candidateDestinationField = board.getChessField(candidateDestinationCoordinate);
                if (!candidateDestinationField.isFieldOccupied()) {
                    if (pieceCoordinates[0] + 1 == 7) {
                        legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board, this, candidateDestinationCoordinate)));
                    } else {
                        legalMoves.add(new Move.PawnMove(board, this, candidateDestinationCoordinate));
                    }
                    if (pieceCoordinates[0] == 1) {
                        newOctalString = String.format("%s%s", pieceCoordinates[0] + 2, pieceCoordinates[1]);
                        candidateDestinationCoordinate = Integer.parseInt(newOctalString, 8);

                        final ChessField JumpCandidateDestinationField = board.getChessField(candidateDestinationCoordinate);
                        if (!JumpCandidateDestinationField.isFieldOccupied()) {
                            legalMoves.add(new Move.PawnJump(board, this, candidateDestinationCoordinate));
                        }
                    }
                }
                if (pieceCoordinates[1] - 1 >= 0) {
                    newOctalString = String.format("%s%s", pieceCoordinates[0] + 1, pieceCoordinates[1] - 1);
                    candidateDestinationCoordinate = Integer.parseInt(newOctalString, 8);

                    final ChessField AttackCandidateDestinationField = board.getChessField(candidateDestinationCoordinate);
                    if (AttackCandidateDestinationField.isFieldOccupied()) {
                        final Piece pieceAtDestination = AttackCandidateDestinationField.getPiece();
                        final PieceColor color = pieceAtDestination.getPieceColor();
                        if (this.getPieceColor() != color) {
                            if (pieceCoordinates[0] + 1 == 7) {
                                legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)));
                            } else {
                                legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                            }
                        }
                    }
                    else if (board.getEnPassantPawn() != null) {
                        final String enPassantPawnToOctalString = Integer.toOctalString(board.getEnPassantPawn().getPiecePosition());   
                        final int[] enPassantPiecePosition =  {enPassantPawnToOctalString.length() == 1 ? 0 : Character.getNumericValue(enPassantPawnToOctalString.charAt(0)), enPassantPawnToOctalString.length() == 1 ? Character.getNumericValue(enPassantPawnToOctalString.charAt(0)) : Character.getNumericValue(enPassantPawnToOctalString.charAt(1))};
                        if (enPassantPiecePosition[0] == pieceCoordinates[0] && enPassantPiecePosition[1] == pieceCoordinates[1] - 1) {
                            final Piece pieceOnTile = board.getEnPassantPawn();
                            if (this.getPieceColor() != pieceOnTile.getPieceColor()) {
                                legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnTile));
                            }
                        }
                    }
                }
                if (pieceCoordinates[1] + 1 <= 7) {
                    newOctalString = String.format("%s%s", pieceCoordinates[0] + 1, pieceCoordinates[1] + 1);
                    candidateDestinationCoordinate = Integer.parseInt(newOctalString, 8);

                    final ChessField AttackCandidateDestinationField = board.getChessField(candidateDestinationCoordinate);
                    if (AttackCandidateDestinationField.isFieldOccupied()) {
                        final Piece pieceAtDestination = AttackCandidateDestinationField.getPiece();
                        final PieceColor color = pieceAtDestination.getPieceColor();
                        if (this.getPieceColor() != color) {
                            if (pieceCoordinates[0] + 1 == 7) {
                                legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)));
                            } else {
                                legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                            }
                        }
                    }
                    else if (board.getEnPassantPawn() != null) {
                        final String enPassantPawnToOctalString = Integer.toOctalString(board.getEnPassantPawn().getPiecePosition());   
                        final int[] enPassantPiecePosition =  {enPassantPawnToOctalString.length() == 1 ? 0 : Character.getNumericValue(enPassantPawnToOctalString.charAt(0)), enPassantPawnToOctalString.length() == 1 ? Character.getNumericValue(enPassantPawnToOctalString.charAt(0)) : Character.getNumericValue(enPassantPawnToOctalString.charAt(1))};
                        if (enPassantPiecePosition[0] == pieceCoordinates[0] && enPassantPiecePosition[1] == pieceCoordinates[1] + 1) {
                            final Piece pieceOnTile = board.getEnPassantPawn();
                            if (this.getPieceColor() != pieceOnTile.getPieceColor()) {
                                legalMoves.add(new Move.PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnTile));
                            }
                        }
                    }
                }
            }
        }
        
        return Collections.unmodifiableList(legalMoves);
    }  
    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceColor());
    }
    

}
