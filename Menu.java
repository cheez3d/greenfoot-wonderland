import java.awt.Color;

import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.GreenfootSound;

public class Menu extends GameWorld {
    private static final int WIDTH = 16*TileActor.LENGTH;
    private static final int HEIGHT = 10*TileActor.LENGTH;
    
    private static final String EXIT_KEY = "escape";
    
    private static final String MENU_TEXT = "{0xff0000}W{0xff6303}o{0xf6fd00}n{0xd1ff00}d{0x2bff15}e{0x00ffff}r{0x00dcff}l{0x0098ff}a{0xc500fd}n{0xef23ec}d{0xffffff}{n}Apasa tasta corespunzatoare nivelului{n}pe care doresti sa il joci{n}{n}1. Primii pasi{n}2. Capcane";
    
    
    
    
    public Menu() {
        super(WIDTH, HEIGHT, 1);
        
        setBackground(new GreenfootImage("Floor.gif"));
        
        Message menuTextLabel = new Message(MENU_TEXT, 32.0f);
        
        addGameActor(menuTextLabel, getWidth()/2, getHeight()/2);
    }
    
    
    @Override
    public void keyPressed(String key) {
        switch (key) {
            case EXIT_KEY: {
                if (Game.LEVEL == null) return;
                
                Greenfoot.setWorld(Game.LEVEL);
                
                break;
            }
            
            case "1": case "2": {
                Game.LEVEL = new Level(key + ".map");
                
                Greenfoot.setWorld(Game.LEVEL);
            }
        }
    }
}
