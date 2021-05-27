package Lesson7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final Server server;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    private String name;

    public String getName() {
        return name;
    }

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            new Thread(() -> {
                try {
                    authentication();
                    readMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }

    public void authentication() throws IOException {
        while (true) {
            String str = inputStream.readUTF();
            if (str.toLowerCase().startsWith(Constants.AUTH)) {
                String[] parts = str.split("\\s");
                String nick = server.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                if (nick != null) {
                    if (!server.isNickBusy(nick)) {
                        sendMsg(Constants.AUTH_OK + nick);
                        name = nick;
                        server.broadcastMsg(name + " вошёл в чат");
                        server.subscribe(this);
                        return;
                    } else {
                        sendMsg("Учетная запись уже используется");
                    }
                } else {
                    sendMsg("Неверные логин/пароль");
                }
            }
        }
    }

    public void readMessages() throws IOException {
        while (true) {
            String strFromClient = inputStream.readUTF();
            System.out.println(name + ": " + strFromClient);

            if (strFromClient.toLowerCase().startsWith(Constants.STOP_WORD)) return;

            if (strFromClient.toLowerCase().startsWith(Constants.WHISPERING)) {
                String[] parts = strFromClient.split("\\s");
                String message = strFromClient.substring(3 + parts[1].length());
                String fullMessage = "[" + name + "] шепчет [" + parts[1] + "]:" + message;
                boolean sendSuccess = server.directMsg(fullMessage, parts[1]);
                sendMsg(sendSuccess ? fullMessage : Constants.NO_USER);
            } else
                server.broadcastMsg("[" + name + "]:  " + strFromClient);
        }
    }

    public void sendMsg(String msg) {
        try {
            outputStream.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        server.broadcastMsg(name + " вышел из чата");
        server.unsubscribe(this);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
