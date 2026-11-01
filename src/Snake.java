package com.applehunter;
import java.awt.*;
public class Snake extends GameObject {
    private final int floorNumber;
    public Snake(int x,int y,int floorNumber) { super(x,y); this.floorNumber=floorNumber; }
    public int getFloorNumber() { return floorNumber; }
    @Override public void draw(Graphics2D g) {
        g.setColor(new Color(69,165,92));
        for(int i=0;i<5;i++) g.fillRoundRect(x+i*17,y,18,15,7,7);
        g.setColor(Color.BLACK); g.fillOval(x+76,y+3,3,3);
    }
}
