
import java.awt.Color;
import java.util.ArrayList;

public class SkeletonLich extends SpawnerEnemy {
    public SkeletonLich(int x, int y) {
        super("SkeletonLich", x, y, 85, 125, 1, 1500, 550,
                new AttackStats(10, 35, 14, 325, 2, 35, new Color(0, 82, 94), false,
                        new AttackStats(10, 35, 21, 325, 2, 35, new Color(0, 82, 94), false,
                                new AttackStats(6, 20, 20, 175, 1, 3, new Color(196, 73, 16)))),
                9000);
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY);
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(),
                    angle + Math.toRadians(35),
                    this.getAttackStats(),
                    (x1, y1, angle1, splitStats) -> moreAttack(x1, y1, angle1, splitStats)));
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(),
                    angle,
                    this.getAttackStats(),
                    (x1, y1, angle1, splitStats) -> moreAttack(x1, y1, angle1, splitStats)));
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(),
                    angle - Math.toRadians(35),
                    this.getAttackStats(),
                    (x1, y1, angle1, splitStats) -> moreAttack(x1, y1, angle1, splitStats)));
        return newProjs;
    }

    public ArrayList<Projectile> moreAttack(double x, double y, double angle,
            AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y,
                    angle + Math.PI,
                    splitStats,
                (x1, y1, angle1, splitStats1) -> evenMoreAttack(x1, y1, angle1, splitStats1)));
        return newProjs;
    }
    public ArrayList<Projectile> evenMoreAttack(double x, double y, double angle,
            AttackStats splitStats) {
        this.heal(6);
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Enemy> spawn(Map map) {
        this.resetSpawnTimer();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        if (Math.random() >= 0.35) {
            newEnemies.add(new SkeletonShaman((int) this.getCenterX(), (int) this.getCenterY()));
        } else {
            for (int i = 0; i < 3; i++) {
                double randAngle = Math.random() * Math.PI * 2; // in radians
                double randMagnitude = Math.random() * 100;
                newEnemies.add(new Skeleton((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                        (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
            }
        }
        return newEnemies;
    }
}