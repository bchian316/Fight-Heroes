
import java.awt.Color;
import java.util.ArrayList;

public class Grave extends SpawnerEnemy {
    public Grave(int x, int y) {
        super("Grave", x, y, 50, 55, 0, 1500, 0, new AttackStats(7, 30, 18, 300, 1, 20, new Color(57, 57, 57)), 3000);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) + Math.toRadians(i * 45),
                    this.getAttackStats()));
        }
        return newProjs;
    }

    @Override
    public ArrayList<Enemy> spawn(Map map) {
        this.resetSpawnTimer();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        newEnemies.add(new Ghost((int)this.getCenterX(), (int)this.getCenterY()));
        return newEnemies;
    }
}