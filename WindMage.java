
import java.awt.Color;
import java.util.ArrayList;

public class WindMage extends Mage {
    public WindMage() {
        super("Wind Mage", 60, 100, 25, 8, 1500, 6000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = -1; i <= 1; i += 2) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) + i*Math.toRadians(15),  new AttackStats(20, 40, 5, 200, -1, 1, new Color(125, 125, 125))));
            
        }
        return newProjs;
    }

    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY){
        p.addStatusEffect(new StatusEffect("Swift", 0, 0, 0, 0.35, 0, 2000, new Color(170, 170, 170)));
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            newProjs.add(new Projectile(p.getCenterX(), p.getCenterY(), Game.getAngle(p.getCenterX(), p.getCenterY(), targetX, targetY) + i*Math.toRadians(72),  new AttackStats(10, 60, 3, 125, -1, 1, new Color(70, 70, 70))));
            
        }
        return newProjs;
    }

    @Override
    public String toString() {
        return super.toString() + ": Fire 2 slow, piercing bursts of wind";
    }
}