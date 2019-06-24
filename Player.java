public class Player extends Entity {
    private int spawnRow, spawnColumn;
    
    private Direction spawnOrientation;
    
    
    public Player(int spawnRow, int spawnColumn, Direction spawnOrientation) {
        this.spawnRow = spawnRow;
        this.spawnColumn = spawnColumn;
        this.spawnOrientation = spawnOrientation;
        
        setOrientation(spawnOrientation);
    }
   
    
    
    @Override
    public boolean step(Direction dir, float progInc) {
        boolean success = super.step(dir, progInc);
        
        if (success && (dir != getOrientation() || isOrienting())) orient(dir, progInc);
        
        return success;
    }
}
