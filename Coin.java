import greenfoot.GreenfootSound;

public class Coin extends Entity {
    private static final String IMAGE_FILE = "Coin.gif";
    private static final String SOUND_FILE = "Coin.wav";
    
    
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
            
            level.removeTileActor(this);
            level.setCollectedCoinsCount(level.getCollectedCoinsCount() + 1);
        }
    }
    
    
    
    @Override
    public boolean isSteppable(TileActor visitor, Direction dir) {
        if (visitor instanceof Player) return true;
        
        return super.isSteppable(visitor, dir);
    }
}
