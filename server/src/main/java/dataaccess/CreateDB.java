package dataaccess;

public class CreateDB {

    public static final String createUserTable = """
            CREATE TABLE IF NOT EXISTS user (
                id INT NOT NULL AUTO_INCREMENT,
                username VARCHAR(256) UNIQUE NOT NULL,
                password VARCHAR(256) NOT NULL,
                email VARCHAR(256),
                json TEXT,
                PRIMARY KEY (id),
                INDEX(username)
            )
            """;

    public static final String createAuthTable = """
            CREATE TABLE IF NOT EXISTS auth (
                id INT NOT NULL AUTO_INCREMENT,
                authToken VARCHAR(256) UNIQUE NOT NULL,
                username VARCHAR(256) NOT NULL,
                json TEXT,
                PRIMARY KEY (id),
                INDEX(authToken),
                INDEX(username)
            )
            """;

    public static final String createGameTable = """
            CREATE TABLE IF NOT EXISTS game (
                gameID INT NOT NULL AUTO_INCREMENT,
                whiteUsername VARCHAR(256),
                blackUsername VARCHAR(256),
                gameName VARCHAR(256) UNIQUE,
                json TEXT,
                PRIMARY KEY (gameID),
                INDEX(whiteUsername),
                INDEX(blackUsername),
                INDEX(gameName)
            )
            """;



}
