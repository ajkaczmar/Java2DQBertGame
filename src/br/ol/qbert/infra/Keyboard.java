package br.ol.qbert.infra;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Keyboard class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Keyboard implements KeyListener {

    public static boolean[] keys = new boolean[256];
    private static KeyListener listener;

    public static void setListener(KeyListener listener) {
        Keyboard.listener = listener;
    }
    
    public static boolean isKeyPressed(int keyCode) {
        return keys[keyCode];
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        listener.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() >= 0 && e.getKeyCode() < 256) {
            keys[e.getKeyCode()] = true;
            listener.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() >= 0 && e.getKeyCode() < 256) {
            keys[e.getKeyCode()] = false;
            listener.keyReleased(e);
        }
    }
    
}
