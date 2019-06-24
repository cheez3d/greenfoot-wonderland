import greenfoot.GreenfootImage;

public class Belt extends Tile {
    private static final String IMAGE_FILE = "Belt.gif";
    
    private static final GifImage IMAGE = new GifImage(IMAGE_FILE);
    
    public Belt(Direction orientation) {
        setOrientation(orientation);
    }
    
    public void act() {
        super.act();
        
        setImage(IMAGE.getCurrentImage());
    }
    
    @Override
    public void steppedOn(TileActor visitor) {
        if (visitor instanceof Player || visitor instanceof Box) {
            boolean success = visitor.step(getOrientation(), transportSpeed);
            
            if (!success) visitor.getLevel().removeTileActor(visitor);
        }
    }
    
    
    @Override
    public boolean isSteppable(TileActor visitor, Direction dir) {
        if (visitor instanceof Player || visitor instanceof Box) return true;
            
        return super.isSteppable(visitor, dir);
    }
    
    
    private float transportSpeed = 0.1f;
    public float getTransportSpeed() { return transportSpeed; }
    public void setTransportSpeed(float transportSpeed) { this.transportSpeed = transportSpeed; }
}
