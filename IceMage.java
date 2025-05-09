
import java.awt.Color;
import java.util.ArrayList;

public class IceMage extends PlayerType {
    public IceMage() {
        super("Ice Mage", 50, 75, 3, 1000, new AttackStats(3, 20, 15, 300, new Color(0, 100, 200)));
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            newProjs.add(new Projectile(x + this.getSize()/2.0, y + this.getSize()/2.0, Game.getAngle(x + this.getSize()/2.0, y + this.getSize()/2.0, targetX, targetY) + Math.toRadians((i-2)*20), this.getAttackStats()));
        }
        return newProjs;
    }

}