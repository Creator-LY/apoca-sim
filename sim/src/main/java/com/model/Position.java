package com.model;

import javafx.scene.canvas.Canvas;

public class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Position(Canvas world, int diameter) {
        this(Math.random() * (world.getWidth() - diameter), 
             Math.random() * (world.getHeight() - diameter));
    }

    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }

    public void move(double dx, double dy, Canvas world, int diameter) {
        x += dx;
        y += dy;

        if (x < 0) {
            x = world.getWidth()-diameter;
        } else if (x > world.getWidth()-diameter) {
            x = 0;
        }

        if (y < 0) {
            y = world.getHeight()-diameter;
        } else if (y > world.getHeight()-diameter) {
            y = 0;
        }
    }
}
