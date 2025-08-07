
import java.awt.Color;
import java.util.ArrayList;

public class FireMage extends Mage {
    public FireMage() {
        super("Fire Mage", 50, 175, 30, 6, 150, 7000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY), new AttackStats(9, 40, 10, 150, -1, 5, new Color(255, 0, 0))));
        return newProjs;
        
    }

    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(p.getCenterX(), p.getCenterY(),
                Game.getAngle(p.getCenterX(), p.getCenterY(), targetX, targetY),
                new AttackStats(new StatusEffect("Burn", -5, 0, 0, 0, 0, 4000, new Color(200, 50, 50)), 20, 30, 25, 300, 3, 20, new Color(255, 100, 100))));
        return newProjs;
    }

    @Override
    public String toString() {
        return super.toString() + ": Fire a constant stream of fire";
    }
}