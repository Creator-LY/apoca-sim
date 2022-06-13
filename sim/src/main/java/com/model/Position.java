package com.model;

public class Position {
    private double x;
    private double y;
    private double worldWidth;
    private double worldHeight;
    private double diameter;

    public Position(double x, double y, double worldWidth, double worldHeight, int diameter) {
        this.x = x;
        this.y = y;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.diameter = diameter;
    }

    public Position(double worldWidth, double worldHeight, int diameter) {
        this(Math.random() * (worldWidth - diameter), 
             Math.random() * (worldHeight - diameter),
             worldWidth, worldHeight, diameter);
    }   

    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getRelativeDistance(Position other) {
        if (other == null) {
            return Double.POSITIVE_INFINITY;
        }
        return Math.abs(getX()-other.getX()) + Math.abs(getY()-other.getY());
    }

    public void move(double dx, double dy) {
        x += dx;
        y += dy;

        if (x < 0) {
            x = worldWidth-diameter;
        } else if (x > worldWidth-diameter) {
            x = 0;
        }

        if (y < 0) {
            y = worldHeight-diameter;
        } else if (y > worldHeight-diameter) {
            y = 0;
        }
    }
}
