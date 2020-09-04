package client.packag;

import network.packag.TCPConnection;
import network.packag.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener{

    private static  String ip_adress = "127.0.0.1";
    private static  int PORT = 8189;
    private static  int WIDTH = 600;
    private static  int HEIGHT = 400;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });

    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickName = new JTextField("Anton");
    private final JTextField fieldInput = new JTextField("Hello");

    private TCPConnection connection;


    private ClientWindow(){
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(WIDTH, HEIGHT);
    setLocationRelativeTo(null);
    setAlwaysOnTop(true);

    log.setEditable(false);
    log.setLineWrap(true);
    fieldInput.addActionListener(this);
    add(log, BorderLayout.CENTER);
    add(fieldInput, BorderLayout.SOUTH);
    add(fieldNickName, BorderLayout.NORTH);

        setVisible(true);

        try {
            connection = new TCPConnection(this, ip_adress, PORT);
        } catch (IOException e) {
            printMag("Connection exception: "+ e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if(msg.equals("")) return;
        fieldInput.setText(null);
        connection.sendString(fieldNickName.getText() + " : " + msg);
        }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMag("Connection ready");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMag(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMag("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMag("Connection exception: "+ e);
    }

    private synchronized void printMag(String mag){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(mag+"\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

}
