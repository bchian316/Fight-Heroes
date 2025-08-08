package game;

import java.util.ArrayList;

public interface CanAttack {
    //implemented by all players and enemies: spawns bullets
    //playertypes do not "attack"; they createProjectiles - a whole different method that takes in x and y
    public ArrayList<Projectile> attack(double targetX, double targetY);

    public boolean isLoaded();
}