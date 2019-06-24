import greenfoot.Greenfoot;
import greenfoot.GreenfootSound;

public class Initializer extends GameWorld {
    private static final String MUSIC_FILE = "Music.wav";
    
    public Initializer() {
        super(1, 1, 1);
        
        Game.MENU = new Menu();
        
        Greenfoot.setWorld(Game.MENU);
        
        Game.MUSIC.setVolume(50);
        // Game.MUSIC.playLoop();
    }
}
