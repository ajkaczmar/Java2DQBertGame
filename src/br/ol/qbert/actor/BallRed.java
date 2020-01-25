package br.ol.qbert.actor;

import br.ol.qbert.infra.Enemy;
import br.ol.qbert.infra.PlayField;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.Scene;

/**
 * BallRed class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class BallRed extends Enemy {

    public BallRed(Scene scene, QBert qbert, PlayField playField) {
        super(scene, Axis.Z_AXIS, qbert, 50, playField, 0);
    }

    @Override
    public void init() {
        addFrame("ball_red_0", 8, 5);
        addFrame("ball_red_1", 8, 5);
        addFrame("ball_red_0", 8, 5);
        addFrame("ball_red_1", 8, 5);
        addFrame("ball_red_0", 8, 5);
        addFrame("ball_red_1", 8, 5);
        addFrame("ball_red_0", 8, 5);
        addFrame("ball_red_1", 8, 5);
    }

    @Override
    public void reset() {
        fall1b(0);
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
     
}
