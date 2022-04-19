/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.engine.controller;

import hr.algebra.Client;
import hr.algebra.engine.PieceColor;
import hr.algebra.engine.model.player.MoveTransition;
import hr.algebra.engine.model.board.Board;
import hr.algebra.engine.model.board.Board.Builder;
import hr.algebra.engine.model.board.BoardUtils;
import static hr.algebra.engine.model.board.BoardUtils.getCoordinateAtPosition;
import hr.algebra.engine.model.board.ChessField;
import hr.algebra.engine.model.board.Move;
import hr.algebra.engine.model.board.Move.PawnPromotion;
import hr.algebra.engine.model.pieces.Bishop;
import hr.algebra.engine.model.pieces.Knight;
import hr.algebra.engine.model.pieces.Pawn;
import hr.algebra.engine.model.pieces.Piece;
import hr.algebra.engine.model.pieces.Queen;
import hr.algebra.engine.model.pieces.Rook;
import hr.algebra.packets.AddPlayerPacket;
import hr.algebra.packets.CreateGamePacket;
import hr.algebra.packets.MovePacket;
import hr.algebra.packets.NewGamePacket;
import hr.algebra.packets.RemovePlayerPacket;
import server.rmi.ChatClient;
import hr.algebra.threads.ClockThread;
import hr.algebra.utilities.BuilderXmlParser;
import hr.algebra.utilities.ReflectionUtils;
import hr.algebra.utilities.SerializationUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author fran
 */
public class ChessController implements Initializable {

    //Game related variables
    private Board chessBoard;
    private ChessField sourceChessField;
    private ChessField destinationChessField;
    private Piece movedPiece;
    private BoardDirection boardDirection = BoardDirection.NORMAL;
    private PieceColor color;
    private boolean isOnline = false;
   
    private final List<ChessFieldPane> boardFields = new ArrayList<>();
    
    //multiplayer related variables
    private Client client;

    //Message related variables
    private final String TIME_FORMAT = "HH:mm:ss";
    private static final String MESSAGE_FORMAT = "%s (%s): %s";
    private static final String CLIENT_NAME = "Server";
    private static final int MESSAGE_LENGHT = 78;
    private static final int FONT_SIZE = 15;
    private ChatClient chatClient;
    private int roomKey;

    private ObservableList<Node> messages;
    
    //GUI related variables
//    private static String DEFAULT_PIECE_IMAGE_PATH = "@../resources/Images/"; 
    private static final String DEFAULT_PIECE_IMAGE_PATH = "C:\\Java2\\Chess\\src\\hr\\algebra\\engine\\resources\\Images\\";
    private static final int TILE_DIMENSION = 50;

    //Serialization related variables
    private static final String FILE_NAME = "chessBoard.ser";

    //Xml related variables
    private static List<Builder> builderList = new ArrayList<>();
    
    @FXML
    private TilePane boardTilePane;
    @FXML
    private Label lblStatus;
    @FXML
    private AnchorPane apMainBoard;
    @FXML
    private ComboBox<Piece> cbWhitePromotionPiece;
    @FXML
    private ComboBox<Piece> cbBlackPromotionPiece;
    @FXML
    private Button btnFlipBoard;
    @FXML
    private Button btnNewGame1;
    @FXML
    private MenuItem miShowDocumentation;
    @FXML
    private TextField tfMessage;
    @FXML
    private VBox vbMessages;
    @FXML
    private Button btnSend;
    @FXML
    private ScrollPane spContainer;
    @FXML
    private Label lblClock;
    @FXML
    private MenuItem miCreateNewMultiplayerGame;
    @FXML
    private TextField tfGameKey;
    @FXML
    private Button btnJoinGame;
    @FXML
    private MenuItem miSaveGame;
    @FXML
    private Button btnPreviousMove;
    @FXML
    private Button btnNextMove;
    @FXML
    private MenuItem miPlayGme;
    @FXML
    private MenuItem miDisconnectFromServeer;
    @FXML
    private MenuItem miWatchReplay;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        init();
        initClockThread();
        initMessages();
    }

    private void init() {
        initComboBoxes();
        initChessBoard();
    }
    
    private void initComboBoxes() {
        cbWhitePromotionPiece.setItems(FXCollections.observableArrayList(
                new Queen(1, PieceColor.WHITE),
                new Knight(1, PieceColor.WHITE),
                new Rook(1, PieceColor.WHITE),
                new Bishop(1, PieceColor.WHITE)));
        cbBlackPromotionPiece.setItems(FXCollections.observableArrayList(
                new Queen(1, PieceColor.BLACK),
                new Knight(1, PieceColor.BLACK),
                new Rook(1, PieceColor.BLACK),
                new Bishop(1, PieceColor.BLACK)));
    }

    private void initChessBoard() {
        for (int i = 0; i < BoardUtils.TILE_NUMBER; i++) {
            final ChessFieldPane chessFieldPane = new ChessFieldPane(boardTilePane, i);
            boardFields.add(chessFieldPane);
            boardTilePane.getChildren().add(chessFieldPane);
        }
        try {
            Builder builder = (Builder) SerializationUtils.read(FILE_NAME);
            chessBoard = builder.build();
            drawBoard(chessBoard);
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChessController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initClockThread() {
        ClockThread clockThread = new ClockThread(lblClock);
        new Thread(clockThread).start();
    }

    private void initMessages() {
        
        messages = FXCollections.observableArrayList();
        Bindings.bindContentBidirectional(messages, vbMessages.getChildren());
        tfMessage.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() >= MESSAGE_LENGHT) {
                    ((StringProperty)observable).setValue(oldValue);
                }
            }
        });
    }
    
    public void drawBoard(final Board chessBoard) {
        boardTilePane.getChildren().clear();
        for (final ChessFieldPane chessFieldPane : boardDirection.traverse(boardFields)) {
            chessFieldPane.drawField(chessBoard);
            boardTilePane.getChildren().add(chessFieldPane);
        }
    }

    public void setColor(PieceColor color) {
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    public void setChatRoom(int roomKey) {
        this.roomKey = roomKey;
    }

    public void setBoardDirection(PieceColor color) {
        this.boardDirection = color == PieceColor.WHITE ? BoardDirection.NORMAL : BoardDirection.FLIPPED;
    }
    
    public void setRoomKey(int gameKey) {
        this.roomKey = gameKey;
    }

    public int getRoomKey() {
        return this.roomKey;
    }

    @FXML
    private void startNewGame(MouseEvent event) {
        builderList.clear();
        this.chessBoard = Board.createStandardBoard();
        this.boardDirection = BoardDirection.NORMAL;
        builderList.add(chessBoard.makeBuilder());
        lblStatus.setText("White player moves");
        if (isOnline) {
            client.sendObject(new NewGamePacket(roomKey));
        } else {
            drawBoard(chessBoard);
        }
    }

    @FXML
    private void flipBoard(MouseEvent event) {
        boardDirection = boardDirection.opposite();
        drawBoard(chessBoard);
    }

    @FXML
    private void showDocumentation(ActionEvent event) {
        ReflectionUtils.writeDocumentation();
    }

    @FXML
    private void createNewMultiplayerGame(ActionEvent event) {
        client = new Client("localhost", 6666, this);
        System.out.println("client about to be created");
        client.connect();
        System.out.println("client is connected");
        System.out.println("client is running");
        System.out.println("about to send packet");
        client.sendObject(new CreateGamePacket());
        System.out.println("packet send");
        isOnline = true;
        
//        btnSend.setDisable(false);
//        btnJoinGame.setDisable(true);
//        miCreateNewMultiplayerGame.setDisable(true);
//        miWatchReplay.setDisable(true);
//        miPlayGme.setDisable(true);
//        System.out.println(roomKey);
//
        System.out.println("trying to connect to chat server");
        chatClient = new ChatClient(this); 
        System.out.println("connected to chat server");
    }
    
    @FXML
    private void joinMultiplayerGame(ActionEvent event) {
        
        client = new Client("localhost", 6666, this);
        client.connect();
        int gameKey = Integer.parseInt(tfGameKey.getText());
        client.sendObject(new AddPlayerPacket(gameKey));
//        isOnline = true;
//        btnSend.setDisable(false);
//        btnJoinGame.setDisable(true);
//        miCreateNewMultiplayerGame.setDisable(true);
//        miWatchReplay.setDisable(true);
//        miPlayGme.setDisable(true);
        chatClient = new ChatClient(this); 


    }
        
    public void handleConnect() {
        isOnline = true;
        btnSend.setDisable(false);
        btnJoinGame.setDisable(true);
        miDisconnectFromServeer.setDisable(false);
        miCreateNewMultiplayerGame.setDisable(true);
        miWatchReplay.setDisable(true);
        miPlayGme.setDisable(true);
//        chatClient = new ChatClient(this);
    }
    
    @FXML
    private void DisconnectFromServer(ActionEvent event) {
        btnSend.setDisable(true);
        btnJoinGame.setDisable(false);
        miCreateNewMultiplayerGame.setDisable(false);
        miWatchReplay.setDisable(false);
        miPlayGme.setDisable(false);
        miDisconnectFromServeer.setDisable(true);
        client.sendObject(new RemovePlayerPacket());
    }
    
    @FXML
    private void send(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }
    
    @FXML
    private void sendMessage() {
        if (tfMessage.getText().trim().length() > 0) {
            System.out.println("trying to read message");
            chatClient.sendMessage(tfMessage.getText().trim(), roomKey);
            System.out.println("message read");
            tfMessage.clear();
        }
    }

    @FXML
    private void SaveGame(ActionEvent event) {
        BuilderXmlParser.creteXmlDocument(builderList);
    }

    int curentMove = 0;
    @FXML
    private void previousMove(ActionEvent event) {    
        if (curentMove > 0) {
            curentMove --;
            chessBoard = builderList.get(curentMove).build();
            drawBoard(chessBoard);      
        }
    }

    @FXML
    private void nextMove(ActionEvent event) {
        if (curentMove < builderList.size() - 1) {
            curentMove ++;
            chessBoard = builderList.get(curentMove).build();
            drawBoard(chessBoard);       
        }
    }

    @FXML
    private void watchReplay(ActionEvent event) {
        btnJoinGame.setDisable(true);
        btnNewGame1.setDisable(true);
        btnSend.setDisable(true);
        miCreateNewMultiplayerGame.setDisable(true);
        btnJoinGame.setDisable(true);
        btnNextMove.setDisable(false);
        btnPreviousMove.setDisable(false);
        builderList = BuilderXmlParser.readXmlDocFromFile().getBuilderList();
        curentMove = 0;
        chessBoard = builderList.get(curentMove).build();
        drawBoard(chessBoard);
    }

    @FXML
    private void PlayGame(ActionEvent event) {
        btnJoinGame.setDisable(false);
        btnNewGame1.setDisable(false);
        miCreateNewMultiplayerGame.setDisable(false);
        btnJoinGame.setDisable(false);
        btnNextMove.setDisable(true);
        btnPreviousMove.setDisable(true);
        chessBoard = Board.createStandardBoard();
        drawBoard(chessBoard);
    }
    

    private void addMessage(String message, String name, Color color) {
        Label label = new Label();
        label.setFont(new Font(FONT_SIZE));
        label.setTextFill(color);
        label.setWrapText(true);
        label.setText(
                String.format(
                        MESSAGE_FORMAT, 
                        LocalTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMAT)),
                        name,
                        message));
        messages.add(label);
        moveScrollPane();
    }

    private void moveScrollPane() {
        spContainer.applyCss();
        spContainer.layout();
        spContainer.setVvalue(1D);
    }
    
    public void postMessage(String message, String name, Color color) {
        Platform.runLater(() -> addMessage(message, name, color));
    };

    public void updateBoard(Builder builder) {
        System.out.println("Updating board from server");
        chessBoard = builder.build();
        drawBoard(chessBoard);
        System.out.println("board updated from server");
    }
  
    public void updateBoard(Board board) {
        chessBoard = board;
        drawBoard(chessBoard);
    }

    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greška pri spajanju sa serverom");
        alert.setHeaderText("Klijent se nije uspio spojiti na server");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void showInfoMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uspješno spajanje sa serverom");
        alert.setHeaderText("Klijent se uspio spojiti na server");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private class ChessFieldPane extends Pane {

        private final int tileId;

        ChessFieldPane(final TilePane boardTilePane, final int tileId) {
            super(new Pane());
            this.tileId = tileId;
            setPrefSize(TILE_DIMENSION, TILE_DIMENSION);
            assignFieldColor();
            assignFieldPieceIcon(chessBoard);

            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    if (event.getButton().equals(MouseButton.SECONDARY)) {
                    sourceChessField = null;
                    destinationChessField = null;
                    movedPiece = null;
                    drawBoard(chessBoard);
                    } else if (event.getButton().equals(MouseButton.PRIMARY)) {
                        if (sourceChessField == null) {
                            sourceChessField = chessBoard.getChessField(tileId);
                            movedPiece = sourceChessField.getPiece();
                            if (movedPiece == null) {
                                sourceChessField = null;
                            }
                            if (isOnline && color != chessBoard.currentPlayer().getPieceColor()){
                                sourceChessField = null;
                            }
                            highlightLegalMoves(chessBoard);
                        } else {
                            destinationChessField = chessBoard.getChessField(tileId);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceChessField.getFieldPosition(), destinationChessField.getFieldPosition());
                            if (move instanceof PawnPromotion) {
                                Pawn pawn = (Pawn) movedPiece;
                                if (pawn.getPieceColor() == PieceColor.WHITE) {
                                    pawn.setPromotionPiece(cbWhitePromotionPiece.getValue());
                                } else {
                                    pawn.setPromotionPiece(cbBlackPromotionPiece.getValue());
                                }
                            }
                            final MoveTransition moveTransition = chessBoard.currentPlayer().makeMove(move);
                            if (moveTransition.getMoveStatus().isDone()) {
                                chessBoard = moveTransition.getTrasitionBoard();
                                lblStatus.setText(moveTransition.toString());
                                System.out.println(chessBoard.toString());

                                Builder builder = chessBoard.makeBuilder();
                                
                                builderList.add(builder);
                                try {
                                    SerializationUtils.write(builder, FILE_NAME);
                                } catch (IOException ex) {
                                    Logger.getLogger(ChessController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                            sourceChessField = null;
                            destinationChessField = null;
                            movedPiece = null;
                            if (isOnline) {
                                client.sendObject(new MovePacket(roomKey, color, chessBoard.makeBuilder()));
                            } else {
                                drawBoard(chessBoard);
                            }
                        }
                    }
                }
            });
        }

        public void drawField(final Board board) {
            assignFieldColor();
            assignFieldPieceIcon(board);
        }

        private void assignFieldPieceIcon(final Board board) {
            if (board == null) {
                return;
            }
            this.getChildren().clear();
            if (board.getChessField(tileId).isFieldOccupied()) {
                try {
                    FileInputStream imageStream = new FileInputStream(DEFAULT_PIECE_IMAGE_PATH
                            + board.getChessField(tileId).getPiece().getPieceColor().toString().substring(0, 1)
                            + board.getChessField(tileId).getPiece().toString() + ".gif");
                    Image image = new Image(imageStream);
                    ImageView pic = new ImageView();
                    pic.setFitWidth(TILE_DIMENSION);
                    pic.setFitHeight(TILE_DIMENSION);
                    pic.setImage(image);
                    getChildren().add(new Pane(pic));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ChessController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void assignFieldColor() {
            final int[] piecePosition = getCoordinateAtPosition(this.tileId);
            if (piecePosition[0] % 2 != 0) {
                if (piecePosition[1] % 2 == 0) {
                    setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            } else {
                if (piecePosition[1] % 2 != 0) {
                    setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        }

        private void highlightLegalMoves(final Board board) {
            for (final Move move : pieceLegalMoves(board)) {
                for (ChessFieldPane boardField : boardFields) {
                    if (move.getDestinationCoordinate() == boardField.tileId) {
                        if (move.isAttack()) {
                            boardField.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN, CornerRadii.EMPTY, Insets.EMPTY)));
                        } else {
                            boardField.setBackground(new Background(new BackgroundFill(Color.DARKSEAGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(Board board) {
            if (movedPiece != null && movedPiece.getPieceColor() == board.currentPlayer().getPieceColor()) {
                return movedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

    }

    private enum BoardDirection {
        NORMAL {
            @Override
            List<ChessFieldPane> traverse(List<ChessFieldPane> boardFields) {
                return boardFields;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        }, FLIPPED {
            @Override
            List<ChessFieldPane> traverse(List<ChessFieldPane> boardFields) {
                List<ChessFieldPane> fieldPanes = new ArrayList<ChessFieldPane>(boardFields);
                Collections.reverse(fieldPanes);
                return fieldPanes;
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<ChessFieldPane> traverse(final List<ChessFieldPane> boardFields);

        abstract BoardDirection opposite();
    }

}
