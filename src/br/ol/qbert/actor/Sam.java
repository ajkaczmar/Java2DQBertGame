package br.ol.qbert.actor;

import br.ol.qbert.infra.PlayField;
import br.ol.qbert.infra.Harmless;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.HudInfo;
import br.ol.qbert.infra.Scene;
import static br.ol.qbert.infra.ScoreInfo.*;

/**
 * Sam class.
 * 
 * Changes the color of the cube he touches.
 * 
 * Note: if the current level needs 2 jumps to obtain the target color,
 *       and the current color is target color,
 *       then Sam returns it to the intermediate color.
 * 
 * Referece:
 * https://fontsinuse.com/uses/29256/qbert
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Sam extends Harmless {
    
    private int avoidWait;
    
    public Sam(int id, Scene scene, QBert qbert, PlayField playField) {
        super(id, scene, Axis.Z_AXIS, qbert, 40, playField, 600);
    }

    @Override
    public void init() {
        addFrame("sam_0", 7, 14);
        addFrame("sam_1", 7, 14);
        addFrame("sam_0", 7, 14);
        addFrame("sam_1", 7, 14);
        addFrame("sam_0", 7, 14);
        addFrame("sam_1", 7, 14);
        addFrame("sam_0", 7, 14);
        addFrame("sam_1", 7, 14);
    }

    @Override
    public void reset() {
        fall1(1, 1, 7, 0);
    }
    
    @Override
    public void onDead() {
        avoidWait = (int) (900 + 300 * Math.random()) ;
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
        Audio.playSound("jump3");
    }

    @Override
    public void onCaught() {
        kill(false);
        HudInfo.addScore(SCORE_SAM_SLICK_CAPTURED);
    }
    
    @Override
    public void onStepOnPlayfield() {
        playField.restoreFloor(location[0] >> 4, location[1] >> 4, 1);
    }    
    
    public boolean keepDead() {
        return avoidWait > 0;
    }
    
}
