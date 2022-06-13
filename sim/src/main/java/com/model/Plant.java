package com.model;


public class Plant extends Specie {
    private boolean isRoot;
    private int energy;
    private double childX;
    private double childY;

    public Plant(double x, double y, double worldWidth, double worldHeight, int diameter) {
        super(x, y, worldWidth, worldHeight, diameter);
        setSpeed(3.5f);
        isRoot = false;
    }

    public Plant(double worldWidth, double worldHeight, int diameter, boolean isRoot) {
        super(worldWidth, worldHeight, diameter);
        setSpeed(3.5f);
        this.isRoot = isRoot;
    }

    public double getChildX() {
        return childX;
    }

    public void setChildX(double childX) {
        this.childX = childX;
    }

    public double getChildY() {
        return childY;
    }

    public void setChildY(double childY) {
        this.childY = childY;
    }

    public void extend() {
        setChildX(getX()+getDx()*getSpeed());
        setChildY(getY()+getDy()*getSpeed());
    }

    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public boolean getIsRoot() {
        return isRoot;
    }

    public int getEnergy() {
        return energy;
    }

    public void inEnergy(int amount) {
        energy += amount;
    }

    public boolean grow(boolean isDay) {
        if (!isDay) { return false; }
        if (energy >= 100) {
            energy -= 100;
            return true;
        }
        return false;
    }

    public void setDefault(double x, double y) {
        super.setinit(x, y);
        isRoot = false;
        energy = 0;
    }
}
