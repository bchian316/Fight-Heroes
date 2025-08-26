package net.client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.ConcurrentHashMap;

public class Client extends JFrame {
    private DataOutputStream out;
    private DataInputStream in;
    private int playerId;
    private ConcurrentHashMap<Integer, Point> players = new ConcurrentHashMap<>();

    public Client(String host, int port) {
        setTitle("RPG Client");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (var entry : players.entrySet()) {
                    int id = entry.getKey();
                    Point pos = entry.getValue();
                    g.setColor(id == playerId ? Color.BLUE : Color.RED);
                    g.fillRect(pos.x, pos.y, 20, 20);
                }
            }
        };
        add(gamePanel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    byte action = -1;
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W -> action = 0;
                        case KeyEvent.VK_S -> action = 1;
                        case KeyEvent.VK_A -> action = 2;
                        case KeyEvent.VK_D -> action = 3;
                    }
                    if (action != -1) {
                        out.writeInt(playerId);
                        out.writeByte(action);
                        out.flush();
                    }
                } catch (IOException ex) {}
            }
        });

        try {
            Socket socket = new Socket(host, port);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            // Thread to listen for server updates
            new Thread(() -> {
                try {
                    while (true) {
                        int id = in.readInt();
                        int x = in.readInt();
                        int y = in.readInt();
                        players.put(id, new Point(x, y));

                        if (playerId == 0) {
                            playerId = id; // first update gives us our ID
                        }

                        gamePanel.repaint();
                    }
                } catch (IOException e) {}
            }).start();

        } catch (IOException e) {}
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;
        SwingUtilities.invokeLater(() -> new Client(host, port).setVisible(true));
    }
}
