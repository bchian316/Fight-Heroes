
import java.awt.Color;
import java.util.ArrayList;

public class LightningMage extends Mage {
    private static final int SPECIAL_SPREAD = 35; //pixels between projectiles
    private static final int SPECIAL_ANGLE = 60;

    public LightningMage() {
        super("Lightning Mage", 35, 35, 20, 7, 2500, 3000);
    }

    @Override
    public ArrayList<Projectile> createProjectiles(double x, double y, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY), new AttackStats(60, 75, 20, 350, 2, 20, new Color(178, 78, 204))));
        return newProjs;
    }
    @Override
    public ArrayList<Projectile> special(Player p, double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(p.getCenterX(), p.getCenterY(), targetX, targetY);
        for (int i = -1; i <= 1; i++) {
            newProjs.add(new Projectile(
                    p.getCenterX() + i * Game.getVectorX(angle + Math.PI / 2, SPECIAL_SPREAD),
                    p.getCenterY() + i * Game.getVectorY(angle + Math.PI / 2, SPECIAL_SPREAD),
                    angle - Math.toRadians(SPECIAL_ANGLE/2),
                    new AttackStats(20, 20, 10, 100, 3, 10, new Color(148, 48, 164), false,
                    new AttackStats(20, 20, 10, 200, 3, 10, new Color(148, 48, 164), false,
                    new AttackStats(20, 20, 10, 100, 3, 10, new Color(148, 48, 164)))),
                    (x1, y1, angle1, splitStats) -> special2(x1, y1, angle1, splitStats)));
        }
        return newProjs;
    }

    public ArrayList<Projectile> special2(double x, double y, double angle,
            AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y, angle + Math.toRadians(SPECIAL_ANGLE), splitStats,
                (x1, y1, angle1, splitStats1) -> special3(x1, y1, angle1, splitStats1)));

        return newProjs;
    }

    public ArrayList<Projectile> special3(double x, double y, double angle,
            AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        newProjs.add(new Projectile(x, y, angle - Math.toRadians(SPECIAL_ANGLE), splitStats));

        return newProjs;
    }

    @Override
    public String toString() {
        return super.toString() + ": Fires a massive blast of lightning";
    }

}