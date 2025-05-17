
import java.awt.Color;
import java.util.ArrayList;

public class Vampire extends Enemy {

    public Vampire(double x, double y) {
        super("Vampire", x, y, 35, 50, 3, 3000, 600, 1000, new AttackStats(30, 25, 20, 400, 1, new Color(255, 18, 18)));
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                this.getAttackStats()));
        return newProjs;
    }
    
}