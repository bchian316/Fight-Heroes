
import java.awt.Color;
import java.util.ArrayList;

public class Mummy extends Enemy {
    public static final int ATTKOFFSET = 35;
    public Mummy(int x, int y) {
        super("Mummy", x, y, 75, 100, 0, 750, 0, 0, new AttackStats(5, 30, 12, 400, 1, new Color(145, 104, 0)));
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        double angle = Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY);
        for (int i = 0; i < 3; i++) {
            newProjs.add(new Projectile(this.getCenterX() + Game.getVectorX(angle + Math.PI/2, Mummy.ATTKOFFSET * (i-1)), this.getCenterY() + Game.getVectorY(angle + Math.PI/2, Mummy.ATTKOFFSET * (i-1.5)), angle, this.getAttackStats()));
        }
        return newProjs;
    }
    
}