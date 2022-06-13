package com.model;

public class Corpse extends Entity {
    private boolean isMutated;

    Corpse(double x, double y, double worldWidth, double worldHeight, int diameter, boolean isMutated) {
        super(x, y, worldWidth, worldHeight, diameter);
        this.isMutated = isMutated;
    }

    public boolean getIsMutated() {
        return isMutated;
    }

    public void setDefault(double x, double y, boolean isMutated) {
        setRemoved(false);
        getPos().setXY(x, y);
        this.isMutated = isMutated;
    }
}
