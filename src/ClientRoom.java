import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientRoom extends JFrame {

    private JPanel panel;
    private JPanel inputPanel;
    private JTextArea textArea;
    private JTextField inputField;
    private JButton sendButton;
    private JScrollPane scrollPanel;
    private Client client;
    private String username;

    public ClientRoom(Client client) {
        this.client = client;
        panel = new JPanel(new BorderLayout());
        textArea = new JTextArea();
        scrollPanel = new JScrollPane(textArea);
        inputField = new JTextField();
        sendButton = new JButton("Send");

        // Set up the GUI
        setTitle("Messaging System");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getUserInput();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getUserInput();
            }
        });

        initializeOptionPane();
        initializePanels();
        client.listenForMessage();
    }


    public void initializeOptionPane() {
        UIManager.put("OptionPane.minimumSize", new Dimension(400, 135));
        UIManager.put("OptionPane.maximumSize", new Dimension(400, 135));
        UIManager.put("OptionPane.background", new Color(206, 201, 201));
        UIManager.put("Panel.background", new Color(206, 201, 201));
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
        UIManager.put("OptionPane.messageForeground", new Color(0, 0, 0));
        UIManager.put("Button.background", new Color(32, 63, 91));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 12));
        UIManager.put("TextField.border", BorderFactory.createLineBorder(new Color(32, 32, 92)));

        while (true) {
            // Display custom JOptionPane
            username = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter a username :",
                    "Username Input",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null
            );
            if (!username.isBlank()) break;
        }
        client.setUsername(username);
        client.sendMessage(username);
    }

    public void initializePanels() {
        panel.add(scrollPanel, BorderLayout.CENTER);
        inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.SOUTH);
        textArea.setEditable(false);
        add(panel);
        setVisible(true);
    }

    public void getUserInput() {
        String message = inputField.getText();
        inputField.setText("");
        textArea.append("You: " + message + "\n");
        client.sendMessage(username + " : " + message);
    }

    public void appendMessage(String message) {
        textArea.append(message + "\n");
    }


}
