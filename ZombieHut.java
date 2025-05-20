
import java.awt.Color;
import java.util.ArrayList;

public class ZombieHut extends SpawnerEnemy {
    public ZombieHut(int x, int y) {
        super("Zombie Hut", x, y, 60, 100, 0, 0, 0, 0, new AttackStats(0, 0, 0, 0, 0, new Color(0, 0, 0)), 3000);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Enemy> spawn(double playerX, double playerY) {
        this.resetSpawnTimer();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        newEnemies.add(new Zombie((int)this.getCenterX(), (int)this.getCenterY()));
        return newEnemies;
    }
}