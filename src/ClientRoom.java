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

    public ClientRoom(Client client) {
        panel = new JPanel(new BorderLayout());
        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPanel = new JScrollPane(textArea);
        inputField = new JTextField();
        sendButton = new JButton("Send");
        this.client = client;


        // Set up the GUI
        setTitle("Messaging System");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getUserInput();
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getUserInput();
            }
        });

        initializePanels();
    }

    public void initializePanels() {
        panel.add(scrollPanel, BorderLayout.CENTER);
        inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.SOUTH);

        add(panel);

        setVisible(true);
    }

    public void getUserInput() {
        String message = inputField.getText();
        inputField.setText("");
        textArea.append("You: " + message + "\n");
        client.sendMessage(message);
    }


}
