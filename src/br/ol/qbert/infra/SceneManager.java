package br.ol.qbert.infra;

import static br.ol.qbert.infra.Constants.*;
import java.awt.AlphaComposite;
import static java.awt.AlphaComposite.*;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import br.ol.qbert.scene.GameOver;
import br.ol.qbert.scene.Hiscores;
import br.ol.qbert.scene.Instructions;
import br.ol.qbert.scene.Level;
import br.ol.qbert.scene.LevelPresentation;
import br.ol.qbert.scene.OLPresents;
import br.ol.qbert.scene.Title;
import br.ol.qbert.scene.YouDidIt;
import java.awt.Color;
import java.awt.Composite;

/**
 * SceneManager class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class SceneManager {
    
    private final Background background;
    
    protected final Map<String, Scene> scenes = new HashMap<>();
    protected Scene currentScene;
    protected Scene nextScene;
    
    private static final AlphaComposite[] ALPHAS = new AlphaComposite[21];
    private int fadeValue;
    private int fadeStatus;
    private int waitBetweenFade = 40;
    private int flashValue;
    
    public SceneManager() {
        background = new Background();
        
        // cache all alpha composite's
        for (int i = 0; i < 21; i++) {
            float a = i / 20f;
            ALPHAS[i] = AlphaComposite.getInstance(SRC_OVER, a);
        }
    }

    public Map<String, Scene> getScenes() {
        return scenes;
    }
    
    public void start() {
        // background.start();
        createAllScenes();
        startAllScenes();
        
        // start scene
        changeScene(SCENE_OL_PRESENTS);
        //changeScene(SCENE_TITLE);
        //changeScene(SCENE_HISCORES);
        //changeScene(SCENE_INSTRUCTIONS);
        //changeScene(SCENE_LEVEL_PRESENTATION);
        //changeScene(SCENE_LEVEL);
        //changeScene(SCENE_GAME_OVER);
        //changeScene(SCENE_YOU_DID_IT);
    }
    
    protected void createAllScenes() {
        scenes.put(SCENE_OL_PRESENTS, new OLPresents(this));
        scenes.put(SCENE_TITLE, new Title(this));
        scenes.put(SCENE_HISCORES, new Hiscores(this));
        scenes.put(SCENE_INSTRUCTIONS, new Instructions(this));
        scenes.put(SCENE_LEVEL_PRESENTATION, new LevelPresentation(this));
        scenes.put(SCENE_LEVEL, new Level(this));
        scenes.put(SCENE_GAME_OVER, new GameOver(this));
        scenes.put(SCENE_YOU_DID_IT, new YouDidIt(this));
    }
    
    protected void startAllScenes() {
        scenes.values().forEach((scene) -> {
            scene.start();
        });
    }
    
    public void update() {
        background.update();
        
        if (fadeStatus == 1) {
            fadeValue--;
            if (fadeValue < 0) {
                fadeValue = 0;
                fadeStatus = 3;
                if (currentScene != null) {
                    currentScene.onLeave();
                }
                currentScene = nextScene;
                currentScene.onPrepare();
                nextScene = null;
            }
        }
        else if (fadeStatus >= 3) {
            fadeStatus++;
            if (fadeStatus > waitBetweenFade) {
                fadeStatus = 2;
            }
        }
        else if (fadeStatus == 2) {
            fadeValue++;
            if (fadeValue > 20) {
                fadeValue = 20;
                fadeStatus = 0;
                currentScene.onEnter();
            }
        }
        else {
            if (currentScene != null) {
                currentScene.update();
            }
        }
        
        if (flashValue > 0) {
            flashValue -= 2;
        }
    }
    
    public void draw(Graphics2D g) {
        g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        background.draw(g);
        if (flashValue > 0) {
            Composite compositeOriginal = g.getComposite();
            g.setComposite(ALPHAS[flashValue]);
            g.setColor(Color.GREEN);
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            g.setComposite(compositeOriginal);
        }
        if (currentScene != null) {
            if (fadeStatus != 0) {
                g.setComposite(ALPHAS[fadeValue]);
            }
            currentScene.draw(g);
        }
    }

    public void changeScene(String nextSceneId) {
        changeScene(nextSceneId, 40);
    }
    
    public void changeScene(String nextSceneId, int waitBetweenFade) {
        nextScene = scenes.get(nextSceneId);
        fadeStatus = 1;
        fadeValue = 20;
        this.waitBetweenFade = waitBetweenFade;
    }
    
    public void flash() {
        flashValue = 16;
    }
    
}
