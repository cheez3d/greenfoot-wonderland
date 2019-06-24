import greenfoot.GreenfootSound;

public class Portal extends Entity {
    private static final String IMAGE_FILE = "Portal.gif";
    private static final String SOUND_FILE = "Portal.wav";
    
    
    private static final GifImage IMAGE = new GifImage(IMAGE_FILE);
    
    
    public void act() {
        super.act();
        
        setImage(IMAGE.getCurrentImage());
    }
    
    @Override
    public void steppedOn(TileActor visitor) {
        if (visitor instanceof Player) {
            (new GreenfootSound(SOUND_FILE)).play();
            
            Level level = getLevel();
            
            level.removeTileActor(visitor);
            
            level.finished();
        }
    }
    
    
    
    @Override
    public boolean isSteppable(TileActor visitor, Direction dir) {
        if (visitor instanceof Player) return true;
        
        return super.isSteppable(visitor, dir);
    }
}
