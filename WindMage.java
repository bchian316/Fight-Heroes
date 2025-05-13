
import java.awt.Color;
import java.util.ArrayList;

public class WindMage extends Mage {
    public WindMage() {
        super("Wind Mage", 60, 75, 10, 1500, new AttackStats(7, 40, 5, 200, new Color(125, 125, 125)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) + Math.toRadians(-15), this.getAttackStats()));
        newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) + Math.toRadians(15), this.getAttackStats()));
        return newProjs;
    }

}