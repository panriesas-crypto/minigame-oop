package com.applehunter;
import java.awt.Graphics2D;
public abstract class GameObject {
    protected int x, y;
    protected GameObject(int x, int y) { this.x=x; this.y=y; }
    public abstract void draw(Graphics2D g);
}
