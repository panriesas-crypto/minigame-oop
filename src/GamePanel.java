import javax.swing.*;
import java.awt.*;
public class GamePanel extends JPanel {
    private final GameManager manager=new GameManager();
    public GamePanel(){setPreferredSize(new Dimension(600,540));setBackground(new Color(23,40,60));setFocusable(true);}
    @Override protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D g=(Graphics2D)graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        for(Floor floor:manager.getFloors()){
            g.setColor(new Color(168,124,77));g.fillRect(0,floor.getY(),600,9);
            g.setColor(Color.WHITE);g.drawString("Floor "+floor.getNumber(),12,floor.getY()-35);
            floor.getSnake().draw(g);floor.getApple().draw(g);
        }
        for(Ladder ladder:manager.getLadders()) ladder.draw(g);
        manager.getPlayer().draw(g);
        g.setColor(Color.WHITE);g.drawString("HP: "+manager.getPlayer().getHp()+"     Score: 0",420,22);
        g.drawString("Prototype - movement, jumping and enemies coming next",12,525);
        g.dispose();
    }
}
