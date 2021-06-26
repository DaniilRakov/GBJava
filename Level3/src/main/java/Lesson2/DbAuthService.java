package Lesson2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbAuthService implements AuthService {
    private static class Entry {
        private final String login;
        private final String password;
        private String nickname;

        public Entry(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }

        public void setNickname(String newNickname) {
            nickname = newNickname;
        }
    }

    private static List<Entry> entries = null;

    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static Statement statement;

    public DbAuthService() {
        entries = new ArrayList<>();
        try {
            connect();
            readTableLogin();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            connect();
            entries = new ArrayList<>();
            dropTableLogin();
            createTableLogin();
            insertNewUser("login1", "pass1", "nick1");
            readTableLogin();

            insertNewUser("login2", "pass2", "nick2");
            readTableLogin();

            insertNewUser("login3", "pass3", "nick3");
            readTableLogin();
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
    public boolean updateNick(String oldNickname, String newNickname) {
        for (Entry entry : entries) {
            if (entry.nickname.equals(oldNickname)) {
                try {
                    entry.setNickname(newNickname);
                    updateNickname(entry.login, newNickname);
                    return true;
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        }
        return false;
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

    private static void createTableLogin() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS login (\n" +
                "        id         INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "        login      TEXT,\n" +
                "        password   TEXT,\n" +
                "        nickname   TEXT\n" +
                "    )");
    }

    private static void dropTableLogin() throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS login");
    }

    private static void readTableLogin() throws SQLException {
        try (ResultSet resultSet = statement.executeQuery("SELECT * FROM login")) {
            while (resultSet.next()) {
                entries.add(new Entry(
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getString("nickname"))
                );

                System.out.println(
                        resultSet.getInt(1) + " " +
                                resultSet.getString("login") + " " +
                                resultSet.getString("password") + " " +
                                resultSet.getString("nickname")
                );
            }
        }
        System.out.println();
    }

    private static void clearTableLogin() throws SQLException {
        statement.executeUpdate("DELETE FROM login");
    }

    private static void deleteEx(String nickname) throws SQLException {
        preparedStatement = connection.prepareStatement("DELETE FROM login WHERE nickname = ?");
        preparedStatement.setString(1, nickname);
        preparedStatement.executeUpdate();
    }

    private static void updateNickname(String login, String newNickname) throws SQLException {
        System.out.println("Команда updateNickname");
        preparedStatement = connection.prepareStatement("UPDATE login SET nickname = ? WHERE login = ?");
        preparedStatement.setString(1, newNickname);
        preparedStatement.setString(2, login);
        System.out.println("Изменено записей: " + preparedStatement.executeUpdate());
    }

    private static void insertNewUser(String login, String password, String nickname) throws SQLException {
        System.out.println("Команда insertNewUser");
        preparedStatement = connection.prepareStatement("INSERT INTO login (login, password, nickname) VALUES " +
                "(?, ?, ?)");
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, nickname);
        System.out.println("Добавлено записей: " + preparedStatement.executeUpdate());
    }
}
