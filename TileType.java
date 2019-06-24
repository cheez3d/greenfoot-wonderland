import java.util.Map;
import java.util.LinkedHashMap;

public enum TileType {
    BARRIER(0), WALL(1), FLOOR(2), BELT(3);
    
    private static final Map<Integer, TileType> IDToTileType = new LinkedHashMap<Integer, TileType>();
    static {
        for (TileType tileType : TileType.values())
            IDToTileType.put(tileType.getID(), tileType);
    }
    
    public static TileType fromID(int ID) {
        return IDToTileType.get(ID);
    }
    
    
    private final int ID;
    
    
    TileType(int ID) {
        this.ID = ID;
    }
    
    
    public int getID() {
        return ID;
    }
}