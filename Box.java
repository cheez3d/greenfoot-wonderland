import greenfoot.GreenfootSound;

public class Box extends Entity {
    private static final String SOUND_FILE = "Box.wav";
    
    
    @Override
    public boolean isPushable(TileActor pusher) {
        if (pusher instanceof Player) return true;
        
        return super.isPushable(pusher);
    }
    
    
    @Override
    public boolean step(Direction dir, float progInc) {
        boolean success = super.step(dir, progInc);
        
        if (success) (new GreenfootSound(SOUND_FILE)).play();
        
        return success;
    }
}
