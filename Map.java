import java.awt.Graphics;
import java.util.ArrayList;

public class Map implements Drawable{
    private final int WIDTH, HEIGHT;
    
    private final Tile[][] map;

    public Map(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.map = new Tile[this.getWallY() / Tile.SIZE + 1][this.getWallX() / Tile.SIZE + 1];
    }

    public final int getWallX() {
        return this.WIDTH - Tile.SIZE - GameRunner.WIDTHOFFSET;
    }

    public final int getWallY() {
        return this.HEIGHT - Tile.SIZE - GameRunner.HEIGHTOFFSET;
    }
    
    public final void setMap(Tile[] walls) {
        //called every new level - takes custom walls
        for (int i = 0; i <= this.getWallY(); i += Tile.SIZE) {
            for (int j = 0; j <= this.getWallX(); j += Tile.SIZE) {
                if (i == 0 || j == 0 || i == this.getWallY() || j == this.getWallX()) {//sets unbreakable walls in border
                    this.map[i / Tile.SIZE][j / Tile.SIZE] = new Tile(j, i);
                } else {
                    this.map[i / Tile.SIZE][j / Tile.SIZE] = new Tile(j, i, 0);
                }
                this.map[i / Tile.SIZE][j / Tile.SIZE].setImage();
            }
        }
        for (Tile t : walls) {
            this.map[t.getY() / Tile.SIZE][t.getX() / Tile.SIZE] = t;
            t.setImage();
        }
    }

    @Override
    public void draw(Graphics g) {
        for (int i = 0; i <= this.getWallY(); i += Tile.SIZE) {
            for (int j = 0; j <= this.getWallX(); j += Tile.SIZE) {
                this.map[i / Tile.SIZE][j / Tile.SIZE].draw(g);
            }
        }
    }
    
    public void checkProjectiles(ArrayList<Projectile> projectiles) {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile p = projectiles.get(i);
            Tile t = this.returnWallCollided(p.getCenterX(), p.getCenterY(),
                    2 + Tile.COLLISION_CUSHION*2);
            //offset collision cushion, *2 because we want +cushion for radius, but this takes in diameter (size is always diameter)
            //we only want the center of the projectile to trigger a wall collision (like nita peeking)
            if (t != null) {
                //no splitting (like if gene shoots a wall)
                t.getDamaged(projectiles.remove(i).getDamage());
            }
        }
    }

    public Tile[][] getMap() {
        return this.map;
    }
    public Tile returnWallCollided(double centerX, double centerY, int size) {
        //doesn't work for only ground tiles
        double lowestX = centerX - (size / 2);
        if (lowestX < 0) {
            lowestX = 0;
        }
        double highestX = centerX + (size / 2);
        if (highestX > GameRunner.SCREENWIDTH - Tile.SIZE) {
            highestX = GameRunner.SCREENWIDTH - Tile.SIZE; //prevent > 750
        }
        double lowestY = centerY - (size / 2);
        if (lowestY < 0) {
            lowestY = 0;
        }
        double highestY = centerY + (size / 2);
        if (highestY > GameRunner.SCREENHEIGHT - Tile.SIZE) {
            highestY = GameRunner.SCREENHEIGHT - Tile.SIZE; //prevent > 550
        }
        //lowestXY and highestXY will be coords - check all walls between these ranges
        for (int i = (int) (lowestY / Tile.SIZE); i < highestY / Tile.SIZE; i++) {//rows first
            for (int j = (int) (lowestX / Tile.SIZE); j < highestX / Tile.SIZE; j++) {
                //i and j will be small numbers, not coords
                Tile currentTile = this.map[i][j];
                if (!currentTile.isDead()
                        && currentTile.circleCollided(centerX, centerY, size)) {
                    return currentTile;
                }
            }
        }
        return null;
    }
}