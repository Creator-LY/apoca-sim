package com.model;

public abstract class Specie extends Entity {
    private double dx;
    private double dy;
    private boolean direct = false;
    private float speed = 1.8f;

    public Specie(double x, double y, double worldWidth, double worldHeight, int diameter) {
        super(x, y, worldWidth, worldHeight, diameter);
    }

    public Specie(double worldWidth, double worldHeight, int diameter) {
        super(worldWidth, worldHeight, diameter);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void setVelocity(double angle) {
        setDx(Math.cos(angle) * speed);
        setDy(Math.sin(angle) * speed);
    }

    public boolean getDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    public void move() {
        getPos().move(dx, dy);
    }

    public void setinit(double x, double y) {
        setRemoved(false);
        getPos().setXY(x, y);
        direct = false;
    }
}
