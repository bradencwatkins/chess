package websocket.messages;

public class ErrorMessage extends ServerMessage {
    private final String error;

    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.error = errorMessage;
    }

    public String getError() {
        return error;
    }

}
