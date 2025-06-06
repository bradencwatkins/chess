import chess.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static Collection<String> validPreCommands = List.of("register", "r", "login", "l", "quit", "q", "help", "h");
    public static Collection<String> validPostCommands = List.of("create", "c", "list", "join", "j", "observe", "o", "logout", "quit", "q", "help", "h");
    public static Scanner scanner = new Scanner(System.in);
    public static String input = " ";



    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        System.out.println("♕ Welcome to chess time ♕");
        preLogin();

    }

    public static void preLogin() {
        var status = "LOGGED OUT";
        String[] inputWords;

        do {
            System.out.println();
            System.out.println("   register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
            System.out.println("   login <USERNAME> <PASSWORD> - to enter chess time");
            System.out.println("   quit - stop chess time");
            System.out.println("   help - get possible commands");


            System.out.print("[" + status + "] >>> ");
            input = scanner.nextLine();
            inputWords = input.trim().split("\\s+");

        } while (inputWords.length == 0 || !validPreCommands.contains(inputWords[0].toLowerCase()));

        if (inputWords[0].equalsIgnoreCase("register") || inputWords[0].equalsIgnoreCase("r")) {
            //register logic
        }

        if (inputWords[0].equalsIgnoreCase("login") || inputWords[0].equalsIgnoreCase("l")) {
            //login logic
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
            System.out.println("   create <NAME> - to create a game");
            System.out.println("   list - list games");
            System.out.println("   join <ID> [WHITE|BLACK] - join a game");
            System.out.println("   observe <ID> - watch a game");
            System.out.println("   logout - logout and return to main menu");
            System.out.println("   help - get possible commands");
            System.out.println("   quit - stop chess time");


            System.out.print("[" + status + "] >>> ");
            input = scanner.nextLine();
            inputWords = input.trim().split("\\s+");
        } while (inputWords.length == 0 || !validPostCommands.contains(inputWords[0].toLowerCase()));

        if (inputWords[0].equalsIgnoreCase("create") || inputWords[0].equalsIgnoreCase("c")) {
            //register logic
        }

    }
}