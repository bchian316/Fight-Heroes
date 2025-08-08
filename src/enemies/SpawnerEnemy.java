package enemies;

import game.DamageCounter;
import game.Map;
import game.Projectile;
import game.Timer;
import java.util.ArrayList;

public abstract class SpawnerEnemy extends Enemy {
    private Timer spawnTimer;

    public SpawnerEnemy(String name, int x, int y, int size, int health, int speed, int reload, int passiveRange,
            int regen, int regenTick, int spawnTime, int power) {
        super(name, x, y, size, health, speed, reload, passiveRange, regen, regenTick, power);
        this.spawnTimer = new Timer(spawnTime, (int) (Math.random() * spawnTime));
    }
    
    public SpawnerEnemy(String name, int x, int y, int size, int health, int speed, int reload, int passiveRange,
            int spawnTime, int power) {
        this(name, x, y, size, health, speed, reload, passiveRange, 0, 0, spawnTime, power);
        
    }

    public abstract ArrayList<Enemy> spawn();

    public boolean spawnLoaded() {
        return spawnTimer.isDone();
    }

    public void resetSpawnTimer() {
        this.spawnTimer.reset();
    }

    @Override
    public ArrayList<DamageCounter> update(double playerX, double playerY, ArrayList<Projectile> projectiles, Map map) {
        this.spawnTimer.update();
        return super.update(playerX, playerY, projectiles, map);
    }
}