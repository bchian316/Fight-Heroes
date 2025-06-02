
import java.awt.Color;
import java.util.ArrayList;

public class WaterMage extends Mage {
    public WaterMage() {
        super("Water Mage", 50, 75, 5, 7, 650, new AttackStats(15, 25, 15, 150, 1, 20, new Color(0, 25, 217), false,
                new AttackStats(8, 10, 15, 125, 1, 10, new Color(0, 25, 217))));
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
        
        for (int i = 0; i < 9; i++) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) - Math.toRadians((i-4) * 15.0), splitStats));
        }
        
        
        return newProjs;
    }

}