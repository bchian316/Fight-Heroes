package enemies;

import game.Projectile;

import java.util.ArrayList;

public class ZombieHut extends SpawnerEnemy {
    public ZombieHut(int x, int y) {
        super("Zombie Hut", x, y, 60, 95, 0, 0, 300, 4500, 2);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Enemy> spawn() {
        super.spawn();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        newEnemies.add(new Zombie((int)this.getCenterX(), (int)this.getCenterY()));
        return newEnemies;
    }
}