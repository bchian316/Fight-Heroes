package enemies;

import game.DamageCounter;
import game.Map;
import game.Projectile;
import game.Timer;
import java.util.ArrayList;

public abstract class SpawnerEnemy extends Enemy {
    private final Timer spawnTimer;
    private final ArrayList<Enemy> spawnedEnemies = new ArrayList<>();
    private final int spawnLimit; //max amount of spawanbles. -1 for infinite

    public SpawnerEnemy(String name, int x, int y, int size, int health, int speed, int reload, int passiveRange, int aggroRange,
            int spawnTime, int spawnLimit, int power) {
        super(name, x, y, size, health, speed, reload, passiveRange, aggroRange, power);
        this.spawnTimer = new Timer(spawnTime, (int) (Math.random() * spawnTime));
        this.spawnLimit = spawnLimit;
    }
    

    public ArrayList<Enemy> spawn() {
        this.resetSpawnTimer();
        return new ArrayList<>();
    }

    public void addSpawnedEnemies(ArrayList<Enemy> enemies){
        this.spawnedEnemies.addAll(enemies);
    }

    /**
     * 
     * @return True if the Enemy has reached it's spawn limit, false if the enemy can still spawn
     */
    public boolean spawnLimitReached(){
        //simply based off size
        return this.spawnedEnemies.size() >= this.spawnLimit;
    }

    public boolean spawnLoaded() {
        //if spawnTimer is ready and spawnlimit is not reached yet or spawnlimit is -1
        return spawnTimer.isDone() && (!this.spawnLimitReached() || spawnLimit == -1);
    }

    public ArrayList<Enemy> getSpawnedEnemies(){
        return this.spawnedEnemies;
    }

    private void resetSpawnTimer() {
        this.spawnTimer.reset();
    }

    /**
     * Updates the spawnedEnemies attribute, which keeps track of how many enemies this guy spawned
     */
    private void removeSpawnedEnemies(){
        for (int i = spawnedEnemies.size() - 1; i >= 0; i--) {
            if(spawnedEnemies.get(i).isDead()){
                spawnedEnemies.remove(i);
            }
        }
    }

    @Override
    public ArrayList<DamageCounter> update(double playerX, double playerY, ArrayList<Projectile> projectiles, Map map) {
        this.spawnTimer.update();
        this.removeSpawnedEnemies();
        return super.update(playerX, playerY, projectiles, map);
    }
}