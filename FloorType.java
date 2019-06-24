import java.util.Map;
import java.util.LinkedHashMap;

public enum FloorType {
    FLOOR(0), FLOOR_INFO(1), FLOOR_LAVA(2);
    
    private static final Map<Integer, FloorType> IDToFloorType = new LinkedHashMap<Integer, FloorType>();
    static {
        for (FloorType floorType : FloorType.values())
            IDToFloorType.put(floorType.getID(), floorType);
    }
    
    public static FloorType fromID(int ID) {
        return IDToFloorType.get(ID);
    }
    
    
    private final int ID;
    
    
    FloorType(int ID) {
        this.ID = ID;
    }
    
    
    public int getID() {
        return ID;
    }
}