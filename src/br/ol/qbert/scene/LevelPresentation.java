package br.ol.qbert.scene;

import br.ol.qbert.infra.BitmapFont;
import br.ol.qbert.infra.SceneManager;
import br.ol.qbert.infra.Scene;
import static br.ol.qbert.infra.Constants.*;
import br.ol.qbert.infra.LevelInfo;
import java.awt.Graphics2D;

/**
 * LevelPresentation scene class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class LevelPresentation extends Scene {
    
    private int frames;
    
    public LevelPresentation(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public void onPrepare() {
        frames = 0;
    }

    @Override
    public void update() {
        if (frames++ > 120) {
            sceneManager.changeScene(SCENE_LEVEL);
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        BitmapFont.drawText(g, "LEVEL  " + 
            LevelInfo.getLevelRoundStr(), 11, 14);
    }

}
