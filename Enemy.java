
import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Enemy implements hasHealth, canAttack, Drawable{
    private int health;
    private final int speed;
    private final AttackStats attk;

    public Enemy(AttackStats attk, int health, int speed) {
        this.attk = attk;
        this.health = health;
        this.speed = speed;
    }

    public int getSpeed() {
        return this.speed;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void getDamaged(int damage) {
        this.health -= damage;
    }

    public abstract void drawHealthBar();

    @Override
    public abstract void draw(Graphics g);

    @Override
    public abstract ArrayList<Projectile> attack(double targetX, double targetY);
}