package br.ol.qbert.infra;

import br.ol.qbert.actor.QBert;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Baloon class.

 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Baloon extends Entity {
    
    private final QBert qbert;
    private final BufferedImage image;

    public Baloon(Scene scene, QBert qBert) {
        super(scene);
        this.qbert = qBert;
        image = Assets.getImage("baloon");
        visible = false;
        zOrder = 1000;
    }

    @Override
    public void draw(Graphics2D g) {
        int x = qbert.spx - 24;
        int y = qbert.spy - 36;
        g.drawImage(image, x, y, null);
    }
    
}
