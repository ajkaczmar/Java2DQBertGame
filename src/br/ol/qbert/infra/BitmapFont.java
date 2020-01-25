package br.ol.qbert.infra;

import static br.ol.qbert.infra.Constants.*;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * BitmapFont class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class BitmapFont {
    
    private static BufferedImage bitmapFontImage;
    private static BufferedImage[] letters;
    
    private static int letterWidth = 8;
    private static int letterHeight = 8;

    static {
        loadFont(Assets.getImage(RES_BITMAP_FONT_ID), 16, 16);
    }
    
    public static void drawText(Graphics2D g, String text, int col, int row) {
        if (letters == null) {
            return;
        }
        int x = col * letterWidth;
        int y = row * letterHeight;
        int px = 0;
        int py = 0;
        for (int i=0; i<text.length(); i++) {
            int c = text.charAt(i);
            if (c == (int) '\n') {
                py += letterHeight;
                px = 0;
                continue;
            }
            else if (c == (int) '\r') {
                continue;
            }
            Image letter = letters[c];
            g.drawImage(letter, (int) (px + x), (int) (py + y), null);
            px += letterWidth;
        }
    }
    
    private static void loadFont(BufferedImage image, Integer cols, Integer rows) {
        int lettersCount = cols * rows; 
        bitmapFontImage = image;
        letters = new BufferedImage[lettersCount];
        letterWidth = bitmapFontImage.getWidth() / cols;
        letterHeight = bitmapFontImage.getHeight() / rows;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                letters[y * cols + x] = new BufferedImage(
                    letterWidth, letterHeight, BufferedImage.TYPE_INT_ARGB);
                
                Graphics2D ig = 
                    (Graphics2D) letters[y * cols + x].getGraphics();
                
                ig.drawImage(bitmapFontImage, 0, 0, letterWidth, letterHeight, 
                    x * letterWidth, y * letterHeight, 
                    x * letterWidth + letterWidth, 
                    y * letterHeight + letterHeight, null);
            }
        }
    }
    
}
