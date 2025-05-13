
import java.awt.Color;
import java.util.ArrayList;

public class Zombie extends Enemy {

    public Zombie(double x, double y) {
        super("Zombie", x, y, 50, 50, 3, 2000, new AttackStats(5, 40, 20, 50, new Color(0, 150, 0)));
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                this.getAttackStats()));
        return newProjs;
    }
    
}