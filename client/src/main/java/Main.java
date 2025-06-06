import chess.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static Collection<String> validCommands = List.of("register", "r", "login", "l", "quit", "q", "help", "h");


    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        System.out.println("♕ Welcome to chess time ♕");
        preLogin();

    }

    public static void preLogin() {
        Scanner scanner = new Scanner(System.in);
        var status = "LOGGED OUT";
        String input = " ";
        String firstWord = " ";

        while (!validCommands.contains(firstWord)) {
            System.out.println();
            System.out.println("   register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
            System.out.println("   login <USERNAME> <PASSWORD> - to enter chess time");
            System.out.println("   help - get possible commands");
            System.out.println("   quit - stop chess time");


            System.out.print("[" + status + "] >>> ");
            input = scanner.nextLine();
            firstWord = input.split(" ")[0];
        }

        if (firstWord.equalsIgnoreCase("register") || firstWord.equalsIgnoreCase("r")) {
            //register logic
            System.out.println("ahhghg");
        }

        if (firstWord.equalsIgnoreCase("login") || firstWord.equalsIgnoreCase("l")) {
            //login logic
        }

        if (firstWord.equalsIgnoreCase("help") || firstWord.equalsIgnoreCase("h")) {
            //register logic
        }

        if (firstWord.equalsIgnoreCase("quit") || firstWord.equalsIgnoreCase("q")) {
            System.exit(0);
        }


    }
}