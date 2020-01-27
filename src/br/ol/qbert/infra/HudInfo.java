package br.ol.qbert.infra;

import static br.ol.qbert.infra.Constants.*;

/**
 * HudInfo class.
 * 
 * Keep information about the number of lives, etc.
 * 
 * Reference:
 * https://strategywiki.org/wiki/Q*bert/Walkthrough
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class HudInfo {
    
    public static int lives;
    public static int nextExtraLife;
    public static int score;
    
    public static void reset() {
        lives = 4;
        score = 0;
        nextExtraLife = SCORE_EXTRA_LIFE_1;
        LevelInfo.level = 1;
        LevelInfo.round = 1;
    }
    
    public static void addScore(int point) {
        score += point;
        if (score >= nextExtraLife) {
            nextExtraLife += SCORE_EXTRA_LIFE_2;
            lives++;
            Audio.playSound("prize");
        }
    }
    
    public static String getScoreStr() {
        String scoreStr = "0000000" + score;
        scoreStr = scoreStr.substring(scoreStr.length() - 7, scoreStr.length());
        return scoreStr;
    }

}
