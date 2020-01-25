package br.ol.qbert.infra;

import static br.ol.qbert.infra.Constants.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Background class.

 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Background {
    
    private final int[] palette = new int[256];
    private final BufferedImage offscreen;
    private final int[] data;
    private final int[] data2;

    public Background() {
        offscreen = new BufferedImage(
            SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        
        data = ((DataBufferInt) offscreen.getRaster().
            getDataBuffer()).getData();
        
        data2 = new int[SCREEN_WIDTH * SCREEN_HEIGHT];
        
        for (int x = 0; x < SCREEN_WIDTH; x++) {
            palette[x] = (x >> 5) << 13;
        }
    }

    public void start() {
        for (int i = 0; i < 64; i++) {
            update();
        }
    }
    
    private int getData2(int x, int y) {
        while (x < 0) {
            x += SCREEN_WIDTH;
        }
        return data2[x + SCREEN_WIDTH * y];
    }
    
    private void setData2(int x, int y, int v) {
        while (x < 0) {
            x += SCREEN_WIDTH;
        }
        data2[x + SCREEN_WIDTH * y] = v;
    }
    
    public void update() {
        for (int x = 0; x < SCREEN_WIDTH; x++) {
            setData2(x, SCREEN_HEIGHT - 2, Math.random() > 0.55 ? 0 : 255);
        }
        for (int y = 5; y < SCREEN_HEIGHT - 2; y++) {
            for (int x = 0; x < SCREEN_WIDTH; x++) {
                int v = (int) ((int) ( (getData2(x, y)
                    + getData2(x - 1, y + 1)
                    + getData2(x + 0, y + 1)
                    + getData2(x + 1, y + 1)
                    + getData2(x + 0, y + 2) 
                        ) / 5.03 ) * 1.01);
                setData2(x, y, v);
            }
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = palette[data2[i]];
        }        
    }
    
    public void draw(Graphics2D g) {
        g.drawImage(offscreen, 0, 20, 256, 240, 0, 0, 256, 220, null);
    }
    
}
