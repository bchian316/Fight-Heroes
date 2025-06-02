
import java.awt.Color;
import java.util.ArrayList;

public class Skeleton extends Enemy {

    public Skeleton(int x, int y) {
        super("Skeleton", x, y, 50, 35, 2, 1000, 400, new AttackStats(7, 15, 20, 200, 1, 7, new Color(255, 255, 255)));
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                this.getAttackStats()));
        return newProjs;
    }
    
}