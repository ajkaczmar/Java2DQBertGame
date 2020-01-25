package br.ol.qbert.infra;

import static br.ol.qbert.infra.Constants.*;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Display class.

 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Display extends Canvas {
    
    private final BufferedImage offscreen;
    private BufferStrategy bs;
    private Thread mainThread;
    private boolean running;
    private final SceneManager sceneManager;
    
    public Display(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        offscreen = new BufferedImage(Constants.SCREEN_WIDTH, 
            Constants.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
    }
    
    public void start() {
        createBufferStrategy(2);
        bs = getBufferStrategy();
        Audio.start();
        sceneManager.start();
        startMainLoop();
        addKeyListener(new Keyboard());
    }
    
    private void startMainLoop() {
        running = true;
        mainThread = new Thread(() -> {
            while (running) {
                sceneManager.update();
                sceneManager.draw((Graphics2D) offscreen.getGraphics());
                Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
                g2d.scale(SCREEN_SCALE_X, SCREEN_SCALE_Y);
                g2d.drawImage(offscreen, 0, 0, null);
                g2d.dispose();
                bs.show();
                
                try {
                    Thread.sleep(GRAPHIC_SCREEN_REFRESH_RATE);
                } catch (InterruptedException ex) {
                }
            }
        });
        mainThread.start();
    }

}
