package Lesson6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT)) {
            System.out.println("Сервер запущен, ожидаем подключения...");
            socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            final DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            final DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            final Scanner scanner = new Scanner(System.in);

            // поток считывания сообщений
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String str = inputStream.readUTF();
                            System.out.println("Клиент: " + str);
                            if (str.equals("/end")) {
                                break;
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();

            // поток отправки сообщений
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true)
                        if (scanner.hasNextLine())
                            try {
                                outputStream.writeUTF(scanner.nextLine());
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
