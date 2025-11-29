package game;

import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Map implements Drawable {
    public static final int MAP_WIDTH = 1500;
    public static final int MAP_HEIGHT = 1500;
    
    private final Tile[][] map;

    public Map() {
        this.map = new Tile[MAP_HEIGHT / Tile.SIZE][MAP_WIDTH / Tile.SIZE];
    }

    public final int getWallX() {
        return MAP_WIDTH - Tile.SIZE;
    }

    public final int getWallY() {
        return MAP_HEIGHT - Tile.SIZE;
    }

    public final void setMap(String filename) {
        
        try (Scanner reader = new Scanner(new File(filename))) {
            int y = 0;
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(" ");
                for (int x = 0; x < data.length; x++) {
                    this.map[y][x] = new Tile(x*Tile.SIZE, y*Tile.SIZE, Integer.parseInt(data[x].substring(1)), data[x].charAt(0));
                }
                y++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND");
        }
    }

    @Override
    public void draw(Graphics g, double offsetX, double offsetY) {
        for (int i = 0; i <= this.getWallY(); i += Tile.SIZE) {
            for (int j = 0; j <= this.getWallX(); j += Tile.SIZE) {
                this.map[i / Tile.SIZE][j / Tile.SIZE].draw(g, offsetX, offsetY);
            }
        }
    }
    
    public void checkProjectiles(ArrayList<Projectile> projectiles) {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile p = projectiles.get(i);
            Tile t = this.returnWallCollided(p.getCenterX(), p.getCenterY(),
                    p.getCollisionRadius() + Tile.COLLISION_CUSHION*2);
            //offset collision cushion, *2 because we want +cushion for radius, but this takes in diameter (size is always diameter)
            //we only want the center half of the projectile to trigger a wall collision (like nita peeking)
            if (t != null) {
                //no splitting (like if gene shoots a wall)
                t.getDamaged(projectiles.remove(i).getDamage(), p.getColor());
                if (p.splitsOnWall()) {//if they split ON IMPACT, then they can split off a wall. Otherwise, if they only split from range, they dont split
                    Tools.addProjectiles(projectiles, p.split());
                }
            }
        }
    }

    public Tile[][] getMap() {
        return this.map;
    }

    public Tile returnWallCollided(double centerX, double centerY, int size) {
        //only returns solid tiles that are not dead
        //doesn't work for only ground tiles
        double lowestX = centerX - (size / 2);
        if (lowestX < 0) {
            lowestX = 0;
        }
        double highestX = centerX + (size / 2);
        if (highestX > MAP_WIDTH) {
            highestX = MAP_WIDTH; //prevent > 750
        }
        double lowestY = centerY - (size / 2);
        if (lowestY < 0) {
            lowestY = 0;
        }
        double highestY = centerY + (size / 2);
        if (highestY > MAP_HEIGHT) {
            highestY = MAP_HEIGHT; //prevent > 550
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

    public Tile returnTile(double centerX, double centerY){
        return this.map[(int)(centerY/Tile.SIZE)][(int)(centerX/Tile.SIZE)];
    }
    
    public boolean inMap(double centerX, double centerY) {
        //returns true if in map
        return centerX >= 0
                && centerX <= MAP_WIDTH
                && centerY >= 0
                && centerY <= MAP_HEIGHT;
    }
}