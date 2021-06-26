package Lesson2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Server {

    private List<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public Server() {
        try (ServerSocket server = new ServerSocket(Constants.SERVER_PORT)) {
            authService = new DbAuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true) {
                System.out.println("Сервер ожидает подключения");
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            System.out.println("Ошибка в работе сервера");
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getMyNickname().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastMessage(String message) {
        clients.forEach(client -> client.sendMessage(message));
    }

    public synchronized void broadcastToSeveralClients(String message, List<String> nicknames) {
        clients.forEach(clientHandler -> {
            if (nicknames.contains(clientHandler.getMyNickname()))
                clientHandler.sendMessage(message);
        });
    }

    public synchronized void broadcastClientsList() {
        String message = Constants.CLIENTS + " " + clients
                .stream()
                .map(ClientHandler::getMyNickname)
                .collect(Collectors.joining(" "));
        broadcastMessage(message);
    }

    public synchronized boolean directMessage(String message, String recipient) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getMyNickname().equals(recipient)) {
                clientHandler.sendMessage(message);
                return true;
            }
        }
        return false;
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientsList();
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientsList();
    }

    public void updateNick(String oldNickname, String newNickname) {
        authService.updateNick(oldNickname, newNickname);
    }
}
