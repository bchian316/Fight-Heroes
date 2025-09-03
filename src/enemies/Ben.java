package enemies;

import game.AttackStats;
import game.Game;
import game.HasHealth;
import game.Projectile;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;


public class Ben extends SpawnerEnemy {
    private final static int SPAWN_RANGE = 200;
    public Ben(int x, int y) {
        super("Ben", x, y, 150, 450, 2, 3500, 800, 1000, 12000, 25);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (double i = -1.5; i <= 1.5; i++) {
            double angle = Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY)
                    + Math.toRadians(i * 30);
            newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), angle,
                    new AttackStats(16, 50, 10, 250, 1, 30, new Color(222, 140, 15), false, true, false,
                            new AttackStats(5, 25, 8, 500, 2, 10, new Color(252, 170, 45))),
                        (x1, y1, angle1, splitStats, hitObjects) -> moreAttack(x1, y1, angle1, splitStats, hitObjects), null));
        }
        return newProjs;
    }
    
    public ArrayList<Projectile> moreAttack(double x, double y, double angle, AttackStats splitStats, HashSet<HasHealth> hitObjects) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        //shoots twice from himself
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), angle - Math.toRadians(30), splitStats, hitObjects));
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), angle + Math.toRadians(30), splitStats, hitObjects));
        return newProjs;
    }


    @Override
    public ArrayList<Enemy> spawn() {
        super.spawn();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        double randAngle = Game.getRandomAngle(); // in radians
        double randMagnitude = Math.random() * Ben.SPAWN_RANGE;
        newEnemies.add(new Mummy((int)(this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                (int)(this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
        return newEnemies;
    }
    
}