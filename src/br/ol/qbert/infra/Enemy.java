package br.ol.qbert.infra;

import br.ol.qbert.actor.QBert;

/**
 * Enemy class.
 * 
 * @author admin
 */
public class Enemy extends Actor {
    
    protected final QBert qbert;
    
    public Enemy(int id, Scene scene, Axis topAxis, QBert qbert, 
        int jumpWaitUntil, PlayField playField, int minLevelFrames) {
        
        super(id, scene, topAxis, playField);
        this.qbert = qbert;
        this.jumpWaitUntil = jumpWaitUntil;
        this.minLevelFrames = minLevelFrames;
    }

    @Override
    public void init() {
        kill(false);
    }

}
