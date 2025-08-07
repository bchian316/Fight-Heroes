import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.awt.Color;
public abstract class Entity implements CanAttack, HasHealth, Drawable {

    private static final int STATUS_EFFECT_CUSHION = 45; //distance between status effect centers (size is 30)
    private static final int STATUS_EFFECT_OFFSET = 20;
    //move static methods from Game into here
    private final String name;
    private double x, y;
    private int health;
    private final int size;
    private final int maxHealth;
    private final double speed;
    private final int reload;

    private final Image image;

    private final Timer reloadTimer;
    
    private final ArrayList<StatusEffect> statusEffects = new ArrayList<>();
    
    public Entity(String name, String addOn, int x, int y, int size, int health, int speed, int reload) {
        this.name = name;
        //no going out of screen
        this.x = Math.max(Math.min(x - size/2, GameRunner.SCREENWIDTH - GameRunner.WIDTHOFFSET - size - Tile.SIZE), Tile.SIZE + size/2);
        this.y = Math.max(Math.min(y - size/2, GameRunner.SCREENHEIGHT - GameRunner.HEIGHTOFFSET - size - Tile.SIZE), Tile.SIZE + size/2);
        this.size = size;
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.reload = reload;
        this.reloadTimer = new Timer(this.reload, (int) (Math.random() * this.reload));
        this.image = new ImageIcon("assets/" + addOn + "/" + this.name + ".png").getImage()
                .getScaledInstance(this.size, this.size, Image.SCALE_DEFAULT);
    }

    public Entity(String addOn, int x, int y, Mage mage) {
        //for Player
        this(mage.getName(), addOn, x, y, mage.getSize(), mage.getHealth(), mage.getSpeed(), mage.getReload());
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int getSize() {
        return this.size;
    }

    public void setX(double value) {
        this.x = value;
    }

    public void setY(double value) {
        this.y = value;
    }


    public int setBorders(Map map, double dx, double dy, boolean wallCollide) {
        //wallCollide is for wall restriction - if an enemy just spawned, don't do this so they can move through walls
        //returns 0 if no collision, 1 if x collision, 2 if y collision, and 3 if both
        //this implements moving also
        //returns 1 or 2 or 3 if there is collision
        //this method sets the entity to a position where it is not colliding with walls (squares)
        int collisions = 0;
        //should not be stuck in wall rn

        dx = dx + dx * this.speedChange();
        dy = dy + dy * this.speedChange();

        this.x += dx;

        Tile xTile = map.returnWallCollided(this.getCenterX(), this.getCenterY(), this.size);
        if (xTile != null || !map.entityInMap(this)) {
            //the vectorX expression sets the centerX to where it should be, subtract half of size to get the x (top left corner)
            /*double centerX = xTile.getX() + Tile.SIZE + Game.getVectorX(
                    Game.getAngle(xTile.getClosestX(this.getCenterX()), xTile.getClosestY(this.getCenterY()), this.getCenterX(), this.getCenterY()),
                    this.size / 2.0); //where the centerX should be
            System.out.print("CenterX:");
            System.out.println(centerX);
            this.x = centerX - this.size / 2.0; */
            if (wallCollide) {
                this.x -= dx;
            }
            collisions++;
        }

        this.y += dy;

        Tile yTile = map.returnWallCollided(this.getCenterX(), this.getCenterY(), this.size);
        if (yTile != null || !map.entityInMap(this)) { //set y border
            /*double centerY = yTile.getY() + Tile.SIZE + Game.getVectorY(
                    Game.getAngle(yTile.getClosestX(this.getCenterX()), yTile.getClosestY(this.getCenterY()), this.getCenterX(), this.getCenterY()),
                    this.size / 2.0); //where the centerY should be
            System.out.print("CenterY:");
            System.out.println(centerY);
            this.y = centerY - this.size/2.0; */
            if (wallCollide) {
                this.y -= dy; //cancel movement
            }
            collisions += 2;
        }

        //allocate for wall movement reduction
        if (wallCollide) {
            if (xTile == null && yTile != null) {
                //collision on y but not x (left or right)
                if (dx > 0) {
                    this.x += this.speed - dx;
                } else if (dx < 0) {
                    this.x -= this.speed + dx;
                }
            } else if (xTile != null && yTile == null) {
                if (dy > 0) {
                    this.y += this.speed - dy;
                } else if (dy < 0) {
                    this.y -= this.speed + dy;
                }
            }
        }
        return collisions; //if is 2, then the entity is stuck (cuz it is blocked on 2 sides)
        //if is one, then he only hit one wall and is 'sliding'
    }

    
    //for updating (movement)
    public double getSpeed() {
        return this.speed;
    }

    public void reload() { //player uses this for reload reduction
        this.reloadTimer.update(this.reloadChange());
    }

    public void load() {
        this.reloadTimer.beDone();
    }

    public void unload() {
        this.reloadTimer.reset();
    }
    
    public double getReloadFraction() {
        return this.reloadTimer.doneFraction();
    }
    public double getHealthFraction() {
        return ((double) this.health) / ((double) this.maxHealth);
    }

    public void fullHeal() {
        this.health = this.maxHealth;
    }

    public void heal(int health) {
        if (health > 0) {
            //only adds health, not for regen purposes
            this.health += health + (int)(this.regenChange() * health);
            if (this.health > this.maxHealth) {
                this.health = this.maxHealth;
            }
        }
    }

    @Override
    public boolean isDead() {
        return this.health <= 0;
    }

    @Override
    public final double getCenterX() {
        return this.x + this.size / 2.0;
    }

    @Override
    public final double getCenterY() {
        return this.y + this.size / 2.0;
    }

    @Override
    public DamageCounter getDamaged(int damage, Color c) {
        //cannot heal when using this method
        int damageDealt = damage + (int) (this.defenseChange() * damage);
        if (damageDealt >= 0) { //max the damage change
            this.health -= damageDealt;
            return new DamageCounter(this.getCenterX(), this.getCenterY(), damageDealt, c);
        }
        return null;
    }

    @Override
    public boolean isLoaded() {
        return this.reloadTimer.isDone();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.image, (int) this.x, (int) this.y, null);
        //draw health bars by overriding
        this.drawStatusEffects(g);
    }

    @Override
    public abstract ArrayList<Projectile> attack(double targetX, double targetY);

    @Override
    public boolean isHit(Projectile p) {
        if (this.isDead()) {
            return false;
        }
        return Game.getDistance(p.getCenterX(), p.getCenterY(), this.getCenterX(),
                this.getCenterY()) < (p.getSize() + this.size) / 2.0;
    }

    //StatusEffect methods
    
    private void drawStatusEffects(Graphics g) {
        //for 1 item, we want 0
        //2 item: -0.5, 0.5
        //3 item: -1, 0, 1
        int index = 0;
        for (double i = -(this.statusEffects.size()-1)/2.0; i <= (this.statusEffects.size()-1)/2.0; i++) {
            this.statusEffects.get(index).drawIcon(g, (int)(this.getCenterX() + (i * STATUS_EFFECT_CUSHION)),
                    (int) this.getCenterY() - this.getSize()/2 - Entity.STATUS_EFFECT_OFFSET);
            index++;
        }
    }

    public void addStatusEffect(StatusEffect e) {
        if (e == null) {
            return;
        }
        for (StatusEffect effect : this.statusEffects) {
            if(e.getName().equals(effect.getName())){
                effect.resetTimer(); //reset timer if inflicted with same effect
                return;
            }
        }
        this.statusEffects.add(e);
    }

    private void updateStatusEffects() { //for expiring status effects
        for (int i = this.statusEffects.size() - 1; i >= 0; i--) {
            if (this.statusEffects.get(i).updateAndExpire()) {
                this.statusEffects.remove(i);
            }
        }
    }

    public void clearStatusEffects() { //delete all status effects
        this.statusEffects.clear();
    }

    //statusEffect methods for accumulating total statuseffect impact
    
    private ArrayList<DamageCounter> tickHealthChange() {
        ArrayList<DamageCounter> damageCounters = new ArrayList<>();
        for (StatusEffect e : this.statusEffects) {
            int t = e.getTickHealthChange();
            if (t < 0) {
                damageCounters.add(this.getDamaged(-t, e.getColor()));
            } else if (t > 0) {
                this.heal(t);
            }
        }
        return damageCounters;
    }
    
    private double regenChange() {
        //will be used in the regenerate method
        double counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getRegenChange();
        }
        return counter;
    }

    private double defenseChange() { //will be a percentage
        double counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getDefenseChange();
        }
        return counter;
    }

    private double speedChange() { //will be a percentage, maximum 1
        //returns something like 0.8, 80% speed buff
        double counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getSpeedChange();
        }
        return counter;
    }

    private double reloadChange() { //will be a percentage, maximum 1
        double counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getReloadChange();
        }
        return counter;
    }

    public ArrayList<DamageCounter> update() {
        this.reload();
        this.updateStatusEffects();
        return this.tickHealthChange();
    }
}