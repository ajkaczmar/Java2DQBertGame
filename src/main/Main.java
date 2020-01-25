package main;

import static br.ol.qbert.infra.Constants.SCALED_SCREEN_HEIGHT;
import static br.ol.qbert.infra.Constants.SCALED_SCREEN_WIDTH;
import br.ol.qbert.infra.Display;
import br.ol.qbert.infra.SceneManager;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Main class.
 * 
 * This is the Q*Bert game's entry point.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SceneManager qbert = new SceneManager();
            Display display = new Display(qbert);
            display.setBackground(Color.BLACK);
            display.setPreferredSize(
                    new Dimension(SCALED_SCREEN_WIDTH, SCALED_SCREEN_HEIGHT));
            
            JFrame frame = new JFrame();
            frame.setTitle("Java Q*Bert Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(display);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            display.start();
            display.requestFocus();
        });
    }
    
}
