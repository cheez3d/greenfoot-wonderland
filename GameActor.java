import greenfoot.Actor;

public class GameActor extends Actor {
    private long actDelta;
    public long getActDelta() { return actDelta; }
    
    private long lastActTime;
    public void act() {
        long actTime = System.currentTimeMillis();
        
        // calculate delta only if previousActTime exists
        if (lastActTime != 0L) actDelta = actTime - lastActTime;
        lastActTime = actTime;
    }
    
    protected void addedToGameWorld(GameWorld gameWorld) {}
    protected void removedFromGameWorld(GameWorld gameWorld) {}
    
    
    private GameWorld gameWorld;
    public GameWorld getGameWorld() { return gameWorld; }
    protected void setGameWorld(GameWorld gameWorld) { this.gameWorld = gameWorld; }
}
