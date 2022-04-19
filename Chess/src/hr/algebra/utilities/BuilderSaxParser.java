/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utilities;

import hr.algebra.engine.PieceColor;
import hr.algebra.engine.model.board.Board.Builder;
import hr.algebra.engine.model.pieces.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 *
 * @author fran
 */
public class BuilderSaxParser extends DefaultHandler {
    
    private static final String BUILDERS = "Builders";
    private static final String BUILDER = "Builder";
    private static final String BOARD_CONFIG = "boardConfig";
    private static final String PIECE = "Piece";
    private static final String ENPASSANT_PAWN = "enPassantPawn";

    private Game game;
    private StringBuilder elementValue;
    private Piece piece;
    
    String pieceType = "";
    String isFirstMove = "";
    String pieceColr = "";
    String piecePosition = "";
    
    private boolean inBoardConfig = false;

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementValue == null) {
            elementValue = new StringBuilder();
        } else {
            elementValue.append(ch, start, length);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        game = new Game();
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        switch (qName) {
            case BUILDERS:
                game.builderList = new ArrayList<>();
                break;
            case BUILDER:
                Builder builder = new Builder();
                PieceColor pieceColor = attr.getValue("nextMoveMaker").equals(PieceColor.WHITE.name())? PieceColor.WHITE: PieceColor.BLACK;
                builder.setMoveMaker(pieceColor);
                game.builderList.add(builder);
                break;
            case BOARD_CONFIG:
                inBoardConfig = true;
                Map<Integer, Piece> boardConfig = new HashMap<>();
                break;
            case PIECE:
                pieceType = attr.getValue("PieceType");
                isFirstMove = attr.getValue("isFirstMove");
                pieceColr = attr.getValue("pieceColor");
                piecePosition = attr.getValue("piecePosition");
                piece = createNewPiece(pieceType, isFirstMove, pieceColr, piecePosition);
                break;
            case ENPASSANT_PAWN:
                pieceType = attr.getValue("PieceType");
                isFirstMove = attr.getValue("isFirstMove");
                pieceColr = attr.getValue("pieceColor");
                piecePosition = attr.getValue("piecePosition");
                piece = createNewPiece(pieceType, isFirstMove, pieceColr, piecePosition);
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case PIECE:
                 latestBuilder().setPiece(piece);
                break;
            case ENPASSANT_PAWN:
                latestBuilder().setEnPassantPawn((Pawn)piece);
                break;
        }
    }

    private Builder latestBuilder() {
        List<Builder> articleList = game.builderList;
        int latestArticleIndex = articleList.size() - 1;
        return articleList.get(latestArticleIndex);
    }

    public Game getGame() {
        return game;
    }

    private Piece createNewPiece(String pieceType, String firstMove, String pieceColr, String piecePosition) {
        Piece piece = null;
        switch (pieceType) {
            case "ROOK":
                piece = new Rook(
                        Integer.parseInt(piecePosition),
                        pieceColr.equals(PieceColor.WHITE.name()) ? PieceColor.WHITE : PieceColor.BLACK, 
                        "True".equals(firstMove));
                break;
            case "KNIGHT":
                piece = new Knight(
                        Integer.parseInt(piecePosition),
                        pieceColr.equals(PieceColor.WHITE.name()) ? PieceColor.WHITE : PieceColor.BLACK, 
                        "True".equals(firstMove));
                break;
            case "BISHOP":
                piece = new Bishop(
                        Integer.parseInt(piecePosition),
                        pieceColr.equals(PieceColor.WHITE.toString()) ? PieceColor.WHITE : PieceColor.BLACK, 
                        "True".equals(firstMove));
                break;
            case "QUEEN":
                piece = new Queen(
                        Integer.parseInt(piecePosition),
                        pieceColr.equals(PieceColor.WHITE.toString()) ? PieceColor.WHITE : PieceColor.BLACK, 
                        "True".equals(firstMove));
                break;
            case "KING":
                piece = new King(
                        Integer.parseInt(piecePosition),
                        pieceColr.equals(PieceColor.WHITE.toString()) ? PieceColor.WHITE : PieceColor.BLACK, 
                        "True".equals(firstMove));
                break;
            case "PAWN":
                piece = new Pawn(
                        Integer.parseInt(piecePosition),
                        pieceColr.equals(PieceColor.WHITE.toString()) ? PieceColor.WHITE : PieceColor.BLACK, 
                        "True".equals(firstMove));
                break;
        }
        return piece;
    }

    public class Game {
        private List<Builder> builderList;

        public List<Builder> getBuilderList() {
            return builderList;
        }
        
    }
}
