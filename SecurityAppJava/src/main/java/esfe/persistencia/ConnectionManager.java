package esfe.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class ConnectionManager {


    private static final String STR_CONNECTION = "jdbc:sqlserver://JosueEsc\\SQLEXPRESS:1433; " +
            "encrypt=true; " +
            "database=SecurityDB2025; " +
            "trustServerCertificate=true;" +
            "user=sa;" +
            "password=1234";

    private Connection connection;


    private static ConnectionManager instance;


    private ConnectionManager() {
        this.connection = null;
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            // Si el driver no se encuentra, se lanza una excepción indicando el error.
            throw new RuntimeException("Error al cargar el driver JDBC de SQL Server", e);
        }
    }

    public synchronized Connection connect() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            try {
                this.connection = DriverManager.getConnection(STR_CONNECTION);
            } catch (SQLException exception) {
                throw new SQLException("Error al conectar a la base de datos: " + exception.getMessage(), exception);
            }
        }
        return this.connection;
    }

    public void disconnect() throws SQLException {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException exception) {

                throw new SQLException("Error al cerrar la conexión: " + exception.getMessage(), exception);
            } finally {

                this.connection = null;
            }
        }
    }


    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }
}
