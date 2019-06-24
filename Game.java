import greenfoot.GreenfootSound;

public class Game {
    // Acts per millisecond (acte pe millisecunda) pentru a executa instrunctiunile din functiile act la aceeasi viteza indiferent de framerate
    public static final float REFERENCE_APMS = 0.06f;
    
    private static final String MUSIC_FILE = "Music.wav";
    
    // prin activarea developer mode de aici se dezactiveaza coliziunile
    public static final boolean DEVELOPER_MODE = false;
    
    
    public static final GreenfootSound MUSIC = new GreenfootSound(MUSIC_FILE);
    
    public static Menu MENU;
    public static Level LEVEL;
}
