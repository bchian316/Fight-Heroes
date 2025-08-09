package game;

import java.util.ArrayList;
import java.util.HashSet;

public interface SplitProjectileInterface {

    public abstract ArrayList<Projectile> split(double x, double y, double angle, AttackStats splitStats,
            HashSet<HasHealth> hitObjects);

}