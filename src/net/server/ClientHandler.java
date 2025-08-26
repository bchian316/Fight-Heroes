package net.server;

import java.io.*;
import java.net.Socket;

//this is the server client (the server has a different version of a client than the actual client)

public class ClientHandler implements Runnable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Player p;

    public ClientHandler(Socket socket, Player p) {
        this.socket = socket;
        this.p = p;

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            sendPosition(p);
        } catch (IOException e) {}
    }

    @Override
    public void run() {
        try {
            while (true) {
                int id = in.readInt();
                byte action = in.readByte();

                if (p != null) {
                    p.move(action);
                    Server.broadcastPosition(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Player " + p.id + " disconnected.");
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
            Server.removeClient(this, p.id);
        }
    }

    public void sendPosition(Player p) {
        try {
            out.writeInt(p.id);
            out.writeInt(p.x);
            out.writeInt(p.y);
            out.flush();
        } catch (IOException e) {}
    }
}
