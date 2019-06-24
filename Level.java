import java.awt.Color;

import java.io.File;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Scanner;

import javafx.animation.Interpolator;

import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.GreenfootSound;

public class Level extends GameWorld {
    private static final int WIDTH = 16*TileActor.LENGTH;
    private static final int HEIGHT = 10*TileActor.LENGTH;
    
    private static final String EXIT_KEY = "escape";
    public static final String EXIT_SOUND_FILE = "Key.wav";
    
    private static final String CAMERA_OVERRIDE_KEY = "space";
    private static final String[] STEP_KEYS = {"up", "down", "left", "right"};
    
    private static final String ALL_COINS_COLLECTED_SOUND_FILE = "Gate.wav";
    
    private static final String SCORE_TEXT = "Monede Curcubeu: %1$d din %2$d{n}Puncte Bonus: %3$d";
    
    
    
    public Level(String mapFile) {
        super(WIDTH, HEIGHT, 1, false);

        setPaintOrder(
            TextLabel.class, Message.class,
            Player.class, Entity.class,
            Tile.class
        );
        
        loadMap(mapFile);
        
        addGameActor(scoreTextLabel, getWidth()/2, 10 + scoreTextLabel.getHeight()/2);
        updateScoreTextLabel();
    }
    
    
    
    
    private boolean isCameraLocationReset = true;
    public void act() {
        super.act();

        if (isCameraStepping) {
            setCameraLocation(
                cameraStepMotion.interpolate(cameraStepSrcX, cameraStepDestX, cameraStepProg),
                cameraStepMotion.interpolate(cameraStepSrcY, cameraStepDestY, cameraStepProg)
            );

            cameraStepProg += (getActDelta()*Game.REFERENCE_APMS) * cameraStepProgInc;

            if (cameraStepProg >= 1.0f) {
                isCameraStepping = false;

                setCameraLocation(cameraStepDestX, cameraStepDestY);
            }
        }

        if (isCameraMoving) {
            setCameraLocation(
                cameraMoveMotion.interpolate(cameraMoveSrcX, cameraMoveDestX, cameraMoveProg),
                cameraMoveMotion.interpolate(cameraMoveSrcY, cameraMoveDestY, cameraMoveProg)
            );

            cameraMoveProg += (getActDelta()*Game.REFERENCE_APMS) * cameraMoveProgInc;

            if (cameraMoveProg >= 1.0f) {
                isCameraMoving = false;

                setCameraLocation(cameraMoveDestX, cameraMoveDestY);
            }
        }
        
        
        // daca nu exista player care sa fie controlat
        if (player == null) return;
        
        if (player.isStepping() || isCameraStepping || isCameraMoving) return;
        
        if (!Greenfoot.isKeyDown(CAMERA_OVERRIDE_KEY) && !isCameraLocationReset) {
            moveCamera(player.getX(), player.getY(), cameraSpeed);

            isCameraLocationReset = true;
        } else
            for (String key : STEP_KEYS)
                if (Greenfoot.isKeyDown(key)) {
                    Direction stepDir = Direction.fromKey(key);
                    
                    if (Greenfoot.isKeyDown(CAMERA_OVERRIDE_KEY)) {
                        stepCamera(stepDir, cameraSpeed);
                        
                        if (isCameraLocationReset) isCameraLocationReset = false;
                    } else {
                        player.step(stepDir, player.getStepSpeed());
                    }
                        
                    break;
                }
    }
    
    @Override
    public void keyPressed(String key) {
        switch (key) {
            case EXIT_KEY: {
                Greenfoot.setWorld(Game.MENU);
                
                (new GreenfootSound(EXIT_SOUND_FILE)).play();
                
                break;
            }
        }
    }
    
    public void finished() {
        removeGameActor(scoreTextLabel);
        
        addGameActor(
            new Message("SCOR FINAL{n}" + scoreTextLabel.getText() + "{n}{n}Apasa Esc pentru a reveni la meniu."),
            getWidth()/2,
            getHeight()/2
        );
    }
    
    
    
    public void addTileActor(TileActor tileActor, int row, int column) {
        TileActor[][] map;
        if (tileActor instanceof Tile) map = maps.get(Tile.class);
        else if (tileActor instanceof Entity) map = maps.get(Entity.class);
        else throw new IllegalStateException("Unknown TileActor at (" + row + ", " + column + ")");

        addGameActor(tileActor);

        tileActor.setMap(map);
        tileActor.setMapLocation(row, column);
    }
    
    public void removeTileActor(TileActor tileActor) {
        int row = tileActor.getRow();
        int column = tileActor.getColumn();
        
        // BUG: player se misca cu camera la colectarea unui coin/bonus fara acest cod
        TileActor[][] map = tileActor.getMap();
        if (map[row][column] == tileActor) map[row][column] = null;
        tileActor.setMap(null);
        
        if (tileActor == player) player = null;
        
        removeGameActor(tileActor);
    }
    
    
    
    
    
    public TileActor getTileActor(Class<? extends TileActor> c, int row, int column) { return maps.get(c)[row][column]; }
    public Map<Class<? extends TileActor>, TileActor> getTileActors(int row, int column) {
        Map<Class<? extends TileActor>, TileActor> tileActors = new LinkedHashMap<Class<? extends TileActor>, TileActor>();

        for (Class<? extends TileActor> c : maps.keySet()) {
            TileActor tileActor = getTileActor(c, row, column);

            tileActors.put(c, tileActor);
        }

        return tileActors;
    }
    
    
    private Player player;
    public Player getPlayer() { return player; }

    
    
    private int cameraX, cameraY;
    public int getCameraX() { return cameraX; }
    public int getCameraY() { return cameraY; }
    
    public void setCameraLocation(int x, int y) {
        for (int row = 0; row < rowsCount; ++row)
            for (int column = 0; column < columnsCount; ++column)
                for (TileActor tileActor : getTileActors(row, column).values()) {
                    if (tileActor == null) continue;

                    tileActor.setLocalLocation(
                        tileActor.getX() + (getWidth()/2 - x),
                        tileActor.getY() + (getHeight()/2 - y)
                    );
                }

        cameraX = x;
        cameraY = y;
    }

    
    private float cameraSpeed = 0.1f;
    public float getCameraSpeed() { return cameraSpeed; }
    public void setCameraSpeed(float cameraSpeed) { this.cameraSpeed = cameraSpeed; }
    
    
    private boolean isCameraStepping = false;
    public boolean isCameraStepping() { return isCameraStepping; }
    
    private int cameraStepSrcX, cameraStepSrcY;
    private int cameraStepDestX, cameraStepDestY;

    private Interpolator cameraStepMotion = Interpolator.LINEAR;
    private float cameraStepProgInc;
    private float cameraStepProg;

    public void stepCamera(Direction dir, float progInc) {
        if (isCameraStepping) return;

        cameraStepSrcX = cameraX;
        cameraStepSrcY = cameraY;

        cameraStepDestX = cameraStepSrcX + TileActor.LENGTH*dir.getColumnOffset();
        cameraStepDestY = cameraStepSrcY + TileActor.LENGTH*dir.getRowOffset();
        
        // limita distanta
        int deltaX = cameraStepDestX - player.getX();
        int deltaY = cameraStepDestY - player.getY();
        if (deltaX < -getWidth()/2 || deltaX > getWidth()/2
            || deltaY < -getHeight()/2 || deltaY > getHeight()/2) return;

        cameraStepProgInc = progInc;
        cameraStepProg = 0.0f;

        isCameraStepping = true;
    }

    
    private boolean isCameraMoving = false;
    public boolean isCameraMoving() { return isCameraMoving; }
    
    private int cameraMoveSrcX, cameraMoveSrcY;
    private int cameraMoveDestX, cameraMoveDestY;

    private Interpolator cameraMoveMotion = Interpolator.EASE_BOTH;
    private float cameraMoveProgInc;
    private float cameraMoveProg;

    public void moveCamera(int x, int y, float progInc) {
        if (isCameraMoving) return;

        cameraMoveSrcX = cameraX;
        cameraMoveSrcY = cameraY;

        cameraMoveDestX = x;
        cameraMoveDestY = y;

        cameraMoveProgInc = progInc;
        cameraMoveProg = 0.0f;

        isCameraMoving = true;
    }
    
    
    private TextLabel scoreTextLabel = new TextLabel(SCORE_TEXT);
    private void updateScoreTextLabel() {
        scoreTextLabel.setText(String.format(
            SCORE_TEXT,
            collectedCoinsCount, totalCoinsCount, bonusPoints
        ));
    }
    
    private int collectedCoinsCount = 0;
    private int totalCoinsCount = 0;
    protected int getCollectedCoinsCount() { return collectedCoinsCount; }
    protected void setCollectedCoinsCount(int count) {
        collectedCoinsCount = count;
        
        updateScoreTextLabel();
        
        if (collectedCoinsCount >= totalCoinsCount) {
            (new GreenfootSound(ALL_COINS_COLLECTED_SOUND_FILE)).play();
            
            for (Gate gate : gates) removeTileActor(gate);
        }
    }
    
    
    
    private int bonusPoints = 0;
    protected int getBonusPoints() { return bonusPoints; }
    protected void setBonusPoints(int bonusPoints) {
        this.bonusPoints = bonusPoints;
        
        updateScoreTextLabel();
    }
    
    
    
    
    private Map<Class<? extends TileActor>, TileActor[][]> maps = new LinkedHashMap<Class<? extends TileActor>, TileActor[][]>();
    protected Map<Class<? extends TileActor>, TileActor[][]> getMaps() { return maps; }
    
    private int rowsCount, columnsCount;

    private List<Gate> gates = new ArrayList<Gate>();
    
    public void loadMap(String mapFile) {
        Scanner mapReader = null;
        
        try {
            mapReader = new Scanner(new File("maps\\" + mapFile));

            // permite si ':' ca delimiter
            mapReader.useDelimiter(":|\\s+");

            // BACKGROUND COLOR
            int bgRed = mapReader.nextInt(), bgGreen = mapReader.nextInt(), bgBlue = mapReader.nextInt();
            Color bgColor = new Color(bgRed, bgGreen, bgBlue);

            GreenfootImage bgImage = new GreenfootImage(1, 1);
            bgImage.setColorAt(0, 0, bgColor);

            setBackground(bgImage);

            // FLOOR_INFO TEXTS
            int textsCount = mapReader.nextInt();
            List<String> texts = new ArrayList<String>(textsCount);
            
            mapReader.nextLine(); // for skipping to beginning of next line
            for (int text = 0; text < textsCount; ++text)
                texts.add(mapReader.nextLine());

            // MAP
            rowsCount = mapReader.nextInt();
            columnsCount = mapReader.nextInt();

            maps.put(Tile.class, new Tile[rowsCount][columnsCount]);
            maps.put(Entity.class, new Entity[rowsCount][columnsCount]);
            
            for (int row = 0; row < rowsCount; ++row)
                for (int column = 0; column < columnsCount; ++column) {
                    Tile tile;

                    TileType tileType = TileType.fromID(mapReader.nextInt());
                    
                    switch (tileType) {
                        case BARRIER: tile = new Barrier(); break;
                        case WALL: tile = new Wall(); break;
                        case FLOOR: {
                            FloorType floorType = FloorType.fromID(mapReader.nextInt());
                            
                            switch (floorType) {
                                case FLOOR: tile = new Floor(); break;
                                case FLOOR_INFO: {
                                    int textIndex = mapReader.nextInt();

                                    tile = new FloorInfo(texts.get(textIndex)); break;
                                }
                                case FLOOR_LAVA: {
                                    long offTime = mapReader.nextLong();
                                    long onTime = mapReader.nextLong();
                                    
                                    tile = new FloorLava(offTime, onTime); break;
                                }

                                default: throw new IllegalStateException("Unknown Floor variant (" + floorType.getID() + ") at (" + row + ", " + column + ")");
                            }

                            break;
                        }
                        case BELT: {
                            Direction orientation = Direction.fromID(mapReader.nextInt());
                            
                            tile = new Belt(orientation); break;
                        }

                        default: throw new IllegalStateException("Unknown Tile ID (" + tileType.getID() + ") at (" + row + ", " + column + ")"); // unknown tile
                    }

                    addTileActor(tile, row, column);
                    
                    
                    Entity entity;

                    EntityType entityType = EntityType.fromID(mapReader.nextInt());
                    
                    switch (entityType) {
                        case EMPTY: continue;
                        case PLAYER: {
                            Direction orientation = Direction.fromID(mapReader.nextInt());

                            player = new Player(row, column, orientation);
                            
                            entity = player; break;
                        }
                        case COIN: {
                            ++totalCoinsCount;
                            
                            entity = new Coin(); break;
                        }
                        case BOX: entity = new Box(); break;
                        case BONUS: entity = new Bonus(); break;
                        case GATE: {
                            Direction orientation = Direction.fromID(mapReader.nextInt());
                            
                            Gate gate = new Gate(orientation);
                            gates.add(gate);
                            
                            entity = gate; break;
                        }
                        case PORTAL: entity = new Portal(); break;

                        default: throw new IllegalStateException("Unknown Entity ID (" + entityType.getID() + ") at (" + row + ", " + column + ")");// unknown entity
                    }

                    addTileActor(entity, row, column);
                }
        } catch (Exception e) {
            System.err.println("Couldn't load " + mapFile);

            e.printStackTrace();
        } finally {
            if (mapReader != null)
                mapReader.close();
        }
    }
}