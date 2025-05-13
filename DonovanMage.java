
import java.awt.Color;
import java.util.ArrayList;

public class DonovanMage extends Mage {
    public DonovanMage() {
        super("Donovan Mage", 75, 120, 10, 2000, new AttackStats(20, 30, 12, 100, new Color(214, 15, 15), false,
                new AttackStats(10, 20, 12, 100, new Color(0, 0, 200), false,
                        new AttackStats(5, 10, 12, 100, new Color(0, 200, 0)))));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) + Math.toRadians(60.0*i), this.getAttackStats(),
                    (x1, y1, targetX1, targetY1, splitStats) -> createMoreProjectiles(x1, y1, targetX1, targetY1, splitStats)));
        }
        return newProjs;
    }

    //to spawn split shots, needs a specific set of stats
    //MAKE SURE THAT THE OFFSET IS OFF OF THE PROJECTILE NOT THE PLAYER
    public ArrayList<Projectile> createMoreProjectiles(double x, double y, double targetX, double targetY, AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) - Math.toRadians((i-2) * 15.0), splitStats, 
                    (x1, y1, targetX1, targetY1, splitStats1) -> createEvenMoreProjectiles(x1, y1, targetX1, targetY1, splitStats1)));
        }
        return newProjs;
    }

    public ArrayList<Projectile> createEvenMoreProjectiles(double x, double y, double targetX, double targetY, AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        
        for (int i = 0; i < 3; i++) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) - Math.toRadians((i-1) * 15.0), splitStats));
        }
        return newProjs;
    }
}