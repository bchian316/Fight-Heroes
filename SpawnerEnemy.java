import java.util.ArrayList;

public abstract class SpawnerEnemy extends Enemy {
    private final int spawnTime;
    private int spawnTimer;
    public SpawnerEnemy(String name, int x, int y, int size, int health, int speed, int reload, int passiveRange,
            int passiveTime, AttackStats attk, int spawnTime) {
        super(name, x, y, size, health, speed, reload, passiveRange, passiveTime, attk);
        this.spawnTime = spawnTime;
        this.spawnTimer = (int) (Math.random() * spawnTime);
    }

    public abstract ArrayList<Enemy> spawn(double playerX, double playerY);

    public boolean spawnLoaded() {
        return spawnTimer >= spawnTime;
    }

    public void resetSpawnTimer() {
        this.spawnTimer = 0;
    }

    @Override
    public void update(double playerX, double playerY, ArrayList<Projectile> projectiles, Tile[][] walls) {
        super.update(playerX, playerY, projectiles, walls);
        this.spawnTimer += Game.updateDelay();
    }
}