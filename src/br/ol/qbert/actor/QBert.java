package br.ol.qbert.actor;

import br.ol.qbert.infra.Actor;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.HudInfo;
import br.ol.qbert.infra.Keyboard;
import br.ol.qbert.infra.PlayField;
import br.ol.qbert.infra.Scene;
import static br.ol.qbert.infra.ScoreInfo.*;
import br.ol.qbert.scene.Level;
import java.awt.event.KeyEvent;

/**
 * QBert class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class QBert extends Actor {

    public QBert(int id, Scene scene, PlayField playField) {
        super(id, scene, Axis.Z_AXIS, playField);
    }

    @Override
    public void init() {
        addFrame("qbert_0", 6, 14);
        addFrame("qbert_1", 6, 14);
        addFrame("qbert_2", 10, 14); 
        addFrame("qbert_3", 10, 14);
        addFrame("qbert_4", 8, 14); 
        addFrame("qbert_5", 8, 14);
        addFrame("qbert_6", 8, 14); 
        addFrame("qbert_7", 8, 14);
        set(1, 1, 7, 0, 0, 0);
        state = State.IDLE;
    }

    @Override
    public void reset() {
        state = State.IDLE;
        currentFrame = 0;
        zOrder = 0;
        int col = location[0] >> 4;
        int row = location[1] >> 4;
        int z = location[2] >> 4;
        // fall from playField Z up axis
        if (col <= 0 || row <= 0 || col + row > 8) {
            set(1, 1, 7, 0, 0, 0);
        }            
        else {
            set(col, row, z, 0, 0, 0);
            updateRelativeZAxis();
        }        
    }
    
    public boolean deathByFall() {
        int col = location[0] >> 4;
        int row = location[1] >> 4;
        return (col <= 0 || row <= 0 || col + row > 8);
    }
    
    @Override
    public void updateIdle() {
        jumpCount = 0;
        target[xIndex] = 0;
        target[yIndex] = 0;
        
        boolean jumped = false;
        if (Keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
            jumpX(1);
            jumped = true;
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
            jumpX(-1);
            jumped = true;
        }

        if (Keyboard.isKeyPressed(KeyEvent.VK_UP)) {
            jumpY(-1);
            jumped = true;
        }
        else if (Keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
            jumpY(1);
            jumped = true;
        }

        // for debugging purposes
        //if (Keyboard.isKeyPressed(KeyEvent.VK_S)) {
        //    HudInfo.addScore(950);
        //}
        //if (Keyboard.isKeyPressed(KeyEvent.VK_N)) {
        //    ((Level) scene).nextStage();
        //}
        
        if (jumped) {
            Audio.playSound("jump");
        }
    }

    @Override
    protected void updateFlyingDisc() {
        jumpCount++;
        if (jumpCount < 19) {
            location[zIndex] -= fallingVelocity;
            location[xIndex] += target[xIndex];
            location[yIndex] += target[yIndex];
            if (fallingVelocity < 12) {
                fallingVelocity++;
            }
        }
        else if (jumpCount == 19) {
            location[zIndex] -= 4;
            currentFrame = 0;
        }
        else if (jumpCount > 19) {
            if (liftFlyingDiscY > 0){
                location[xIndex] += 2;
            }
            else if (liftFlyingDiscX > 0){
                location[yIndex] += 2;
            }
            location[zIndex] += 4; 
            
            if (spy < 16) {
                fall1(1, 1, 7, 0);
                location[zIndex] = 0x90;
                liftFlyingDiscX = 255;
                liftFlyingDiscY = 255;
                return;
            }
            
            if (scene instanceof br.ol.qbert.scene.Level && jumpCount == 20) {
                ((br.ol.qbert.scene.Level) scene).
                    liftFlyingDisc(liftFlyingDiscX, liftFlyingDiscY);
            }
        }
    }
    
    @Override
    public void onFall() {
        Audio.playSound("qbert_fall");
    }
    
    @Override
    public void onStepOnPlayfield() {
        if (playField.stepOn(location[0] >> 4, location[1] >> 4)) {
            HudInfo.addScore(SCORE_SQUARE_CHANGED_TARGET_COLOR);
        }
    }
     
}
