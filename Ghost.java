
import java.awt.Color;
import java.util.ArrayList;

public class Ghost extends Enemy {

    public Ghost(double x, double y) {
        super("Ghost", x, y, 50, 30, 5, 1000, new AttackStats(8, 25, 10, 75, new Color(134, 134, 134)));
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) - Math.toRadians(15),
                this.getAttackStats()));
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) + Math.toRadians(15),
                this.getAttackStats()));
        return newProjs;
    }
    
}