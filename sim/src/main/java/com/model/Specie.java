package com.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Specie {
    public int diameter = 10;

    private Position pos;
    private double dx;
    private double dy;
    private Canvas world;
    private GraphicsContext gc;

    public Specie(double x, double y, Canvas world) {
        pos = new Position(x, y);

        this.world = world;
        gc = world.getGraphicsContext2D();
    }

    public Specie(Canvas world) {
        pos = new Position(world, diameter);

        this.world = world;
        gc = world.getGraphicsContext2D();
    }

    public Position getPos() {
        return pos;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void move() {
        pos.move(dx, dy, world, diameter);
    }

    public void draw(Color color) {
        gc.setFill(color);
        gc.strokeOval(pos.getX(), pos.getY(), diameter, diameter);
        gc.fillOval(pos.getX(), pos.getY(), diameter, diameter);
    }
}
