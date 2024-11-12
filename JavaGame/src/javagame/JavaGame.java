
package javagame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


/**
 *
 * @author vadim
 */
public class JavaGame extends JFrame {
    private static JavaGame java_game;
    
    private static Image bank;
    private static Image money;
    private static Image end;
    
    private static int score = 0;
    private static long last_frame_time;
    private static float drop_left = 200;
    private static float drop_top = -10;
    private static float drop_v = 200;
    
    public static void main(String[] args) throws IOException {
        bank = ImageIO.read( JavaGame.class.getResourceAsStream("bank.jpg") );
        money = ImageIO.read( JavaGame.class.getResourceAsStream("money.png") );
        end = ImageIO.read( JavaGame.class.getResourceAsStream("over.png") );
        
        java_game = new JavaGame();
        java_game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        java_game.setLocation(200, 50);
        java_game.setSize(900, 600);
        java_game.setResizable(false);
        
        last_frame_time = System.nanoTime();
        
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                
                float drop_right = drop_left + end.getWidth(null);
                float drop_bottom = drop_top + end.getHeight(null);
                
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                
                if(is_drop) {
                    drop_top = -10;
                    drop_left = (int)(Math.random() * (game_field.getWidth() - end.getWidth(null)));
                    drop_v = drop_v + 10;
                    score++;
                    java_game.setTitle("Score: " + score);
                }
            }
        });
        
        java_game.add( game_field );
        
        java_game.setVisible(true);
    }
    
    public static void onRepaint( Graphics g ) {
        long current_time = System.nanoTime();
        float delta_time = ( current_time - last_frame_time ) * 0.000000001f;
        last_frame_time = current_time;
        drop_top = drop_top + drop_v * delta_time;
        
        g.drawImage(bank, 300, 300, java_game);
        g.drawImage(money, (int)drop_left, (int)drop_top, 100, 100, java_game);
        
        if ( drop_top > java_game.getHeight() ) g.drawImage(end, 210, 150, java_game);
    }
    
    public static class GameField extends JPanel {
        @Override
        protected void paintComponent( Graphics g ) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
    
}
