package br.ol.qbert.actor;

import br.ol.qbert.infra.Actor;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.PlayField;
import br.ol.qbert.infra.Scene;

/**
 * FlyingDisc class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class FlyingDisc extends Actor {
    
    private int frameIndex;
    public int x;
    public int y;
    
    public FlyingDisc(int id, Scene scene, PlayField playField, int x, int y) {
        super(id, scene, Axis.Z_AXIS, playField);
        this.x = x;
        this.y = y;
    }

    @Override
    public void init() {
        addFrame("flying_disc_0", 8, 0);
        addFrame("flying_disc_1", 8, 0);
        addFrame("flying_disc_2", 8, 0);
        addFrame("flying_disc_3", 8, 0);
        reset();
    }

    @Override
    public void reset() {
        state = State.IDLE;
        location[xIndex] = (x << 4);
        location[yIndex] = (y << 4);
    }
    
    @Override
    public void updateIdle() {
        updateAnimation();
        updateRelativeZAxis();
    }
    
    private void updateAnimation() {
        frameIndex += 8;
        currentFrame = (frameIndex >> 4) % 4;
    }
    
    @Override
    public void updateScreenPosition() {
        spx = 128 - location[0] + location[1];
            //- (location[0] > 0 ? 4 : 0) + (location[1] > 0 ? 4 : 0);
        
        spy = 143 + (location[0] >> 1) + (location[1] >> 1) - location[2] + 20;
        zOrder = -3;
    }

    @Override
    protected void updateFlyingDisc() {
        updateAnimation();
        
        if (y > 0){
            location[xIndex] += 2;
        }
        else if (x > 0){
            location[yIndex] += 2;
        }
        location[zIndex] += 4; 
        
        if (spy < 20) {
            Audio.stopSound("lift");
            Audio.playSound("lift_fall");
            kill(false);
        }
    }
    
    public void lift() {
        state = State.FLYING_DISC;
        Audio.playSound("lift");
    }
     
}
