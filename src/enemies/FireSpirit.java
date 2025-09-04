package enemies;

import game.AttackStats;
import game.Projectile;
import game.StatusEffect;
import game.Tools;
import java.awt.Color;
import java.util.ArrayList;

public class FireSpirit extends Enemy {

    public FireSpirit(int x, int y) {
        super("Fire Spirit", x, y, 40, 35, 6, 100, 150, 200, 2);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {

        ArrayList<Projectile> newProjs = new ArrayList<>();

        if (Tools.getDistance(this.getCenterX(), this.getCenterY(), targetX, targetY) <= 65) {
            this.getDamaged(35, new Color(230, 23, 0));

            newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Tools.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                    new AttackStats(
                            new StatusEffect("Burn", -5, 0, 0, 0, 0, 2000,
                                    new Color(230, 23, 0)),
                        25, 120, 1, 10, 2, 10, new Color(230, 23, 0)), null));
            
        }
        
        return newProjs;
    }
    
}