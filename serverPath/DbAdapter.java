package serverPath;

import java.sql.*;
import java.sql.Connection;

public class DbAdapter {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/users";
    private static final String USER = "katrin";
    private static final String PASS = "whitecrolick0";

    static Connection connection = null;
    static Statement statement = null;
    static ResultSet resultSet = null;


    static void connect() throws SQLException, ClassNotFoundException {

        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        statement = connection.createStatement();
        System.out.println("Успешное соединение с базой данных");
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        DbAdapter.connection = connection;
    }

    public static Statement getStatement() {
        return statement;
    }

    public static void setStatement(Statement statement) {
        DbAdapter.statement = statement;
    }

    public static ResultSet getResultSet() {
        return resultSet;
    }

    public static void setResultSet(ResultSet resultSet) {
        DbAdapter.resultSet = resultSet;
    }
}
