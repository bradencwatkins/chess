package websocket.commands;

public class MakeMoveCommand extends UserGameCommand {

    private final String from;
    private final String to;

    public MakeMoveCommand(String authToken, Integer gameID, String from, String to){
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

}
