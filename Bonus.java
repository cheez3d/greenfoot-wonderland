import greenfoot.GreenfootSound;

public class Bonus extends Entity {
    private static final String IMAGE_FILE = "Bonus.gif";
    private static final String SOUND_FILE = "Coin.wav";
    
    
    private static final GifImage IMAGE = new GifImage(IMAGE_FILE);
    
    
    private int bonusPoints = 100;
    
    
    public void act() {
        super.act();
        
        setImage(IMAGE.getCurrentImage());
    }
    
    @Override
    public void steppedOn(TileActor visitor) {
        if(visitor instanceof Player) {
            (new GreenfootSound(SOUND_FILE)).play();
            
            Level level = getLevel();
            
            level.removeTileActor(this);
            level.setBonusPoints(level.getBonusPoints() + bonusPoints);
        }
    }
    
    
    
    @Override
    public boolean isSteppable(TileActor visitor, Direction dir) {
        if (visitor instanceof Player) return true;
        
        return super.isSteppable(visitor, dir);
    }
}
