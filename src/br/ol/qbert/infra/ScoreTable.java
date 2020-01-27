package br.ol.qbert.infra;

/**
 * ScoreTable class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class ScoreTable {
    
    public static final int SCORE_SQUARE_CHANGED_TARGET_COLOR = 25;
    public static final int SCORE_SQUARE_CHANGED_INTERMEDIATE_COLOR = 15;
    public static final int SCORE_GREEN_BALL_CAPTURED = 100;
    public static final int SCORE_SAM_SLICK_CAPTURED = 300;
    public static final int SCORE_LURING_COILY_OVER_EDGE = 500;
    public static final int SCORE_UNUSED_DISC_END_ROUND = 50;

    private static final int SCORE_BONUS_COMPLETING_ROUND[][] = {
        { 1000, 1250, 1500, 1750 }, // level 1
        { 2000, 2250, 2500, 2750 }, // level 2
        { 3000, 3250, 3500, 3750 }, // level 3
        { 4000, 4250, 4500, 4750 }, // level 4
        { 4750, 5000, 5000, 5000 }, // level 5
        { 5000 }  // level >= 6
    };
    
    public static int getScoreBonusCompletingRound() {
        int level = LevelInfo.level;
        int round = LevelInfo.round;
        int score;
        if (level > 5) {
            score = SCORE_BONUS_COMPLETING_ROUND[5][0];
        }
        else {
            score = SCORE_BONUS_COMPLETING_ROUND[level - 1][round - 1];
        }
        return score;
    }
    
}
