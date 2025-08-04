
import java.awt.Color;
import java.util.ArrayList;

public class Crypt extends SpawnerEnemy {
    private static final int SPLITANGLE = 40;
    public Crypt(int x, int y) {
        super("Crypt", x, y, 100, 80, 0, 1000, 0,
                new AttackStats(10, 30, 7, 250, 1, 30, new Color(101, 161, 48), true,
                        new AttackStats(3, 15, 9, 200, 1, 10, new Color(111, 42, 49))),
        7, 5000);
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY);
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), angle, this.getAttackStats(),
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