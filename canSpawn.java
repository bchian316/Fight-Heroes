import java.util.ArrayList;

public interface canSpawn {
    public abstract ArrayList<Enemy> spawn(double pLayerX, double playerY);

    public abstract boolean spawnLoaded();
}