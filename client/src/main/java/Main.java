import model.AuthData;
import model.UserData;

import serverfacade.*;
import chess.*;
import ui.EscapeSequences;
import ui.HelpPrint;
import websocket.NotificationHandler;
import websocket.WebSocketClient;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.System.out;
import static ui.EscapeSequences.*;


public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static String input = " ";
    private static int boardSize = 8;
    private static int squareLength = 9;
    private static int squareHeight = 3;
    private static int borderLength = 4;
    private static ChessGame chessGame = new ChessGame();
    private static ServerFacade server = new ServerFacade("http://localhost:8080");
    private static ServerFacadeHandler serverHandler = new ServerFacadeHandler(server);
    private static String username = " ";
    private static EscapeSequences escSeq = new EscapeSequences();
    private static String teamColor = "";
    private static int currGameID = 0;
    private static String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h" };
    private static WebSocketClient client = null;
    private static String authToken = " ";
    private static boolean joinedAsPlayer;



    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        //find way to find team color organically
        System.out.println("♕ Welcome to chess time ♕");
        preLogin();

        out.print(ERASE_SCREEN);

    }

    public static void preLogin() {
        var status = "LOGGED OUT";
        String[] inputWords;
        HelpPrint.printPreLogin(out);
        out.print("[" + status + "] >>> ");
        input = scanner.nextLine();
        inputWords = input.trim().split("\\s+");

        //SEND REQUEST TO SERVER TO REGISTER
        if (inputWords[0].equalsIgnoreCase("register") && inputWords.length == 4) {
            if (serverHandler.registerHandler(inputWords)) {
                serverHandler.loginHandler(inputWords);
                username = inputWords[1];
                authToken = ClientSession.authToken;
                postLogin();
            } else {
                preLogin();
            }
        } else if (inputWords[0].equalsIgnoreCase("register") && inputWords.length != 4){
            out.println("\u001b[31mYou must enter USERNAME, PASSWORD, EMAIL");
            preLogin();
        }

        //SEND REQUEST TO SERVER FOR LOGGING IN
        else if (inputWords[0].equalsIgnoreCase("login") && inputWords.length == 3) {
            if (serverHandler.loginHandler(inputWords)) {
                username = inputWords[1];
                authToken = ClientSession.authToken;
                postLogin();
            }
            else {
                preLogin();
            }
        } else if (inputWords[0].equalsIgnoreCase("login") && inputWords.length != 3){
            out.println("\u001b[31m  You must enter USERNAME, PASSWORD");
            preLogin();
        }

        //HELP INFO
        else if (inputWords[0].equalsIgnoreCase("help")) {
            HelpPrint.printPreLoginHelp(out);
            preLogin();
        }

        //QUIT THAT CHESS TIME
        else if (inputWords[0].equalsIgnoreCase("quit")) {
            System.exit(0);
        }

        else {
            out.println("  \u001b[31m  You must enter a valid command");
            preLogin();
        }
    }


    public static void postLogin() {
        var status = username;
        String[] inputWords;
        HelpPrint.printPostLogin(out);
        out.print("[" + status + "] >>> ");
        input = scanner.nextLine();
        inputWords = input.trim().split("\\s+");

        //SEND REQUEST TO CREATE A GAME
        if (inputWords[0].equalsIgnoreCase("create") && inputWords.length == 2) {
            serverHandler.createGameHandler(inputWords);
            postLogin();
        } else if (inputWords[0].equalsIgnoreCase("create") && inputWords.length != 2){
            out.println("\u001b[31m  You must enter a game name");
            postLogin();
        }

        //SEND REQUEST TO LIST GAMES
        else if (inputWords[0].equalsIgnoreCase("list")) {
            serverHandler.listGameHandler();
            postLogin();
        }

        //JOINING A GAME
        else if (inputWords[0].equalsIgnoreCase("join") && inputWords.length == 3) {
            if (inputWords[2].equalsIgnoreCase("white") || inputWords[2].equalsIgnoreCase("black")){
                serverHandler.joinGameHandler(inputWords);
                currGameID = Integer.parseInt(inputWords[1]);
                chessGame = serverHandler.getGameHandler(currGameID);
                if (chessGame == null) {
                    postLogin();
                }
                teamColor = inputWords[2];
                String serverUrl = "http://localhost:8080";
                if (client != null && client.isOpen()) {
                    client.close();
                }
                client = new WebSocketClient(serverUrl, handler);
                UserGameCommand connectCommand = new UserGameCommand(
                        UserGameCommand.CommandType.CONNECT, authToken, currGameID);
                client.send(connectCommand);
                chessGame.getBoard().flipBoardVerticalAxis();
                if (teamColor.equalsIgnoreCase("black")) {
                    chessGame.getBoard().reverseBoard();
                }
                drawLetters(out, 1, teamColor);
                printBoard(out, teamColor, null);
                drawLetters(out, 2, teamColor);
                joinedAsPlayer = true;
                gameMenu(client);
                postLogin();
            } else {
                out.println("\u001b[31m  You must enter WHITE or BLACK");
                postLogin();
            }
        } else if (inputWords[0].equalsIgnoreCase("join") && inputWords.length != 3){
            out.println("\u001b[31m  You must enter ID, COLOR");
            postLogin();
        }
        //OBSERVE A GAME
        else if (inputWords[0].equalsIgnoreCase("observe") && inputWords.length == 2) {
            try {
                currGameID = Integer.parseInt(inputWords[1]);
                chessGame = serverHandler.getGameHandler(currGameID);
                if (chessGame == null) {
                    out.println("\u001b[31m  Game not found");
                    postLogin();
                    return;
                }
                if (client != null && client.isOpen()) {
                    client.close();
                }
                String serverUrl = "http://localhost:8080";
                client = new WebSocketClient(serverUrl, handler);

                UserGameCommand connectCommand = new UserGameCommand(
                        UserGameCommand.CommandType.CONNECT, authToken, currGameID);
                client.send(connectCommand);

                chessGame.getBoard().flipBoardVerticalAxis();
                drawLetters(out, 1, "white");
                printBoard(out, "white", null);
                drawLetters(out, 2, "white");
                joinedAsPlayer = false;
                gameMenu(client);
            } catch (NumberFormatException e) {
                out.println("\u001b[31m  Invalid game ID");
                postLogin();
            }
        } else if (inputWords[0].equalsIgnoreCase("observe") && inputWords.length != 2){
            out.println("\u001b[31m  You must enter a game ID");
            postLogin();
        }
        //LOGGING OUT
        else if (inputWords[0].equalsIgnoreCase("logout")) {
            serverHandler.logoutHandler();
            preLogin();
        }
        //HELP STUFF
        else if (inputWords[0].equalsIgnoreCase("help")) {
            HelpPrint.printPostLoginHelp(out);
            postLogin();
        }

        //QUITTING
        else if (inputWords[0].equalsIgnoreCase("quit") || inputWords[0].equalsIgnoreCase("q")) {
            System.exit(0);
        }

        else {
            out.println("  \u001b[31m  You must enter a valid command");
            postLogin();
        }
    }

    public static void gameMenu(WebSocketClient client) {
        var status = username;
        String[] inputWords;
        HelpPrint.printGameMenu(out);
        out.print("[" + status + "] >>> ");
        input = scanner.nextLine();
        inputWords = input.trim().split("\\s+");

        //REDRAW THE BOARD
        if (inputWords[0].equalsIgnoreCase("redraw") && inputWords.length == 1) {
            chessGame = serverHandler.getGameHandler(currGameID);
            chessGame.getBoard().flipBoardVerticalAxis();
            if (teamColor.equalsIgnoreCase("black")) {
                chessGame.getBoard().reverseBoard();
            }
            drawLetters(out, 1, teamColor);
            printBoard(out, teamColor, null);
            drawLetters(out, 2, teamColor);
            gameMenu(client);
        }


        else if (inputWords[0].equalsIgnoreCase("highlight") && inputWords.length == 2) {
            chessGame = serverHandler.getGameHandler(currGameID);
            chessGame.getBoard().flipBoardVerticalAxis();
            if (teamColor.equalsIgnoreCase("black")) {
                chessGame.getBoard().reverseBoard();
            }
            String position = inputWords[1];
            drawLetters(out, 1, teamColor);
            printBoard(out, teamColor, position);
            drawLetters(out, 2, teamColor);
            gameMenu(client);
            //redraw logic with highlighted squares
        } else if (inputWords[0].equalsIgnoreCase("highlight") && inputWords.length != 2){
            out.println("\u001b[31m  You must enter a square");
            gameMenu(client);
        }
        //MAKING A MOVE
        else if (inputWords[0].equalsIgnoreCase("move") && inputWords.length == 3) {
            String fromRaw = inputWords[1];
            String toRaw = inputWords[2];
            ChessPosition from = convertChessMove(fromRaw);
            ChessPosition to = convertChessMove(toRaw);
            ChessMove move = new ChessMove(from, to, null);
            MakeMoveCommand moveCommand = new MakeMoveCommand(authToken, currGameID, move);
            client.send(moveCommand);

            gameMenu(client);
        } else if (inputWords[0].equalsIgnoreCase("move") && inputWords.length != 3) {
            out.println("\u001b[31m  You must enter two squares");
            gameMenu(client);
        }

        else if (inputWords[0].equalsIgnoreCase("leave")) {
            UserGameCommand leaveCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, currGameID);
            client.send(leaveCommand);
            client.close();
            if (joinedAsPlayer) {
                serverHandler.leaveGameHandler(currGameID, username);
            }
            postLogin();
        }

        else if (inputWords[0].equalsIgnoreCase("resign")) {
            UserGameCommand resignCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, currGameID);
            client.send(resignCommand);
            gameMenu(client);
        }


        //HELP STUFF
        else if (inputWords[0].equalsIgnoreCase("help")) {
            HelpPrint.printGameMenuHelp(out);
            gameMenu(client);
        }
        else {
            out.println("  \u001b[31m  You must enter a valid command");
            gameMenu(client);
        }

    }

    private static void drawLetters(PrintStream out, int iteration, String teamColor) {
        if (iteration == 1) {
            System.out.println();
            setGray(out);
            out.print(SPACE.repeat(80));
            out.println("\u001b[0m");
        }
        setGray(out);
        out.print(SPACE.repeat(borderLength));
        for (int i = 0; i < headers.length; i++) {
            if (teamColor.equalsIgnoreCase("white")) {
                drawLetter(out, headers[i]);
            } else {
                drawLetter(out, headers[7-i]);
            }
            out.print(SPACE.repeat(4));
        }
        out.print(SPACE.repeat(borderLength));
        out.println("\u001b[0m");
        if (iteration == 2) {
            setGray(out);
            out.print(SPACE.repeat(80));
            out.println("\u001b[0m");
            String currTurn = chessGame.getTeamTurn().toString();
            out.println("\u001b[33mCURRENT TEAM TURN: " + currTurn);
        }
    }

    private static void drawLetter(PrintStream out, String letter) {
        int gapLength = 4;

        out.print(SPACE.repeat(gapLength));
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_TEXT_BOLD);
        out.print(letter);
        setGray(out);
    }

    //FUNCTION THAT PRINTS THE SQUARES OF THE BOARD WITH THE PIECES IN THEM
    public static void printBoard(PrintStream out, String teamColor, String position) {
        Set<ChessPosition> highlightSquares = new HashSet<>();
        if (position != null) {
            highlightSquare(position, highlightSquares);
        }

        for (int boardRow = 0; boardRow < boardSize; boardRow++) {
            int rank;
            if (teamColor.equalsIgnoreCase("white")) {
                rank = 8 - boardRow;
            } else {
                rank = boardRow + 1;
            }
            for (int i = 0; i < squareHeight; i++) {
                setGray(out);
                printRank(out, i, rank, 1);

                for (int boardColumn = 0; boardColumn < boardSize; boardColumn++) {
                    boolean isLight = (boardRow + boardColumn) % 2 == 0;
                    ChessPosition currSquare = new ChessPosition(8 - boardRow, boardColumn + 1);
                    boolean highlight = highlightSquares.contains(currSquare);

                    if (highlight) {
                        out.print(SET_TEXT_COLOR_BLACK);
                        out.print(SET_TEXT_BOLD);
                        out.print(isLight ? SET_BG_COLOR_GREEN : SET_BG_COLOR_DARK_GREEN);
                    }
                    else if (isLight) {
                        setLightBrown(out);
                    } else {
                        setBrown(out);
                    }

                    if (i == 1) {
                        printPiece(7 - boardRow, 7 - boardColumn);
                    } else {
                        out.print(SPACE.repeat(squareLength));
                    }
                }
                setGray(out);
                if (i == 1){
                    printRank(out, i, rank, 2);
                } else {
                    out.print(SPACE.repeat(borderLength));
                }
                out.println("\u001b[0m");
            }
        }

    }

    //FUNCTION TO PRINT THE PIECE ON THE SQUARE
    private static void printPiece(int boardRow, int boardColumn) {
        out.print(SPACE.repeat(squareLength / 2));
        ChessPiece piece = chessGame.getBoard().getPiece(new ChessPosition(boardRow+1, boardColumn+1));
        if (piece != null) {
            String chessChar = escSeq.changeText(piece.toString());
            out.print(chessChar);
        } else {
            out.print(SPACE);
        }
        out.print(SPACE.repeat(squareLength / 2));
    }

    //FUNCTION TO PRINT THE NUMBER ON THE SIDE OF THE SQUARES
    private static void printRank(PrintStream out, int i, int rank, int iteration){
        if (i == 1){
            if (iteration == 1) {
                out.print(SPACE.repeat(borderLength - 2));
            }
            else {
                out.print(SPACE);
            }
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(SET_TEXT_BOLD);
            out.print(rank);
            setGray(out);
            if (iteration == 1) {
                out.print(SPACE);
            }
            else {
                out.print(SPACE.repeat(borderLength - 2));
            }
        } else {
            out.print(SPACE.repeat(borderLength));
        }
    }

    private static void highlightSquare(String position, Set<ChessPosition> highlightSquares){
        ChessPosition piecePosition = convertChessMove(position);
        ChessPiece piece = chessGame.getBoard().getPiece(piecePosition);
        if (piece != null){
            for (ChessMove move : chessGame.validMoves(piecePosition)) {
                highlightSquares.add(move.getEndPosition());
            }
        }
    }

    private static ChessPosition convertChessMove(String position) {
        String letter = String.valueOf(position.charAt(0));
        String number = String.valueOf(position.charAt(1));
        int col = java.util.Arrays.asList(headers).indexOf(letter) + 1;
        int row = Integer.parseInt(number);
        ChessPosition piecePosition = new ChessPosition(row, col);
        return piecePosition;
    }

    static NotificationHandler handler = new NotificationHandler() {
        @Override
        public void notify(ServerMessage message) {
            if (message instanceof NotificationMessage) {
                System.out.println(((NotificationMessage) message).getMessage());
            } else if (message instanceof ErrorMessage) {
                System.err.println("Error: " + ((ErrorMessage) message).getErrorMessage());
                loadBoard();
                gameMenu(client);
            } else if (message instanceof LoadGameMessage) {
                LoadGameMessage loadGameMessage = (LoadGameMessage) message;
                chessGame = loadGameMessage.getGame();
                chessGame.getBoard().flipBoardVerticalAxis();
                if (teamColor.equalsIgnoreCase("black")) {
                    chessGame.getBoard().reverseBoard();
                }
                loadBoard();
                gameMenu(client);
            }
        }
    };

    public static void loadBoard() {
        drawLetters(System.out, 1, teamColor);
        printBoard(System.out, teamColor, null);
        drawLetters(System.out, 2, teamColor);
    }

    private static void setGray(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setBrown(PrintStream out) {
        out.print(SET_BG_COLOR_BROWN);
        out.print(SET_TEXT_COLOR_BROWN);
    }

    private static void setLightBrown(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_BROWN);
        out.print(SET_TEXT_COLOR_LIGHT_BROWN);
    }

}