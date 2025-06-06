
import java.awt.Color;
import java.util.ArrayList;

public class Ben extends SpawnerEnemy {
    private final static int SPAWN_RANGE = 200;
    public Ben(int x, int y) {
        super("Ben", x, y, 150, 450, 2, 3500, 800, new AttackStats(16, 50, 10, 250, 1, 30, new Color(222, 140, 15), false,
                new AttackStats(5, 25, 8, 500, 2, 10, new Color(252, 170, 45))), 12000);
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
    public ArrayList<Enemy> spawn(Map map) {
        this.resetSpawnTimer();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        double randAngle = Math.random() * Math.PI * 2; // in radians
        double randMagnitude = Math.random() * Ben.SPAWN_RANGE;
        newEnemies.add(new Mummy((int)(this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                (int)(this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
        return newEnemies;
    }
    
}