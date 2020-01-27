package br.ol.qbert.infra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ScoreInfo class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class ScoreInfo {
    
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

    // hiscore

    public static class Player implements Comparable<Player> {
        
        public String name;
        public int score;

        public Player(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public int compareTo(Player o) {
            return o.score - score;
        }
        
    }

    public static final List<Player> HISCORES = new ArrayList<>();
    public static int newHiscorePlayerIndex;
    
    static {
        // initial hiscores
        addHiscore("ACE", 4000);
        addHiscore("TJC", 3000);
        addHiscore("JML", 2500);
        addHiscore("JAH", 2000);
        addHiscore("MJS", 1750);
        addHiscore("ECW", 1500);
        addHiscore("BLT", 1250);
        addHiscore("BMW", 1000);
        addHiscore("DMV", 950);
        addHiscore("FDA", 900);
        addHiscore("LMG", 825);
        addHiscore("DDT", 800);
        addHiscore("JCM", 775);
        addHiscore("ZAP", 750);
        addHiscore("NAB", 725);
        addHiscore("JUN", 700);
        addHiscore("HFR", 675);
        addHiscore("RON", 650);
        addHiscore("FXS", 625);
        addHiscore("DLB", 600);
        addHiscore("LEE", 575);
        addHiscore("CPB", 550);
        addHiscore("WBD", 525);
    }
    
    public static int addHiscore(String name, int score) {
        Player player = new Player(name, score);
        HISCORES.add(player);
        Collections.sort(HISCORES);
        int position = 0;
        for (int i = 0; i < HISCORES.size(); i++) {
            if (HISCORES.get(i).equals(player)) {
                newHiscorePlayerIndex = i;
                position = i;
                break;
            }
        }
        while (HISCORES.size() > 23) {
            HISCORES.remove(HISCORES.size() - 1);
        }
        return position;
    }
    
    public static boolean isHighscore() {
        return HudInfo.score > HISCORES.get(22).score;
    }
 
    public static String getPlayerStr(int index) {
        String order = "  " + (index + 1);
        order = order.substring(order.length() - 2, order.length());
        Player player = HISCORES.get(index);
        String name = player.name.trim() + "   ";
        name = name.substring(0, 3);
        return order + ") " + name + " " + player.score;
    } 
    
}
