import ServerFacade.*;
import chess.*;
import ui.EscapeSequences;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.System.out;
import static ui.EscapeSequences.*;


public class Main {
    public static Collection<String> validPreCommands = List.of("register", "login", "quit", "help");
    public static Collection<String> validPostCommands = List.of("create", "list", "join", "observe", "logout", "quit", "help");
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

        out.println();
        out.print("\u001b[32m");
        out.println("   \u001b[32mregister <\u001b[34mUSERNAME\u001b[32m> <\u001b[34mPASSWORD\u001b[32m> <\u001b[34mEMAIL\u001b[32m> - \u001b[33mto create an account");
        out.println("   \u001b[32mlogin\u001b[32m <\u001b[34mUSERNAME\u001b[32m> <\u001b[34mPASSWORD\u001b[32m> - \u001b[33mto enter chess time");
        out.println("   \u001b[32mquit\u001b[32m - \u001b[33mstop chess time");
        out.println("   \u001b[32mtype 'help' to explain the commands");

        out.print("\u001b[36m");
        out.print("[" + status + "] >>> ");
        input = scanner.nextLine();
        inputWords = input.trim().split("\\s+");

        //SEND REQUEST TO SERVER TO REGISTER
        if (inputWords[0].equalsIgnoreCase("register") && inputWords.length == 4) {
            if (serverHandler.registerHandler(inputWords)) {
                serverHandler.loginHandler(inputWords);
                username = inputWords[1];
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
            out.println();
            out.println("  \u001b[4mCOMMANDS\u001b[0m");
            out.println("   \u001b[33mregister\u001b[32m - \u001b[31mThis is how you create an account to play." +
                        " Enter the word 'register' followed by a space, then your username and password followed by a space, then your email");
            out.println("   \u001b[33mlogin\u001b[32m - \u001b[31mThis is how you login to play." +
                    " Enter the word 'login' followed by a space, then your username followed by a space, then your password");
            out.println("   \u001b[33mquit\u001b[32m - \u001b[31menter 'quit' to exit the application.");
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

    out.println();
    out.print("\u001b[32m");
    out.println("   \u001b[32mcreate <\u001b[34mNAME\u001b[32m> - \u001b[33mto create a game");
    out.println("   \u001b[32mlist - \u001b[33mlist games");
    out.println("   \u001b[32mjoin <\u001b[34mID\u001b[32m> [\u001b[36mWHITE\u001b[32m|\u001b[30mBLACK\u001b[32m] - \u001b[33mjoin a game");
    out.println("   \u001b[32mobserve <\u001b[34mID\u001b[33m> - watch a game");
    out.println("   \u001b[32mlogout - \u001b[33mlogout and return to main menu");
    out.println("   \u001b[32mquit - \u001b[33mstop chess time");
    out.println("   \u001b[32mtype 'help' to explain the commands");

    out.print("\u001b[36m");
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
            chessGame = serverHandler.getGameHandler(Integer.parseInt(inputWords[1]));
            if (chessGame == null) {
                postLogin();
            }
            String teamColor = inputWords[2];
            chessGame.getBoard().flipBoardVerticalAxis();
            if (teamColor.equalsIgnoreCase("black")) {
                chessGame.getBoard().reverseBoard();
            }
            drawLetters(out, 1, teamColor);
            printBoard(out, teamColor);
            drawLetters(out, 2, teamColor);
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
        serverHandler.observeGameHandler(inputWords);
        postLogin();
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
        out.println();
        out.println("  \u001b[4mCOMMANDS\u001b[0m");
        out.println("   \u001b[33mlist\u001b[32m - \u001b[31mtype 'list' to list all current games");
        out.println("   \u001b[33mjoin\u001b[32m - \u001b[31mThis is how you join a game." +
                " Enter the word 'join' followed by a space, then game ID that you would like to join (from the listed games), " +
                " followed by the color you would like to play as (WHITE or BLACK)");
        out.println("   \u001b[33mobserve\u001b[32m - \u001b[31mThis is how you observe a game." +
                " Enter the word 'observe' followed by a space, then the game ID for the game you would like to watch (from the listed games)");
        out.println("   \u001b[33mlogout\u001b[32m - \u001b[31mtype 'logout' to logout and return to the main menu");
        out.println("   \u001b[33mquit\u001b[32m - \u001b[31menter 'quit' to exit the application.");
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

private static void drawLetters(PrintStream out, int iteration, String teamColor) {
    if (iteration == 1) {
        System.out.println();
        setGray(out);
        out.print(SPACE.repeat(80));
        out.println("\u001b[0m");
    }
    setGray(out);
    String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h" };
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
public static void printBoard(PrintStream out, String teamColor) {
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
                if (isLight) {
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

private static void setBlack(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_BLACK);
}

private static void setGray(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_LIGHT_GREY);
}

private static void setWhite(PrintStream out) {
    out.print(SET_BG_COLOR_WHITE);
    out.print(SET_TEXT_COLOR_WHITE);
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