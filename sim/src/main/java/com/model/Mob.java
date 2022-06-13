package com.model;

public class Mob extends Specie {
    private int spotRadius = 45;
    private SpecieTypes huntedType;
    private int huntedIndex;
    private double perferAngle = Math.random() * 2 * Math.PI;
    private boolean birth = false;
    private int birthPeriod = 0;
    private int starveCount = 0;  //168 actions = 7 days
    private int energy = 200;

    public Mob(double x, double y, double worldWidth, double worldHeight, int diameter) {
        super(x, y, worldWidth, worldHeight, diameter);
        setSpeed(2.4f);
    }

    public Mob(double worldWidth, double worldHeight, int diameter) {
        super(worldWidth, worldHeight, diameter);
        setSpeed(2.4f);
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

    public double getPerferAngle() {
        return perferAngle;
    }

    public boolean getBirth() {
        return birth;
    }

    public void setBirth(boolean birth) {
        this.birth = birth;
    }

    public void incrementBirthPeriod() {
        birthPeriod++;
    }

    public void resetBirthPeriod() {
        birthPeriod = 0;
    }

    public boolean giveBirth() {
        return birthPeriod >= 200;
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
        energy--;
    }

    public int getStarveCount() {
        return starveCount;
    }

    public boolean checkStarve() {
        if (energy == 0) {
            starveCount ++;
        }
        if (starveCount > 200) {
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
        huntedType = null;
        birth = false;
        birthPeriod = 0;
        starveCount = 0;
        energy = 250;
        perferAngle = Math.random() * 2 * Math.PI;
    }
}
