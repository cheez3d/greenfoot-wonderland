import java.util.Map;
import java.util.LinkedHashMap;

public enum Direction {
        UP(0, "up", -1, 0, 0),
        DOWN(1, "down", 1, 0, 180),
        LEFT(2, "left", 0, -1, 270),
        RIGHT(3, "right", 0, 1, 90);
        
        private static final Map<Integer, Direction> IDToDirection = new LinkedHashMap<Integer, Direction>();
        static {
            for (Direction dir : Direction.values())
                IDToDirection.put(dir.getID(), dir);
        }
        public static Direction fromID(int ID) { return IDToDirection.get(ID); }
        
        private static final Map<String, Direction> KeyToDirection = new LinkedHashMap<String, Direction>();
        static {
            for (Direction dir : Direction.values())
                KeyToDirection.put(dir.getKey(), dir);
        }
        public static Direction fromKey(String key) { return KeyToDirection.get(key); }
        
        
        
        
        private Direction(int ID, String key, int rowOffset, int columnOffset, int rotation) {
            this.ID = ID;
            
            this.key = key;
            
            this.rowOffset = rowOffset;
            this.columnOffset = columnOffset;
            
            this.rotation = rotation;
        }
        
        
        
        
        private final int ID;
        public int getID() { return ID; }
        
        private final String key;
        public String getKey() { return key; }
        
        private final int rowOffset, columnOffset;
        public int getRowOffset() { return rowOffset; }
        public int getColumnOffset() { return columnOffset; }
        
        private final int rotation;
        public int getRotation() { return rotation; }
    }