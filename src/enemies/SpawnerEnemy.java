package enemies;

import game.DamageCounter;
import game.Map;
import game.Projectile;
import game.Timer;
import java.util.ArrayList;

public abstract class SpawnerEnemy extends Enemy {
    private final Timer spawnTimer;

    public SpawnerEnemy(String name, int x, int y, int size, int health, int speed, int reload, int passiveRange, int aggroRange,
            int spawnTime, int power) {
        super(name, x, y, size, health, speed, reload, passiveRange, aggroRange, power);
        this.spawnTimer = new Timer(spawnTime, (int) (Math.random() * spawnTime));
    }
    

    public ArrayList<Enemy> spawn() {
        this.resetSpawnTimer();
        return new ArrayList<>();
    }

    public boolean spawnLoaded() {
        return spawnTimer.isDone();
    }

    private void resetSpawnTimer() {
        this.spawnTimer.reset();
    }

    @Override
    public ArrayList<DamageCounter> update(double playerX, double playerY, ArrayList<Projectile> projectiles, Map map) {
        this.spawnTimer.update();
        return super.update(playerX, playerY, projectiles, map);
    }
}