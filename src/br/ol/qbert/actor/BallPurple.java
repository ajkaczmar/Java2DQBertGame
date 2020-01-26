package br.ol.qbert.actor;

import br.ol.qbert.infra.Enemy;
import br.ol.qbert.infra.PlayField;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.Scene;

/**
 * BallPurple class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class BallPurple extends Enemy {
    
    private final Coily coily;
    
    public BallPurple(int id, Scene scene, QBert qbert, 
        PlayField playField, Coily coily) {
        
        super(id, scene, Axis.Z_AXIS, qbert, 50, playField, 30);
        this.coily = coily;
    }

    @Override
    public void init() {
        addFrame("ball_coily_0", 7, 6);
        addFrame("ball_coily_1", 7, 6);
        addFrame("ball_coily_0", 7, 6);
        addFrame("ball_coily_1", 7, 6);
        addFrame("ball_coily_0", 7, 6);
        addFrame("ball_coily_1", 7, 6);
        addFrame("ball_coily_0", 7, 6);
        addFrame("ball_coily_1", 7, 6);
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
        Audio.playSound("jump2");

        // coily born
        int x = location[xIndex] >> 4;
        int y = location[yIndex] >> 4;
        int z = location[zIndex] >> 4;
        if (x + y == 8) {
            coily.hatch(x, y, z);
            kill(false);
        }
    }
     
}
