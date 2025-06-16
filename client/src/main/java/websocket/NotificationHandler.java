package websocket;

import chess.ChessGame;
import websocket.messages.ServerMessage;

public interface NotificationHandler {
    void notify(ServerMessage message);

}
