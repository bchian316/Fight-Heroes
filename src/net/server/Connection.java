package net.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    private final Socket socket;

    private final DataInputStream in;
    private final DataOutputStream out;
    
    public Connection(Socket socket) throws IOException{
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }
    
    public void write(int data) {
        try {
            this.out.writeInt(data);
        } catch (IOException ex) {}
    }

    public int read() {
        try {
            return this.in.readInt();
        } catch (IOException ex) {}
        return 0;
    }
}