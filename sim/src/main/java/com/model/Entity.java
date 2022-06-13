package com.model;


public abstract class Entity {
    private Position pos;
    private boolean removed;

    public Entity(double x, double y, double worldWidth, double worldHeight, int diameter) {
        pos = new Position(x, y, worldWidth, worldHeight, diameter);
    }

    public Entity(double worldWidth, double worldHeight, int diameter) {
        pos = new Position(worldWidth, worldHeight, diameter);
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

    public boolean getRemoved() {
        return removed;
    }

    public void setRemoved(boolean isRemove) {
        removed = isRemove;
    }
}
