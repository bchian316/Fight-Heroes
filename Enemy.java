
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Enemy extends Entity {
    private static final int MAX_MANUEVER_DISTANCE = Tile.SIZE * 3;

    private static final int BAROFFSETY = 5;
    private static final int HEALTHBARHEIGHT = 10;
    private static final Color HEALTHBAR_BGCOLOR = new Color(0, 0, 0);
    private static final Color HEALTHBAR_FGCOLOR = new Color(255, 0, 0); //display green over red

    private double moveTargetX, moveTargetY;

    private boolean justSpawned = true;

    private final int passiveRange; //should always stay within this range

    private boolean maneuvering = false;

    public Enemy(String name, int x, int y, int size, int health, int speed, int reload, int passiveRange,
            AttackStats attk) {
        super(name, "monsters", x, y, size, health, speed, reload, attk);

        this.passiveRange = passiveRange; //even stationary enemies should have >0 passive range to move out of walls

        this.moveTargetX = this.getCenterX();
        this.moveTargetY = this.getCenterY();
    }

    private boolean isWiggling(int collisions) {
        return (collisions == 1 && Math.abs(this.getCenterY() - this.moveTargetY) <= this.getSpeed())
                || (collisions == 2 && Math.abs(this.getCenterX() - this.moveTargetX) <= this.getSpeed());
    }

    //so it can't be overridden
    private void setMoveTarget(Map map, double playerX, double playerY) {
        //manuever if hit wall, no manuever if pursuing player, can't select space in wall
        do {
            double randAngle = Math.random() * Math.PI * 2; //in radians
            if (this.maneuvering) {
                double randMagnitude = Math.random() * (Enemy.MAX_MANUEVER_DISTANCE + this.getSize());
                this.moveTargetX = (int) (Game.getVectorX(randAngle, randMagnitude) + this.getCenterX());
                this.moveTargetY = (int) (Game.getVectorY(randAngle, randMagnitude) + this.getCenterY());
                return;
            }
            double randMagnitude = Math.random() * this.passiveRange;
            this.moveTargetX = (int) (Game.getVectorX(randAngle, randMagnitude) + playerX);
            this.moveTargetY = (int) (Game.getVectorY(randAngle, randMagnitude) + playerY);
        } while (map.returnWallCollided(this.moveTargetX, this.moveTargetY, 1) != null);
    }

    @Override
    public void drawHealthBar(Graphics g) {
        //call in draw method
        g.setColor(HEALTHBAR_BGCOLOR);
        g.fillRect((int) this.getX(), (int) this.getY() + this.getSize() + BAROFFSETY, this.getSize(), HEALTHBARHEIGHT);
        g.setColor(HEALTHBAR_FGCOLOR);
        g.fillRect((int) this.getX(), (int) this.getY() + this.getSize() + BAROFFSETY,
                (int) (this.getSize() * this.getHealthFraction()), HEALTHBARHEIGHT);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        this.drawHealthBar(g);
        //g.fillOval((int)(this.moveTargetX-5), (int)(this.moveTargetY-5), 10, 10);
    }

    @Override
    public abstract ArrayList<Projectile> attack(double targetX, double targetY); //spawn projectiles

    private boolean stuckInWall(Map map) {
        //true if we are stuck
        return map.returnWallCollided(this.getCenterX(), this.getCenterY(), this.getSize()) != null;
    }

    public void update(double playerX, double playerY, ArrayList<Projectile> enemyProjectiles, Map map) {

        //set directions

        double dx = Game.getVectorX(
                Game.getAngle(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY),
                this.getSpeed());
        double dy = Game.getVectorY(
                Game.getAngle(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY),
                this.getSpeed());

        if (this.justSpawned && this.stuckInWall(map)) {
            //spawned in wall
            if (this.getSpeed() == 0) {
                dx = Game.getVectorX(
                    Game.getAngle(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY),
                    Tile.SIZE);
                dy = Game.getVectorY(
                    Game.getAngle(this.getCenterX(), this.getCenterY(), this.moveTargetX, this.moveTargetY),
                        Tile.SIZE);
            } else {
                dx *= Tile.SIZE / (double) this.getSpeed();
                dy *= Tile.SIZE / (double) this.getSpeed();
            }
        }

        int collisions = this.setBorders(map, dx, dy, !this.justSpawned);
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

        //reload and attack
        this.reload();
        if (this.isLoaded()) {
            Game.addProjectiles(enemyProjectiles, this.attack(playerX, playerY));
            this.unload();
        }
        if (!this.stuckInWall(map)) {
            this.justSpawned = false;
        }
    }

}