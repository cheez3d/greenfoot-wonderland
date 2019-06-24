import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;

import javafx.animation.Interpolator;

public abstract class TileActor extends GameActor {
    public static final int LENGTH = 64;
    
    public static int getYFromRow(int row) { return LENGTH/2+LENGTH*row; }
    public static int getXFromColumn(int column) { return LENGTH/2+LENGTH*column; }
    
    
    
    
    public void act() {
        super.act();
        
        if (isStepping) {
            setLocation(
                stepMotion.interpolate(stepSrcX, stepDestX, stepProg),
                stepMotion.interpolate(stepSrcY, stepDestY, stepProg)
            );
            
            stepProg += (getActDelta()*Game.REFERENCE_APMS) * stepProgInc;
            
            if (stepProg >= 1.0f) {
                isStepping = false;
                
                setMapLocation(stepDestRow, stepDestColumn);
            }
        }
        
        if (isOrienting) {
            setRotation(orientMotion.interpolate(orientSrc, orientDest, orientProg));
            
            orientProg += (getActDelta()*Game.REFERENCE_APMS) * orientProgInc;
            
            if (orientProg >= 1.0f) {
                isOrienting = false;
                
                setOrientation(orientDestDir);
            }
        }
    }
    
    protected void addedToGameWorld(GameWorld gameWorld) {
        if (gameWorld instanceof Level) level = (Level)gameWorld;
    }
    
    protected void removedFromGameWorld(GameWorld gameWorld) {
        if (gameWorld instanceof Level) level = null;
    }
    
    public void steppingOn(TileActor visitor, Direction dir) {}
    public void steppedOn(TileActor visitor) {}
    
    public void steppingOff(TileActor visitor, Direction dir) {}
    public void steppedOff(TileActor visitor) {}
    
    
    
    public boolean isSteppable(TileActor visitor, Direction dir) { return Game.DEVELOPER_MODE; }
    public boolean isPushable(TileActor pusher) { return false; }
    
    
    
    
    public TileActor getNeighbour(Class<? extends TileActor> c) {
        if (level == null) throw new IllegalStateException();
        
        TileActor neighbour = level.getTileActor(c, getRow(), getColumn());
        // if (neighbour == this) throw new IllegalStateException();
        
        return neighbour;
    }
    
    public Map<Class<? extends TileActor>, TileActor> getNeighbours() {
        if (level == null) throw new IllegalStateException();
        
        Map<Class<? extends TileActor>, TileActor> neighbours = new LinkedHashMap<Class<? extends TileActor>, TileActor>();
        
        for (Class<? extends TileActor> c : level.getMaps().keySet()) {
            TileActor neighbour = getNeighbour(c);
            
            if (neighbour == this) continue;
            
            neighbours.put(c, neighbour);
        }
        
        return neighbours;
    }
    
    
    public TileActor getNeighbour(Class<? extends TileActor> c, Direction dir) {
        if (level == null) throw new IllegalStateException();
        
        int row = getRow() + dir.getRowOffset();
        int column = getColumn() + dir.getColumnOffset();
        
        return level.getTileActor(c, row, column);
    }
    
    public Map<Class<? extends TileActor>, TileActor> getNeighbours(Direction dir) {
        if (level == null) throw new IllegalStateException();
        
        Map<Class<? extends TileActor>, TileActor> neighbours = new LinkedHashMap<Class<? extends TileActor>, TileActor>();
        
        for (Class<? extends TileActor> c : level.getMaps().keySet()) {
            TileActor neighbour = getNeighbour(c, dir);
            
            neighbours.put(c, neighbour);
        }
        
        return neighbours;
    }
    
    
    
    private Level level;
    protected Level getLevel() { return level; }
    
    
    private TileActor[][] map;
    protected TileActor[][] getMap() { return map; }
    protected void setMap(TileActor[][] map) { this.map = map; }
    
    
    private int row = -1, column = -1; // -1 denota faptul ca pozitia pe harta a TileActorului nu a fost inca setata
    public int getRow() { return row; }
    public int getColumn() { return column; }
    public void setMapLocation(int row, int column) {
        if (level == null) throw new IllegalStateException();
        
        // daca exista o pozitie anterioara a acestui TileActor declansam semnalul steppedOff
        if (this.row != -1 || this.column != -1) {
            map[this.row][this.column] = null;
            
            // trimite semnalul steppedOff
            for (TileActor neighbour : getNeighbours().values()) {
                if (neighbour == null) continue;
                
                neighbour.steppedOff(this);
            }
        }
        
        // memoram lista de tile actori pentru a trimite la final semnalul steppedOn
        Collection<TileActor> tileActors = level.getTileActors(row, column).values();
        
        // verificam dupa declanseara semnalului steppedOn pentru a permite o eventuala stergere (eg. colectarea unui Coin)
        // if (map[row][column] != null) throw new IllegalStateException("Other TileActor already present at (" + row + ", " + column + ")");
        
        map[row][column] = this;
        
        setLocation(getXFromColumn(column), getYFromRow(row));
        
        this.row = row;
        this.column = column;
        
        for (TileActor tileActor : tileActors) {
            if (tileActor == null) continue;
            
            tileActor.steppedOn(this);
            
            // daca steppedOn nu a sters obiectul aflat pe aceeasi harta (eg. colectarea unui Coin) il stergem acum
            if (tileActor.getMap() == map) {
                Level level = tileActor.getLevel();
                if (level != null) level.removeTileActor(tileActor);
            }
        }
    }
    
    
    private int x, y;
    public int getX() { return x; }
    public int getY() { return y; }
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        
        if (level == null) return;
        
        if (this == level.getPlayer()) level.setCameraLocation(x, y);
        else
            setLocalLocation(
                x + (level.getWidth()/2 - level.getCameraX()),
                y + (level.getHeight()/2 - level.getCameraY())
            );
    }
    
    
    protected void setLocalLocation(int localX, int localY) {
        // cod pentru rezolvarea spatiului de 1px cand actorul este rotit
        // vizibil la Belt daca acest cod este comentat
        if (orientation == Direction.DOWN) { localX -= 1; localY -= 1; }
        else if (orientation == Direction.LEFT) localY -= 1;
        else if (orientation == Direction.RIGHT) localX -= 1;
        
        super.setLocation(localX, localY);
    }
    
    
    private boolean isStepping = false;
    public boolean isStepping() { return isStepping; }
    
    private int stepSrcRow, stepSrcColumn;
    private int stepDestRow, stepDestColumn;
    
    private int stepSrcX, stepSrcY;
    private int stepDestX, stepDestY;
    
    private Interpolator stepMotion = Interpolator.LINEAR;
    private float stepProgInc;
    private float stepProg;
    
    public boolean step(Direction dir, float progInc) {
        if (level == null) throw new IllegalStateException();
        
        if (isStepping) return false;
        
        // detectarea coliziunilor
        for (TileActor neighbour : getNeighbours(dir).values()) {
            if (neighbour == null
                || neighbour.isSteppable(this, dir)
                || neighbour.isPushable(this) && neighbour.step(dir, stepSpeed)) continue;
            
            return false;
        }
        
        stepSrcRow = getRow();
        stepSrcColumn = getColumn();
        
        stepDestRow = stepSrcRow + dir.getRowOffset();
        stepDestColumn = stepSrcColumn + dir.getColumnOffset();
        
        stepSrcX = getX();
        stepSrcY = getY();
        
        stepDestX = stepSrcX + LENGTH*dir.getColumnOffset();
        stepDestY = stepSrcY + LENGTH*dir.getRowOffset();
        
        stepProgInc = progInc;
        stepProg = 0.0f;
        
        isStepping = true;
        
        for (TileActor neighbour : getNeighbours().values()) {
            if (neighbour == null) continue;
            
            neighbour.steppingOff(this, dir);
        }
        
        for (TileActor neighbour : getNeighbours(dir).values()) {
            if (neighbour == null) continue;
            
            neighbour.steppingOn(this, dir);
        }
        
        return true;
    }
    
    private float stepSpeed = 0.05f;
    public float getStepSpeed() { return stepSpeed; }
    public void setStepSpeed(float speed) { this.stepSpeed = stepSpeed; }
    
    
    
    private Direction orientation = Direction.UP;
    public Direction getOrientation() { return orientation; }
    public void setOrientation(Direction orientation) {
        setRotation(orientation.getRotation());
        
        this.orientation = orientation;
    }
    
    
    private boolean isOrienting = false;
    public boolean isOrienting() { return isOrienting; }
    
    private Direction orientDestDir;
    
    private int orientSrc;
    private int orientDest;
    
    private Interpolator orientMotion = Interpolator.LINEAR;
    private float orientProgInc;
    private float orientProg;
    
    public void orient(Direction dir, float progInc) {
        orientDestDir = dir;
        
        orientSrc = getRotation();
        orientDest = dir.getRotation();
        
        if (Math.abs(orientDest - orientSrc) > 180)
            if (orientSrc > orientDest) orientSrc -= 360;
            else if (orientDest > orientSrc) orientDest -= 360;
        
        orientProgInc = progInc;
        orientProg = 0.0f;
        
        isOrienting = true;
    }
}
