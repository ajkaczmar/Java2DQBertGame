package br.ol.qbert.infra;

import java.awt.Graphics2D;

/**
 * Entity class.
 * 
 * zOrder = back (lower value) <-> front (higher value)
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Entity<T extends Scene> implements Comparable<Entity> {
    
    protected T scene;
    protected boolean visible = true;
    protected int zOrder;

    public Entity(T scene) {
        this.scene = scene;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getZOrder() {
        return zOrder;
    }
    
    public void init() {
        // implement your code here
    }
    
    // restore initial state of this entity
    // so we can reuse it after it dies
    public void reset() {
        // implement your code here
    }
    
    public void update() {
        // implement your code here
    }
    
    public void draw(Graphics2D g) {
        // implement your code here
    }
    
    public void drawDebug(Graphics2D g) {
        // implement your code here
    }

    @Override
    public int compareTo(Entity otherEntity) {
        return zOrder - otherEntity.zOrder;
    }
    
}
