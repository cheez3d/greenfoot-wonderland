import greenfoot.GreenfootImage;

public class FloorLava extends Floor {
    private static final String INACTIVE_IMAGE_FILE = "Floor.gif";
    private static final String ACTIVE_IMAGE_FILE = "FloorLava.gif";
    
    private static final GreenfootImage INACTIVE_IMAGE = new GreenfootImage(INACTIVE_IMAGE_FILE);
    private static final GreenfootImage ACTIVE_IMAGE = new GreenfootImage(ACTIVE_IMAGE_FILE);
    
    
    private long offTime, onTime;
    
    public FloorLava(long offTime, long onTime) {
        this.offTime = 1000*offTime;
        this.onTime = 1000*onTime;
    }
    
    
    public void act() {
        super.act();
        
        if (active && (System.currentTimeMillis() >= deactivationTime)) {
            deactivate();
        } else if (!active && (System.currentTimeMillis() >= activationTime)) {
            activate();
        }
    }
    
    @Override
    public void steppedOn(TileActor visitor) {
        if (active) visitor.getLevel().removeTileActor(visitor);
    }
    
    
    private boolean active = false;
    
    private long deactivationTime;
    private void activate() {
        if (active) return;
        
        setImage(ACTIVE_IMAGE);
        
        deactivationTime = System.currentTimeMillis() + onTime;
        
        for (TileActor neighbour : getNeighbours().values()) {
            if (neighbour == null) continue;
            
            neighbour.getLevel().removeTileActor(neighbour);
        }
        
        active = true;
    }
    
    private long activationTime;
    private void deactivate() {
        if (!active) return;
        
        setImage(INACTIVE_IMAGE);
        
        activationTime = System.currentTimeMillis() + offTime;
        
        active = false;
    }
}
