package Lesson8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends JFrame {

    private static String connectionMessage;

    private JTextField msgInputField;
    private JTextArea chatArea;

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public Client() {
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        prepareGUI();
    }

    public void openConnection() throws IOException {
        try {
            socket = new Socket(Constants.SERVER_ADDRESS, Constants.SERVER_PORT);
            connectionMessage = Constants.GOOD_CONNECTION;
        } catch (Exception ex) {
            connectionMessage = Constants.BAD_CONNECTION;
            return;
        }
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        // Поток чтения сообщений
        new Thread(() -> {
            try {
                while (true) {
                    String strFromServer = inputStream.readUTF();
                    chatArea.append(strFromServer + "\n");
                    if (strFromServer.toLowerCase().startsWith(Constants.STOP_WORD)) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
    }

    public void sendMessage() {
        if (!msgInputField.getText().trim().isEmpty()) {
            try {
                outputStream.writeUTF(msgInputField.getText());
                msgInputField.setText("");
                msgInputField.grabFocus();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, Constants.SEND_MSG_ERROR);
            } catch (NullPointerException exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(this, Constants.SEND_MSG_ERROR);
            }
        }
    }

    public void prepareGUI() {
        // Параметры окна
        setBounds(600, 300, 500, 500);
        setTitle("Клиент");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Текстовое поле для вывода сообщений
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        chatArea.append(connectionMessage + "\n");

        // Нижняя панель с полем для ввода сообщений и кнопкой отправки сообщений
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton btnSendMsg = new JButton("Отправить");
        bottomPanel.add(btnSendMsg, BorderLayout.EAST);
        msgInputField = new JTextField();
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(msgInputField, BorderLayout.CENTER);
        btnSendMsg.addActionListener(e -> sendMessage());
        msgInputField.addActionListener(e -> sendMessage());

        // Настраиваем действие на закрытие окна
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    if (socket == null)
                        return;
                    outputStream.writeUTF(Constants.STOP_WORD);
                    closeConnection();
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}
