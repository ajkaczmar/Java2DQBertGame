package br.ol.qbert.scene;

import br.ol.qbert.actor.QBert;
import br.ol.qbert.infra.Assets;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.BitmapFont;
import br.ol.qbert.infra.SceneManager;
import br.ol.qbert.infra.Scene;
import static br.ol.qbert.infra.Constants.*;
import br.ol.qbert.infra.LevelInfo;
import br.ol.qbert.infra.PlayField;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * LevelPresentation scene class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class LevelPresentation extends Scene {
    
    private int frames;
    private final BufferedImage image;
    private final PlayField playField;
    private final QBert qbert;
    private boolean blinkLevel;
    
    /**
     * 8   0
     *  \ /
     *  / \
     * 2   10
     */
    private static final int[][] STEPS = {
        { 10, 8, 2, 0 }, // level 1
        { 10, 2, 8, 10, 0, 2 }, // level 2
        { 10, 2, 8, 0, 2, 10, 0 }, // level 3
        { 2, 10, 8, 0, 2, 10, 8, 0, 2 } // level >= 4
    };
    
    private boolean stepsFinished;
    private int stepsFinishedFrame;
    
    public LevelPresentation(SceneManager sceneManager) {
        super(sceneManager);
        image = Assets.getImage("level");
        playField = new PlayField(this, 2);
        qbert = new QBert(0, this, playField) {
            @Override
            public void updateIdle() {
                // disable controls
            }
        };
    }

    @Override
    public void start() {
        playField.init();
        qbert.init();
    }

    @Override
    public void onPrepare() {
        frames = 0;
        qbert.reset();
        qbert.set(1, 1, 7, 0, 0, 0);
        qbert.update();
        // LevelInfo.level = 3; // test
        playField.reset(LevelInfo.level);
        playField.update();
        blinkLevel = true;
        stepsFinished = false;
        stepsFinishedFrame = 0;
    }

    @Override
    public void update() {
        frames++;
        playField.update();
        qbert.update();
        blinkLevel = frames > 45 || (((frames >> 3) & 1) == 0);
        boolean jumped = false;
        int stepIndex = frames - 45;
        if (!stepsFinished && stepIndex >= 0 && (stepIndex % 20) == 0) {
            stepIndex = stepIndex / 20;
            int level = (LevelInfo.level > 4 ? 4 : LevelInfo.level) - 1;
            if (stepIndex > STEPS[level].length - 1) {
                qbert.setCurrentFrame(0);
                stepsFinished = true;
                stepsFinishedFrame = frames;
            }
            else {
                int step = STEPS[level][stepIndex];
                if ((step & 0x8) == 0x0) {
                    qbert.jumpX((step & 0x3) - 1);
                    jumped = true;
                }
                else if ((step & 0x8) == 0x8) {
                    qbert.jumpY((step & 0x3) - 1);
                    jumped = true;
                }
            }
        }
        if (jumped) {
            Audio.playSound("jump");
        }
        if (stepsFinished && (frames - stepsFinishedFrame) > 30) {
            sceneManager.changeScene(SCENE_LEVEL);
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        playField.draw(g);
        qbert.draw(g);
        g.drawImage(image, 0, 0, null);
        AffineTransform at = g.getTransform();
        g.scale(3, 2);
        g.translate(-1, 1);
        if (blinkLevel) {
            BitmapFont.drawText(g, "" + LevelInfo.level, 5, 10);
        }
        g.setTransform(at);
    }

}
