import ServerFacade.*;
import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
    private static ServerFacadeHandler serverHandler = new ServerFacadeHandler();
    private static String username = " ";



    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        //find way to find team color organically
        System.out.println("♕ Welcome to chess time ♕");
        preLogin();

        out.print(ERASE_SCREEN);
        drawLetters(out, 1);
        printBoard(out);
        drawLetters(out, 2);

    }

    public static void preLogin() {
        var status = "LOGGED OUT";
        String[] inputWords;

        do {
            System.out.println();
            System.out.print("\u001b[32m");
            System.out.println("   \u001b[32mregister <\u001b[34mUSERNAME\u001b[32m> <\u001b[34mPASSWORD\u001b[32m> <\u001b[34mEMAIL\u001b[32m> - \u001b[33mto create an account");
            System.out.println("   \u001b[32mlogin\u001b[32m <\u001b[34mUSERNAME\u001b[32m> <\u001b[34mPASSWORD\u001b[32m> - \u001b[33mto enter chess time");
            System.out.println("   \u001b[32mquit\u001b[32m - \u001b[33mstop chess time");
            System.out.println("   \u001b[32mhelp\u001b[32m - \u001b[33mget possible commands");

            System.out.print("\u001b[36m");
            System.out.print("[" + status + "] >>> ");
            input = scanner.nextLine();
            inputWords = input.trim().split("\\s+");

        } while (inputWords.length == 0 || !validPreCommands.contains(inputWords[0].toLowerCase()));

        //SEND REQUEST TO SERVER TO REGISTER
        if (inputWords[0].equalsIgnoreCase("register") && inputWords.length == 4) {
            if (serverHandler.registerHandler(inputWords)) {
                serverHandler.loginHandler(inputWords);
                username = inputWords[1];
                postLogin();
            }
            else {
                System.out.println("You must enter USERNAME, PASSWORD, EMAIL");
                preLogin();
            }
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
        }

        else if (inputWords[0].equalsIgnoreCase("help")) {
            //print help
        }

        else if (inputWords[0].equalsIgnoreCase("quit")) {
            System.exit(0);
        }
        else {
            preLogin();
        }
    }


public static void postLogin() {
    var status = username;
    String[] inputWords;

    do {
        System.out.println();
        System.out.print("\u001b[32m");
        System.out.println("   \u001b[32mcreate <\u001b[34mNAME\u001b[32m> - \u001b[33mto create a game");
        System.out.println("   \u001b[32mlist - \u001b[33mlist games");
        System.out.println("   \u001b[32mjoin <\u001b[34mID\u001b[32m> [\u001b[36mWHITE\u001b[32m|\u001b[30mBLACK\u001b[32m] - \u001b[33mjoin a game");
        System.out.println("   \u001b[32mobserve <\u001b[34mID\u001b[33m> - watch a game");
        System.out.println("   \u001b[32mlogout - \u001b[33mlogout and return to main menu");
        System.out.println("   \u001b[32mhelp - \u001b[33mget possible commands");
        System.out.println("   \u001b[32mquit - \u001b[33mstop chess time");

        System.out.print("\u001b[36m");
        System.out.print("[" + status + "] >>> ");
        input = scanner.nextLine();
        inputWords = input.trim().split("\\s+");
    } while (inputWords.length == 0 || !validPostCommands.contains(inputWords[0].toLowerCase()));

    //SEND REQUEST TO CREATE A GAME
    if (inputWords[0].equalsIgnoreCase("create") && inputWords.length == 2) {
        serverHandler.createGameHandler(inputWords);
        postLogin();
    }

    //SEND REQUEST TO LIST GAMES
    else if (inputWords[0].equalsIgnoreCase("list")) {
        serverHandler.listGameHandler();
        postLogin();
    }

    else if (inputWords[0].equalsIgnoreCase("join") && inputWords.length == 3) {
        serverHandler.joinGameHandler(inputWords);
        postLogin();
    }

    else if (inputWords[0].equalsIgnoreCase("observe")) {
        //observe logic
    }

    else if (inputWords[0].equalsIgnoreCase("logout")) {
        serverHandler.logoutHandler();
        preLogin();
    }

    else if (inputWords[0].equalsIgnoreCase("quit") || inputWords[0].equalsIgnoreCase("q")) {
        System.exit(0);
    }

    else {
        postLogin();
    }

}

private static void drawLetters(PrintStream out, int iteration) {
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
        drawLetter(out, headers[i]);
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
public static void printBoard(PrintStream out) {
    for (int boardRow = 0; boardRow < boardSize; boardRow++) {
        for (int i = 0; i < squareHeight; i++) {
            setGray(out);
            if (i == 1){
                out.print(SPACE.repeat(borderLength - 2));
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(SET_TEXT_COLOR_BLACK);
                out.print(SET_TEXT_BOLD);
                out.print(8 - boardRow);
                setGray(out);
                out.print(SPACE);
            } else {
                out.print(SPACE.repeat(borderLength));
            }
            for (int boardColumn = 0; boardColumn < boardSize; boardColumn++) {
                //ODD ROWS, ODD SQUARES (WHITE)
                if (boardRow % 2 == 0 && boardColumn % 2 == 0) {
                    setLightBrown(out);
                    out.print(SPACE.repeat(squareLength));
                } //ODD ROWS, EVEN SQUARES (BLACK)
                else if (boardRow % 2 == 0 && boardColumn % 2 == 1) {
                    setBrown(out);
                    out.print(SPACE.repeat(squareLength));
                }
                //EVEN ROW, EVEN SQUARES (BLACK)
                else if (boardRow % 2 == 1 && boardColumn % 2 == 0) {
                    setBrown(out);
                    out.print(SPACE.repeat(squareLength));
                } //EVEN ROW, ODD SQUARES (WHITE)
                else {
                    setLightBrown(out);
                    out.print(SPACE.repeat(squareLength));
                }
            }
            setGray(out);
            if (i == 1){
                out.print(SPACE.repeat(1));
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(SET_TEXT_COLOR_BLACK);
                out.print(SET_TEXT_BOLD);
                out.print(8 - boardRow);
                setGray(out);
                out.print(SPACE.repeat(borderLength - 2));
            } else {
                out.print(SPACE.repeat(borderLength));
            }
            out.println("\u001b[0m");
        }
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