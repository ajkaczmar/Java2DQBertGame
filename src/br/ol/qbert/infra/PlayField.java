package br.ol.qbert.infra;

import br.ol.qbert.infra.Actor.Axis;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * PlayField class.
 * 
 * Level 1: Hop on a square once to change it to its target color.
 * 
 * Level 2: Hop on a square once to change it to its intermediate color, 
 *          hop off, and hop back on to change it to its target color.
 * 
 * Level 3: Hopping on a square once will change it to the proper color, 
 *          but if you hop on that square again, 
 *          it changes back to the original color.
 * 
 * Level 4: Like Level 2, you must hop on each square twice. 
 *          But if you return to a completed square, 
 *          it returns to the intermediate color.
 * 
 * Level 5: Again, this is like Level 2, but returning to a completed square 
 *          changes the color back to the original color. 
 *          That means that you must start all over on that square. 
 * 
 * Levels 6, 7, 8 and 9 are like Level 5, only faster.
 * 
 * Reference:
 * https://www.atarimagazines.com/cva/v1n2/qbert.php
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class PlayField extends Entity {
    
    private BufferedImage block;
    private final BufferedImage[] blockFloor = new BufferedImage[2];
    
    private final int[][] floorStates = new int[7][7]; // floor[y][x]
    private int floorCount; // 1 + 2 + 3 + 4 + 5 + 6 + 7
    private int requiredChangeCount;
    private int blinkTime;
    
    private int level;
    private int currentViewLevel;
    private int currentViewRound;
    
    private int visibleHeight;
           
    public PlayField(Scene scene) {
        this(scene, 7);
    }

    public PlayField(Scene scene, int visibleHeight) {
        super(scene);
        this.visibleHeight = visibleHeight;
        zOrder = -1;
    }

    @Override
    public void update() {
        if (currentViewLevel != LevelInfo.level || 
            currentViewRound != LevelInfo.round) {
            
            currentViewLevel = LevelInfo.level;
            currentViewRound = LevelInfo.round;
            int tmpLevel = currentViewLevel > 5 ? 
                currentViewLevel % 5 : currentViewLevel;
            
            block = Assets.getImage(
                "block_" + tmpLevel + "_" + currentViewRound);
            
            for (int i = 0; i < requiredChangeCount; i++) {
                blockFloor[i] = Assets.getImage(
                    "block_floor_" + tmpLevel + 
                    "_" + currentViewRound + "_" + i);
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        boolean show = true;
        if (blinkTime > 0) {
            blinkTime--;
            if (((blinkTime >> 2) & 1) == 1) {
                show = false;
            }
        }
        
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                
                if (x + y > visibleHeight ||
                    (x == visibleHeight && y != visibleHeight) ||
                    (x != visibleHeight && y == visibleHeight)) {
                    continue;
                }
                
                if ((x + y) < 7) {
                    
                    int z = (x << 4) + (y << 4);
                    int spx = 113 - (x << 4) + (y << 4);
                    int spy = 48 + (x << 3) + (y << 3) + z;
                    g.drawImage(block, spx - 1, spy, null);
                    int i = floorStates[y][x];
                    if (i < requiredChangeCount && show) {
                        g.drawImage(blockFloor[i], spx, spy, null);
                    }
                } 
            }
        }
    }

    public void reset(int level) {
        this.level = level;
        if (level == 1 || level == 3) {
            requiredChangeCount = 1;
        }
        else {
            requiredChangeCount = 2;
        }
        
        floorCount = 0;
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                if ((x + y) < 7) {
                    floorStates[y][x] = requiredChangeCount;
                    floorCount += floorStates[y][x];
                } 
            }
        }
    }

    public boolean stepOn(int x, int y) {
        if (x < 1 || x > 7 || y < 1 || y > 7) {
            return false;
        }
        boolean stepOnResult;
        switch (level) {
            case 1: stepOnResult = stepOnLevel1_2(x, y); break;
            case 2: stepOnResult = stepOnLevel1_2(x, y); break;
            case 3: stepOnResult = stepOnLevel3_5(x, y, true, true, 1); break;
            case 4: stepOnResult = stepOnLevel3_5(x, y, false, true, 1); break;
            case 5: 
            default:
                stepOnResult = stepOnLevel3_5(x, y, false, true, 2); 
                break;
        }
        return stepOnResult;
    }
    
    private boolean stepOnLevel1_2(int x, int y) {
        if (floorStates[y - 1][x - 1] > 0) {
            floorStates[y - 1][x - 1]--;
            floorCount--;
            return true;
        }
        return false;
    }

    private boolean stepOnLevel3_5(int x, int y, 
        boolean score1, boolean score2, int restoreCount) {
        
        switch (floorStates[y - 1][x - 1]) {
            case 2:
                floorStates[y - 1][x - 1]--;
                floorCount--;
                return score2;
            case 1:
                floorStates[y - 1][x - 1]--;
                floorCount--;
                return score1;
            case 0:
                floorStates[y - 1][x - 1] += restoreCount;
                floorCount += restoreCount;
                return false;
            default:
                throw new RuntimeException(
                    "invalid value for stepOnLevel3_5() !");
        }
    }

    public void restoreFloor(int x, int y) {
        while (floorStates[y - 1][x - 1] < requiredChangeCount) {
            floorStates[y - 1][x - 1]++;
            floorCount++;
        }
    }
    
    public void blink() {
        blinkTime = 90;
    }
    
    public boolean isGameCleared() {
        return floorCount == 0;
    }
    
    public boolean isOut(Axis upAxis, int x, int y) {
        return ((upAxis == Axis.Z_AXIS && (x == 0 || y == 0 || x + y > 8))) ||
            (upAxis != Axis.Z_AXIS && (x + y > 7));
    }
    
}
