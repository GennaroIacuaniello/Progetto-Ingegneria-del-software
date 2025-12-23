package backend.database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());


    private static DatabaseConnection instance;


    private Connection connection = null;


    private DatabaseConnection() throws SQLException {

        Dotenv dotenv = Dotenv.load();
        String connString = dotenv.get("DATABASE_URL");

        connection = DriverManager.getConnection(connString);

    }


    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }


    public Connection getConnection() {
        return connection;
    }


    public void closeConnection() {

        try {
            connection.close();
        } catch (SQLException ex) {

            LOGGER.log(Level.SEVERE, ex.getMessage());
        }

    }
}
