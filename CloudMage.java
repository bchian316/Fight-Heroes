
import java.awt.Color;
import java.util.ArrayList;

public class CloudMage extends Mage {
    public CloudMage() {
        super("Cloud Mage", 65, 80, 8, 4, 750, 10000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(x, y, targetX, targetY);
        newProjs.add(new Projectile(x, y, angle,
                new AttackStats(15, 35, 15, 300, -1, 25, new Color(255, 255, 255), false,
                new AttackStats(15, 35, 20, 300, -1, 25, new Color(255, 255, 255))),
                (x1, y1, angle1, splitStats) -> createMoreProjectiles(x1, y1, angle1, splitStats)));
        return newProjs;
    }

    //to spawn split shots, needs a specific set of stats
    //MAKE SURE THAT THE OFFSET IS OFF OF THE PROJECTILE NOT THE PLAYER
    public ArrayList<Projectile> createMoreProjectiles(double x, double y, double angle,
            AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y, angle + Math.PI, splitStats));

        return newProjs;
    }

    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY) {
        p.heal(15);
        p.addStatusEffect(new StatusEffect("Constitution", 0, 0.9, 0, 0, 0, 6000, new Color(111, 255, 79)));
        return new ArrayList<>();
    }
    
    @Override
    public String toString() {
        return super.toString() + ": Fire a returning cloud";
    }
}