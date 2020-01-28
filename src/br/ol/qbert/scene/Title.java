package br.ol.qbert.scene;

import br.ol.qbert.infra.Assets;
import br.ol.qbert.infra.Keyboard;
import br.ol.qbert.infra.SceneManager;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.BitmapFont;
import br.ol.qbert.infra.Scene;
import static br.ol.qbert.infra.Constants.*;
import br.ol.qbert.infra.HudInfo;
import br.ol.qbert.infra.ScoreInfo;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Title scene class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Title extends Scene {
    
    private final BufferedImage image;
    private int frames;
    
    private boolean pressSpaceVisible;
    
    private final BufferedImage[] flyingDisc = new BufferedImage[4];
    private final BufferedImage[] qbert = new BufferedImage[4];
    private final BufferedImage baloon;
    private int flyingDiscFrameIndex;
    private int qbertFrameIndex;
    private double scale;
    private double x;
    private double y;
    private double prevX;
    private double prevY;
    private int baloonVisibleFrame;
    private boolean baloonVisible;
    
    private int instructionsOrHiscore;
    
    public Title(SceneManager sceneManager) {
        super(sceneManager);
        image = Assets.getImage("title");
        for (int i = 0; i < 4; i++) {
            flyingDisc[i] = Assets.getImage("flying_disc_" + i);
            qbert[i] = Assets.getImage("qbert_" + (i << 1));
        }
        baloon = Assets.getImage("baloon");
    }
    
    @Override
    public void onPrepare() {
        frames = 0;
        pressSpaceVisible = false;
        baloonVisible = false;
        baloonVisibleFrame = 0;
        x = -200;
        y = -200;
        scale = 5;
    }
    
    @Override
    public void update() {
        pressSpaceVisible = ((frames++ >> 3) & 3) > 0 && frames > 8;
        if (frames > 8 && Keyboard.isKeyPressed(KeyEvent.VK_SPACE)) {
            Audio.playSound("coin");
            HudInfo.reset();
            sceneManager.changeScene(SCENE_LEVEL_PRESENTATION, 70);
            return;
        }
        updateQBertFlyingDisc();
        if ((frames > 400 && instructionsOrHiscore == 0) || 
            Keyboard.isKeyPressed(KeyEvent.VK_I)) {
            
            instructionsOrHiscore = 1;
            sceneManager.changeScene(SCENE_INSTRUCTIONS);
        }
        else if (frames > 400 && instructionsOrHiscore == 1) {
            instructionsOrHiscore = 0;
            ScoreInfo.newHiscorePlayerIndex = -1;
            sceneManager.changeScene(SCENE_HISCORES);
        }
    }

    private void updateQBertFlyingDisc() {
        prevX = x;
        prevY = y;
        x = 30 * Math.cos(frames * 0.0436) - 40 * Math.sin(frames * 0.0349);
        y = 40 * Math.sin(frames * 0.0371) + 30 * Math.cos(frames * 0.0473);
        x *= scale;
        y *= scale;
        scale -= 0.02;
        if (scale < 1) scale = 1;
        double difX = x - prevX;
        double difY = y - prevY;
        if (difX > 0 && difY > 0) qbertFrameIndex = 0;
        if (difX < 0 && difY > 0) qbertFrameIndex = 1;
        if (difX < 0 && difY < 0) qbertFrameIndex = 2;
        if (difX > 0 && difY < 0) qbertFrameIndex = 3;
        flyingDiscFrameIndex = (frames >> 1) & 3;
        // random Q*Bert speech
        if ((frames & 0xff) == 0xff) {
            Audio.playSound("qbert_speech_" + ((int) (6 * Math.random())));
            baloonVisibleFrame = frames + 30;
        }
        baloonVisible = frames < baloonVisibleFrame;
    }

    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, 48, 48 + 8, null);
        drawFlyingQBert(g);
        drawText(g);
    }
    
    private void drawText(Graphics2D g) {
        if (pressSpaceVisible) {
            BitmapFont.drawText(g, "PRESS 'SPACE' KEY TO START", 3, 15);
        } 
        BitmapFont.drawText(g, "PRESS 'I' KEY FOR INSTRUCTIONS", 1, 23);
        BitmapFont.drawText(g, "(C)1982 D. GOTTLIEB AND CO.", 2, 26);
        BitmapFont.drawText(g, "(C)2020 O.L.", 2, 27);
    }

    private void drawFlyingQBert(Graphics2D g) {
        int qx = (int) (x + 128);
        int qy = (int) (y + 120);
        g.drawImage(flyingDisc[flyingDiscFrameIndex], qx, qy, null);
        g.drawImage(qbert[qbertFrameIndex], qx, qy - 21, null);
        if (baloonVisible) {
            g.drawImage(baloon, qx - 16, qy - 40, null);
        }
    }

}
