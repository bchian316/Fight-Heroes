
import java.awt.Color;
import java.util.ArrayList;

public class FireMage extends PlayerType {
    public FireMage() {
        super("Fire Mage", 50, 100, 5, 500, new AttackStats(5, 20, 10, 100, new Color(255, 0, 0)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x + this.getSize()/2, y + this.getSize()/2, targetX, targetY, this.getAttackStats()));
        return newProjs;
    }

}