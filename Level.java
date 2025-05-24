public class Level {
    public static final Level[] LEVELS = {
            new Level(new Enemy[] { new Zombie(400, 100)},
                    new Tile[] {new Tile(100, 100, 80, true),
                                new Tile(150, 100, 40, true)}), //introduce zombie
            new Level(new Enemy[] { new Zombie(50, 50),
                                    new Zombie(250, 50),
                                    new Zombie(550, 50)},
                    new Tile[] {new Tile(100, 100, 80, false)}),
            new Level(new Enemy[] { new Zombie(400, 200), //introduce skeleton
                                    new Skeleton(200, 50),
                                    new Skeleton(600, 50)}),
            new Level(new Enemy[] { new Zombie(150, 250), //introduce ghost
                                    new Zombie(350, 250),
                                    new Ghost(200, 150),
                                    new Skeleton(100, 300) }),
            new Level(new Enemy[] { new Mummy(300, 150), //introduce mummy
                                    new Ghost(100, 100),
                                    new Skeleton(100, 100)}),
            new Level(new Enemy[] { new Ghost(350, 100), //introduce zombie hut
                                    new Ghost(650, 100),
                                    new ZombieHut(100, 150),
                                    new Mummy(200, 150)}),
            new Level(new Enemy[] { new Ben(400, 150)}),//miniboss
            new Level(new Enemy[] { new Frankenstein(400, 100),//introduce frankenstein
                                    new Skeleton(100, 200),
                                    new Skeleton(700, 200)}),
            new Level(new Enemy[] { new Frankenstein(250, 100),
                                    new Skeleton(700, 150),
                                    new Zombie(300, 100),
                                    new Mummy(250, 200) }),
            new Level(new Enemy[] { new Vampire(350, 50), //introduce vampire
                                    new Zombie(500, 150),
                                    new Zombie(700, 150),
                                    new Frankenstein(300, 150) }),
            new Level(new Enemy[] { new Vampire(50, 350), 
                                    new Vampire(750, 350),
                                    new ZombieHut(200, 150),
                                    new Skeleton(400, 100)}),
            new Level(new Enemy[] { new Ghoul(450, 50), //introduce ghoul
                                    new Mummy(300, 50),
                                    new Vampire(300, 50),
                                    new Ghost(400, 200)}),
            new Level(new Enemy[] { new Ghoul(550, 150),
                                    new Ghoul(250, 250),
                                    new Vampire(400, 50),
                                    new Zombie(700, 150),
                                    new Skeleton(700, 50)}),
            new Level(new Enemy[] { new Wight(50, 150), //introduce wight
                                    new Zombie(400, 250),
                                    new Zombie(550, 50), 
                                    new Mummy(600, 100) }),
            new Level(new Enemy[] { new Vampire(500, 250), 
                                    new Zombie(300, 350),
                                    new Ghost(700, 150),
                                    new Ghost(500, 150),              
                                    new Wight(200, 300)}),
            new Level(new Enemy[] { new Frankenstein(400, 150), //introduce skeleton shaman
                                    new ZombieHut(200, 650),
                                    new Zombie(600, 50),
                                    new SkeletonShaman(200, 300) }),
            new Level(new Enemy[] { new Ghoul(500, 150),
                                    new Mummy(300, 250),
                                    new SkeletonShaman(700, 150),                 
                                    new Vampire(500, 100)}),
            new Level(new Enemy[] { new Wraith(500, 50), //Introduce wraith
                                    new Wight(600, 100),
                                    new Zombie(200, 150),
                                    new Skeleton(500, 200),
                                    new Frankenstein(400, 100) }),
            new Level(new Enemy[] { new Wraith(500, 150),
                                    new ZombieHut(300, 100),
                                    new SkeletonShaman(750, 200),
                                    new Ghoul(100, 200),
                                    new Wight(600, 50),
                                    new Mummy(400, 300)},
                    new Tile[] {new Tile(100, 100, 80, true),
                                new Tile(150, 100, 40, true)}),
            new Level(new Enemy[] { new Ben(400, 100)}) //final bosses
    };

    private Enemy[] enemies;
    private Tile[] walls;

    public Level(Enemy[] enemies, Tile[] walls) {
        this.enemies = enemies;
        this.walls = walls;
    }

    public Level(Enemy[] enemies) {
        this(enemies, new Tile[] {});//no walls
    }
    
    public Enemy[] getEnemies() {
        return this.enemies;
    }

    public Tile[] getWalls() {
        return this.walls;
    }
}