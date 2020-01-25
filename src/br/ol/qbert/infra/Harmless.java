package br.ol.qbert.infra;

import br.ol.qbert.actor.QBert;

/**
 * Harmless class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Harmless extends Actor {
    
    protected final QBert qbert;
    
    public Harmless(Scene scene, Axis topAxis, QBert qbert, 
        int jumpWaitUntil, PlayField playField, int minLevelFrames) {
        
        super(scene, topAxis, playField);
        this.qbert = qbert;
        this.jumpWaitUntil = jumpWaitUntil;
        this.minLevelFrames = minLevelFrames;
    }

    public void onCaught() {
        // implement your code here
    }
    
}
