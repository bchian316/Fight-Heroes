public class Level {


    Enemy[] enemies;

    public Level(Enemy[] enemies) {
        this.enemies = enemies;
    }
    
    public Enemy[] getEnemies() {
        return this.enemies;
    }
}