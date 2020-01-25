package br.ol.qbert.scene;

import br.ol.qbert.infra.Enemy;
import br.ol.qbert.infra.Entity;
import br.ol.qbert.infra.SceneManager;
import br.ol.qbert.infra.Baloon;
import br.ol.qbert.infra.Harmless;
import br.ol.qbert.infra.PlayField;
import br.ol.qbert.infra.Audio;
import br.ol.qbert.infra.Actor;
import br.ol.qbert.infra.Scene;
import br.ol.qbert.actor.BallGreen;
import br.ol.qbert.actor.BallPurple;
import br.ol.qbert.actor.BallRed;
import br.ol.qbert.actor.Coily;
import br.ol.qbert.actor.FlyingDisc;
import br.ol.qbert.actor.QBert;
import br.ol.qbert.actor.Sam;
import br.ol.qbert.actor.Slick;
import br.ol.qbert.actor.Ugg;
import br.ol.qbert.actor.Wrongway;
import static br.ol.qbert.infra.Actor.State.*;
import static br.ol.qbert.infra.Constants.*;
import br.ol.qbert.infra.Hud;
import br.ol.qbert.infra.HudInfo;
import br.ol.qbert.infra.LevelInfo;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Level scene class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Level extends Scene {
    
    private final List<Entity> entities = new ArrayList<>();
    
    private int frames;
    
    private int spawnActorWait;
    private int deadWaitTime;

    private Baloon baloon;
    private PlayField playField;
    private QBert qbert;
    private Coily coily;
    private BallPurple ballPurple;
    
    private FlyingDisc flyingDisc1;
    private FlyingDisc flyingDisc2;
    private FlyingDisc flyingDisc3;
    private FlyingDisc flyingDisc4;
    
    private int qbertInvicibleTime;
    private boolean gameCleared;
    private int gameClearedTime;
    
    public Level(SceneManager sceneManager) {
        super(sceneManager);
    }

    public QBert getQbert() {
        return qbert;
    }

    @Override
    public void start() {
        addAllEntities();
        initAllEntities();
        HudInfo.reset();
    }

    @Override
    public void onPrepare() {
        frames = 0;
        spawnActorWait = (int) (15 + 15 * Math.random());
        deadWaitTime = 0;
        qbertInvicibleTime = 0;
        gameCleared = false;
        baloon.setVisible(false);
        playField.reset(1);
        qbert.reset();
        // qbert.reset() doesn't ensure Q*Bert initial position
        qbert.set(1, 1, 7, 0, 0, 0); 
        // TODO flying disc's
        flyingDisc1.y = (int) (3 + 3 * Math.random());
        flyingDisc1.reset();
        flyingDisc2.y = (int) (6 + 2 * Math.random());
        flyingDisc2.reset();
        flyingDisc3.x = (int) (3 + 3 * Math.random());
        flyingDisc3.reset();
        flyingDisc4.x = (int) (6 + 2 * Math.random());
        flyingDisc4.reset();
        killAllEnemiesAndHarmless();
        update();
    }
    
    private void addAllEntities() {
        playField = new PlayField(this);
        qbert = new QBert(this, playField);
        baloon = new Baloon(this, qbert);
        coily = new Coily(this, qbert, playField);
        ballPurple = new BallPurple(this, qbert, playField, coily);
        
        entities.add(baloon);
        entities.add(playField);
        entities.add(qbert);
        
        entities.add(coily);
        entities.add(ballPurple);
        
        entities.add(new Wrongway(this, qbert, playField));
        entities.add(new Ugg(this, qbert, playField));
        entities.add(new BallRed(this, qbert, playField));
        entities.add(new BallRed(this, qbert, playField));
        entities.add(new BallRed(this, qbert, playField));
        
        entities.add(new BallGreen(this, qbert, playField));
        entities.add(new Slick(this, qbert, playField));
        entities.add(new Sam(this, qbert, playField));
        
        entities.add(flyingDisc1 = new FlyingDisc(this, playField, 0, 4));
        entities.add(flyingDisc2 = new FlyingDisc(this, playField, 0, 6));
        entities.add(flyingDisc3 = new FlyingDisc(this, playField, 3, 0));
        entities.add(flyingDisc4 = new FlyingDisc(this, playField, 5, 0));             
        
        entities.add(new Hud(this));
    }

    private void initAllEntities() {
        entities.forEach((entity) -> {
            entity.init();
        });
    }
    
    @Override
    public void update() {
        frames++;
        
        if (isQBertInvincible()) {
            qbertInvicibleTime--;
        }

        if (gameCleared) {
            updateGameCleared();
            return;
        }
        
        if (playField.isGameCleared()) {
            killAllEnemiesAndHarmless();
            playField.blink();
            gameCleared = true;
            gameClearedTime = 150;
            Audio.playMusic("clear");
            return;
        }
        
        if (qbert.getState() == Actor.State.DEAD) {
            updateQBertDead();
        }
        else {
            updateAllEntities();
            spawnNewActors();
            checkCollisions();
            checkQBertDead();
        }        
    }

    private void updateGameCleared() {
        playField.update();
        if (gameClearedTime-- < 0) {
            // TODO: for now, all stages are the same.
            //       implement more levels difficulty later.
            LevelInfo.nextStage();
            if (LevelInfo.level == 2) {
                sceneManager.changeScene(SCENE_GAME_OVER);
            }
            else {
                sceneManager.changeScene(SCENE_LEVEL_PRESENTATION);
            }
        }
    }
    
    private void killAllEnemiesAndHarmless() {
        entities.stream().filter((entity) -> (entity instanceof Enemy || 
            entity instanceof Harmless)).forEachOrdered((entity) -> {
                
            ((Actor) entity).kill(false);
        });
    }
    
    private void updateQBertDead() {
        deadWaitTime--;
        if (deadWaitTime == 60) {
            if (!qbert.deathByFall()) {
                baloon.setVisible(true);
                Audio.playSound("qbert_speech_" + ((int) (6 * Math.random())));
            }
        }
        
        if (deadWaitTime <= 0) {
            // game over ?
            if (HudInfo.lives - 1 < 0){
                sceneManager.changeScene(SCENE_GAME_OVER, 0);
            }
            // next life
            else {
                HudInfo.lives--;
                baloon.setVisible(false);
                qbert.reset();
                killAllEnemiesAndHarmless();
            }
        }
    }
    
    private void updateAllEntities() {
        if (isQBertInvincible()) {
            entities.forEach((entity) -> {
                if (entity.equals(qbert) || entity instanceof FlyingDisc) {
                    entity.update();
                }
            });
        }
        else {
            entities.forEach((entity) -> {
                entity.update();
            });
        }
    }
    
    private void spawnNewActors() {
        spawnActorWait--;
        if (spawnActorWait <= 0) {
            
            // in this implementation, coily has priority to be revived
            if (ballPurple.getState() == DEAD && coily.getState() == DEAD
                && coily.needsRevive()) {
                    ballPurple.reset();
                    spawnActorWait = (int) (45 + 60 * Math.random());
                    return;
            }
            
            // others creatures
            Collections.shuffle(entities);
            for (Entity entity : entities) {
                
                // if ballPurple, it can be reused only if coily is also dead
                if (entity.equals(ballPurple) && 
                    coily.getState() != Actor.State.DEAD) {
                    
                    continue;
                }
                
                if (!coily.equals(entity) && 
                    (entity instanceof Enemy || entity instanceof Harmless)) {
                    
                    Actor actor = (Actor) entity;
                    if (frames > actor.getMinLevelFrames() && 
                        actor.getState() == Actor.State.DEAD) {
                        
                        actor.reset();
                        spawnActorWait = (int) (45 + 60 * Math.random());
                        return;
                    }
                }
            }
        }        
    }
    
    private void checkCollisions() {
        for (Entity entity : entities) {
            if (entity instanceof Enemy && !isQBertInvincible()) {
                Enemy enemy = (Enemy) entity;
                if (enemy.getState() != Actor.State.DEAD && 
                    enemy.collides(qbert)) {
                    
                    qbert.kill(true);
                    return;
                }
            }
            else if (entity instanceof Harmless) {
                Harmless harmless = (Harmless) entity;
                if (harmless.getState() != Actor.State.DEAD && 
                    harmless.collides(qbert)) {
                    
                    harmless.onCaught();
                }
            }
        }
    }
    
    private void checkQBertDead() {
        if (qbert.getState() == Actor.State.DEAD) {
            deadWaitTime = 90;
            Audio.playMusic("death");
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        Collections.sort(entities);
        entities.forEach((entity) -> {
            if (entity instanceof Enemy || entity instanceof Harmless) {
                if (entity.isVisible() && 
                    qbert.getState() != Actor.State.DEAD) {
                    
                    entity.draw(g);
                }
            }
            else if (entity.isVisible()) {
                entity.draw(g);
            }
        });
    }

    // when Q*Bert catches green ball.
    public void makeQBertInvicible() {
        qbertInvicibleTime = 120;
        Audio.playMusic("invincible");
    }
    
    public boolean isQBertInvincible() {
        return qbertInvicibleTime > 0;
    }

    public boolean isOnFlyingDisc(int x, int y) {
        for (Entity entity : entities) {
            if (entity instanceof FlyingDisc) {
                FlyingDisc flyingDisc = (FlyingDisc) entity;
                int col = flyingDisc.getLocation()[0] >> 4;
                int row = flyingDisc.getLocation()[1] >> 4;
                if (col == x && row == y) {
                    return true;
                }
            }
        }
        return false;
    }

    public void liftFlyingDisc(int x, int y) {
        for (Entity entity : entities) {
            if (entity instanceof FlyingDisc) {
                FlyingDisc flyingDisc = (FlyingDisc) entity;
                int col = flyingDisc.getLocation()[0] >> 4;
                int row = flyingDisc.getLocation()[1] >> 4;
                if (col == x && row == y) {
                    flyingDisc.lift();
                    return;
                }
            }
        }
    }
    
}