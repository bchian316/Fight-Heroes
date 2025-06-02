
import java.awt.Color;
import java.util.ArrayList;

public class Ghoul extends Enemy {

    public Ghoul(int x, int y) {
        super("Ghoul", x, y, 65, 90, 4, 1500, 450, new AttackStats(9, 30, 6, 250, 2, 7, new Color(31, 122, 240)));
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) + Math.toRadians((i-3)*12),
                    this.getAttackStats()));
        }
        return newProjs;
    }
    
}