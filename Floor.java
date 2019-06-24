public class Floor extends Tile {
    @Override
    public boolean isSteppable(TileActor visitor, Direction dir) {
        if (visitor instanceof Player || visitor instanceof Box) return true;
            
        return super.isSteppable(visitor, dir);
    }
}
