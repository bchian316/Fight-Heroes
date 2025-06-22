
import java.awt.Color;
import java.util.ArrayList;

public class PlasmaMage extends Mage {
    public PlasmaMage() {
        super("Plasma Mage", 50, 90, 10, 5, 700, new AttackStats(13, 25, 10, 200, 1, 25, new Color(255, 115, 115), true,
                new AttackStats(8, 15, 8, 75, 1, 5, new Color(255, 115, 115))));
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
    public ArrayList<Projectile> createMoreProjectiles(double x, double y, double angle, AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            //split is independent of past angle (like spike)
            newProjs.add(new Projectile(x, y, Math.toRadians(i*60.0), splitStats));
        }
        return newProjs;
    }
    @Override
    public String toString() {
        return super.toString() + ": Fire a sphere that explodes into multiple projectiles";
    }
}