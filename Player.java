import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Set;

public class Player extends Entity {
    public static final int PLAYERSTARTX = GameRunner.SCREENWIDTH/2;
    public static final int PLAYERSTARTY = GameRunner.SCREENHEIGHT - 162;

    private static final int STATUS_EFFECT_OFFSET = 20;
    private static final int BAROFFSET = 5;

    private static final int BIGHEALTHBARHEIGHT = 15;
    private static final int HEALTHBARWIDTH = 50;
    private static final int HEALTHBARHEIGHT = 10;
    private static final Color HEALTHBAR_BGCOLOR = new Color(0, 0, 0);
    private static final Color HEALTHBAR_FGCOLOR = new Color(0, 255, 0); //display green over red
    private static final Color HEALTHBAR_REGENCOLOR = new Color(0, 150, 0); //display green over red

    private static final int BIGRELOADBARWIDTH = 15;
    private static final int RELOADBARWIDTH = 10;
    private static final int RELOADBARHEIGHT = 50;
    private static final Color RELOADBAR_BGCOLOR = new Color(80, 80, 80);
    private static final Color RELOADBAR_MGCOLOR = new Color(150, 150, 150);
    private static final Color RELOADBAR_FGCOLOR = new Color(190, 190, 0); //dark gray, light gray, then yellow

    private final Mage mage; //just for attacking

    private int levelNumber = 0;

    private final int regenRate;
    private double regenTimer = 0; //when to regen
    private static final double REGENTIME = 3000; //3secs for regen
    private double regenTickTimer = 0;
    private static final double REGENTICKTIME = 500;
    private boolean isHealing = false; //if regening

    private final ArrayList<StatusEffect> statusEffects = new ArrayList<>();
    

    public Player(Mage mage) {
        super("mages", Player.PLAYERSTARTX, Player.PLAYERSTARTY, mage);
        this.mage = mage;
        this.regenRate = mage.getRegen();
        this.load();
    }
    
    public void setToStartCoords() {
        this.setX(Player.PLAYERSTARTX - this.getSize()/2);
        this.setY(Player.PLAYERSTARTY - this.getSize()/2);
    }

    public int getLevelNumber() {
        return this.levelNumber;
    }
    
    public void incrementLevelNumber() {
        this.levelNumber++;
    }

    private void regenerate() { //update the regen health
        if (this.isHealing) {
            this.regenTickTimer += Game.updateDelay();
            if (this.regenTickTimer >= REGENTICKTIME) {
                regenTickTimer = 0;
                this.heal((int)(this.regenRate * (1 - this.regenReduction())));
            }
        }
        else if (!this.isHealing) {
            this.regenTimer += Game.updateDelay();
            if (this.regenTimer >= REGENTIME) {
                regenTimer = 0;
                this.isHealing = true;
            }
        }
    }
    
    private void stopHealing() {
        this.isHealing = false;
        this.regenTimer = 0;
    }

    
    @Override
    public void getDamaged(int damage) {
        if (damage > 0) { //for tick damage being 0
            super.getDamaged(damage + (int)(damage*this.defenseReduction()));
            this.stopHealing();
        }
    }

    public void update(Set<Integer> pressedKeys, Map map) {
        double dx = 0;
        double dy = 0;
        if (pressedKeys.contains(KeyEvent.VK_W)) {
            dy -= 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_A)) {
            dx -= 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_S)) {
            dy += 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_D)) {
            dx += 1;
        }
        double magnitude = Game.getDistance(dx, dy);
        if (magnitude != 0) {//prevent division by 0
            dx /= magnitude;
            dy /= magnitude;
            dx *= this.getSpeed() - (this.getSpeed() * this.speedReduction());
            dy *= this.getSpeed() - (this.getSpeed() * this.speedReduction());
            this.setBorders(map, dx, dy, true);
        }

        this.reload(1 - this.reloadReduction());

        this.regenerate();
        
        this.getDamaged(this.tickDamage());

        this.updateStatusEffects();
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        //center x and center y
        this.stopHealing();
        return this.mage.createProjectiles(this.getCenterX(), this.getCenterY(), targetX, targetY);
    }
    @Override
    public void draw(Graphics g) {
        super.draw(g);
        this.drawReloadBar(g);
        this.drawHealthBar(g);
        this.drawBigReloadBar(g);
        this.drawBigHealthBar(g);
        this.drawStatusEffects(g);
    }

    @Override
    public void drawHealthBar(Graphics g) {
        //call in draw method
        g.setColor(HEALTHBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() - Player.HEALTHBARWIDTH/2, (int) this.getY() + this.getSize() + BAROFFSET,
                Player.HEALTHBARWIDTH, Player.HEALTHBARHEIGHT);
        if (this.isHealing) {
            g.setColor(HEALTHBAR_REGENCOLOR);
            g.fillRect((int) this.getCenterX() - Player.HEALTHBARWIDTH/2, (int) this.getY() + this.getSize() + BAROFFSET,
                Player.HEALTHBARWIDTH, Player.HEALTHBARHEIGHT);
        }
        g.setColor(HEALTHBAR_FGCOLOR);
        g.fillRect((int) this.getCenterX() - Player.HEALTHBARWIDTH / 2, (int) this.getY() + this.getSize() + BAROFFSET,
                (int)(Player.HEALTHBARWIDTH * this.getHealthFraction()), Player.HEALTHBARHEIGHT);
    }


    private void drawBigHealthBar(Graphics g) {
        g.setColor(Player.HEALTHBAR_BGCOLOR);
        g.fillRect(0, GameRunner.SCREENHEIGHT - GameRunner.HEIGHTOFFSET, GameRunner.SCREENWIDTH,
                Player.BIGHEALTHBARHEIGHT);
        if (this.isHealing) {
            g.setColor(Player.HEALTHBAR_REGENCOLOR);
            g.fillRect(0, GameRunner.SCREENHEIGHT - GameRunner.HEIGHTOFFSET, GameRunner.SCREENWIDTH,
                Player.BIGHEALTHBARHEIGHT);
        }
        g.setColor(Player.HEALTHBAR_FGCOLOR);
        g.fillRect(0, GameRunner.SCREENHEIGHT - GameRunner.HEIGHTOFFSET, (int)(GameRunner.SCREENWIDTH * this.getHealthFraction()), Player.BIGHEALTHBARHEIGHT);
    }

    private void drawReloadBar(Graphics g) { //to the right of player
        g.setColor(Player.RELOADBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() + this.getSize()/2 + Player.BAROFFSET,
                (int) this.getCenterY() - Player.RELOADBARHEIGHT/2,
                Player.RELOADBARWIDTH, Player.RELOADBARHEIGHT);
        g.setColor(Player.RELOADBAR_MGCOLOR);
        g.fillRect((int) this.getCenterX() + this.getSize()/2 + Player.BAROFFSET,
                (int) this.getCenterY() + Player.RELOADBARHEIGHT/2 - (int)(Player.RELOADBARHEIGHT * this.getReloadFraction()), Player.RELOADBARWIDTH,
                (int) (Player.RELOADBARHEIGHT * this.getReloadFraction()));
        if (this.isLoaded()) {
            g.setColor(Player.RELOADBAR_FGCOLOR);
            g.fillRect((int) this.getCenterX() + this.getSize()/2 + Player.BAROFFSET,
                    (int) this.getCenterY() - Player.RELOADBARHEIGHT/2,
                    Player.RELOADBARWIDTH, Player.RELOADBARHEIGHT);
        }
    }

    private void drawBigReloadBar(Graphics g) {
        g.setColor(Player.RELOADBAR_BGCOLOR);
        g.fillRect(GameRunner.SCREENWIDTH - GameRunner.WIDTHOFFSET, 0, Player.BIGRELOADBARWIDTH,
                GameRunner.SCREENHEIGHT);
        g.setColor(Player.RELOADBAR_MGCOLOR);
        g.fillRect(GameRunner.SCREENWIDTH - GameRunner.WIDTHOFFSET,
                GameRunner.SCREENHEIGHT - (int) (GameRunner.SCREENHEIGHT * this.getReloadFraction()),
                Player.BIGRELOADBARWIDTH,
                (int) (GameRunner.SCREENHEIGHT * this.getReloadFraction()));
        if (this.isLoaded()) {
            g.setColor(Player.RELOADBAR_FGCOLOR);
            g.fillRect(GameRunner.SCREENWIDTH - GameRunner.WIDTHOFFSET, 0, Player.BIGRELOADBARWIDTH,
                    GameRunner.SCREENHEIGHT);
        }
    }

    private void drawStatusEffects(Graphics g) {
        //for 1 item, we want 0
        //2 item: -0.5, 0.5
        //3 item: -1, 0, 1
        int index = 0;
        for (double i = -(this.statusEffects.size()-1)/2.0; i <= (this.statusEffects.size()-1)/2.0; i++) {
            this.statusEffects.get(index).drawIcon(g, (int)(this.getCenterX() + (i * STATUS_EFFECT_CUSHION)),
                    (int) this.getCenterY() - this.getSize()/2 - Player.STATUS_EFFECT_OFFSET);
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
    
    private int tickDamage() {
        int counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getTickDamage();
        }
        return counter;
    }
    
    private double regenReduction() {
        //will be used in the regenerate method
        double counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getRegenReduction();
        }
        return counter;
    }

    private double defenseReduction() { //will be a percentage
        double counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getDefenseReduction();
        }
        return counter;
    }

    private double speedReduction() { //will be a percentage, maximum 1
        //returns something like 0.8, 80% speed reduction
        double counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getSpeedReduction();
        }
        return Math.min(counter, 1);
    }

    private double reloadReduction() { //will be a percentage, maximum 1
        double counter = 0;
        for (StatusEffect e : this.statusEffects) {
            counter += e.getReloadReduction();
        }
        return Math.min(counter, 1);
    }

    
}