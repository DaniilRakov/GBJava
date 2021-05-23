package Lesson7;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Scanner scanner;

    public Client() {
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }

    public void openConnection() throws IOException {
        try {
            socket = new Socket(Constants.SERVER_ADDRESS, Constants.SERVER_PORT);
            System.out.println("Подключение успешно");
        } catch (Exception ex) {
            System.out.println("Не удалось подключиться к серверу");
            return;
        }
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        scanner = new Scanner(System.in);

        // поток отправки сообщений
        new Thread(() -> {
            while (true)
                if (scanner.hasNextLine()) {
                    sendMessage();
                }
        }).start();

        // поток считывания сообщений
        new Thread(() -> {
            try {
                while (true) {
                    String strFromServer = inputStream.readUTF();
                    System.out.println("Сервер: " + strFromServer);
                    if (strFromServer.equalsIgnoreCase("/end")) {
                        closeConnection();
                        break;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public void closeConnection() {
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
        scanner.close();
    }

    public void sendMessage() {
        try {
            outputStream.writeUTF(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка отправки сообщения");
        }
    }
}

