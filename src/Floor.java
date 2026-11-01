package com.applehunter;
public class Floor {
    private final int number, y;
    private final Snake snake;
    private final Apple apple;
    public Floor(int number,int y) {
        this.number=number; this.y=y;
        this.snake=new Snake(125+(number%2)*110,y-15,number);
        this.apple=new Apple(65+(number%2)*175,y-22);
    }
    public int getNumber(){return number;}
    public int getY(){return y;}
    public Snake getSnake(){return snake;}
    public Apple getApple(){return apple;}
}
