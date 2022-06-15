package com.model;

import java.util.EnumMap;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Drawer {
    private double CANVAS_WIDTH;
    private double CANVAS_HEIGHT;
    private GraphicsContext gc;
    private EnumMap<SpecieTypes, Color> specieToColor;
    private EnumMap<SpecieTypes, Integer> diameters;

    Drawer(Canvas world, double worldWidth, double worldHeight) {
        CANVAS_WIDTH = worldWidth;
        CANVAS_HEIGHT = worldHeight;
        gc = world.getGraphicsContext2D();

        specieToColor = new EnumMap<>(SpecieTypes.class);  //Mapping Species enum to color
        specieToColor.put(SpecieTypes.Resource, Color.BLUE);
        specieToColor.put(SpecieTypes.Human, Color.ORANGE);
        specieToColor.put(SpecieTypes.Corpse, Color.GREY);
        specieToColor.put(SpecieTypes.Zombie, Color.DARKRED);
        specieToColor.put(SpecieTypes.SubHuman, Color.PURPLE);
        specieToColor.put(SpecieTypes.Mob, Color.ORANGERED);
        specieToColor.put(SpecieTypes.Plant, Color.GREEN);
    }

    public void setDiameterMap(EnumMap<SpecieTypes, Integer> diameters) {
        this.diameters = diameters;
    }

    // public void drawOvals() {
    //     gc.strokeOval(0, 10, 10, 10);
    // }

    public void drawRect(double x, double y, int width, int height) {
        gc.setStroke(Color.RED);
        gc.strokeRect(x, y, width, height);
    }

    public void drawLine(double x1, double y1, double x2, double y2) {
        gc.setStroke(Color.RED);
        gc.strokeLine(x1, y1, x2, y2);
    }

    // public void drawTriangle() {
    //     gc.strokePolygon(new double[]{20.0, 20.0, 25.0}, new double[]{20.0, 30.0, 30.0}, 3);
    // }

    public void replaceColor(double x, double y, int diameter, boolean isStroke) {
        gc.setFill(Color.RED);
        gc.fillOval(x-diameter/2, y-diameter/2, diameter, diameter);
        if (isStroke) {
            gc.setStroke(Color.RED);
            gc.strokeOval(x-diameter/2, y-diameter/2, diameter, diameter);
        }
    }

    // public void drawResources(List<Resource> resources, int diameter) {
    //     for (Resource r : resources) {
    //         gc.setFill(specieToColor.get(SpecieTypes.Resource));
    //         gc.fillOval(r.getX()-diameter/2, r.getY()-diameter/2, diameter, diameter);
    //     }
    // }

    public void drawStep(List<Resource> resources, List<Human> humans, List<Zombie> zombies, 
                         List<SubHuman> subhumans, List<Corpse> corpses, List<Mob> mobs, List<Plant> plants) {
        int resourceDiameter = diameters.get(SpecieTypes.Resource);
        int specieDiameter = diameters.get(SpecieTypes.Specie);

        gc.setFill(Color.CORNSILK);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        for (Corpse c : corpses) {
            if (!c.getRemoved()) {
                gc.setFill(specieToColor.get(SpecieTypes.Corpse));
                gc.strokeOval(c.getX()-specieDiameter/2, c.getY()-specieDiameter/2, specieDiameter, specieDiameter);
                gc.fillOval(c.getX()-specieDiameter/2, c.getY()-specieDiameter/2, specieDiameter, specieDiameter);
            }
        }

        for (Resource r : resources) {
            if (!r.getRemoved()) {
                gc.setFill(specieToColor.get(SpecieTypes.Resource));
                gc.fillOval(r.getX()-resourceDiameter/2, r.getY()-resourceDiameter/2, resourceDiameter, resourceDiameter);
            }
        }
        
        double changeY = specieDiameter/2 * Math.tan(0.523599);  //30 degrees
        for (Plant p : plants) {
            if (!p.getRemoved()) {
                if (p.getIsRoot()) {
                    gc.setFill(specieToColor.get(SpecieTypes.Plant));
                    gc.strokeOval(p.getX()-specieDiameter/2, p.getY()-specieDiameter/2, specieDiameter, specieDiameter);
                    gc.fillOval(p.getX()-specieDiameter/2, p.getY()-specieDiameter/2, specieDiameter, specieDiameter);
                } else {
                    gc.setFill(specieToColor.get(SpecieTypes.Plant));
                    gc.strokePolygon(new double[]{p.getX()-specieDiameter/2, p.getX()-specieDiameter/2, p.getX(), p.getX(),
                                    p.getX()+specieDiameter/2, p.getX()+specieDiameter/2},
                                    new double[]{p.getY()+changeY, p.getY()-changeY, p.getY()+specieDiameter/2, p.getY()-specieDiameter/2,
                                    p.getY()+changeY, p.getY()-changeY}, 6);
                    gc.fillPolygon(new double[]{p.getX()-specieDiameter/2, p.getX()-specieDiameter/2, p.getX(), p.getX(),
                                    p.getX()+specieDiameter/2, p.getX()+specieDiameter/2},
                                    new double[]{p.getY()+changeY, p.getY()-changeY, p.getY()+specieDiameter/2, p.getY()-specieDiameter/2,
                                    p.getY()+changeY, p.getY()-changeY}, 6);
                } 
            }
        }

        for (Mob m : mobs) {
            if (!m.getRemoved()) {
                gc.setFill(specieToColor.get(SpecieTypes.Mob));
                gc.strokeOval(m.getX()-specieDiameter/2, m.getY()-specieDiameter/2, specieDiameter, specieDiameter);
                gc.fillOval(m.getX()-specieDiameter/2, m.getY()-specieDiameter/2, specieDiameter, specieDiameter);
            }
        }

        for (Zombie z : zombies) {
            if (!z.getRemoved()) {
                gc.setFill(specieToColor.get(SpecieTypes.Zombie));
                gc.strokeOval(z.getX()-specieDiameter/2, z.getY()-specieDiameter/2, specieDiameter, specieDiameter);
                gc.fillOval(z.getX()-specieDiameter/2, z.getY()-specieDiameter/2, specieDiameter, specieDiameter);
            }
        }

        for (SubHuman s : subhumans) {
            if (!s.getRemoved()) {
                gc.setFill(specieToColor.get(SpecieTypes.SubHuman));
                gc.strokeOval(s.getX()-specieDiameter/2, s.getY()-specieDiameter/2, specieDiameter, specieDiameter);
                gc.fillOval(s.getX()-specieDiameter/2, s.getY()-specieDiameter/2, specieDiameter, specieDiameter);
            }
        }

        for (Human h : humans) {
            if (!h.getRemoved()) {
                gc.setFill(specieToColor.get(SpecieTypes.Human));
                gc.strokeOval(h.getX()-specieDiameter/2, h.getY()-specieDiameter/2, specieDiameter, specieDiameter);
                gc.fillOval(h.getX()-specieDiameter/2, h.getY()-specieDiameter/2, specieDiameter, specieDiameter);
            }
        }
    }
}
