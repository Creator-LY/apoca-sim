package com.model;

public class Human extends Specie {
    private String gender = selectGender();
    private boolean birth = false;
    private int birthPeriod = 0;
    private int starveCount = 0;  //168 actions = 7 days
    private int energy = 100;
    private int spotRadius = 30;
    
    public Human(double x, double y, double worldWidth, double worldHeight, int diameter) {
        super(x, y, worldWidth, worldHeight, diameter);
    }

    public Human(double worldWidth, double worldHeight, int diameter) {
        super(worldWidth, worldHeight, diameter);
    }

    public String selectGender() {
        if(Math.random() > 0.5) {return "Male"; }
        else { return "Female"; }
    }

    public String getGender() {
        return gender;
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
        return birthPeriod >= 100;
    }

    public int getSpotRadius() {
        return spotRadius;
    }

    public int getEnergy() {
        return energy;
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
        gender = selectGender();
        birth = false;
        birthPeriod = 0;
        starveCount = 0;
        energy = 100;
    }
}
