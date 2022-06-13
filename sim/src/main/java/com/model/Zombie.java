package com.model;

public class Zombie extends Specie {
    private int spotRadius = 40;
    private SpecieTypes huntedType;
    private int huntedIndex;
    
    public Zombie(double x, double y, double worldWidth, double worldHeight, int diameter) {
        super(x, y, worldWidth, worldHeight, diameter);
        setSpeed(2.2f);
    }

    public Zombie(double worldWidth, double worldHeight, int diameter) {
        super(worldWidth, worldHeight, diameter);
        setSpeed(2.2f);
    }

    public int getSpotRadius() {
        return spotRadius;
    }

    public SpecieTypes getHuntedType() {
        return huntedType;
    }

    public int getHuntedIndex() {
        return huntedIndex;
    }

    public void setHuntedType(SpecieTypes huntedType) {
        this.huntedType = huntedType;
    }

    public void setHuntedIndex(int huntedIndex) {
        this.huntedIndex = huntedIndex;
    }

    public void setDefault(double x, double y) {
        super.setinit(x, y);
        huntedType = null;
    }
}
