import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import java.io.File;

import java.util.Map;
import java.util.LinkedHashMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import greenfoot.GreenfootImage;

public class TextLabel extends GameActor {
    private static final String FONT_FILE = "Minecraft.ttf";
    private static final String DEFAULT_HEX_COLOR_CODE = "{0xffffff}";
    
    
    
    private static final String HEX_PATTERN_STRING = "\\{(0x[A-Fa-f0-9]{6})\\}";
    private static final Pattern HEX_PATTERN = Pattern.compile(HEX_PATTERN_STRING);
    
    private static final String NEWLINE_PATTERN_STRING = "\\{n\\}";
    
    private static Font BASE_FONT;
    private static final Map<Float, Font> cachedFonts = new LinkedHashMap<Float, Font>();
    static {
        try {
            BASE_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("fonts\\" + FONT_FILE));
        } catch (Exception e) {
            e.printStackTrace();
            
            BASE_FONT = new Font(Font.DIALOG, Font.PLAIN, 16);
        }
        
        cachedFonts.put(BASE_FONT.getSize2D(), BASE_FONT);
    }
    
    
    
    
    public TextLabel(String text) {
        this.text = text;
        
        updateImage();
    }
    
    
    
    private GreenfootImage image;
    private void updateImage() {
        Font font = cachedFonts.get(fontSize);
        if (font == null) {
            font = BASE_FONT.deriveFont(fontSize);
            
            cachedFonts.put(fontSize, font);
        }
        
        FontMetrics fontMetrics = getImage()
            .getAwtImage()
            .createGraphics()
            .getFontMetrics(font);
        
        // DETERMINARE DIMENSIUNI
        // utilizam o copie a textului original cu toate codurile de culoare inalturate
        String[] lines = text.replaceAll(HEX_PATTERN_STRING, "").split(NEWLINE_PATTERN_STRING);
        
        int maxLineWidth = 0;
        for (String line : lines) {
            int lineWidth = fontMetrics.stringWidth(line);
            
            if (lineWidth > maxLineWidth) maxLineWidth = lineWidth;
        }
        
        int lineHeight = fontMetrics.getHeight() + fontMetrics.getDescent();
        
        int width = maxLineWidth;
        int height = lineHeight * lines.length;
        
        image = new GreenfootImage(width, height); // image.fill();
        image.setFont(font);
        
        
        lines = text.split(NEWLINE_PATTERN_STRING);
        
        String lastHexColorCode = DEFAULT_HEX_COLOR_CODE; // retine ultimul cod gasit pentru a-l perpetua pe urmatoarea linie
        int y = lineHeight - fontMetrics.getDescent();
        for (String line : lines) {
            // perpetueaza ultimul cod pe aceasta linie
            line = lastHexColorCode + line;
            
            Matcher matcher = HEX_PATTERN.matcher(line);
            
            int index = 0;
            int x = (int)((width - fontMetrics.stringWidth(line.replaceAll(HEX_PATTERN_STRING, "")))*alignment);
            while (matcher.find(index)) {
                String hexColorCode = matcher.group();
                
                // transforma codul hex in culoare
                Color color = Color.decode(matcher.group(1));
                image.setColor(color);
                
                // pozitiile start si end pentru textul aflat intre doua coduri hex
                int start = matcher.end();
                int end = matcher.find() ? matcher.start() : line.length();
                
                String sub = line.substring(start, end);
                image.drawString(sub, x, y);
                
                index = end;
                x += fontMetrics.stringWidth(sub);
                
                lastHexColorCode = hexColorCode;
            }
            
            y += lineHeight;
        }
        
        setImage(image);
    }
    
    public int getWidth() { return image.getWidth(); }
    public int getHeight() { return image.getHeight(); }
    
    
    private String text;
    public String getText() { return text; }
    public void setText(String text) { this.text = text; updateImage(); }
    
    private float alignment = 0.5f;
    public float getAlignment() { return alignment; }
    public void setAlignment() { this.alignment = alignment; updateImage(); }
    
    
    private float fontSize = 16.0f;
    public float getFontSize() { return fontSize; }
    public void setFontSize(float fontSize) { this.fontSize = fontSize; updateImage(); }
}