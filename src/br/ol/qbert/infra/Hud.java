package br.ol.qbert.infra;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Hud class.

 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Hud extends Entity {
    
    private int frames;
    private final BufferedImage[] player = new BufferedImage[4];
    private final BufferedImage[] changeTo = new BufferedImage[3];
    private BufferedImage lives;
    private BufferedImage blockTarget;
    private int level;
    private int round;
    public Hud(Scene scene) {
        super(scene);
        zOrder = 10000;
    }

    @Override
    public void init() {
        for (int i = 0; i < 4; i++) {
            player[i] = Assets.getImage("player_" + i);
            if (i < 3) {
                changeTo[i] = Assets.getImage("change_to_" + i);
            }
        }
        lives = Assets.getImage("lives");
    }
    
    public void refresh() {
        if (level != LevelInfo.level || round != LevelInfo.round) {
            level = LevelInfo.level;
            round = LevelInfo.round;
            int tmpLevel = level > 5 ? level % 5 : level;
            blockTarget = Assets.getImage(
                "block_target_" + tmpLevel + "_" + round);
        }
    }
    
    @Override
    public void update() {
        frames++;
    }

    @Override
    public void draw(Graphics2D g) {
        BitmapFont.drawText(g, "PLAYER:", 3, 4);
        BitmapFont.drawText(g, HudInfo.getScoreStr(), 3, 5);
        BitmapFont.drawText(g, "CHANGE TO:", 3, 6);
        BitmapFont.drawText(g, "=" + HudInfo.lives, 4, 10);

        BitmapFont.drawText(g, "LEVEL:" + level, 23, 6);
        BitmapFont.drawText(g, "ROUND:" + round, 23, 7);
        
        g.drawImage(player[(frames >> 2) & 3], 24, 32, null);
        g.drawImage(changeTo[(frames >> 2) % 3], 24, 64, null);
        g.drawImage(blockTarget, 40, 56, null);
        g.drawImage(lives, 24, 74, null);
    }
    
}
