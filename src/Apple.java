package com.applehunter;
import java.awt.*;
public class Apple extends GameObject {
    public Apple(int x,int y) { super(x,y); }
    @Override public void draw(Graphics2D g) {
        g.setColor(new Color(238,75,70)); g.fillOval(x,y,18,18);
        g.setColor(new Color(90,190,80)); g.fillOval(x+9,y-5,9,5);
    }
}
