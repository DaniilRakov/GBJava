package Lesson2;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {
    private static class Entry {
        private final String login;
        private final String pass;
        private String nickname;

        public Entry(String login, String pass, String nick) {
            this.login = login;
            this.pass = pass;
            this.nickname = nick;
        }

        public void setNickname(String newNickname) {
            nickname = newNickname;
        }
    }

    private final List<Entry> entries;

    @Override
    public void start() {
        System.out.println(Constants.AUTH_SERVICE_STARTED);
    }

    @Override
    public void stop() {
        System.out.println(Constants.AUTH_SERVICE_STOPPED);
    }


    public BaseAuthService() {
        entries = new ArrayList<>();
        entries.add(new Entry("login1", "pass1", "nick1"));
        entries.add(new Entry("login2", "pass2", "nick2"));
        entries.add(new Entry("login3", "pass3", "nick3"));
    }

    @Override
    public String getNickByLoginPass(String login, String pass) {
        for (Entry entry : entries) {
            if (entry.login.equals(login) && entry.pass.equals(pass)) return entry.nickname;
        }
        return null;
    }

    @Override
    public boolean updateNick(String oldNickname, String newNickname) {
        for (Entry entry : entries) {
            if (entry.nickname.equals(oldNickname)) {
                entry.setNickname(newNickname);
                return true;
            }
        }
        return false;
    }
}
