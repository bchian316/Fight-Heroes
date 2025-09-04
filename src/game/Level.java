package game;

import enemies.*;

public class Level {
    public static final Level[] LEVELS = {
            new Level(new Enemy[] { new IceSpirit(200, 500),
                                    new FireSpirit(100, 500),
                                    new Witch(150, 700),
                                    new ZombieHut(250, 850)}), //introduce zombie
            new Level(new Enemy[] { new Zombie(100, 150),
                                    new Zombie(250, 150),
                                    new Grave(400, 325)}),
            new Level(new Enemy[] { new Zombie(400, 150), //introduce skeleton
                                    new Skeleton(200, 75),
                                    new Skeleton(600, 75)}),
            new Level(new Enemy[] { new Zombie(150, 250), //introduce ghost
                                    new Zombie(650, 250),
                                    new Ghost(500, 150),
                                    new Skeleton(100, 300)}),
            new Level(new Enemy[] { new Mummy(250, 150), //introduce mummy
                                    new Ghost(300, 100),
                                    new Ghost(500, 100),
                                    new Ghost(700, 100),
                                    new Ghost(100, 100),
                                    new Skeleton(100, 100)}),
            new Level(new Enemy[] { new Ghost(400, 100), //introduce zombie hut
                                    new Ghost(650, 100),
                                    new ZombieHut(100, 150),
                                    new Mummy(200, 150)}),
            new Level(new Enemy[] { new Ben(400, 150),//miniboss
                                    new Skeleton(200, 150),
                                    new Skeleton(600, 150),
                                    new Zombie(200, 250),
                                    new Zombie(600, 250),
                                    new Ghost(700, 50),
                                    new Ghost(400, 50),
                                    new Ghost(100, 50)}),
            new Level(new Enemy[] { new Frankenstein(400, 150),//introduce frankenstein
                                    new Skeleton(100, 200),
                                    new Skeleton(700, 200),
                                    new ZombieHut(75, 75),
                                    new Ghost(600, 100),
                                    new Ghost(650, 100)}),
            new Level(new Enemy[] { new Frankenstein(250, 150),
                                    new Skeleton(700, 150),
                                    new Skeleton(600, 150),
                                    new ZombieHut(550, 200),
                                    new Zombie(300, 100),
                                    new Mummy(250, 200)}),
            new Level(new Enemy[] { new Vampire(650, 75), //introduce vampire
                                    new Zombie(500, 150),
                                    new Zombie(700, 150),
                                    new Frankenstein(300, 150),
                                    new Skeleton(400, 100)}),
            new Level(new Enemy[] { new Vampire(75, 350), 
                                    new Vampire(725, 350),
                                    new ZombieHut(400, 150),
                                    new Skeleton(400, 100),
                                    new Ghost(200, 75),
                                    new Ghost(400, 75),
                                    new Ghost(600, 75)}),
            new Level(new Enemy[] { new Ghoul(150, 75), //introduce ghoul
                                    new Mummy(400, 75),
                                    new Vampire(700, 50),
                                    new Skeleton(150, 250),
                                    new Skeleton(300, 250),
                                    new Skeleton(500, 250),
                                    new Skeleton(600, 250)}),
            new Level(new Enemy[] { new Ghoul(550, 150),
                                    new Skeleton(175, 250),
                                    new Vampire(400, 75),
                                    new Zombie(700, 150),
                                    new Ghoul(700, 75)}),
            new Level(new Enemy[] { new Wight(50, 150), //introduce wight
                                    new Zombie(375, 300),
                                    new Skeleton(50, 500),
                                    new Ghost(550, 50), 
                                    new Mummy(600, 100),
                                    new Vampire(500, 250),
                                    new Frankenstein(100, 250)}),
            new Level(new Enemy[] { new SkeletonLich(300, 100),
                                    new SkeletonLich(500, 150)}),
            new Level(new Enemy[] { new Ghoul(500, 100), 
                                    new Zombie(100, 350),
                                    new Ghost(725, 150),
                                    new Ghost(625, 150),              
                                    new Skeleton(250, 325),              
                                    new Skeleton(500, 325),              
                                    new Wight(200, 100)}),
            new Level(new Enemy[] { new Frankenstein(200, 100), //introduce skeleton shaman
                                    new Frankenstein(600, 100),                    
                                    new ZombieHut(100, 500),
                                    new Ghost(500, 50),
                                    new SkeletonShaman(650, 450)}),
            new Level(new Enemy[] { new Ghoul(700, 400),
                                    new Mummy(200, 250),
                                    new Mummy(600, 250),
                                    new SkeletonShaman(700, 450),                 
                                    new Vampire(600, 100),
                                    new Zombie(100, 100),
                                    new Zombie(150, 150)}),
            new Level(new Enemy[] { new Wraith(500, 75), //Introduce wraith
                                    new Wight(600, 100),
                                    new Zombie(175, 225),
                                    new Zombie(200, 100),
                                    new Zombie(150, 175),
                                    new Skeleton(575, 200),
                                    new Frankenstein(400, 100),
                                    new SkeletonShaman(700, 500),
                                    new Vampire(100, 500)}),
            new Level(new Enemy[] { new Wraith(400, 250),
                                    new ZombieHut(300, 100),
                                    new SkeletonShaman(700, 400),
                                    new Ghoul(100, 150),
                                    new Wight(650, 125),
                                    new Mummy(400, 250),
                                    new Frankenstein(125, 300)}),
            new Level(new Enemy[] { new Ryan(400, 100)}) 
    };

    private final Enemy[] enemies;

    public Level(Enemy[] enemies) {
        this.enemies = enemies;
    }
    
    public Enemy[] getEnemies() {
        return this.enemies;
    }
}