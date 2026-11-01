package com.applehunter;
import java.awt.*;
public class Player extends GameObject {
    private int hp = 100;
    public Player(int x,int y) { super(x,y); }
    public int getHp() { return hp; }
    @Override public void draw(Graphics2D g) {
        g.setColor(new Color(255,207,165)); g.fillOval(x+5,y,14,14);
        g.setColor(new Color(65,155,240)); g.fillRect(x+4,y+14,16,17);
        g.setColor(new Color(80,110,190)); g.fillRect(x+4,y+31,6,8); g.fillRect(x+14,y+31,6,8);
    }
}
