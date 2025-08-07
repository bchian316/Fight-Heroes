import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Set;

public final class Player extends Entity {
    public static final int PLAYERSTARTX = GameRunner.SCREENWIDTH / 2;
    public static final int PLAYERSTARTY = GameRunner.SCREENHEIGHT - 162;

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

    private final Timer specialTimer;
    //private static final int BIGSPECIALBARWIDTH = 15;
    private static final int SPECIALBARWIDTH = 10;
    private static final int SPECIALBARHEIGHT = 50;
    private static final Color SPECIALBAR_BGCOLOR = new Color(80, 80, 80);
    private static final Color SPECIALBAR_MGCOLOR = new Color(255, 150, 0);
    private static final Color SPECIALBAR_FGCOLOR = new Color(240, 0, 0);

    private final Mage mage; //just for attacking

    private int levelNumber = 20;

    private final int regenRate;
    private static final int REGENTIME = 3000; //3secs for regen
    private static final int REGENTICKTIME = 500;
    private final Timer regenTimer = new Timer(REGENTIME); //when to regen
    private final Timer regenTickTimer = new Timer(REGENTICKTIME);
    private boolean isHealing = false; //if regening

    public Player(Mage mage) {
        super("mages", Player.PLAYERSTARTX, Player.PLAYERSTARTY, mage);
        this.mage = mage;
        this.specialTimer = new Timer(this.mage.getSpecialReload());
        this.regenRate = mage.getRegen();
        this.load();
        this.loadSpecial();
    }

    public void setToStartCoords() {
        this.setX(Player.PLAYERSTARTX - this.getSize() / 2);
        this.setY(Player.PLAYERSTARTY - this.getSize() / 2);
    }

    public int getLevelNumber() {
        return this.levelNumber;
    }

    public void incrementLevelNumber() {
        this.levelNumber++;
    }

    private void regenerate() { //update the regen health
        if (this.isHealing) {
            this.regenTickTimer.update();
            if (this.regenTickTimer.isDone()) {
                regenTickTimer.reset();
                this.heal(this.regenRate);
            }
        } else if (!this.isHealing) {
            this.regenTimer.update();
            if (this.regenTimer.isDone()) {
                regenTimer.pause();
                this.isHealing = true;
                regenTickTimer.reset();
            }
        }
    }

    private void stopHealing() {
        this.isHealing = false;
        this.regenTimer.reset();
        this.regenTimer.start();
    }

    @Override
    public DamageCounter getDamaged(int damage, Color c) {
        this.stopHealing();
        return super.getDamaged(damage, c);
        
    }

    public ArrayList<DamageCounter> update(Set<Integer> pressedKeys, Map map) {
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
            dx *= this.getSpeed();
            dy *= this.getSpeed();
            this.setBorders(map, dx, dy, true);
        }

        this.regenerate();

        this.specialTimer.update();

        return super.update();
    }

    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        //check if loaded first
        //center x and center y
        this.stopHealing();
        this.unload();
        return this.mage.createProjectiles(this.getCenterX(), this.getCenterY(), targetX, targetY);
    }

    public ArrayList<Projectile> special(double targetX, double targetY) {
        //check if loaded first
        this.stopHealing();
        this.specialTimer.reset();
        return this.mage.special(this, targetX, targetY);
    }

    public void loadSpecial() {
        this.specialTimer.beDone();
    }

    public boolean isSpecialLoaded() {
        return this.specialTimer.isDone();
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        this.drawReloadBar(g);
        this.drawHealthBar(g);
        this.drawBigReloadBar(g);
        this.drawBigHealthBar(g);
        this.drawSpecialBar(g);
    }

    @Override
    public void drawHealthBar(Graphics g) {
        //call in draw method
        g.setColor(HEALTHBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() - Player.HEALTHBARWIDTH / 2, (int) this.getY() + this.getSize() + BAROFFSET,
                Player.HEALTHBARWIDTH, Player.HEALTHBARHEIGHT);
        if (this.isHealing) {
            g.setColor(HEALTHBAR_REGENCOLOR);
            g.fillRect((int) this.getCenterX() - Player.HEALTHBARWIDTH / 2,
                    (int) this.getY() + this.getSize() + BAROFFSET,
                    Player.HEALTHBARWIDTH, Player.HEALTHBARHEIGHT);
        }
        g.setColor(HEALTHBAR_FGCOLOR);
        g.fillRect((int) this.getCenterX() - Player.HEALTHBARWIDTH / 2, (int) this.getY() + this.getSize() + BAROFFSET,
                (int) (Player.HEALTHBARWIDTH * this.getHealthFraction()), Player.HEALTHBARHEIGHT);
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
        g.fillRect(0, GameRunner.SCREENHEIGHT - GameRunner.HEIGHTOFFSET,
                (int) (GameRunner.SCREENWIDTH * this.getHealthFraction()), Player.BIGHEALTHBARHEIGHT);
    }

    private void drawReloadBar(Graphics g) { //to the right of player
        g.setColor(Player.RELOADBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() + this.getSize() / 2 + Player.BAROFFSET,
                (int) this.getCenterY() - Player.RELOADBARHEIGHT / 2,
                Player.RELOADBARWIDTH, Player.RELOADBARHEIGHT);
        g.setColor(Player.RELOADBAR_MGCOLOR);
        g.fillRect((int) this.getCenterX() + this.getSize() / 2 + Player.BAROFFSET,
                (int) this.getCenterY() + Player.RELOADBARHEIGHT / 2
                        - (int) (Player.RELOADBARHEIGHT * this.getReloadFraction()),
                Player.RELOADBARWIDTH,
                (int) (Player.RELOADBARHEIGHT * this.getReloadFraction()));
        if (this.isLoaded()) {
            g.setColor(Player.RELOADBAR_FGCOLOR);
            g.fillRect((int) this.getCenterX() + this.getSize() / 2 + Player.BAROFFSET,
                    (int) this.getCenterY() - Player.RELOADBARHEIGHT / 2,
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

    private double getSpecialFraction() {
        return this.specialTimer.doneFraction();
    }

    private void drawSpecialBar(Graphics g) { //to the right of player
        g.setColor(Player.SPECIALBAR_BGCOLOR);
        g.fillRect((int) this.getCenterX() - this.getSize() / 2 - Player.BAROFFSET - Player.SPECIALBARWIDTH,
                (int) this.getCenterY() - Player.SPECIALBARHEIGHT / 2,
                Player.SPECIALBARWIDTH, Player.SPECIALBARHEIGHT);
        g.setColor(Player.SPECIALBAR_MGCOLOR);
        g.fillRect((int) this.getCenterX() - this.getSize() / 2 - Player.BAROFFSET - Player.SPECIALBARWIDTH,
                (int) this.getCenterY() + Player.SPECIALBARHEIGHT / 2
                        - (int) (Player.SPECIALBARHEIGHT * this.getSpecialFraction()),
                Player.SPECIALBARWIDTH,
                (int) (Player.SPECIALBARHEIGHT * this.getSpecialFraction()));
        if (this.isSpecialLoaded()) {
            g.setColor(Player.SPECIALBAR_FGCOLOR);
            g.fillRect((int) this.getCenterX() - this.getSize() / 2 - Player.BAROFFSET - Player.SPECIALBARWIDTH,
                    (int) this.getCenterY() - Player.SPECIALBARHEIGHT / 2,
                    Player.SPECIALBARWIDTH, Player.SPECIALBARHEIGHT);
        }
    }
}