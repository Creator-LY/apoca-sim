package com.model;

import java.util.ArrayList;

import com.gui.MainView;

import javafx.scene.canvas.Canvas;

public class Simulation {
    private int steps;
    private Canvas world;
    private ArrayList<Human> humans;
    private ArrayList<Resource> resources;

    public Simulation(Canvas world, int population) {
        this.world = world;
        humans = new ArrayList<Human>();
        resources = new ArrayList<Resource>();

        for (int i = 0; i < population; i++) {
            humans.add(new Human(world));
            
        }
        for (int i = 0; i < 1000; i++) {
            resources.add(new Resource(world));
        }
        draw();
    }

    public boolean collisionDetect(Position pos1, int w1, int h1, Position pos2, int w2, int h2) {
        if (pos1.getX() < pos2.getX() + w2 &&
        pos1.getX() + w1 > pos2.getX() &&
        pos1.getY() < pos2.getY() + h2 &&
        pos1.getY() + h1 > pos2.getY()) {
        // collision detected!
            return true;
        } else {
        // no collision
            return false;
        }
    }

    public void action() {
        steps++;
        timeStatus();
        for (int i = 0; i < humans.size(); i++) {
            if (humans.get(i).getBirth()) {
                humans.get(i).incrementBirthPeriod();
                if (humans.get(i).giveBirth()) {
                    humans.add(new Human(humans.get(i).getPos().getX(), humans.get(i).getPos().getY(), world));
                    humans.get(i).resetBirthPeriod();
                    humans.get(i).setBirth(false);
                }
            }

            humans.get(i).radar(resources);
            humans.get(i).move();
            for (int j = 0; j < resources.size(); j++) {
                if (collisionDetect(humans.get(i).getPos(), humans.get(i).getDiameter(), humans.get(i).getDiameter(),
                resources.get(j).getPos(), resources.get(j).getDiameter(), resources.get(j).getDiameter())) {
                    resources.remove(j);
                    humans.get(i).inEnergy();
                    humans.get(i).setSetdirection(false);
                } else {
                    if (humans.get(i).checkStarve()) {
                        humans.remove(i);
                        break;
                    }
                }
            }
            for (int j = 0; j < humans.size(); j++) {
                if (collisionDetect(humans.get(i).getPos(), humans.get(i).getDiameter(), humans.get(i).getDiameter(),
                humans.get(j).getPos(), humans.get(j).getDiameter(), humans.get(j).getDiameter())) {
                    if (!humans.get(i).getGender().equals(humans.get(j).getGender())) {
                        if (humans.get(i).getGender().equals("Female")) {
                            humans.get(i).setBirth(true);
                            break;
                        } else { humans.get(j).setBirth(true); }
                    }
                }
            }
        }
    }

    public void draw() {
        for (Human h : humans) {
            h.draw();
        }
        for (Resource r : resources) {
            r.draw();
        }
    }

    public void timeStatus() {
        String time = String.format("Time: %02d:00", steps%24);
        MainView.setTime("Steps: " + steps, time, "Days: " + Math.floorDiv(steps, 24));
    }
}
