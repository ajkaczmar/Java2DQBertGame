package br.ol.qbert.infra;

import java.awt.Graphics2D;

/**
 * Scene class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Scene {
    
    protected SceneManager sceneManager;
    
    public Scene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }
    
    public void start() {
        // implement your code here
    }
    
    public void onPrepare() {
        // implement your code here
    }
    
    public void onEnter() {
        // implement your code here
    }

    public void onLeave() {
        // implement your code here
    }
    
    public void update() {
        // implement your code here
    }
    
    public void draw(Graphics2D g) {
        // implement your code here
    }

}
