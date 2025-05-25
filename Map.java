import java.awt.Graphics;
import java.util.ArrayList;

public class Map implements Drawable{
    private final int WIDTH, HEIGHT;
    
    private final Tile[][] map;

    public Map(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.map = new Tile[this.getWallY() / Tile.IMAGE_SIZE + 1][this.getWallX() / Tile.IMAGE_SIZE + 1];
    }

    public final int getWallX() {
        return this.WIDTH - Tile.IMAGE_SIZE - GameRunner.WIDTHOFFSET;
    }

    public final int getWallY() {
        return this.HEIGHT - Tile.IMAGE_SIZE - GameRunner.HEIGHTOFFSET;
    }
    
    public final void setMap(Tile[] walls) {
        //called every new level - takes custom walls
        for (int i = 0; i <= this.getWallY(); i += Tile.IMAGE_SIZE) {
            for (int j = 0; j <= this.getWallX(); j += Tile.IMAGE_SIZE) {
                if (i == 0 || j == 0 || i == this.getWallY() || j == this.getWallX()) {//sets unbreakable walls in border
                    this.map[i / Tile.IMAGE_SIZE][j / Tile.IMAGE_SIZE] = new Tile(j, i, 0, false);
                } else {
                    this.map[i / Tile.IMAGE_SIZE][j / Tile.IMAGE_SIZE] = new Tile(j, i, 0, true);
                }
                this.map[i / Tile.IMAGE_SIZE][j / Tile.IMAGE_SIZE].setImage();
            }
        }
        for (Tile t : walls) {
            this.map[t.getY() / Tile.IMAGE_SIZE][t.getX() / Tile.IMAGE_SIZE] = t;
            t.setImage();
        }
    }

    @Override
    public void draw(Graphics g) {
        for (int i = 0; i <= this.getWallY(); i += Tile.IMAGE_SIZE) {
            for (int j = 0; j <= this.getWallX(); j += Tile.IMAGE_SIZE) {
                this.map[i / Tile.IMAGE_SIZE][j / Tile.IMAGE_SIZE].draw(g);
            }
        }
    }
    
    public void checkProjectiles(ArrayList<Projectile> projectiles) {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile p = projectiles.get(i);
            Tile t = Game.returnWallCollided(this.map, p.getCenterX(), p.getCenterY(),
                    p.getSize() + Tile.COLLISION_CUSHION);
            //offset collision cushion
            if (t != null) {
                //no splitting
                t.getDamaged(projectiles.remove(i).getDamage());
            }
        }
    }

    public Tile[][] getMap() {
        return this.map;
    }
}