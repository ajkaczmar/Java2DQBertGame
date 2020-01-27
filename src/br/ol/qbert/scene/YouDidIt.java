package br.ol.qbert.scene;

import br.ol.qbert.infra.BitmapFont;
import static br.ol.qbert.infra.Constants.*;
import br.ol.qbert.infra.HudInfo;
import br.ol.qbert.infra.Keyboard;
import br.ol.qbert.infra.Scene;
import br.ol.qbert.infra.SceneManager;
import br.ol.qbert.infra.ScoreInfo;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

/**
 * YouDidIt scene class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class YouDidIt extends Scene implements KeyListener {
    
    private int frames;
    private boolean blinkCursor;
    
    private final char[] playerInitials = new char[3];
    private int cursorIndex;
    
    public YouDidIt(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public void start() {
        Keyboard.setListener(this);
    }

    @Override
    public void onPrepare() {
        frames = 0;
        cursorIndex = 0;
        playerInitials[0] = ' ';
        playerInitials[1] = ' ';
        playerInitials[2] = ' ';
    }
    
    @Override
    public void update() {
        frames++;
        blinkCursor = ((frames >> 2) & 3) > 1;
        
        if (cursorIndex == 3 && Keyboard.isKeyPressed(KeyEvent.VK_ENTER)) {
            ScoreInfo.addHiscore("" + playerInitials[0] + playerInitials[1] + 
                playerInitials[2], HudInfo.score);
            
            sceneManager.changeScene(SCENE_HISCORES);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        BitmapFont.drawText(g, "YOU HAVE USURPED ALL", 6, 8);
        BitmapFont.drawText(g, "OTHERS TO BECOME THE", 6, 9);
        BitmapFont.drawText(g, "   SUPREME NOSER!   ", 6, 11);
        BitmapFont.drawText(g, "   \u0004\u0004\u0004\u0004\u0004\u0004"
            + "\u0004\u0004\u0004\u0004\u0004\u0004\u0004\u0004", 6, 12);
        
        BitmapFont.drawText(g, "KINDLY ENTER YOUR INITIALS", 3, 14);
        BitmapFont.drawText(g, "      FOR ALL TO SEE      ", 3, 15);

        AffineTransform at = g.getTransform();
        g.scale(2, 2);
        BitmapFont.drawText(g, "YOU DID IT!", 3, 2);
        
        BitmapFont.drawText(g, "" + 
            playerInitials[0] + playerInitials[1] + playerInitials[2], 6, 10);

        if (blinkCursor) {
            BitmapFont.drawText(g, "\u0004", 6 + cursorIndex, 11);
        }
        
        g.setTransform(at);
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (cursorIndex > 0 && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            playerInitials[--cursorIndex] = ' ';
        }
        else if (Character.isAlphabetic(e.getKeyChar()) && cursorIndex < 3) {
            playerInitials[cursorIndex++] = 
                Character.toUpperCase(e.getKeyChar());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
