package enemies;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import game.DamageCounter;
import game.Drawable;
import game.Entity;
import game.Game;
import game.Map;
import game.Projectile;
import game.Tile;
import game.Timer;

public abstract class Enemy extends Entity {

    private static final int MAX_MANUEVER_DISTANCE = Tile.SIZE * 4;

    private static final int BAROFFSETY = 5;
    private static final int HEALTHBARHEIGHT = 10;
    private static final Color HEALTHBAR_BGCOLOR = new Color(0, 0, 0);
    private static final Color HEALTHBAR_FGCOLOR = new Color(255, 0, 0); //display green over red

    private final int power;

    private double moveTargetX, moveTargetY;

    private boolean justSpawned = true;

    private final int passiveRange; //should always stay within this range

    private final Timer regenTimer;
    private final int regen; //how much health it heals and how often

    private boolean maneuvering = false;

    public Enemy(String name, int x, int y, int size, int health, int speed, int reload, int passiveRange, int regen,
            int regenTick, int power) {
        super(name, "enemies", x, y, size, health, speed, reload);

        this.passiveRange = passiveRange; //even stationary enemies should have >0 passive range to move out of walls

        this.regen = regen;
        this.regenTimer = new Timer(regenTick, (int) (Math.random() * regenTick));

        this.power = power; //also unique to enemies - a rating for their power to create rooms

        this.moveTargetX = this.getCenterX();
        this.moveTargetY = this.getCenterY();
    }
    
    public Enemy(String name, int x, int y, int size, int health, int speed, int reload, int passiveRange, int power) {
        this(name, x, y, size, health, speed, reload, passiveRange, 0, 0, power);
    }

    private boolean isWiggling(int collisions) {
        return (collisions == 1 && Math.abs(this.getCenterY() - this.moveTargetY) <= this.getSpeed())
                || (collisions == 2 && Math.abs(this.getCenterX() - this.moveTargetX) <= this.getSpeed());
    }

    //so it can't be overridden
    private void setMoveTarget(Map map, double playerX, double playerY) {
        //manuever if hit wall, no manuever if pursuing player, can't select space in wall
        do {
            double randAngle = Game.getRandomAngle(); //in radians
            if (this.maneuvering) {
                double randMagnitude = Math.random() * Enemy.MAX_MANUEVER_DISTANCE;
                this.moveTargetX = (int) (Game.getVectorX(randAngle, randMagnitude) + this.getCenterX());
                this.moveTargetY = (int) (Game.getVectorY(randAngle, randMagnitude) + this.getCenterY());
            }
            double randMagnitude = Math.random() * this.passiveRange;
            this.moveTargetX = (int) (Game.getVectorX(randAngle, randMagnitude) + playerX);
            this.moveTargetY = (int) (Game.getVectorY(randAngle, randMagnitude) + playerY);
        } while (map.returnWallCollided(this.moveTargetX, this.moveTargetY, 1) != null);
    }

    @Override
    public void drawHealthBar(Graphics g, double offsetX, double offsetY) {
        //call in draw method
        g.setColor(HEALTHBAR_BGCOLOR);
        g.fillRect((int)(this.getX() - offsetX), (int)(this.getY() + this.getSize() + BAROFFSETY - offsetY), this.getSize(), HEALTHBARHEIGHT);
        g.setColor(HEALTHBAR_FGCOLOR);
        g.fillRect((int)(this.getX() - offsetX), (int)(this.getY() + this.getSize() + BAROFFSETY - offsetY),
                (int) (this.getSize() * this.getHealthFraction()), HEALTHBARHEIGHT);
    }

    @Override
    public void draw(Graphics g, double offsetX, double offsetY) {
        super.draw(g, offsetX, offsetY);
        if (Drawable.inScreen(offsetX, offsetY, this.getX(), this.getY(), this.getSize(), this.getSize())) {
            this.drawHealthBar(g, offsetX, offsetY);
            //g.fillOval((int)(this.moveTargetX-5), (int)(this.moveTargetY-5), 10, 10);
        }
    }

    @Override
    public abstract ArrayList<Projectile> attack(double targetX, double targetY); //spawn projectiles

    private boolean stuckInWall(Map map) {
        //true if we are stuck
        return map.returnWallCollided(this.getCenterX(), this.getCenterY(), this.getSize()) != null;
    }

    private void flowFromWall(Map map) {
        while (this.stuckInWall(map)) {
            //spawned in wall
            double randAngle = Game.getRandomAngle();
            
            double dx = Game.getVectorX(randAngle, Tile.SIZE);
            double dy = Game.getVectorY(randAngle, Tile.SIZE);
            
            this.setBorders(map, dx, dy, false);
        }
        //if enemy is in wall, enter flowstate and move away from wall; keep moving until not in wall
    }

    public ArrayList<DamageCounter> update(double playerX, double playerY, ArrayList<Projectile> enemyProjectiles, Map map) {

        if (this.justSpawned) {
            this.flowFromWall(map);
            //enemy is now guaranteed to not be in wall
            this.justSpawned = false;
        }

        //set directions

        double dx = Game.getVectorX(
                Game.getAngle(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY),
                this.getSpeed());
        double dy = Game.getVectorY(
                Game.getAngle(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY),
                this.getSpeed());

        int collisions = this.setBorders(map, dx, dy, true);
        if (collisions == 3 || this.isWiggling(collisions)) {
            //is collliding with 2 wall or is wiggling
            if (!this.maneuvering) {
                this.maneuvering = true;
            }
            setMoveTarget(map, playerX, playerY);
        }
        if (Game.getDistance(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY) < this.getSize()
                / 2.0) {
            //if arrived
            this.maneuvering = false;
            this.setMoveTarget(map, playerX, playerY);
        }
        if (!this.maneuvering
                && Game.getDistance(this.moveTargetX, moveTargetY, playerX, playerY) > this.passiveRange) {
            //player has left the spot outside of passive range, find new spot
            //ONLY DO THIS IF NOT MANUEVERING
            this.setMoveTarget(map, playerX, playerY);
        }

        //attack if loaded
        if (this.isLoaded()) {
            Game.addProjectiles(enemyProjectiles, this.attack(playerX, playerY));
            this.unload();
        }

        if (this.regenTimer.update()) {
            this.heal(this.regen);
            this.regenTimer.reset();
        }

        return super.update();
    }
    
    public int getPower() {
        return this.power;
    }

}
