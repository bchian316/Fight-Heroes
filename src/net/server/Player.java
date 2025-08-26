package net.server;

public class Player {
    public int id;
    public int x, y;
    private static final int SPEED = 5;

    public Player(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void move(byte action) {
        switch (action) {
            case 0 -> y -= SPEED; // UP
            case 1 -> y += SPEED; // DOWN
            case 2 -> x -= SPEED; // LEFT
            case 3 -> x += SPEED; // RIGHT
        }
    }
}
