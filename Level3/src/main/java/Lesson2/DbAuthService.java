package Lesson2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbAuthService implements AuthService {
    private static class Entry {
        private final String login;
        private final String password;
        private final String nickname;

        public Entry(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

    private static List<Entry> entries = null;

    private static Connection connection;
    private static Statement statement;

    public DbAuthService() {
        entries = new ArrayList<>();
        try {
            connect();
            readEx();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            connect();
            entries = new ArrayList<>();
            readEx();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } finally {
            disconnect();
        }
    }

    @Override
    public void start() {
        System.out.println(Constants.AUTH_SERVICE_STARTED);
    }

    @Override
    public String getNickByLoginPass(String login, String password) {
        for (Entry entry : entries) {
            if (entry.login.equals(login) && entry.password.equals(password)) return entry.nickname;
        }
        return null;
    }

    @Override
    public void stop() {
        disconnect();
        System.out.println(Constants.AUTH_SERVICE_STOPPED);
    }

    public static void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:javadb.db");
        statement = connection.createStatement();
        System.out.println("Соединение установлено успешно");
    }

    public static void disconnect() {
        try {
            if (statement != null)
                statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        try {
            if (connection != null) {
                connection.close();
                System.out.println("Соединение разорвано");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private static void createTableEx() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS login (\n" +
                "        id    INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "        nickname  TEXT,\n" +
                "        login TEXT,\n" +
                "        password TEXT\n" +
                "    );");
    }

    private static void dropTableEx() throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS login;");
    }

    private static void readEx() throws SQLException {
        try (ResultSet rs = statement.executeQuery("SELECT * FROM login;")) {
            while (rs.next()) {
                entries.add(new Entry(
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("nickname"))
                );

                System.out.println(
                        rs.getInt(1) + " " +
                        rs.getString("nickname") + " " +
                        rs.getString("login") + " " +
                        rs.getString("password")
                );
            }
        }
    }

    private static void clearTableEx() throws SQLException {
        statement.executeUpdate("DELETE FROM login;");
    }

    private static void deleteEx(String nick) throws SQLException {
        statement.executeUpdate("DELETE FROM login WHERE nickname = '" + nick + "';");
    }

    private static void updateEx() throws SQLException {
        statement.executeUpdate("UPDATE login SET login = 'login2' WHERE name = 'nick2';");
    }

    private static void insertEx(String nick, String login, String pass) throws SQLException {
        statement.executeUpdate("INSERT INTO login (nickname, login, password) VALUES " +
                "('" + nick + "', '" + login + "', '" + pass + "');");
    }
}
