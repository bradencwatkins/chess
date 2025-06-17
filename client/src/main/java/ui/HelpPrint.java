package ui;

import java.io.PrintStream;

import static java.lang.System.out;

public class HelpPrint {

    public static void printPreLogin(PrintStream out) {
        out.println();
        out.print("\u001b[32m");
        out.println("   \u001b[32mregister <\u001b[34mUSERNAME\u001b[32m> <\u001b[34mPASSWORD\u001b[32m> <\u001b[34mEMAIL\u001b[32m>" +
                " - \u001b[33mto create an account");
        out.println("   \u001b[32mlogin\u001b[32m <\u001b[34mUSERNAME\u001b[32m> <\u001b[34mPASSWORD\u001b[32m> - \u001b[33mto enter chess time");
        out.println("   \u001b[32mquit\u001b[32m - \u001b[33mstop chess time");
        out.println("   \u001b[33mtype \u001b[32mhelp\u001b[33m to explain the commands");

        out.print("\u001b[36m");
    }

    public static void printPostLogin(PrintStream out){
        out.println();
        out.print("\u001b[32m");
        out.println("   \u001b[32mcreate <\u001b[34mNAME\u001b[32m> - \u001b[33mto create a game");
        out.println("   \u001b[32mlist - \u001b[33mlist games");
        out.println("   \u001b[32mjoin <\u001b[34mID\u001b[32m> [\u001b[36mWHITE\u001b[32m|\u001b[30mBLACK\u001b[32m] - \u001b[33mjoin a game");
        out.println("   \u001b[32mobserve <\u001b[34mID\u001b[33m> - watch a game");
        out.println("   \u001b[32mlogout - \u001b[33mlogout and return to main menu");
        out.println("   \u001b[32mquit - \u001b[33mstop chess time");
        out.println("   \u001b[33mtype \u001b[32mhelp\u001b[33m to explain the commands");

        out.print("\u001b[36m");
    }

    public static void printGameMenu(PrintStream out) {
        out.println("\u001b[32m");
        out.println("   \u001b[32mredraw - \u001b[33mto re-print the board");
        out.println("   \u001b[32mmove <\u001b[34mSTARTPOSITION\u001b[32m> <\u001b[34mENDPOSITION\u001b[32m> - \u001b[33mto move a piece");
        out.println("   \u001b[32mhighlight <\u001b[34mPOSITION\u001b[32m> - \u001b[33mto highlight all possible moves a piece can make");
        out.println("   \u001b[32mleave - \u001b[33mto leave the game");
        out.println("   \u001b[32mresign - \u001b[33mto forfeit the game");
        out.println("   \u001b[33mtype \u001b[32mhelp\u001b[33m to explain the commands");

        out.print("\u001b[36m");
    }

    public static void printPreLoginHelp(PrintStream out){
        out.println();
        out.println("  \u001b[4mCOMMANDS\u001b[0m");
        out.println("   \u001b[33mregister\u001b[32m - \u001b[31mThis is how you create an account to play." +
                " Enter the word 'register' followed by a space, then your username and password followed by a space, then your email");
        out.println("   \u001b[33mlogin\u001b[32m - \u001b[31mThis is how you login to play." +
                " Enter the word 'login' followed by a space, then your username followed by a space, then your password");
        out.println("   \u001b[33mquit\u001b[32m - \u001b[31menter 'quit' to exit the application.");
    }

    public static void printPostLoginHelp(PrintStream out) {
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
    }

    public static void printGameMenuHelp(PrintStream out) {
        out.println();
        out.println("  \u001b[4mCOMMANDS\u001b[0m");
        out.println("   \u001b[33mredraw\u001b[32m - \u001b[31mtype 'redraw' to re-print the current board (does not affect gameplay)");
        out.println("   \u001b[33mmove\u001b[32m - \u001b[31mThis is how you move a piece." +
                " Enter the word 'move' followed by a space, then the position of the piece you would like to move (ex: a4)" +
                " followed by a valid space you would like the piece to move to (ex: a6)");
        out.println("   \u001b[33mhighlight\u001b[32m - \u001b[31mType 'highlight' followed by a space," +
                " then the position of the piece you would like to see all moves for. The possible moves that piece can make will be highlighted.");
        out.println("   \u001b[33mleave\u001b[32m - \u001b[31mtype 'leave' to leave the game and return to the menu." +
                " It does not end the game.");
        out.println("   \u001b[33mresign\u001b[32m - \u001b[31menter 'resign' to forfeit the current game." +
                " It does not make you leave the game.");
    }

}
