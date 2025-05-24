public interface Moveable {
    public abstract Tile returnWallX(Tile[][] walls, double dx);
    public abstract Tile returnWallY(Tile[][] walls, double dy);
    //returns Tile if sprite collided with wall
}