package com.model;

public class SubHuman extends Specie {
    private int starveCount = 0;  //168 actions = 7 days
    private int energy = 100;
    private int spotRadius = 35;

    public SubHuman(double x, double y, double worldWidth, double worldHeight, int diameter) {
        super(x, y, worldWidth, worldHeight, diameter);
    }

    public SubHuman(double worldWidth, double worldHeight, int diameter) {
        super(worldWidth, worldHeight, diameter);
    }

    public int getSpotRadius() {
        return spotRadius;
    }
    
    public void inEnergy(int amount) {
        if (starveCount == 0) {
            energy += 72*amount;
        } else {
            energy += 36*amount;
            starveCount = 0;
        }
    }

    public void deEnergy() {
        energy-=2;
    }

    public int getStarveCount() {
        return starveCount;
    }

    public boolean checkStarve() {
        if (energy == 0) {
            starveCount ++;
        }
        if (starveCount > 168) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void move() {
        if (energy > 0) {
            super.move();
            deEnergy();
        } 
    }

    public void setDefault(double x, double y) {
        super.setinit(x, y);
        starveCount = 0;
        energy = 100;
    }
}
