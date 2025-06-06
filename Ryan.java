
import java.awt.Color;
import java.util.ArrayList;

public class Ryan extends SpawnerEnemy {
    public Ryan(int x, int y) {
        super("Ryan", x, y, 150, 650, 3, 6000, 800, new AttackStats(18, 75, 6, 350, 1, 75, new Color(235, 20, 5), true,
                new AttackStats(8, 50, 8, 275, 1, 20, new Color(235, 77, 5), true,
                        new AttackStats(6, 25, 10, 200, 1, 20, new Color(235, 112, 5), false,
                                new AttackStats(5, 15, 3, 100, 1, 1, new Color(235, 162, 5))))), 19000);
    }


    @Override
    public ArrayList<Projectile> attack(double targetX, double targetY) {
        //targetX will remain constant for all child bullets - game.getangle will be current direction
        this.heal(20);
        ArrayList<Projectile> newProjs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            newProjs.add(new Projectile(this.getCenterX(), this.getCenterY(),
                    Game.getAngle(this.getCenterX(), this.getCenterY(), targetX, targetY)
                            + Math.toRadians((i - 1) * 45),
                    this.getAttackStats(), (x1, y1, targetX1, targetY1, splitStats) -> moreAttack(x1, y1, targetX1, targetY1, splitStats)));
        }
        return newProjs;
    }
    
    public ArrayList<Projectile> moreAttack(double x, double y, double targetX, double targetY, AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        //shoots twice from himself
        for (int i = 0; i < 5; i++) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) + Math.toRadians((i - 2) * 72), splitStats,
                    (x1, y1, targetX1, targetY1, splitStats1) -> evenMoreAttack(x1, y1, targetX1, targetY1, splitStats1)));
        }
        return newProjs;
    }
    public ArrayList<Projectile> evenMoreAttack(double x, double y, double targetX, double targetY, AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        //one more range elongation
        for (int i = 0; i < 4; i++) {
            newProjs.add(new Projectile(x, y, Game.getAngle(x, y, targetX, targetY) + Math.toRadians(i*90), splitStats,
                    (x1, y1, targetX1, targetY1, splitStats1) -> evenEvenMoreAttack(x1, y1, targetX1, targetY1, splitStats1)));
            
        }
        return newProjs;
    }

    public ArrayList<Projectile> evenEvenMoreAttack(double x, double y, double targetX, double targetY,
            AttackStats splitStats) {
        ArrayList<Projectile> newProjs = new ArrayList<>();
        //one more range elongation
        for (int i = 0; i < 6; i++) {
            newProjs.add(new Projectile(x, y,
                    Game.getAngle(x, y, targetX, targetY) + Math.toRadians((i - 2.5) * 20) + Math.PI, splitStats));
        }

        return newProjs;
    }




    @Override
    public ArrayList<Enemy> spawn(Map map) {
        this.resetSpawnTimer();
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        int spawnRandom = (int) (Math.random() * 7);
        switch (spawnRandom) {
            case 0 -> {//spawn zombie horde
                for (int i = 0; i < 9; i++) {
                    double randAngle = Math.random() * Math.PI * 2; // in radians
                    double randMagnitude = Math.random() * 450;
                    newEnemies.add(new Zombie((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                            (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
                }
            }
            case 1 -> {//wraith and a few ghosties
                this.heal(75);
                double randAngle = Math.random() * Math.PI * 2; // in radians
                double randMagnitude = Math.random() * 100;
                newEnemies.add(new Wraith((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                        (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
                for (int i = 0; i < 3; i++) {
                    randAngle = Math.random() * Math.PI * 2; // in radians
                    randMagnitude = Math.random() * 200;
                    newEnemies.add(new Ghost((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                            (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
                }
            }
            case 2 -> { //wight and ghoul
                for (int i = 0; i < 3; i++) {
                    double randAngle = Math.random() * Math.PI * 2; // in radians
                    if (Math.random() >= 0.5) {
                        double randMagnitude = Math.random() * 250;
                        newEnemies.add(new Wight((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                                (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
                    } else {
                        double randMagnitude = Math.random() * 100;
                        newEnemies.add(new Ghoul((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                                (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
                    }
                }
            }
            case 3 -> {//skeletons and frankenstein
                double randAngle = Math.random() * Math.PI * 2; // in radians
                double randMagnitude = Math.random() * 100;
                newEnemies.add(new Frankenstein((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                        (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
                for (int i = 0; i < 5; i++) {
                    randAngle = Math.random() * Math.PI * 2; // in radians
                    randMagnitude = Math.random() * 350;
                    newEnemies.add(new Skeleton((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                            (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
                }
            }
            
            case 4 -> {//skeleton shamans and more ghosts
                for (int i = 0; i < 5; i++) {
                    double randAngle = Math.random() * Math.PI * 2; // in radians
                    double randMagnitude = Math.random() * 100;
                    newEnemies.add(new Ghost((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                            (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
                }
                for (int i = 0; i < 2; i++) {
                    double randAngle = Math.random() * Math.PI * 2; // in radians
                    double randMagnitude = Math.random() * 150;
                    newEnemies.add(new SkeletonShaman((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                            (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
                }
            }
            case 5 -> {//Zombie huts, vampires
                this.heal(50);
                double randAngle = Math.random() * Math.PI * 2; // in radians
                double randMagnitude = Math.random() * 400;
                    newEnemies.add(new ZombieHut((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                        (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
                
                for (int i = 0; i < 2; i++) {
                    randAngle = Math.random() * Math.PI * 2; // in radians
                    randMagnitude = Math.random() * 500;
                    newEnemies.add(new Vampire((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                        (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
                }
                
            }
            case 6 -> { //mummies
                this.heal(75);
                for (int i = 0; i < 2; i++) {
                    double randAngle = Math.random() * Math.PI * 2; // in radians
                    double randMagnitude = Math.random() * 600;
                    newEnemies.add(new Mummy((int) (this.getCenterX() + Game.getVectorX(randAngle, randMagnitude)),
                        (int) (this.getCenterY() + Game.getVectorY(randAngle, randMagnitude))));
                }
            }
        }
        
        
        return newEnemies;
    }
    
}