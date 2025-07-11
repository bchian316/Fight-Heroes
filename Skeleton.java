
import java.awt.Color;
import java.util.ArrayList;

public class Skeleton extends Enemy {

    public Skeleton(int x, int y) {
        super("Skeleton", x, y, 50, 30, 2, 1250, 400, new AttackStats(7, 15, 20, 200, 1, 7, new Color(255, 255, 255)));
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        this.heal(3);
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                this.getAttackStats()));
        return newProjs;
    }
    
}