package com.model;

public class Resource extends Entity {
    public Resource(double worldWidth, double worldHeight, int diameter) {
        super(worldWidth, worldHeight, diameter);
    }

    public Resource(double x, double y, double worldWidth, double worldHeight, int diameter) {
        super(x, y, worldWidth, worldHeight, diameter);
    }
}
