
import java.awt.Color;
import java.util.ArrayList;

public class ExplodyMage extends Mage {
    public ExplodyMage() {
        super("Explody Mage", 80, 125, 6, 1000, new AttackStats(10, 25, 10, 300, new Color(227, 84, 18), true,
                new AttackStats(40, 75, 1, 10, new Color(194, 48, 0))));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY), this.getAttackStats(),
                (x1, y1, targetX1, targetY1, splitStats) -> createMoreProjectiles(x1, y1, targetX1, targetY1, splitStats)));
        return newProjs;
    }

    //to spawn split shots, needs a specific set of stats
    //MAKE SURE THAT THE OFFSET IS OFF OF THE PROJECTILE NOT THE PLAYER
    public ArrayList<Projectile> createMoreProjectiles(double x, double y, double targetX, double targetY, AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        
        newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY), splitStats));
        
        return newProjs;
    }

}