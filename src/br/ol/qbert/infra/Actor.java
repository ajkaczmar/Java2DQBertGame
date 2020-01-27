package br.ol.qbert.infra;

import static br.ol.qbert.infra.Actor.Axis.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Actor class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Actor extends Entity {
    
    protected int id;
    
    public static final int[] JUMP_TABLE = 
        { 16, 18, 21, 23, 24, 24, 24, 24, 23, 21, 19, 16, 13, 9, 5, 0 };

    public static enum Axis { X_AXIS, Y_AXIS, Z_AXIS };
    protected Axis upAxis;
    
    public static enum State { 
        WAITING, FALLING1, IDLE, JUMPING, FALLING2, FLYING_DISC, DEAD };

    protected State state;
    
    // will determine wich creatures will be spawned first
    protected int minLevelFrames;
    
    protected int waitFrames;
    protected State nextState;
    
    protected int xIndex;
    protected int yIndex;
    protected int zIndex;

    public int[] location = new int[3];
    public int[] target = new int[3];

    protected int jumpCount = 0;
    protected int jumpWaitUntil = 24;
    
    protected int spx = 0;
    protected int spy = 0;

    protected PlayField playField;
    protected int fallingVelocity;

    public int liftFlyingDiscX = 255;
    public int liftFlyingDiscY = 255;
    
    // animation frames
    
    public static class Frame {
        BufferedImage image;
        int originX;
        int originY;
    }
    
    protected List<Frame> frames = new ArrayList<>();
    protected int currentFrame;
    
    public Actor(int id,Scene scene, Axis topAxis, PlayField playField) {
        super(scene);
        this.id = id;
        int[][] axisInfo = { { 1, 2, 0 }, { 2, 0, 1 }, { 0, 1, 2 } };
        this.upAxis = topAxis;
        xIndex = axisInfo[topAxis.ordinal()][0]; 
        yIndex = axisInfo[topAxis.ordinal()][1]; 
        zIndex = axisInfo[topAxis.ordinal()][2]; 
        this.playField = playField;
        // DEAD as initial state
        location[zIndex] = 0xf0;
        target[zIndex] = 0xf0;
        state = State.DEAD;
    }

    public int getId() {
        return id;
    }

    public void set(int l0, int l1, int l2, int t0, int t1, int t2) {
        location[xIndex] = l0 << 4; 
        location[yIndex] = l1 << 4; 
        location[zIndex] = l2 << 4;
        target[xIndex] = t0 << 4;
        target[yIndex] = t1 << 4;
        target[zIndex] = t2 << 4;
    }

    public State getState() {
        return state;
    }

    public int[] getLocation() {
        return location;
    }

    public int getMinLevelFrames() {
        return minLevelFrames;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    @Override
    public void update() {
        switch (state) {
            case WAITING:
                updateWaiting();
                break;
            case FALLING1:
                updateFalling1();
                break;
            case IDLE:
                updateIdle();
                // updateRelativeZAxis();
                break;
            case JUMPING:
                updateJumping();
                updateRelativeZAxis();
                break;
            case FLYING_DISC:
                updateFlyingDisc();
                break;
            case DEAD:
                updateDead();
                break;
        }
        
        // workaround: updateFalling2() needs to be called 
        // after updateJumping() in the same frame when
        // state change from JUMPING -> FALLING2, don't know why :*( 
        //
        // TODO: needs to check later why does it delays 1 frame when
        //       state change from JUMPING -> FALLING2
        if (state == State.FALLING2) {
            updateFalling2();
        }
        
        updateScreenPosition();
        checkOutOfScreen();
    }
    
    public void updateWaiting() {
        waitFrames--;
        if (waitFrames <= 0) {
            state = nextState;
        }
    }
    
    public void updateFalling1() {
        location[zIndex] -= fallingVelocity; 

        if (fallingVelocity < 5) {
            fallingVelocity++;
        }
        
        if (location[zIndex] < target[zIndex]) {
            location[zIndex] = target[zIndex];
            currentFrame &= 0xfffffffe; 
            sleep(30, State.IDLE);
        }
    }
    
    public void updateIdle() {
        // implement
    }

    private void updateJumping() {
        if (jumpCount < 16) {
            location[xIndex] += target[xIndex];
            location[yIndex] += target[yIndex];
            zOrder = (location[0] >> 4) + (location[1] >> 4);
        }
        jumpCount++;
        if (jumpCount == 16) {
            int col = location[xIndex] >> 4;
            int row = location[yIndex] >> 4;
            
            // check flying disc
            if (upAxis == Z_AXIS && scene instanceof br.ol.qbert.scene.Level && 
                ((br.ol.qbert.scene.Level) scene).isOnFlyingDisc(
                    this, col, row)) {
                
                liftFlyingDiscX = col;
                liftFlyingDiscY = row;
                state = State.FLYING_DISC;
                fallingVelocity = 5;
                return;
            }
                
            // fall from playField Z up axis
            if (upAxis == Axis.Z_AXIS && 
                (col == 0 || row == 0 || col + row > 8)) {
                
                state = State.FALLING2;
                fallingVelocity = col + row > 8 ? 5 : 2;
                onFall();
            }
            // fall from playField X or Y up axis
            else if (upAxis != Axis.Z_AXIS && (col + row > 7)) {
                state = State.FALLING2;
                fallingVelocity = 5;
                onFall();
            }
            else {
                currentFrame &= 0xfffffffe; 
                onStepOnPlayfield();
            }
        }
        else if (jumpCount == jumpWaitUntil) {
            state = State.IDLE;
        }
    }

    protected void updateFlyingDisc() {
        // implement
    }

    public void updateDead() {
        // implement your code here
    }
    
    public void onFall() {
        // implement
    }

    public void onStepOnPlayfield() {
        // implement
    }

    public void updateFalling2() {
        location[zIndex] -= fallingVelocity;
        location[xIndex] += target[xIndex];
        location[yIndex] += target[yIndex];
        if (fallingVelocity < 12) {
            fallingVelocity++;
        }
        zOrder = -2;
    }
    
    public void updateRelativeZAxis() {
        location[zIndex] = 0x70;
        location[zIndex] -= (location[xIndex] & 0xf0);
        location[zIndex] -= (location[yIndex] & 0xf0);
        location[zIndex] += JUMP_TABLE[location[xIndex] & 0x0f];
        location[zIndex] += JUMP_TABLE[location[yIndex] & 0x0f];
    }
    
    public void updateScreenPosition() {
        spx = 128 - location[0] + location[1];
        spy = 143 + (location[0] >> 1) + (location[1] >> 1) - location[2];
    }

    public void checkOutOfScreen() {
        final int maxSpriteSize = 32;
        if (state != State.DEAD && (
            (upAxis == Axis.X_AXIS && spx > 256 + maxSpriteSize) || 
            (upAxis == Axis.Y_AXIS && spx < 0 - maxSpriteSize) || 
            (upAxis == Axis.Z_AXIS && spy > 240 + maxSpriteSize))) {
            
            kill(true);
        }
    }
    
    @Override
    public void drawDebug(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillOval((int) (spx - 8), (int) (spy - 8), 16, 16);
        g.drawString("(" + (location[0] >> 4) + ", " + (location[1] >> 4) + 
            ", " + (location[2] >> 4) + ")", 20, 10);
    }
    
    @Override
    public void draw(Graphics2D g) {
        Frame frame = frames.get(currentFrame);
        if (frame != null) {
            int x = spx - frame.originX;
            int y = spy - frame.originY;
            g.drawImage(frame.image, x, y, null);
        }
    }
    
    public boolean collides(Actor otherActor) {
        return Math.abs(otherActor.location[0] - location[0]) < 5 &&
            Math.abs(otherActor.location[1] - location[1]) < 5 &&
            Math.abs(otherActor.location[2] - location[2]) < 9;
    } 
    
    public void addFrame(String frameResource, int originX, int originY) {
        BufferedImage frameImage = Assets.getImage(frameResource);
        Frame frame = new Frame();
        frame.image = frameImage;
        frame.originX = originX;
        frame.originY = originY;
        frames.add(frame);
    }
    
    public void sleep(int waitFrames, State nextState) {
        this.waitFrames = waitFrames;
        this.nextState = nextState;
        state = State.WAITING;        
    }
    
    public void fall1(int x, int y, int z, int waitFrames) {
        fallingVelocity = 0;
        set(x, y, 15, x, y, z);
        sleep(waitFrames, State.FALLING1);
        currentFrame = 1;
        zOrder = 0;
    }

    public void fall1b(int waitFrames) {
        int[] position;
        if (Math.random() < 0.5) {
            position = new int[] { 1, 2 };
        }
        else {
            position = new int[] { 2, 1 };
        }
        fall1(position[0], position[1], 6, waitFrames);
    }
        
    public void kill(boolean keepPosition) {
        if (!keepPosition) {
            set(0, 0, 15, 0, 0, 15);
        }
        state = State.DEAD;
        updateScreenPosition();
        onDead();
    }
    
    public void onDead() {
        // implement your code here
    }
    
    public void jumpX(int forward) {
        if (upAxis != Axis.Z_AXIS && forward < 0) {
            throw new RuntimeException(
                "can't move backward when up axis is not Z !");
        }
        jumpCount = 0;
        target[yIndex] = 0;
        target[xIndex] = forward;
        state = State.JUMPING;
        if (forward > 0) {
            currentFrame = 3;
        }
        else {
            currentFrame = 7;
        }
    }

    public void jumpY(int forward) {
        if (upAxis != Axis.Z_AXIS && forward < 0) {
            throw new RuntimeException(
                "can't move backward when up axis is not Z !");
        }
        jumpCount = 0;
        target[xIndex] = 0;
        target[yIndex] = forward;
        state = State.JUMPING;
        currentFrame = 1;
        if (forward > 0) {
            currentFrame = 1;
        }
        else {
            currentFrame = 5;
        }
    }
    
}
