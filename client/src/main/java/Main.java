import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import ui.EscapeSequences;

import static ui.EscapeSequences.*;


public class Main {
    public static Collection<String> validPreCommands = List.of("register", "r", "login", "l", "quit", "q", "help", "h");
    public static Collection<String> validPostCommands = List.of("create", "c", "list", "join", "j", "observe", "o", "logout", "quit", "q", "help", "h");
    public static Scanner scanner = new Scanner(System.in);
    public static String input = " ";
    private static int boardSize = 8;
    private static int squareLength = 5;
    private static int squareHeight = 2;
    private static int borderLength = 4;



    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        System.out.println("♕ Welcome to chess time ♕");
//        preLogin();
//        postLogin();

        out.print(ERASE_SCREEN);
        drawLetters(out);
        printBoard(out);
        drawLetters(out);

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

        if (inputWords[0].equalsIgnoreCase("register") || inputWords[0].equalsIgnoreCase("r")) {
            //register logic
        }

        if (inputWords[0].equalsIgnoreCase("login") || inputWords[0].equalsIgnoreCase("l")) {

        }

        if (inputWords[0].equalsIgnoreCase("help") || inputWords[0].equalsIgnoreCase("h")) {
            //register logic
        }

        if (inputWords[0].equalsIgnoreCase("quit") || inputWords[0].equalsIgnoreCase("q")) {
            System.exit(0);
        }

    }


    public static void postLogin() {
        var status = "LOGGED IN";
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

        if (inputWords[0].equalsIgnoreCase("create") || inputWords[0].equalsIgnoreCase("c")) {
            //register logic
        }

    }

    private static void drawLetters(PrintStream out) {
        setGray(out);
        out.print(SPACE.repeat(48));
        out.println("\u001b[0m");
        setGray(out);
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h" };
        out.print(SPACE.repeat(borderLength));
        for (int i = 0; i < headers.length; i++) {
            drawLetter(out, headers[i]);
            out.print(SPACE.repeat(2));
        }
        out.print(SPACE.repeat(borderLength));
        out.println("\u001b[0m");
    }

    private static void drawLetter(PrintStream out, String letter) {
        int gapLength = 2;

        out.print(SPACE.repeat(gapLength));
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_TEXT_BOLD);
        out.print(letter);
        setGray(out);
    }

    public static void printBoard(PrintStream out) {



        for (int boardRow = 0; boardRow < boardSize; boardRow++) {


            for (int i = 0; i < squareHeight; i++) {
                //PLACE NUMBER TO SIGNIFY ROW
                setGray(out);
                out.print(SPACE.repeat(borderLength));
                for (int boardColumn = 0; boardColumn < boardSize; boardColumn++) {
                    //ODD ROWS, ODD SQUARES (WHITE)
                    if (boardRow % 2 == 0 && boardColumn % 2 == 0) {
                        setWhite(out);
                        out.print(SPACE.repeat(squareLength));
                    } //ODD ROWS, EVEN SQUARES (BLACK)
                    else if (boardRow % 2 == 0 && boardColumn % 2 == 1) {
                        setBlack(out);
                        out.print(SPACE.repeat(squareLength));
                    }
                    //EVEN ROW, EVEN SQUARES (BLACK)
                    else if (boardRow % 2 == 1 && boardColumn % 2 == 0) {
                        setBlack(out);
                        out.print(SPACE.repeat(squareLength));
                    } //EVEN ROW, ODD SQUARES (WHITE)
                    else {
                        setWhite(out);
                        out.print(SPACE.repeat(squareLength));
                    }
                }
                setGray(out);
                out.print(SPACE.repeat(borderLength));
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

}