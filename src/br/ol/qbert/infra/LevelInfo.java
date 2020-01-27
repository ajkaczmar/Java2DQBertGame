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

    public static final int MASK_COILY = 1;
    public static final int MASK_RED_BALL_1 = 2;
    public static final int MASK_RED_BALL_2 = 4;
    public static final int MASK_RED_BALL_3 = 8;
    public static final int MASK_UGG = 16;
    public static final int MASK_WRONG_WAY = 32;
    public static final int MASK_GREEN_BALL = 64;
    public static final int MASK_SLICK = 128;
    public static final int MASK_SAM = 256;
    public static final int MASK_DISC_1 = 512;
    public static final int MASK_DISC_2 = 1024;
    public static final int MASK_DISC_3 = 2048;
    public static final int MASK_DISC_4 = 4096;
    
    public static final int AVAILABLE_CHARACTERS[][] = {
        { MASK_RED_BALL_1 | MASK_RED_BALL_2 | MASK_COILY |
            MASK_DISC_1 | MASK_DISC_4, // 1-1
            
          MASK_RED_BALL_1 | MASK_RED_BALL_2 | MASK_RED_BALL_3 | MASK_COILY |
            MASK_DISC_2 | MASK_DISC_3,//1-2
          
          MASK_GREEN_BALL | MASK_COILY | MASK_UGG | MASK_WRONG_WAY |
            MASK_DISC_1 | MASK_DISC_4, // 1-3
          
          MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_SLICK | MASK_SAM |
            MASK_DISC_2 | MASK_DISC_3}, // 1-4
        
        { MASK_GREEN_BALL | MASK_COILY | MASK_UGG | MASK_WRONG_WAY | 
            MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_4, // 2-1
            
          MASK_GREEN_BALL | MASK_COILY | MASK_UGG | MASK_WRONG_WAY | 
            MASK_SLICK | MASK_SAM |
            MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4, // 2-2
          
          MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_4, // 2-3
          
          MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_UGG | 
            MASK_WRONG_WAY | MASK_SLICK | MASK_SAM |
            MASK_DISC_2 | MASK_DISC_3}, // 2-4
        
        { MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4, // 3-1
            
          MASK_GREEN_BALL | MASK_COILY | MASK_UGG | MASK_WRONG_WAY | 
            MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4, // 3-2
          
          MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_UGG | 
            MASK_WRONG_WAY | MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_4, // 3-3
          
          MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_UGG | 
            MASK_WRONG_WAY | MASK_SLICK | MASK_SAM |
            MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4}, // 3-4
        
        { MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4, // 4-1
            
          MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_UGG | 
            MASK_WRONG_WAY | MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4, // 4-2
          
          MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4, // 4-3
          
          MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_UGG | 
            MASK_WRONG_WAY | MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4}, // 4-4
        
        { MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4, // 5-1
            
          MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4, // 5-2
          
          MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_UGG | 
            MASK_WRONG_WAY | MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4, // 5-3
          
          MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_UGG | 
            MASK_WRONG_WAY | MASK_SLICK | MASK_SAM |
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4}, // 5-4
        
        { MASK_GREEN_BALL | MASK_RED_BALL_1 | MASK_RED_BALL_2 | 
            MASK_RED_BALL_3 | MASK_COILY | MASK_UGG | 
            MASK_WRONG_WAY | MASK_SLICK | MASK_SAM |
            
            MASK_DISC_1 | MASK_DISC_2 | MASK_DISC_3 | MASK_DISC_4} // >6
    };
    
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
            if (level > 9) {
                level = 9;
            }
        }
    }
    
    public static boolean isCharacterAvailable(int characterMask) {
        int available;
        if (level > 5) {
            available = AVAILABLE_CHARACTERS[5][0];
        }
        else {
            available = AVAILABLE_CHARACTERS[level - 1][round - 1];
        }
        return (available & characterMask) == characterMask;
    }
    
}
