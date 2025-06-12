package ServerFacade;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;
    private final Gson gson = new Gson();
    private String authToken = null;

    public ServerFacade(String serverUrl){
        this.serverUrl = serverUrl;
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    public Map<String, Object> registerUser(Map<String, Object> request) throws Exception {
        return makeRequest("POST", "/user", request);
    }

    public Map<String, Object> loginUser(Map<String, Object> request) throws Exception {
        return makeRequest("POST", "/session", request);
    }

    public Map<String, Object> logoutUser() throws Exception {
        return makeRequest("DELETE", "/session", null);
    }

    public Map<String, Object> createGame(Map<String, Object> request) throws Exception {
        return makeRequest("POST", "/game", request);
    }

    public Map<String, Object> listGames() throws Exception {
        return makeRequest("GET", "/game", null);
    }

    public Map<String, Object> joinGame(Map<String, Object> request) throws Exception {
        return makeRequest("PUT", "/game", request);
    }

    public Map<String, Object> getGameState(int gameID) throws Exception {
        return makeRequest("GET", "/game/" + gameID, null);
    }

    public void clear() throws Exception {
        makeRequest("DELETE", "/db", null);
    }


    //CONNECTS SERVER FACADE TO SERVER
    private Map<String, Object> makeRequest(String method, String path, Map<String, Object> requestBody) throws Exception {
        URI uri = new URI(serverUrl + path);
        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod(method);
        connection.addRequestProperty("Content-Type", "application/json");

        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }

        if (requestBody != null) {
            connection.setDoOutput(true);
            try (OutputStream out = connection.getOutputStream()) {
                out.write(gson.toJson(requestBody).getBytes());
            }
        }

        connection.connect();
        int status = connection.getResponseCode();
        InputStream stream = (status == 200)
                ? connection.getInputStream()
                : connection.getErrorStream();

        try (InputStreamReader reader = new InputStreamReader(stream)) {
            if (status == 200) {
                return gson.fromJson(reader, Map.class);
            } else {
                ErrorResponse error = gson.fromJson(reader, ErrorResponse.class);
                throw new ServerException(error.message());
            }
        }
    }
}
