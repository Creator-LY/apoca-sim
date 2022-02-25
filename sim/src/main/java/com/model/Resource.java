package com.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Resource {
    private Color color = Color.BLUE;
    public int diameter = 6;
    private Position pos;
    private Canvas world;
    private GraphicsContext gc;

    public Resource(Canvas world) {
        pos = new Position(world, diameter);
        this.world = world;
        draw();
    }

    public double getX() {
        return pos.getX();
    }

    public double getY() {
        return pos.getY();
    }

    public Position getPos() {
        return pos;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw() {
        gc = world.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillOval(pos.getX(), pos.getY(), diameter, diameter);
    }
}
