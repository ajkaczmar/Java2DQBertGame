package br.ol.qbert.scene;

import br.ol.qbert.infra.Assets;
import br.ol.qbert.infra.SceneManager;
import br.ol.qbert.infra.Scene;
import static br.ol.qbert.infra.Constants.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * OLPresents scene class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class OLPresents extends Scene {
    
    private final BufferedImage image;
    private int frames;
    
    public OLPresents(SceneManager sceneManager) {
        super(sceneManager);
        image = Assets.getImage("ol_presents");
    }
    
    @Override
    public void onPrepare() {
        frames = 0;
    }
    
    @Override
    public void update() {
        if (frames++ > 150) {
            sceneManager.changeScene(SCENE_TITLE);
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, 48, 96, null);
    }

}
