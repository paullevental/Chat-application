package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class ClientRoom extends JFrame {

    private CardLayout pageLayout;
    private JScrollPane scrollPanel;
    private JPanel mainPanel;
    private JPanel inputPanel;
    private JPanel serverPage;
    private JPanel messageRoomPage;
    private JTextArea textArea;
    private JTextField inputField;
    private JButton sendButton;
    private Client client;
    private String username;
    private int portNumber;

    public ClientRoom(Client client) {
        setSize(400, 480);
        setTitle("Messaging System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        this.pageLayout = new CardLayout();
        this.client = client;
        this.textArea = new JTextArea();
        this.scrollPanel = new JScrollPane(textArea);
        this.inputField = new JTextField();
        this.sendButton = new JButton("Send");
        this.inputPanel = new JPanel(new BorderLayout());
        this.mainPanel = new JPanel(pageLayout);

        sendUsernameHandler();
        createServerSelectionPage();
        mainPanel.add(serverPage, "Server Page");

        createMessageRoom();
        mainPanel.add(messageRoomPage, "Room Page");

        add(mainPanel);
        pageLayout.show(mainPanel, "Server Page");
        listenForInputField();
        listenForSendButton();
    }

    public void sendUsernameHandler() {
        initializeOptionPane();
        while (true) {
            username = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter a username:",
                    "Username Input",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null
            );
            if (username != null && !username.trim().isEmpty()) {
                break;
            } else if (username == null) {
                System.exit(0); // Exit the program if the user cancels or closes the dialog
            }
        }
        client.setUsername(username);
    }

    public void createServerSelectionPage() {
        this.serverPage = new JPanel();
        serverPage.setPreferredSize(new Dimension(500, 480));

        for (int i = 0; i < 8; i++) {
            int index = i;

            JButton serverButton = new JButton(ServerCapacityTracker.getInstance(8).getCapacity(i) + " Clients" );
            setButtonStyle(serverButton, i);

            serverButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleButtonActionCommand(index);
                    pageLayout.show(mainPanel, "Room Page");
                }
            });

            serverPage.add(serverButton);
        }

        serverPage.revalidate();
        serverPage.repaint();
        setVisible(true);
    }

    public void setButtonStyle(JButton button, int i) {
        button.setActionCommand(String.valueOf(i));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Sans", Font.BOLD, 15));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void handleButtonActionCommand(int index) {
        ServerCapacityTracker.getInstance(8).incrementCapacity(index);
        startServer(index);

        client.sendMessage(username);
    }

    public void startServer(int portIndex) {
        System.out.println(Server.serverPorts.length);
        portNumber = Server.serverPorts[portIndex];

        try {
            client.connectClientToServer(new Socket("localhost", portNumber));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setTitle(username + " localhost : " + portNumber);
        client.listenForMessage();
    }

    public void createMessageRoom() {
        this.messageRoomPage = new JPanel(new BorderLayout());
        inputPanel.setSize(new Dimension(400, 30));
        inputPanel.setPreferredSize(new Dimension(400, 30));
        textArea.setFont(new Font("Arial", Font.BOLD, 12));
        textArea.setSize(new Dimension(400, 300));
        textArea.setEditable(false);
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(new Color(33, 31, 31));
        inputField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        inputPanel.add(inputField, BorderLayout.CENTER);

        sendButton.setFocusPainted(false);
        sendButton.setBackground(new Color(70, 130, 180));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Sans", Font.BOLD, 15));
        sendButton.setOpaque(true);
        sendButton.setBorderPainted(false);
        sendButton.setHorizontalAlignment(SwingConstants.CENTER);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        messageRoomPage.add(inputPanel, BorderLayout.SOUTH);
        messageRoomPage.add(scrollPanel, BorderLayout.CENTER);

        scrollPanel.getViewport().setBackground(new Color(33, 31, 31));
    }

    public void listenForInputField() {
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getUserInput();
            }
        });
    }

    public void listenForSendButton() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getUserInput();
            }
        });
    }

    public void initializeOptionPane() {
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("OptionPane.messageForeground", new Color(0, 0, 0));
        UIManager.put("Button.background", new Color(32, 63, 91));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 12));
        UIManager.put("TextField.border", BorderFactory.createEmptyBorder());
    }



    public void getUserInput() {
        if (!inputField.getText().isBlank()) {
            String message = inputField.getText();
            inputField.setText("");
            client.sendMessage(username + " : " + message);
        }
    }

    public void appendMessage(String message) {
        textArea.append(message + "\n");
    }
}

