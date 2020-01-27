package br.ol.qbert.scene;

import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.BitmapFont;
import br.ol.qbert.infra.SceneManager;
import br.ol.qbert.infra.Scene;
import static br.ol.qbert.infra.Constants.*;
import br.ol.qbert.infra.ScoreInfo;
import java.awt.Graphics2D;

/**
 * GameOver scene class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class GameOver extends Scene {
    
    private int frames;
    private int gameOverLength;
    
    public GameOver(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public void onPrepare() {
        frames = 0;
        gameOverLength = 0;
    }

    @Override
    public void onEnter() {
        Audio.playSound("game_over");
    }
    
    @Override
    public void update() {
        if (frames++ > 210) {
            if (ScoreInfo.isHighscore()) {
                sceneManager.changeScene(SCENE_YOU_DID_IT);
            }
            else {
                ScoreInfo.newHiscorePlayerIndex = -1;
                sceneManager.changeScene(SCENE_HISCORES);
            }
        }
        gameOverLength = Math.min(frames >> 2, 10);
    }
    
    @Override
    public void draw(Graphics2D g) {
        String gameOver = "GAME  OVER".substring(0, gameOverLength);
        BitmapFont.drawText(g, gameOver, 11, 14);
    }

}
