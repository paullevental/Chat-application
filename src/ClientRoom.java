import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// GUI for Clients chat
public class ClientRoom extends JFrame {

    private final JFrame frame;
    private final JPanel panel;
    private final JPanel inputPanel;
    private final JTextArea textArea;
    private final JTextField inputField;
    private final JButton sendButton;
    private final Client client;
    private String username;

    // Instantiates ClientRoom class
    // Creates Swing objs, and calls methods to initialize GUI obj and call action listener methods
    public ClientRoom(Client client) {
        this.client = client;
        this.frame = new JFrame();
        this.panel = new JPanel(new BorderLayout());
        this.textArea = new JTextArea();
        JScrollPane scrollPanel = new JScrollPane(textArea);
        this.inputField = new JTextField();
        this.sendButton = new JButton("Send");
        this.inputPanel = new JPanel(new BorderLayout());

        listenForInputField();
        listForSendButton();
        initializeMainPanel();
        initializeOptionPane();
        sendUsernameHandler();
        initializePanels();
        client.listenForMessage();
    }

    // listener for input field
    public void listenForInputField() {
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getUserInput();
            }
        });
    }

    // listener for send button
    public void listForSendButton() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getUserInput();
            }
        });
    }

    // creates JFrame, will display messaging platform
    public void initializeMainPanel() {
        frame.setTitle("Messaging System");
        frame.setSize(new Dimension(400, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.GRAY);
        frame.setResizable(false);
    }

    // creates JOption pane to ask user for username
    public void initializeOptionPane() {
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("OptionPane.messageForeground", new Color(0, 0, 0));
        UIManager.put("Button.background", new Color(32, 63, 91));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 12));
        UIManager.put("TextField.border", BorderFactory.createEmptyBorder());
    }

    // gets user input
    public void sendUsernameHandler() {
        while (true) {
            // Display custom JOptionPane
            username = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter a username:",
                    "Username Input",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null
            );
            if (!username.isBlank()) {
                break;
            }

        }
        client.setUsername(username);
    }

    // creates inner panels, and main panel that is added to JFrame
    public void initializePanels() {
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setSize(new Dimension(400, 30));
        inputPanel.setPreferredSize(new Dimension(400, 30));
        textArea.setFont(new Font("Arial", Font.BOLD,12));
        inputField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        inputPanel.add(inputField, BorderLayout.CENTER);
        createSendButton();
        inputPanel.add(sendButton, BorderLayout.EAST);
        textArea.setEditable(false);
        panel.add(inputPanel, BorderLayout.SOUTH);
        panel.add(textArea, BorderLayout.CENTER);
        frame.add(panel);
        frame.setVisible(true);
    }

    // create send JButton
    public void createSendButton() {
        sendButton.setFocusPainted(false);
        sendButton.setBackground(new Color(70, 130, 180)); // Set the button color
        sendButton.setForeground(Color.WHITE); // Set the text color
        sendButton.setFont(new Font("Arial", Font.BOLD, 14)); // Set the font
        sendButton.setOpaque(true);
        sendButton.setBorderPainted(false); // Remove the border
    }

    // gets user entered message
    public void getUserInput() {
        if (!inputField.getText().isBlank()) {
            String message = inputField.getText();
            inputField.setText("");
            textArea.append("You : " + message + "\n");
            client.sendMessage(username + " : " + message);
        }
    }

    // adds message to GUI
    public void appendMessage(String message) {
        textArea.append(message + "\n");
    }

}
