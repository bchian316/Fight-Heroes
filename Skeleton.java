
import java.awt.Color;
import java.util.ArrayList;

public class Skeleton extends Enemy {

    public Skeleton(double x, double y) {
        super("Skeleton", x, y, 50, 30, 2, 1000, new AttackStats(10, 15, 20, 150, new Color(0, 0, 0)));
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                this.getAttackStats()));
        return newProjs;
    }
    
}