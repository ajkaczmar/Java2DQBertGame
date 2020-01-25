package br.ol.qbert.scene;

import br.ol.qbert.infra.Assets;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.BitmapFont;
import static br.ol.qbert.infra.Constants.*;
import br.ol.qbert.infra.Keyboard;
import br.ol.qbert.infra.SceneManager;
import br.ol.qbert.infra.Scene;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Instructions scene class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Instructions extends Scene {
    
    private int frames;
    private final BufferedImage titleSmall;
    
    private final BufferedImage[] qbert = new BufferedImage[3];
    private int qbertIndex;
    private double qbertX;
    private double qbertY;
    private double qbertTy;
    private double qbertVy;
    
    private final BufferedImage[] redBall = new BufferedImage[2];
    private int redBallIndex;
    private double redBallX;
    private double redBallY;
    private double redBallTy;
    private double redBallVy;
    
    private final BufferedImage baloon;
    private int baloonVisibleFrame;
    private boolean baloonVisible;
    
    private final String text =
        "\r\n" +
        "       JUMP ON SQUARES TO\n" +
        "       CHANGE THEM TO\n" +
        "       THE TARGET COLOR\n" +
        "                    \r\n" +
        "        STAY ON PLAYFIELD!\n" +
        "        JUMPING OFF RESULTS\n" +
        "        IN A FATAL PLUMMET\n" +
        "        UNLESS A DISK IS THERE\n" +
        "                    \r\n" +
        "         AVOID ALL OBJECTS\n" +
        "         AND CREATURES THAT\n" +
        "         ARE NOT GREEN\n" +
        "                    \r\n" +
        "          USE SPINNING DISKS\n" +
        "          TO LURE SNAKE TO\n" +
        "          HIS DEATH\n" +
        "                    \r\n" +
        "           EXTRA LIFE AT\n" +
        "           8000 AND EACH\n" +
        "           ADDITIONAL 14000 ";
    
    private int textIndex;
    
    public Instructions(SceneManager sceneManager) {
        super(sceneManager);
        titleSmall = Assets.getImage("title_small");
        qbert[0] = Assets.getImage("qbert_0");
        qbert[1] = Assets.getImage("qbert_1");
        qbert[2] = Assets.getImage("qbert_8");
        redBall[0] = Assets.getImage("ball_red_0");
        redBall[1] = Assets.getImage("ball_red_1");
        baloon = Assets.getImage("baloon");
    }

    @Override
    public void onPrepare() {
        frames = 0;
        qbertX = 20;
        qbertY = 28;
        qbertTy = 24;
        qbertVy = 0;
        qbertIndex = 0;
        redBallX = 24;
        redBallY = -16;
        redBallTy = -16;
        redBallVy = 0;
        redBallIndex = 0;
        textIndex = 0;
        baloonVisibleFrame = -100;
        baloonVisible = false;
    }

    @Override
    public void update() {
        frames++;
        textIndex = frames >> 1;
        if (textIndex > text.length() - 1) {
            textIndex = text.length() - 1;
        }
        if (frames > 1250 || Keyboard.isKeyPressed(KeyEvent.VK_SPACE)) {
            sceneManager.changeScene(SCENE_TITLE);
        }
        if (text.charAt(textIndex) == '\r' && (frames & 0x01) == 1) {
            jumpQBert(qbertY + 33);
        }
        
        if (frames == 1020) {
            fallRedBall();
        }
        else if (frames < 1020) {
            updateQBert();
        }
        else if (frames > 1020) {
            updateRedBall();
        }
        
        baloonVisible = frames >= baloonVisibleFrame && 
            frames < baloonVisibleFrame + 60;
        
        if (frames == baloonVisibleFrame) {
            Audio.playSound("qbert_speech_" + ((int) (6 * Math.random())));
        }
    }
    
    private void updateQBert() {
        if (qbertTy > qbertY) {
            qbertX += 0.5;
            qbertY += qbertVy;
            qbertVy += 1;
            qbertVy = qbertVy > 6 ? 6 : qbertVy;
            qbertIndex = 1;
        }
        if (qbertTy <= qbertY) {
            qbertY = qbertTy;
            qbertVy = 0;
            qbertIndex = 0;
        }
    }
    
    private void jumpQBert(double ty) {
        qbertTy = ty;
        qbertVy = -4;
        Audio.playSound("jump");
    }
    
    private void updateRedBall() {
        if (redBallTy > redBallY) {
            redBallX += 0.5;
            redBallY += redBallVy;
            redBallVy += 1;
            redBallVy = redBallVy > 6 ? 6 : redBallVy;
            redBallIndex = 1;
        }
        if (redBallTy <= redBallY && redBallY < 240) {
            redBallY = redBallTy;
            redBallTy = 300;
            redBallVy = -redBallVy;
            redBallIndex = 0;
            qbertIndex = 2;
            baloonVisibleFrame = frames + 30;
            Audio.playSound("qbert_hit");
        }
    }

    private void fallRedBall() {
        redBallTy = 190;
        redBallVy = -12;
        Audio.playSound("ball_falling");
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.drawImage(titleSmall, 16, 16, null);
        BitmapFont.drawText(g, text.substring(0, textIndex), 0, 6);
        g.drawImage(qbert[qbertIndex], (int) qbertX, (int) qbertY, null);
        
        g.drawImage(redBall[redBallIndex], 
            (int) redBallX, (int) redBallY, null);

        if (baloonVisible) {
            g.drawImage(baloon, (int) (qbertX - 16), (int) (qbertY - 16), null);
        }
    }

}
