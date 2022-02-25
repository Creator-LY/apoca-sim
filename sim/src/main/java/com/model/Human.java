package com.model;

import java.util.ArrayList;
import java.util.Collections;


import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class Human extends Specie {
    private String gender = selectGender();;
    private boolean birth = false;
    private int birthPeriod = 0;
    private int starve_count = 0;  //168 actions = 7 days
    private int health = 1;
    private int energy = 100;
    private float speed = (float)(Math.random() * 2 + 0.5);
    private float power = 5.1f;
    private int spot_radius = 30;
    private boolean setdirection = false;
    private Color color = Color.ORANGE;

    public Human(double x, double y, Canvas world) {
        super(x, y, world);
    }

    public Human(Canvas world) {
        super(world);
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
    
    public void inEnergy() {
        if (starve_count == 0) {
            energy += 72;
        } else {
            energy += 36;
            starve_count = 0;
        }
    }

    public void deEnergy() {
        energy--;
    }

    public boolean checkStarve() {
        if (energy == 0) {
            starve_count ++;
        }
        if (starve_count > 168) {
            return true;
        } else {
            return false;
        }
    }

    public void move() {
        if (energy > 0) {
            super.move();
            deEnergy();
        } 
    }

    public void draw() {
        super.draw(color);
    }

    public void radar(ArrayList<Resource> resources) {
        if (!setdirection) {
            ArrayList<Double> arr = new ArrayList<Double>();
            ArrayList<Integer> arr2 = new ArrayList<Integer>();
            for (Resource r : resources) {
                if ((Math.abs(super.getPos().getX() + (super.getDiameter() / 2) - r.getX()) <= spot_radius) &&
                (Math.abs(super.getPos().getY() + (super.getDiameter() / 2) - r.getY()) <= spot_radius)) {
                    arr.add(Math.abs(super.getPos().getX()-r.getX()) + Math.abs(super.getPos().getY()-r.getY()));
                    arr2.add(resources.indexOf(r));
                } 
            }
            if (arr.size() == 0) {
                double dir =  Math.random() * 2 * Math.PI;
                super.setDx(Math.cos(dir) * speed);
                super.setDy(Math.sin(dir) * speed);
            } else {
                int index = arr.indexOf(Collections.min(arr));
                double angle = Math.atan2(resources.get(arr2.get(index)).getY()-super.getPos().getY()+(super.getDiameter() / 2),
                                        resources.get(arr2.get(index)).getX()-super.getPos().getX()+(super.getDiameter() / 2));
                super.setDx(Math.cos(angle) * speed);
                super.setDy(Math.sin(angle) * speed);
            }
            setdirection = true;
        }
    }

    public void setSetdirection(boolean setdirection) {
        this.setdirection = setdirection;
    }
}
