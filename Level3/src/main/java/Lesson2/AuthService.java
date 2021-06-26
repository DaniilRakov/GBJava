package Lesson2;

public interface AuthService {
    void start();
    String getNickByLoginPass(String login, String pass);
    boolean updateNick(String oldNickname, String newNickname);
    void stop();
}
