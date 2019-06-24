import greenfoot.Greenfoot;
import greenfoot.World;

abstract class GameWorld extends World {
    protected GameWorld(int width, int height, int cellSize, boolean bounded) {
        super(width, height, cellSize, bounded);
    }
    
    protected GameWorld(int width, int height, int cellSize) {
        this(width, height, cellSize, true);
    }
    
    
    private long actDelta;
    public long getActDelta() { return actDelta; }
    
    private long lastActTime;
    public void act() {
        long actTime = System.currentTimeMillis();
        
        // calculate delta only if previousActTime exists
        if (lastActTime != 0L) actDelta = actTime - lastActTime;
        lastActTime = actTime;
        
        
        String key = Greenfoot.getKey();
        if (key != null) keyPressed(key);
    }
    
    public void keyPressed(String key) {}
    
    
    public void addGameActor(GameActor gameActor) {
        addGameActor(gameActor, 0, 0);
    }
    
    public void addGameActor(GameActor gameActor, int x, int y) {
        addObject(gameActor, x, y);
        
        gameActor.setGameWorld(this);
        gameActor.addedToGameWorld(this);
    }
    
    public void removeGameActor(GameActor gameActor) {
        removeObject(gameActor);
        
        gameActor.setGameWorld(null);
        gameActor.removedFromGameWorld(this);
    }
    
    
    
}