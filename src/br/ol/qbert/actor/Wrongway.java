package br.ol.qbert.actor;

import br.ol.qbert.infra.Enemy;
import br.ol.qbert.infra.PlayField;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.Scene;

/**
 * Wrongway class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Wrongway extends Enemy {
    
    public Wrongway(int id, Scene scene, QBert qbert, PlayField playField) {
        super(id, scene, Axis.X_AXIS, qbert, 40, playField, 120);
    }

    @Override
    public void init() {
        addFrame("wrongway_0", 4, 6);
        addFrame("wrongway_1", 4, 6);
        addFrame("wrongway_2", 4, 6); 
        addFrame("wrongway_3", 4, 6);
    }

    @Override
    public void reset() {
        fall1(1, 0, 8, 0);
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
     
}
