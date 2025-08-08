package game;

import java.util.ArrayList;

public interface SplitProjectileInterface {

    public abstract ArrayList<Projectile> split(double x, double y, double angle, AttackStats splitStats);

}