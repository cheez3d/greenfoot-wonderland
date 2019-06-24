import java.awt.Color;

import java.io.File;

import greenfoot.GreenfootImage;

public class Message extends GameActor {
    private static final Color DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 0, 200);
    
    
    private String text;
    private TextLabel textLabel;
    
    public Message(String text, float fontSize) {
        this.text = text;
        
        this.textLabel = new TextLabel(text);
        textLabel.setFontSize(fontSize);
    }
    
    public Message(String text) { this(text, 16.0f); }
    
    
    protected void addedToGameWorld(GameWorld gameWorld) {
        int width = gameWorld.getWidth();
        int height = gameWorld.getHeight();
        
        GreenfootImage image = new GreenfootImage(width, height);
        
        image.setColor(DEFAULT_BACKGROUND_COLOR);
        image.fill();
        
        image.drawImage(
            textLabel.getImage(),
            (width - textLabel.getWidth())/2,
            (height - textLabel.getHeight())/2
        );
        
        setImage(image);
    }
}
