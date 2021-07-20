package Lesson2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final Server server;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    private String myNickname;

    public String getMyNickname() {
        return myNickname;
    }

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.myNickname = "";
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
                String nickname = server.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                if (nickname != null) {
                    if (!server.isNickBusy(nickname)) {
                        sendMessage(Constants.AUTH_OK + nickname);
                        myNickname = nickname;
                        server.broadcastMessage(myNickname + " вошёл в чат");
                        server.subscribe(this);
                        return;
                    } else {
                        sendMessage("Учетная запись уже используется");
                    }
                } else {
                    sendMessage("Неверные логин/пароль");
                }
            }
        }
    }

    public void readMessages() throws IOException {
        while (true) {
            String strFromClient = inputStream.readUTF();
            System.out.println(myNickname + ": " + strFromClient);

            if (strFromClient.toLowerCase().startsWith(Constants.STOP_WORD)) return;

            if (strFromClient.toLowerCase().startsWith(Constants.CHANGE_NICKNAME)) {
                String[] parts = strFromClient.split("\\s");
                String newNickname = strFromClient.substring(parts[0].length() + 1);
                server.updateNick(myNickname, newNickname);
                server.broadcastMessage("[" + myNickname + "]  сменил никнейм на  [" + newNickname + "]");
                myNickname = newNickname;
                continue;
            }

            if (strFromClient.toLowerCase().startsWith(Constants.WHISPERING)) {
                String[] parts = strFromClient.split("\\s");
                String message = strFromClient.substring(parts[0].length() + 1 + parts[1].length());
                String fullMessage = "[" + myNickname + "] шепчет [" + parts[1] + "]: " + message;
                boolean sendSuccess = server.directMessage(fullMessage, parts[1]);
                sendMessage(sendSuccess ? fullMessage : Constants.NO_USER);
            } else
                server.broadcastMessage("[" + myNickname + "]:  " + strFromClient);
        }
    }

    public void sendMessage(String msg) {
        try {
            outputStream.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        server.broadcastMessage(myNickname + " вышел из чата");
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
