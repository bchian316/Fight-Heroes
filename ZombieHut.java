
import java.awt.Color;
import java.util.ArrayList;

public class ZombieHut extends Enemy implements canSpawn {
    private final int reloadTime = 2000;
    private int reloadTimer = (int)(Math.random() * reloadTime);
    public ZombieHut(double x, double y) {
        super("Zombie Hut", x, y, 60, 150, 0, 0, new AttackStats(0, 0, 0, 0, new Color(0, 0, 0)));
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Enemy> spawn(double playerX, double playerY) {
        this.reloadTimer = 0;
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        newEnemies.add(new Zombie(this.getCenterX(), this.getCenterY()));
        return newEnemies;
    }

    @Override
    public boolean spawnLoaded() {
        return reloadTimer >= reloadTime;
    }

    @Override
    public void update(double playerX, double playerY, ArrayList<Projectile> projectiles, int borderX1, int borderY1, int borderX2, int borderY2) {
        super.update(playerX, playerY, projectiles, borderX1, borderY1, borderX2, borderY2);
        this.reloadTimer += Game.updateDelay();
    }
    
}