
import java.awt.Color;
import java.util.ArrayList;

public class Skeleton extends Enemy {

    public Skeleton(int x, int y) {
        super("Skeleton", x, y, 50, 30, 2, 1500, 400, new AttackStats(new StatusEffect("Burn", 0, 0.8, 0.2, 0, 0, 7000, new Color(232, 78, 12)), 7, 15, 12, 200, 1, 7, new Color(255, 255, 255)), 1);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                this.getAttackStats()));
        return newProjs;
    }
    
}