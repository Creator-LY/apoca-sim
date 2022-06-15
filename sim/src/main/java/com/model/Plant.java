package com.model;


public class Plant extends Specie {
    private boolean isRoot;
    private int energy;
    private double childX;
    private double childY;
    private boolean onFire;

    public Plant(double x, double y, double worldWidth, double worldHeight, int diameter) {
        super(x, y, worldWidth, worldHeight, diameter);
        setSpeed(5.0f);
        isRoot = false;
    }

    public Plant(double worldWidth, double worldHeight, int diameter, boolean isRoot) {
        super(worldWidth, worldHeight, diameter);
        setSpeed(5.0f);
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
        setChildX(getX()+getDx()*(getSpeed()+Math.random()*10));
        setChildY(getY()+getDy()*(getSpeed()+Math.random()*10));
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
        if (energy >= 200) {
            energy -= 200;
            return true;
        }
        return false;
    }

    public void setOnFire(boolean fire) {
        onFire = fire;
    }

    public boolean getOnFire() {
        return onFire;
    }

    public void setDefault(double x, double y) {
        super.setinit(x, y);
        isRoot = false;
        energy = 0;
    }
}
