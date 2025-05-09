import java.awt.Graphics;

public class Projectile implements Drawable{
    private double x, y;
    private final double velX, velY;
    private final AttackStats attk;
    private double distanceTraveled = 0;

    public Projectile(double x, double y, double targetX, double targetY, AttackStats attk) {
        //x and y should be the center of the player, not his actual coords
        this.x = x - attk.size()/2;
        this.y = y - attk.size()/2;
        this.attk = attk;

        //temporary variables to calculate projectile velocity
        double diffX = (targetX - this.x);
        double diffY = (targetY - this.y);
        double magnitude = Math.sqrt(diffX * diffX + diffY * diffY);

        if (magnitude != 0) {
            diffX /= magnitude;
            diffY /= magnitude;
            //distance will now be 1
            this.velX = diffX * this.attk.speed();
            this.velY = diffY * this.attk.speed();
            //scale velocities up to speed
        } else {
            //if player clicks on exact spot, shoot up
            this.velX = -this.attk.speed();
            this.velY = 0;
        }
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(this.attk.color());
        g.fillOval((int)this.x, (int)this.y, this.attk.size(), this.attk.size());
    }
    
    public void update() {
        this.x += this.velX;
        this.y += this.velY;
        this.distanceTraveled += Math.sqrt(this.velX * this.velX + this.velY * this.velY);
    }

    public boolean shouldKillSelf() {
        //returns true if we should delete this projectile
        if (this.distanceTraveled > this.attk.range()) {
            return true;
        }
        return this.x < 0 || this.x > GameRunner.SCREENWIDTH || this.y < 0 || this.y > GameRunner.SCREENHEIGHT;
    }
}