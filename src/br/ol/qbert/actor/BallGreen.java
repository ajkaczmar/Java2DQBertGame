package br.ol.qbert.actor;

import br.ol.qbert.infra.PlayField;
import br.ol.qbert.infra.Harmless;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.HudInfo;
import br.ol.qbert.infra.Scene;
import static br.ol.qbert.infra.ScoreInfo.*;
import br.ol.qbert.scene.Level;

/**
 * BallGreen class.
 * 
 * This magic ball will freeze the board for a short time if Q*Bert catches it.
 * 
 * - If Q*Bert touches enemies, Q*Bert won't be killed.
 * - In this implementation, Q*Bert can catch green entities.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class BallGreen extends Harmless {

    private int avoidWait;

    public BallGreen(int id, Scene scene, QBert qbert, PlayField playField) {
        super(id, scene, Axis.Z_AXIS, qbert, 50, playField, 1200);
    }

    @Override
    public void init() {
        addFrame("ball_green_0", 7, 6);
        addFrame("ball_green_1", 7, 6);
        addFrame("ball_green_0", 7, 6);
        addFrame("ball_green_1", 7, 6);
        addFrame("ball_green_0", 7, 6);
        addFrame("ball_green_1", 7, 6);
        addFrame("ball_green_0", 7, 6);
        addFrame("ball_green_1", 7, 6);
    }

    @Override
    public void reset() {
        fall1b(0);
    }

    @Override
    public void onDead() {
        avoidWait = (int) (300 + 300 * Math.random()) ;
    }

    @Override
    public void updateDead() {
        avoidWait--;
    }
    
    @Override
    public void updateIdle() {
        if (Math.random() < 0.5) {
            jumpX(1);
        }
        else { 
            jumpY(1);
        }
        Audio.playSound("jump2");
    }

    @Override
    public void onCaught() {
        if (scene instanceof Level) {
            Level level = (Level) scene;
            level.makeQBertInvicible();
        }
        kill(false);
        HudInfo.addScore(SCORE_GREEN_BALL_CAPTURED);
    }
    
    public boolean keepDead() {
        return avoidWait > 0;
    }
    
}
