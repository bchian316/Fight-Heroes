
import java.awt.Color;
import java.util.ArrayList;

public class PulseMage extends Mage {
    private static final int ATTKOFFSET = 50;
    public PulseMage() {
        super("Pulse Mage", 50, 50, 4, 5, 1000, new AttackStats(12, 20, 15, 200, 2, new Color(0, 115, 0)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(x, y, targetX, targetY);
        newProjs.add(new Projectile(x + PulseMage.ATTKOFFSET, y, angle, this.getAttackStats()));
        newProjs.add(new Projectile(x - PulseMage.ATTKOFFSET, y, angle, this.getAttackStats()));
        newProjs.add(new Projectile(x, y + PulseMage.ATTKOFFSET, angle, this.getAttackStats()));
        newProjs.add(new Projectile(x, y - PulseMage.ATTKOFFSET, angle, this.getAttackStats()));
        newProjs.add(new Projectile(x, y, angle, this.getAttackStats()));
        return newProjs;
    }

}