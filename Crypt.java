
import java.awt.Color;
import java.util.ArrayList;

public class Crypt extends SpawnerEnemy {
    private static final int SPLITANGLE = 40;
    public Crypt(int x, int y) {
        super("Crypt", x, y, 100, 80, 0, 1000, 0, 20, 5000, 3000, 7);
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY);
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), angle,
                new AttackStats(new StatusEffect("Poison", -10, 0, 0, 0, 0, 3000, new Color(101, 161, 48)), 10, 30, 7, 250, 1, 30, new Color(89, 200, 54), true,
                new AttackStats(3, 15, 9, 200, 1, 10, new Color(150, 161, 150))),
                    (x1, y1, angle1, splitStats) -> moreAttack(x1, y1, angle1, splitStats)));
        
        return newProjs;
    }

    public ArrayList<Projectile> moreAttack(double x, double y, double angle,
            AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        // shoots twice from himself
        for (int i = 0; i < 3; i++) {
            newProjs.add(new Projectile(x, y, angle - Math.toRadians(90) + Math.toRadians(Crypt.SPLITANGLE * (i - 1)),
                    splitStats));
        }
        for (int i = 0; i < 3; i++) {
            newProjs.add(new Projectile(x, y, angle + Math.toRadians(90) + Math.toRadians(Crypt.SPLITANGLE*(i - 1)), splitStats));
        }
        return newProjs;
    }

    @Override
    public ArrayList<Enemy> spawn(Map map) {
        this.resetSpawnTimer();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        newEnemies.add(new Skeleton((int) this.getCenterX(), (int) this.getCenterY()));
        return newEnemies;
    }

}