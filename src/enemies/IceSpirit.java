package enemies;

import game.Game;
import game.AttackStats;
import game.Projectile;
import game.StatusEffect;

import java.awt.Color;
import java.util.ArrayList;

public class IceSpirit extends Enemy {

    public IceSpirit(int x, int y) {
        super("Ice Spirit", x, y, 40, 45, 5, 200, 150, 2);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {

        ArrayList<Projectile> newProjs = new ArrayList<>();

        if (Game.getDistance(this.getCenterX(), this.getCenterY(), targetX, targetY) <= 75) {
            this.getDamaged(45, new Color(0, 134, 230));
            for (int i = 0; i < 7; i++) {
                newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY) + i*Math.PI*2/7,
                        new AttackStats(
                                new StatusEffect("Freeze", 0, 0, 0, -0.5, 0, 750,
                                        new Color(0, 134, 230)),
                            10, 30, 8, 100, 1, 10, new Color(0, 140, 230)), null));
                
            }
        }
        
        return newProjs;
    }
    
}