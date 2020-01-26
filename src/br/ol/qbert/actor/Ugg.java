package br.ol.qbert.actor;

import br.ol.qbert.infra.Enemy;
import br.ol.qbert.infra.PlayField;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.Scene;

/**
 * Ugg class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Ugg extends Enemy {
    
    public Ugg(int id, Scene scene, QBert qbert, PlayField playField) {
        super(id, scene, Axis.Y_AXIS, qbert, 40, playField, 90);
    }

    @Override
    public void init() {
        addFrame("ugg_0", 10, 6);
        addFrame("ugg_1", 13, 6);
        addFrame("ugg_2", 9, 8); 
        addFrame("ugg_3", 14, 10);
    }

    @Override
    public void reset() {
        fall1(0, 1, 8, 0);
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
