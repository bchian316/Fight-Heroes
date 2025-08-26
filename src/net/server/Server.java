package net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 5000;
    private static final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static final ConcurrentHashMap<Integer, Player> players = new ConcurrentHashMap<>();
    private static int nextPlayerId = 1;

    public static void main(String[] args) {
        System.out.println("Game server running on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                int playerId = nextPlayerId++;

                Player newPlayer = new Player(playerId, (int)(Math.random()*600), (int)(Math.random()*400)); // spawn at (100,100)
                players.put(playerId, newPlayer);

                ClientHandler clientHandler = new ClientHandler(socket, players.get(playerId));
                clients.add(clientHandler);
                new Thread(clientHandler).start();

                System.out.println("Player " + playerId + " connected.");
            }
        } catch (IOException e) {}
    }

    public static void broadcastPosition(Player player) {
        //why when i remove the argument then i cant see other players?
        for (ClientHandler client : clients) {
            client.sendPosition(player);
        }
    }

    public static void removeClient(ClientHandler client, int playerId) {
        clients.remove(client);
        players.remove(playerId);
    }

}
