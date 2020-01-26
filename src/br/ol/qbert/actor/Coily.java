package br.ol.qbert.actor;

import br.ol.qbert.infra.Enemy;
import br.ol.qbert.infra.PlayField;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.HudInfo;
import br.ol.qbert.infra.Scene;
import static br.ol.qbert.infra.ScoreTable.*;

/**
 * Coily class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Coily extends Enemy {
    
    // in this implementation, coily has priority to be revived
    protected int reviveTime;
    
    public Coily(int id, Scene scene, QBert qbert, PlayField playField) {
        super(id, scene, Axis.Z_AXIS, qbert, 40, playField, 15);
    }

    @Override
    public void init() {
        addFrame("coily_0", 8, 28);
        addFrame("coily_1", 8, 28);
        addFrame("coily_2", 8, 28); 
        addFrame("coily_3", 8, 28);
        addFrame("coily_4", 8, 28); 
        addFrame("coily_5", 8, 28);
        addFrame("coily_6", 8, 28); 
        addFrame("coily_7", 8, 28);
        reviveTime = 15;
    }

    public void hatch(int x, int y, int z) {
        set(x, y, z, 0, 0, 0);
        state = State.IDLE;
    }
    
    @Override
    public void reset() {
        fall1(1, 1, 7, 0);
    }
    
    @Override
    public void updateIdle() {
        int qx = qbert.location[0] >> 4;
        int qy = qbert.location[1] >> 4;
        int qz = qbert.location[2] >> 4;

        int x = location[0] >> 4;
        int y = location[1] >> 4;
        int z = location[2] >> 4;

        int dif[] = new int[3];
        dif[0] = qx - x;
        dif[1] = qy - y;
        dif[2] = qz - z;
        dif[0] = dif[0] > 0 ? 1 : dif[0] < 0 ? -1 : 0;
        dif[1] = dif[1] > 0 ? 1 : dif[1] < 0 ? -1 : 0;
        dif[2] = dif[2] > 0 ? 1 : dif[2] < 0 ? -1 : 0;
        target[xIndex] = 0;
        target[yIndex] = 0;
        
        boolean jumped = false;
        int choice = 0;
        
        // will fall trying to catch the Q*Bert with flying disc
        if (x - 1 == qbert.liftFlyingDiscX && y == qbert.liftFlyingDiscY) {
            jumpX(-1);
            jumped = true;
        }
        else if (x == qbert.liftFlyingDiscX && y - 1 == qbert.liftFlyingDiscY) {
            jumpY(-1);
            jumped = true;
        }
        // chase Q*Bert but stay in the playfield
        else {
            if (dif[xIndex] != 0 && 
                !playField.isOut(upAxis, x + dif[xIndex], y)) {
                // mark jumpX as available
                choice |= 0x01;
            }
            if (dif[yIndex] != 0 && 
                !playField.isOut(upAxis, x, y + dif[yIndex])) {
                // mark jumpY as available
                choice |= 0x02;
            }
        }
        
        // if both jumpX and jumpY are available, then select one randomly
        if (choice == 3) {
            choice = Math.random() <= 0.5 ? 1 : 2;
        }
        
        if (choice == 1) {
            jumpX(dif[xIndex]);
            jumped = true;
        }
        else if (choice == 2) {
            jumpY(dif[yIndex]);
            jumped = true;
        }

        if (jumped) {
            Audio.playSound("jump4");
        }
    }

    @Override
    public void updateDead() {
        reviveTime--;
    }
    
    @Override
    public void onFall() {
        Audio.playSound("coily_fall");
        HudInfo.addScore(SCORE_LURING_COILY_OVER_EDGE);
    }

    @Override
    public void onDead() {
        reviveTime = 90;
    }
    
    public boolean needsRevive() {
        return reviveTime <= 0;
    }
    
}
