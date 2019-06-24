import java.util.Map;
import java.util.LinkedHashMap;

public enum EntityType {
    EMPTY(0), PLAYER(1), COIN(2), BOX(3), BONUS(4), GATE(5), PORTAL(6);
    
    private static final Map<Integer, EntityType> IDToEntityType = new LinkedHashMap<Integer, EntityType>();
    static {
        for (EntityType entityType : EntityType.values())
            IDToEntityType.put(entityType.getID(), entityType);
    }
    
    public static EntityType fromID(int ID) {
        return IDToEntityType.get(ID);
    }
    
    
    private final int ID;
    
    
    EntityType(int ID) {
        this.ID = ID;
    }
    
    
    public int getID() {
        return ID;
    }
}