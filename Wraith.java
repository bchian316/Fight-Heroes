
import java.awt.Color;
import java.util.ArrayList;

public class Wraith extends SpawnerEnemy {
    public Wraith(int x, int y) {
        super("Wraith", x, y, 60, 100, 3, 1750, 600,
                new AttackStats(20, 40, 15, 200, 1, 40, new Color(102, 102, 102), false,
                        new AttackStats(15, 25, 10, 200, 2, 25, new Color(4, 189, 35), false,
                                new AttackStats(10, 20, 8, 200, 2, 20, new Color(0, 105, 17)))),
        2250);
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(),
                    Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                    this.getAttackStats(),
                    (x1, y1, targetX1, targetY1, splitStats) -> moreAttack(x1, y1, targetX1, targetY1, splitStats)));
        
        return newProjs;
    }

    public ArrayList<Projectile> moreAttack(double x, double y, double targetX, double targetY,
            AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        // shoots twice from himself
        newProjs.add(new Projectile(x, y,
                Game.getAngle(x, y, targetX, targetY) - Math.toRadians(45),
                splitStats, 
                (x1, y1, targetX1, targetY1, splitStats1) -> evenMoreAttack(x1, y1, targetX1, targetY1, splitStats1)));
        newProjs.add(new Projectile(x, y,
                Game.getAngle(x, y, targetX, targetY) + Math.toRadians(45),
                splitStats, 
                (x1, y1, targetX1, targetY1, splitStats1) -> evenMoreAttack(x1, y1, targetX1, targetY1, splitStats1)));
        return newProjs;
    }

    public ArrayList<Projectile> evenMoreAttack(double x, double y, double targetX, double targetY,
            AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y,
                Game.getAngle(x, y, targetX, targetY) - Math.toRadians(30),
                splitStats));
        newProjs.add(new Projectile(x, y,
                Game.getAngle(x, y, targetX, targetY) + Math.toRadians(30),
                splitStats));
        return newProjs;
    }

    @Override
    public ArrayList<Enemy> spawn(Map map) {
        this.resetSpawnTimer();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        newEnemies.add(new Ghost((int) this.getCenterX(), (int) this.getCenterY()));
        return newEnemies;
    }

}