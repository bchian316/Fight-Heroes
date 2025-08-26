package enemies;

import game.Game;
import game.AttackStats;
import game.Projectile;
import game.StatusEffect;

import java.awt.Color;
import java.util.ArrayList;

public class Witch extends Enemy {
    public static final int ATTKOFFSET = 35;
    public Witch(int x, int y) {
        super("Witch", x, y, 50, 50, 1, 1250, 450, 4);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        StatusEffect randomEffect;
        int rand = (int) (Math.random() * 3);
        randomEffect = switch (rand) {
            case 0 -> new StatusEffect("Poison", -5, 0, 0, 0, 0, 2500, new Color(101, 161, 48));
            case 1 -> new StatusEffect("Freeze", 0, 0, 0, -0.5, 0, 1000, new Color(0, 140, 230));
            default -> new StatusEffect("Burn", -8, 0, 0, 0, 0, 1500, new Color(230, 23, 0));
        }; //5 damage every 3 seconds for 10 seconds
        //slow by half for 5 seconds
        //reduce attack damage by 2 for 8 seconds
        newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(), Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY),
                new AttackStats(randomEffect, 0, 25, 8, 300, 1, 20, randomEffect.getColor()), null));
        
        return newProjs;
    }
    
}