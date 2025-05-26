public class Level {
    public static final Level[] LEVELS = {
            new Level(new Enemy[] { new Wraith(400, 100),
                                    new Wraith(400, 100),
                                    new Wraith(400, 100),
                                    new Wraith(400, 100),
                                    new Wraith(400, 100)},
                    new Tile[] {    new Tile(300, 200, 240, false),
                                    new Tile(350, 200, 240, false),
                                    new Tile(400, 200, 240, false),
                                    new Tile(450, 200, 240, false)}), //introduce zombie
            new Level(new Enemy[] { new Zombie(100, 150),
                                    new Zombie(250, 150),
                                    new Zombie(550, 150)},
                    new Tile[] {    new Tile(100, 300, 80, false)}),
            new Level(new Enemy[] { new Zombie(400, 200), //introduce skeleton
                                    new Skeleton(200, 75),
                                    new Skeleton(600, 75)}),
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
            new Level(new Enemy[] { new Ben(400, 150),//miniboss
                                    new Mummy(200, 150),
                                    new Mummy(600, 150)},
                    new Tile[] {    new Tile(350, 300, 150, true),
                                    new Tile(400, 300, 150, true),
                                    new Tile(350, 350, 150, true),
                                    new Tile(400, 350, 150, true)}),
            new Level(new Enemy[] { new Frankenstein(400, 150),//introduce frankenstein
                                    new Skeleton(100, 200),
                                    new Skeleton(700, 200)}),
            new Level(new Enemy[] { new Frankenstein(250, 150),
                                    new Skeleton(700, 150),
                                    new Zombie(300, 100),
                                    new Mummy(250, 200) }),
            new Level(new Enemy[] { new Vampire(350, 75), //introduce vampire
                                    new Zombie(500, 150),
                                    new Zombie(700, 150),
                                    new Frankenstein(300, 150) }),
            new Level(new Enemy[] { new Vampire(75, 350), 
                                    new Vampire(725, 350),
                                    new ZombieHut(200, 150),
                                    new Skeleton(400, 100)}),
            new Level(new Enemy[] { new Ghoul(450, 50), //introduce ghoul
                                    new Mummy(300, 100),
                                    new Vampire(300, 50),
                                    new Ghost(400, 200)}),
            new Level(new Enemy[] { new Ghoul(550, 150),
                                    new Ghoul(250, 250),
                                    new Vampire(400, 75),
                                    new Zombie(700, 150),
                                    new Skeleton(700, 50)}),
            new Level(new Enemy[] { new Wight(50, 150), //introduce wight
                                    new Zombie(400, 250),
                                    new Zombie(550, 50), 
                                    new Mummy(600, 100)}),
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
            new Level(new Enemy[] { new Wraith(500, 75), //Introduce wraith
                                    new Wight(600, 100),
                                    new Zombie(200, 150),
                                    new Skeleton(500, 200),
                                    new Frankenstein(400, 100) }),
            new Level(new Enemy[] { new Wraith(500, 150),
                                    new ZombieHut(300, 100),
                                    new SkeletonShaman(700, 200),
                                    new Ghoul(100, 200),
                                    new Wight(600, 150),
                                    new Mummy(400, 250)},
                                    
                    new Tile[] {    //left column
                                    new Tile(250, 150, 240, true),
                                    new Tile(250, 200, 240, true),
                                    new Tile(250, 250, 240, true),
                                    new Tile(250, 300, 240, true),
                                    //top row
                                    new Tile(250, 150, 240, true),
                                    new Tile(300, 150, 240, true),
                                    new Tile(350, 150, 240, true),
                                    new Tile(400, 150, 240, true),
                                    new Tile(450, 150, 240, true),
                                    new Tile(500, 150, 240, true),
                                    //right column
                                    new Tile(550, 150, 240, true),
                                    new Tile(550, 200, 240, true),
                                    new Tile(550, 250, 240, true),
                                    new Tile(550, 300, 240, true),
                                    //bottom row
                                    new Tile(250, 300, 240, true),
                                    new Tile(300, 300, 240, true),
                                    new Tile(350, 300, 240, true),
                                    new Tile(400, 300, 240, true),
                                    new Tile(450, 300, 240, true),
                                    new Tile(500, 300, 240, true)}),
            new Level(new Enemy[] { new Ben(400, 200)}) //final bosses
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