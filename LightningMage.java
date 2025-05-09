
import java.awt.Color;
import java.util.ArrayList;

public class LightningMage extends PlayerType {
    public LightningMage() {
        super("Lightning Mage", 25, 50, 8, 2000, new AttackStats(20, 75, 15, 300, new Color(178, 78, 204)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x + this.getSize()/2.0, y + this.getSize()/2.0, Game.getAngle(x + this.getSize()/2.0, y + this.getSize()/2.0, targetX, targetY), this.getAttackStats()));
        return newProjs;
    }

}