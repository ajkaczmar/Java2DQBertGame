package br.ol.qbert.infra;

/**
 * LevelInfo class.
 * 
 * Keep information about the current level.
 * Pyramid colors, number of steps, discs, characters, bonus, etc.
 * 
 * Reference:
 * https://strategywiki.org/wiki/Q*bert/Walkthrough
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class LevelInfo {

    public static int level = 1;
    public static int round = 1;
    
    public static int pyramidColorLeft;
    public static int pyramidColorRight;
    public static int pyramidColorTop1;
    public static int pyramidColorTop2;
    public static int pyramidColorTop3;
    public static int pyramidStepsRequiredToClear;
    public static int discsCount;
    public static int characters;
    public static int bonus;

    public static String getLevelRoundStr() {
        return level + "-" + round;
    }
    
    public static void nextStage() {
        round++;
        if (round > 4) {
            round = 1;
            level++;
        }
    }
    
}
