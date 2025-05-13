
import java.awt.Color;
import java.util.ArrayList;

public class NatureMage extends Mage {
    public NatureMage() {
        super("Nature Mage", 80, 125, 6, 1000, new AttackStats(15, 40, 15, 100, new Color(0, 200, 0), false,
                new AttackStats(10, 20, 15, 100, new Color(0, 255, 0))));
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