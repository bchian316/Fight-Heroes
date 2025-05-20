
import java.awt.Color;
import java.util.ArrayList;

public class Ben extends SpawnerEnemy {
    public Ben(int x, int y) {
        super("Ben", x, y, 200, 300, 3, 2000, 500, 4000, new AttackStats(20, 50, 15, 500, 1, new Color(222, 140, 15), false,
                new AttackStats(5, 25, 10, 500, 2, new Color(252, 170, 45))), 8000);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(),
                    Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY)
                            + Math.toRadians((i - 1.5) * 30),
                    this.getAttackStats(), (x1, y1, targetX1, targetY1, splitStats) -> moreAttack(x1, y1, targetX1, targetY1, splitStats)));
        }
        return newProjs;
    }
    
    public ArrayList<Projectile> moreAttack(double x, double y, double targetX, double targetY, AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        //shoots twice from himself
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) - Math.toRadians(30), splitStats));
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) + Math.toRadians(30), splitStats));
        return newProjs;
    }


    @Override
    public ArrayList<Enemy> spawn(double playerX, double playerY) {
        this.resetSpawnTimer();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        newEnemies.add(new Frankenstein((int) this.getCenterX(), (int) this.getCenterY()));
        return newEnemies;
    }
    
}