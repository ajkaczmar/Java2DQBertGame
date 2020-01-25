package br.ol.qbert.infra;

import br.ol.qbert.infra.Actor.Axis;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * PlayField class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class PlayField extends Entity {
    
    private BufferedImage block;
    private BufferedImage blockFloor;
    
    private final int[][] floorStates = new int[7][7]; // floor[y][x]
    private int floorCount; // 1 + 2 + 3 + 4 + 5 + 6 + 7
    private int requiredChangeCount;
    private int blinkTime;
    
    private int level;
    private int round;
    
    public PlayField(Scene scene) {
        super(scene);
        zOrder = -1;
    }

    @Override
    public void update() {
        if (level != LevelInfo.level || round != LevelInfo.round) {
            level = LevelInfo.level;
            round = LevelInfo.round;
            block = Assets.getImage("block_" + level + "_" + round);
            blockFloor = Assets.getImage("block_floor_" + level + "_" + round);
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
                if ((x + y) < 7) {
                    int z = (x << 4) + (y << 4);
                    int spx = 113 - (x << 4) + (y << 4);
                    int spy = 48 + (x << 3) + (y << 3) + z;
                    g.drawImage(block, spx - 1, spy, null);
                    if (floorStates[y][x] == 0 && show) {
                        g.drawImage(blockFloor, spx, spy, null);
                    }
                } 
            }
        }
    }

    public void reset(int requiredChangeCount) {
        floorCount = 0;
        this.requiredChangeCount = requiredChangeCount;
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
        if (floorStates[y - 1][x - 1] > 0) {
            floorStates[y - 1][x - 1]--;
            if (floorStates[y - 1][x - 1] == 0) {
                floorCount--;
            }
            return true;
        }
        return false;
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
