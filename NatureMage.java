
import java.awt.Color;
import java.util.ArrayList;

public class NatureMage extends Mage {
    public NatureMage() {
        super("Nature Mage", 80, 150, 20, 6, 750,
            new AttackStats(35, 50, 10, 100, -1, 20, new Color(0, 200, 0), false,
                new AttackStats(25, 30, 12, 100, -1, 10, new Color(0, 255, 0), false,
                    new AttackStats(15, 10, 15, 100, -1, 5, new Color(0, 255, 0)))));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(x, y, targetX, targetY);
        newProjs.add(new Projectile(x, y, angle, this.getAttackStats(),
                (x1, y1, angle1, splitStats) -> createMoreProjectiles(x1, y1, angle1, splitStats)));
        return newProjs;
    }

    //to spawn split shots, needs a specific set of stats
    //MAKE SURE THAT THE OFFSET IS OFF OF THE PROJECTILE NOT THE PLAYER
    public ArrayList<Projectile> createMoreProjectiles(double x, double y, double angle,
            AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();

        newProjs.add(new Projectile(x, y, angle, splitStats, 
                    (x1, y1, angle1, splitStats1) -> createEvenMoreProjectiles(x1, y1, angle1, splitStats1)));

        return newProjs;
    }
    
    public ArrayList<Projectile> createEvenMoreProjectiles(double x, double y, double angle, AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        
        newProjs.add(new Projectile(x, y, angle, splitStats));
        
        return newProjs;
    }
    @Override
    public String toString() {
        return super.toString() + ": Fire a shrinking projectile that can hit enemies multiple times";
    }
}