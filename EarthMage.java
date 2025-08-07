
import java.awt.Color;
import java.util.ArrayList;

public class EarthMage extends Mage {
    public EarthMage() {
        super("Earth Mage", 75, 150,25, 7, 650, 5000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) + Math.toRadians(i * 36),
                    new AttackStats(7, 40, 20, 200, 1, 15, new Color(125, 78, 16))));
        }
        return newProjs;
    }

    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY) {
        p.addStatusEffect(new StatusEffect("Toughness", 0, 0, -0.7, 0, 0, 3500, new Color(200, 200, 200)));
        return new ArrayList<>();
    }
    
    
    @Override
    public String toString() {
        return super.toString() + ": Fire a blast of earth in all directions";
    }

}