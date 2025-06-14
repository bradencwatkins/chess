package websocket;

import javax.websocket.*;
import java.util.Scanner;

public class WebSocketClient extends Endpoint {

    public static void main(String[] args) throws Exception {
        var ws = new WebSocketClient();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a message you want to echo");
        while (true) ws.send(scanner.nextLine());
    }

    public Session session;

}
