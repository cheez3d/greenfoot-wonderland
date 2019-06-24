public class FloorInfo extends Floor {
    private String[] text;
    
    private Message message;
    
    
    public FloorInfo(String text) {
        message = new Message(text);
    }
    
    
    @Override
    public void steppedOn(TileActor visitor) {
        if (visitor instanceof Player) {
            Level level = getLevel();
            
            level.addGameActor(message, level.getWidth()/2, level.getHeight()/2);
        }
    }
    
    @Override
    public void steppedOff(TileActor visitor) {
        if (visitor instanceof Player) {
            getLevel().removeGameActor(message);
        }
    }
    
    
    public Message getMessage() {
        return message;
    }
}
