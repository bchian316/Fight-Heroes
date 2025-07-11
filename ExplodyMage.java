
import java.awt.Color;
import java.util.ArrayList;

public class ExplodyMage extends Mage {
    private static final int EXPLOSION_OFFSET = 10;
    public ExplodyMage() {
        super("Explody Mage", 60, 125, 8, 6, 1000, new AttackStats(5, 25, 10, 225, 1, 25, new Color(227, 84, 18), true,
                new AttackStats(30, 125, 1, 15, -1, 1, new Color(194, 48, 0))));
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
        newProjs.add(new Projectile(x - Game.getVectorX(angle, ExplodyMage.EXPLOSION_OFFSET),
                y - Game.getVectorY(angle, ExplodyMage.EXPLOSION_OFFSET), angle, splitStats));

        return newProjs;
    }
    
    @Override
    public String toString() {
        return super.toString() + ": Fire highly explosive spheres";
    }
}