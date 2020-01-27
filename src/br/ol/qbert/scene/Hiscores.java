package br.ol.qbert.scene;

import br.ol.qbert.infra.Assets;
import br.ol.qbert.infra.BitmapFont;
import static br.ol.qbert.infra.Constants.SCENE_TITLE;
import br.ol.qbert.infra.Keyboard;
import br.ol.qbert.infra.Scene;
import br.ol.qbert.infra.SceneManager;
import br.ol.qbert.infra.ScoreInfo;
import br.ol.qbert.infra.ScoreInfo.Player;
import com.sun.glass.events.KeyEvent;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Hiscores scene class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Hiscores extends Scene {
    
    private int frames;
    private final BufferedImage[] qberts = new BufferedImage[2];
    private boolean blinkPlayer;
    
    public Hiscores(SceneManager sceneManager) {
        super(sceneManager);
        qberts[0] = Assets.getImage("qbert_0");
        qberts[1] = Assets.getImage("qbert_2");
    }

    @Override
    public void onPrepare() {
        frames = 0;
    }
    
    @Override
    public void update() {
        frames++;
        blinkPlayer = ((frames >> 2) & 3) > 0;
        if (frames > 300 || 
            (frames > 110 && Keyboard.isKeyPressed(KeyEvent.VK_SPACE))) {
            
            sceneManager.changeScene(SCENE_TITLE);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        BitmapFont.drawText(g, "HIGH  SCORES", 10, 1);
        BitmapFont.drawText(g, "\u0004\u0004\u0004\u0004\u0004\u0004\u0004"
            + "\u0004\u0004\u0004\u0004\u0004", 10, 2);
        g.drawImage(qberts[0], 184, -3, null);
        g.drawImage(qberts[1], 56, -3, null);
        
        for (int i = 21; i >= 0; i -= 2) {
            if (i > 23 - (frames >> 2)) {
                if (ScoreInfo.newHiscorePlayerIndex != i || blinkPlayer) {
                    String playerStrLeft = ScoreInfo.getPlayerStr(i);
                    BitmapFont.drawText(g, playerStrLeft, 1, i + 7);
                }
                if (ScoreInfo.newHiscorePlayerIndex != i + 1 || blinkPlayer) {
                    String playerStrRight = ScoreInfo.getPlayerStr(i + 1);
                    BitmapFont.drawText(g, playerStrRight, 17, i + 7);
                }
            }
        }
        
        if ((frames >> 2) > 24) {
            if (ScoreInfo.newHiscorePlayerIndex == 0 && !blinkPlayer) {
                return;
            }
            
            Player first = ScoreInfo.HISCORES.get(0);
            BitmapFont.drawText(g, "" + first.score, 18, 5);
            AffineTransform at = g.getTransform();
            g.scale(2, 2);
            BitmapFont.drawText(g, first.name, 5, 2);
            g.setTransform(at);
        }
    }
    
}
