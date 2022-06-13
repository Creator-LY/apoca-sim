package com.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import javafx.util.Pair;

import javafx.scene.canvas.Canvas;

import com.gui.MainView;

public class Simulation {
    private Drawer drawer;
    private QuadTree qt;
    private double worldWidth;
    private double worldHeight;
    private int steps;
    private int resourceCount;
    private int humanCount;
    private int zombieCount;
    private int subhumanCount;
    private int mobCount;
    private int plantCount;
    private boolean isDay;
    private EnumMap<SpecieTypes, Integer> SpeciesToDiameter;
    private int removeHumanCount;
    private int removeZombieCount;
    private int removeSubHumanCount;
    private int removeResourceCount;
    private int removeCorpseCount;
    private int removeMobCount;
    private int removePlantCount;
    private ArrayList<Resource> resources;
    private ArrayList<Human> humans;
    private ArrayList<Zombie> zombies;
    private ArrayList<SubHuman> subhumans;
    private ArrayList<Mob> mobs;
    private ArrayList<Plant> plants;
    private ArrayList<Corpse> corpses;

    public Simulation(Canvas world, int population) {
        resourceCount = 0;
        humanCount = 0;
        zombieCount = 0;
        subhumanCount = 0;
        mobCount = 20;
        plantCount = 20;

        drawer = new Drawer(world);
        worldWidth = world.getWidth();
        worldHeight = world.getHeight();

        SpeciesToDiameter = new EnumMap<>(SpecieTypes.class);
        SpeciesToDiameter.put(SpecieTypes.Resource, 7);
        SpeciesToDiameter.put(SpecieTypes.Specie, 10);

        drawer.setDiameterMap(SpeciesToDiameter);
        
        resources = new ArrayList<Resource>(1);
        humans = new ArrayList<Human>(population);
        zombies = new ArrayList<Zombie>(20);
        subhumans = new ArrayList<SubHuman>();
        corpses = new ArrayList<Corpse>(Math.floorDiv(population, 4));
        mobs = new ArrayList<Mob>();
        plants = new ArrayList<Plant>();

        for (int i = 0; i < resourceCount; i++) {
            resources.add(new Resource(worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Resource)));
        }

        for (int i = 0; i < humanCount; i++) {
            humans.add(new Human(worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Specie))); 
        }

        for (int i = 0; i < zombieCount; i++) {
            zombies.add(new Zombie(worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Specie))); 
        }

        for (int i = 0; i < subhumanCount; i++) {
            subhumans.add(new SubHuman(worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Specie))); 
        }

        for (int i = 0; i < mobCount; i++) {
            mobs.add(new Mob(worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Specie))); 
        }

        for (int i = 0; i < plantCount; i++) {
            plants.add(new Plant(worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Specie), true)); 
        }

        draw();
    }

    public void generateQuadTree() {
        qt = new QuadTree();

        for (int i = 0; i < resources.size(); i++) {   
            if (!resources.get(i).getRemoved()) {
                qt.insert(resources.get(i).getX(), resources.get(i).getY(), new Pair<SpecieTypes,Integer>(SpecieTypes.Resource, i));
            }   
        }
        for (int i = 0; i < humans.size(); i++) {
            if (!humans.get(i).getRemoved()) {
                qt.insert(humans.get(i).getX(), humans.get(i).getY(), new Pair<SpecieTypes,Integer>(SpecieTypes.Human, i));
            }
        }
        for (int i = 0; i < zombies.size(); i++) {
            if (!zombies.get(i).getRemoved()) {
                qt.insert(zombies.get(i).getX(), zombies.get(i).getY(), new Pair<SpecieTypes,Integer>(SpecieTypes.Zombie, i));
            }
        }
        for (int i = 0; i < mobs.size(); i++) {
            if (!mobs.get(i).getRemoved()) {
                qt.insert(mobs.get(i).getX(), mobs.get(i).getY(), new Pair<SpecieTypes,Integer>(SpecieTypes.Mob, i));
            }
        }
        for (int i = 0; i < plants.size(); i++) {
            if (!plants.get(i).getRemoved()) {
                qt.insert(plants.get(i).getX(), plants.get(i).getY(), new Pair<SpecieTypes,Integer>(SpecieTypes.Plant, i));
            }
        }
        for (int i = 0; i < corpses.size(); i++) {
            if (!corpses.get(i).getRemoved()) {
                qt.insert(corpses.get(i).getX(), corpses.get(i).getY(), new Pair<SpecieTypes,Integer>(SpecieTypes.Corpse, i));
            }
        }
    }

    public ArrayList<Pair<SpecieTypes, Integer>> rangeSearch(double xmin, double xmax, double ymin, double ymax, SpecieTypes allowed) {
        Boundary rect = new Boundary(xmin, xmax, ymin, ymax);
        return qt.query2D(rect, allowed);
    }

    public ArrayList<Pair<SpecieTypes, Integer>> collisionDetect(Position pos, int range, SpecieTypes allowed) {;
        return rangeSearch(pos.getX()-range, pos.getX()+range, pos.getY()-range, pos.getY()+range, allowed);
    }

    public Position findPosition(Pair<SpecieTypes, Integer> pair) {
        switch(pair.getKey()) {
            case Resource:
                return resources.get(pair.getValue()).getPos();
            case Human:
                return humans.get(pair.getValue()).getPos();
            case SubHuman:
                return subhumans.get(pair.getValue()).getPos();
            case Zombie:
                return zombies.get(pair.getValue()).getPos();
            case Mob:
                return mobs.get(pair.getValue()).getPos();
            case Plant:
                return plants.get(pair.getValue()).getPos();
            default:
                return null;
        }
    }

    public Pair<SpecieTypes, Integer> pickNearest(List<Pair<SpecieTypes, Integer>> find, Position pos) {
        if (find.size() == 0) {
            return new Pair<SpecieTypes, Integer>(SpecieTypes.Specie, -1);
        } else {
            double minimum = pos.getRelativeDistance(findPosition(find.get(0)));
            int chosen = find.get(0).getValue();
            SpecieTypes chooseType = find.get(0).getKey();
            for (int i = 1; i < find.size(); i++) {
                double newDistance = pos.getRelativeDistance(findPosition(find.get(i)));
                if (minimum > newDistance) {
                    minimum = newDistance;
                    chosen = find.get(i).getValue();
                    chooseType = find.get(i).getKey();
                }
            }
            
            if (minimum == Double.POSITIVE_INFINITY) {
                return new Pair<SpecieTypes, Integer>(SpecieTypes.Specie, -1);
            }
            else {
                return new Pair<SpecieTypes, Integer>(chooseType, chosen);
            }
        }
    }

    public double indexToAngle(Pair<SpecieTypes, Integer> pair, Position pos) {
        if (pair.getValue() == -1) {
            return Math.random() * 2 * Math.PI;
        } else {
            int SpecieDiameter = SpeciesToDiameter.get(SpecieTypes.Specie);
            int index = pair.getValue();

            //Debugging move to others
            // drawer.drawLine(pos.getX(), pos.getY(), resources.get(index).getX(), resources.get(index).getY());
            // System.out.println("Direct to: " + index);

            switch(pair.getKey()) {
                case Resource:
                return Math.atan2(resources.get(index).getY()-pos.getY()+(SpecieDiameter / 2),
                resources.get(index).getX()-pos.getX()+(SpecieDiameter / 2));
                case Corpse:
                return Math.atan2(corpses.get(index).getY()-pos.getY()+(SpecieDiameter / 2),
                corpses.get(index).getX()-pos.getX()+(SpecieDiameter / 2));
                case Human:
                return Math.atan2(humans.get(index).getY()-pos.getY()+(SpecieDiameter / 2),
                humans.get(index).getX()-pos.getX()+(SpecieDiameter / 2));
                case SubHuman:
                return Math.atan2(subhumans.get(index).getY()-pos.getY()+(SpecieDiameter / 2),
                subhumans.get(index).getX()-pos.getX()+(SpecieDiameter / 2));
                case Zombie:
                return Math.atan2(zombies.get(index).getY()-pos.getY()+(SpecieDiameter / 2),
                zombies.get(index).getX()-pos.getX()+(SpecieDiameter / 2));
                case Mob:
                return Math.atan2(mobs.get(index).getY()-pos.getY()+(SpecieDiameter / 2),
                mobs.get(index).getX()-pos.getX()+(SpecieDiameter / 2));
                case Plant:
                return Math.atan2(plants.get(index).getY()-pos.getY()+(SpecieDiameter / 2),
                plants.get(index).getX()-pos.getX()+(SpecieDiameter / 2));
                default:
                    return 0;
            }
        }
    }

    public void replaceHuman(double x, double y) {
        boolean flag = false;
        if (removeHumanCount > 0) {
            for (Human human : humans) {
                if (human.getRemoved()) {
                    human.setDefault(x, y);
                    removeHumanCount--;
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            humans.add(new Human(x, y,
            worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Specie)));
        }
    }

    public void replaceSubHuman(double x, double y) {
        boolean flag = false;
        if (removeSubHumanCount > 0) {
            for (SubHuman subhuman : subhumans) {
                if (subhuman.getRemoved()) {
                    subhuman.setDefault(x, y);
                    removeSubHumanCount--;
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            subhumans.add(new SubHuman(x, y,
            worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Specie)));
        }
    }

    public void replaceZombie(double x, double y) {
        boolean flag = false;
        if (removeZombieCount > 0) {
            for (Zombie zombie : zombies) {
                if (zombie.getRemoved()) {
                    zombie.setDefault(x, y);
                    removeZombieCount--;
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            zombies.add(new Zombie(x, y,
            worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Specie)));
        }
    }

    public void replaceCorpse(double x, double y, boolean isMutated) {
        boolean flag = false;
        if (removeCorpseCount > 0) {
            for (Corpse corpse : corpses) {
                if (corpse.getRemoved()) {
                    corpse.setDefault(x, y, isMutated);
                    removeCorpseCount--;
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            corpses.add(new Corpse(x, y, worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Specie), isMutated));
        }
    }

    public void replaceMob(double x, double y) {
        boolean flag = false;
        if (removeMobCount > 0) {
            for (Mob mob : mobs) {
                if (mob.getRemoved()) {
                    mob.setDefault(x, y);
                    removeMobCount--;
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            mobs.add(new Mob(x, y,
            worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Specie)));
        }
    }

    public void replacePlant(double x, double y) {
        boolean flag = false;
        if (removePlantCount > 0) {
            for (Plant plant : plants) {
                if (plant.getRemoved()) {
                    plant.setDefault(x, y);
                    removePlantCount--;
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            plants.add(new Plant(x, y,
            worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Specie)));
        }
    }

    public void replaceResources() {
        boolean flag = false;
        if (removeResourceCount > 0) {
            for (Resource resource : resources) {
                if (resource.getRemoved()) {
                    int diameter = SpeciesToDiameter.get((SpecieTypes.Resource));
                    resource.getPos().setXY(Math.random() * (worldWidth - diameter), Math.random() * (worldWidth - diameter));
                    resource.setRemoved(false);
                    removePlantCount--;
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            resources.add(new Resource(worldWidth, worldHeight, SpeciesToDiameter.get(SpecieTypes.Resource)));
        }
    }

    public void action() {
        steps++;
        timeStatus();
        generateQuadTree();
        int resourceDiameter = SpeciesToDiameter.get(SpecieTypes.Resource);
        int SpecieDiameter = SpeciesToDiameter.get(SpecieTypes.Specie);

        /*******************************************
        Add more resources
        ********************************************/
        if (steps % 168 == 0) {
            for (int i = 0; i < Math.floorDiv(humans.size(), 2); i++) {
                replaceResources();
            }
        }

        for (int i=plants.size()-1; i >= 0; i--) {
            if (!plants.get(i).getRemoved()) {
                /*******************************************
                plant photosynthesis
                ********************************************/
                if (isDay) {
                    plants.get(i).inEnergy(1);
                }
                /*******************************************
                Grow plant and become root
                ********************************************/
                if (plants.get(i).getIsRoot()) {
                    if (plants.get(i).grow(isDay)) {
                        plants.get(i).setVelocity(Math.random() * 2 * Math.PI);
                        plants.get(i).extend();
                        replacePlant(plants.get(i).getChildX(), plants.get(i).getChildY());
                    }
                } else {
                    if (plants.get(i).grow(isDay)) {
                       plants.get(i).setRoot(true);
                    } 
                }
                /*******************************************
                Plant collsion
                ********************************************/
                /*******************************************
                Remove inactive entity from collsion 
                ********************************************/
                ArrayList<Pair<SpecieTypes, Integer>> collidePrey = collisionDetect(plants.get(i).getPos(), SpecieDiameter, SpecieTypes.Specie);
                for (int j = collidePrey.size()-1; j >= 0; j--) {
                    switch(collidePrey.get(j).getKey()) {
                        case Resource:
                        collidePrey.remove(j);
                        break;
                        case Human:
                        if (humans.get(collidePrey.get(j).getValue()).getRemoved()) { collidePrey.remove(j); }
                        break;
                        case SubHuman:
                        if (subhumans.get(collidePrey.get(j).getValue()).getRemoved()) { collidePrey.remove(j); }
                        break;
                        case Zombie:
                        if (zombies.get(collidePrey.get(j).getValue()).getRemoved()) { collidePrey.remove(j); }
                        break;
                        case Mob:
                        collidePrey.remove(j);
                        case Plant:
                        collidePrey.remove(j);
                        break;
                        default:
                        break;
                    }
                }
                /*******************************************
                Handle collision with other entity
                ********************************************/
                for (Pair<SpecieTypes,Integer> pair : collidePrey) {
                    switch(pair.getKey()) {
                        case Corpse:
                        plants.get(i).inEnergy(1);
                        corpses.get(pair.getValue()).setRemoved(true);
                        removeCorpseCount++;
                        break;
                        case Human:
                        plants.get(i).inEnergy(1);
                        humans.get(pair.getValue()).setRemoved(true);
                        removeHumanCount++;
                        break;
                        case SubHuman:
                        plants.get(i).inEnergy(1);
                        subhumans.get(pair.getValue()).setRemoved(true);
                        removeSubHumanCount++;
                        break;
                        case Zombie:
                        plants.get(i).inEnergy(1);
                        zombies.get(pair.getValue()).setRemoved(true);
                        removeZombieCount++;
                        break;
                        default:
                        break;
                    }
                }
            }
        }

        for (int i=mobs.size()-1; i >= 0; i--) {
            if (!mobs.get(i).getRemoved()) {
                /*******************************************
                Create more mobs
                ********************************************/
                if (mobs.get(i).getBirth()) {
                    mobs.get(i).incrementBirthPeriod();
                    if (mobs.get(i).giveBirth()) {
                        mobs.get(i).resetBirthPeriod();
                        mobs.get(i).setBirth(false);
                        replaceMob(mobs.get(i).getX(), mobs.get(i).getY());
                    }
                }
                /*******************************************
                Prey finding for mob
                ********************************************/
                if (!mobs.get(i).getDirect()) {
                    List<Pair<SpecieTypes, Integer>> findPrey = collisionDetect(mobs.get(i).getPos(), mobs.get(i).getSpotRadius(), SpecieTypes.Specie);
                    /*******************************************
                    Remove all inactive entities
                    ********************************************/
                    for (int j = findPrey.size()-1; j >= 0; j--) {
                        switch(findPrey.get(j).getKey()) {
                            case Resource:
                            findPrey.remove(j);
                            break;
                            case Corpse:
                            if (corpses.get(findPrey.get(j).getValue()).getRemoved()) { findPrey.remove(j); }
                            break;
                            case Human:
                            if (humans.get(findPrey.get(j).getValue()).getRemoved()) { findPrey.remove(j); }
                            break;
                            case SubHuman:
                            if (subhumans.get(findPrey.get(j).getValue()).getRemoved()) { findPrey.remove(j); }
                            break;
                            case Zombie:
                            if (zombies.get(findPrey.get(j).getValue()).getRemoved()) { findPrey.remove(j); }
                            break;
                            case Mob:
                            findPrey.remove(j);
                            break;
                            case Plant:
                            if (plants.get(findPrey.get(j).getValue()).getRemoved()) { findPrey.remove(j); }
                            break;
                            default:
                            break;
                        }
                    }
                    /*******************************************
                    Direct to nearest prey
                    ********************************************/
                    Pair<SpecieTypes, Integer> typeIndex = pickNearest(findPrey, mobs.get(i).getPos());
                    if (typeIndex.getValue() != -1) {
                        mobs.get(i).setHuntedType(typeIndex.getKey());
                        mobs.get(i).setHuntedIndex(typeIndex.getValue());
                        mobs.get(i).setDirect(true);
                    } else {
                        mobs.get(i).setHuntedType(SpecieTypes.Specie);
                    }
                }
                /*******************************************
                Handle movement
                ********************************************/
                boolean flag = false;
                if (mobs.get(i).getHuntedType() != SpecieTypes.Specie) {
                    switch(mobs.get(i).getHuntedType()) {
                        case Human:
                        if (!humans.get(mobs.get(i).getHuntedIndex()).getRemoved()) {
                            double angle = indexToAngle(new Pair<>(mobs.get(i).getHuntedType(), mobs.get(i).getHuntedIndex()), mobs.get(i).getPos());
                            mobs.get(i).setVelocity(angle);
                            mobs.get(i).move();
                            flag = true;
                        }
                        break;
                        case SubHuman:
                        if (!subhumans.get(mobs.get(i).getHuntedIndex()).getRemoved()) {
                            double angle = indexToAngle(new Pair<>(mobs.get(i).getHuntedType(), mobs.get(i).getHuntedIndex()), mobs.get(i).getPos());
                            mobs.get(i).setVelocity(angle);
                            mobs.get(i).move();
                            flag = true;
                        }
                        break;
                        case Zombie:
                        if (!zombies.get(mobs.get(i).getHuntedIndex()).getRemoved()) {
                            double angle = indexToAngle(new Pair<>(mobs.get(i).getHuntedType(), mobs.get(i).getHuntedIndex()), mobs.get(i).getPos());
                            mobs.get(i).setVelocity(angle);
                            mobs.get(i).move();
                            flag = true;
                        }
                        break;
                        case Mob:
                        if (!mobs.get(mobs.get(i).getHuntedIndex()).getRemoved()) {
                            double angle = indexToAngle(new Pair<>(mobs.get(i).getHuntedType(), mobs.get(i).getHuntedIndex()), mobs.get(i).getPos());
                            mobs.get(i).setVelocity(angle);
                            mobs.get(i).move();
                            flag = true;
                        }
                        break;
                        case Plant:
                        if (!plants.get(mobs.get(i).getHuntedIndex()).getRemoved()) {
                            double angle = indexToAngle(new Pair<>(mobs.get(i).getHuntedType(), mobs.get(i).getHuntedIndex()), mobs.get(i).getPos());
                            mobs.get(i).setVelocity(angle);
                            mobs.get(i).move();
                            flag = true;
                        }
                        break;
                        default:
                        break;
                    }
                }
                if (!flag) {
                    mobs.get(i).setVelocity(mobs.get(i).getPerferAngle());
                    mobs.get(i).move();
                    mobs.get(i).setDirect(false);
                }
  
                /*******************************************
                Remove inactive entity from collsion 
                ********************************************/
                ArrayList<Pair<SpecieTypes, Integer>> collidePrey = collisionDetect(mobs.get(i).getPos(), SpecieDiameter, SpecieTypes.Specie);
                for (int j = collidePrey.size()-1; j >= 0; j--) {
                    switch(collidePrey.get(j).getKey()) {
                        case Resource:
                        collidePrey.remove(j);
                        break;
                        case Human:
                        if (humans.get(collidePrey.get(j).getValue()).getRemoved()) { collidePrey.remove(j); }
                        break;
                        case SubHuman:
                        if (subhumans.get(collidePrey.get(j).getValue()).getRemoved()) { collidePrey.remove(j); }
                        break;
                        case Zombie:
                        if (zombies.get(collidePrey.get(j).getValue()).getRemoved()) { collidePrey.remove(j); }
                        break;
                        case Mob:
                        if (mobs.get(collidePrey.get(j).getValue()).getRemoved()) { collidePrey.remove(j); }
                        break;
                        case Plant:
                        if (plants.get(collidePrey.get(j).getValue()).getRemoved()) { collidePrey.remove(j); }
                        break;
                        default:
                        break;
                    }
                }
                /*******************************************
                Handle collision with other entity
                ********************************************/
                for (Pair<SpecieTypes,Integer> pair : collidePrey) {
                    switch(pair.getKey()) {
                        case Corpse:
                        mobs.get(i).inEnergy(1);
                        corpses.get(pair.getValue()).setRemoved(true);
                        removeCorpseCount++;
                        break;
                        case Human:
                        mobs.get(i).inEnergy(1);
                        humans.get(pair.getValue()).setRemoved(true);
                        removeHumanCount++;
                        break;
                        case SubHuman:
                        mobs.get(i).inEnergy(1);
                        subhumans.get(pair.getValue()).setRemoved(true);
                        removeSubHumanCount++;
                        break;
                        case Zombie:
                        mobs.get(i).inEnergy(1);
                        zombies.get(pair.getValue()).setRemoved(true);
                        removeZombieCount++;
                        break;
                        case Mob:
                        if (mobs.get(i).getBirth() == false) { mobs.get(i).setBirth(true); }
                        break;
                        case Plant:
                        double chance = Math.random();
                        if (isDay && chance > 0.5 || !isDay && chance > 0.7) {
                            plants.get(pair.getValue()).inEnergy(1);
                            mobs.get(i).setRemoved(true);
                            removeMobCount++;
                        } else {
                            mobs.get(i).inEnergy(1);
                            plants.get(pair.getValue()).setRemoved(true);
                            removePlantCount++;
                        }
                        break;
                        default:
                        break;
                    }
                }
                /*******************************************
                Check starved mob
                ********************************************/
                if (!mobs.get(i).getRemoved() && mobs.get(i).checkStarve()) {
                    mobs.get(i).setRemoved(true);
                    removeMobCount++;
                    replaceCorpse(mobs.get(i).getX(), mobs.get(i).getY(), true);
                }
            }
        }

        for (int i=subhumans.size()-1; i >= 0; i--) {
            if (!subhumans.get(i).getRemoved()) {
                /*******************************************
                Resource finding for sub-human
                ********************************************/
                if (!subhumans.get(i).getDirect()) {
                    List<Pair<SpecieTypes, Integer>> findResources = collisionDetect(subhumans.get(i).getPos(), subhumans.get(i).getSpotRadius(), SpecieTypes.Resource);
                    for (int j = findResources.size()-1; j >= 0; j--) {
                        if (resources.get(findResources.get(j).getValue()).getRemoved()) {
                            findResources.remove(j);
                        }
                    }

                    double angle = indexToAngle(pickNearest(findResources, subhumans.get(i).getPos()), subhumans.get(i).getPos());
                    subhumans.get(i).setVelocity(angle);
                    subhumans.get(i).setDirect(true);

                    //Debugging range finding
                    // int range = subhumans.get(i).getSpotRadius();
                    // drawer.drawRect(subhumans.get(i).getX()-range, subhumans.get(i).getY()-range, 2*range, 2*range);
                    // for (Pair<SpecieTypes,Integer> pair : findResources) {
                    //     drawer.replaceColor(resources.get(pair.getValue()).getX(), resources.get(pair.getValue()).getY(), 7, true);
                    //     System.out.println(pair.getValue());
                    // }
                }
                /*******************************************
                 Handle movement for sub-human
                ********************************************/
                subhumans.get(i).move();

                /*******************************************
                Resource & sub-human collision
                ********************************************/
                ArrayList<Pair<SpecieTypes, Integer>> collideResources = collisionDetect(subhumans.get(i).getPos(), resourceDiameter/2+SpecieDiameter/2, SpecieTypes.Resource);
                for (int j = collideResources.size()-1; j >= 0; j--) {
                    if (resources.get(collideResources.get(j).getValue()).getRemoved()) {
                        collideResources.remove(j);
                    }
                }
                if (collideResources.size() > 0) {
                    subhumans.get(i).inEnergy(collideResources.size());
                    subhumans.get(i).setDirect(false);
                }
                for (Pair<SpecieTypes,Integer> pair : collideResources) {
                    resources.get(pair.getValue()).setRemoved(true);
                    removeResourceCount++;
                }

                //Debugging use resource
                // for (int j = 0; j < collideResources.size(); j++) {
                //     int index = collideResources.get(j).getValue();
                //     drawer.replaceColor(resources.get(index).getX(), resources.get(index).getY(), resourceDiameter, false);
                // }

                /*******************************************
                Sub-Human & Zombie collision
                ********************************************/
                List<Pair<SpecieTypes, Integer>> collideZombie = collisionDetect(subhumans.get(i).getPos(), SpecieDiameter, SpecieTypes.Zombie);
                for (int j = collideZombie.size()-1; j >= 0; j--) {
                    if (zombies.get(collideZombie.get(j).getValue()).getRemoved()) {
                        collideZombie.remove(j);
                    }
                }
                if (collideZombie.size() > 0) {
                    if (isDay) {
                        for (Pair<SpecieTypes, Integer> pair : collideZombie) {
                            zombies.get(pair.getValue()).setRemoved(true);
                            removeZombieCount++;
                            replaceCorpse(zombies.get(pair.getValue()).getX(), zombies.get(pair.getValue()).getY(), true);
                        }
                    } else {
                        subhumans.get(i).setRemoved(true);
                        removeSubHumanCount++;
                        replaceCorpse(subhumans.get(i).getX(), subhumans.get(i).getY(), false);
                    }
                }

                /*******************************************
                Sub-Human & Corpse collision
                ********************************************/
                if (subhumans.get(i).getStarveCount() > 0) {
                    List<Pair<SpecieTypes, Integer>> collideCorpse = collisionDetect(subhumans.get(i).getPos(), SpecieDiameter, SpecieTypes.Corpse);
                    for (int j = collideCorpse.size()-1; j >= 0; j--) {
                        if (corpses.get(collideCorpse.get(j).getValue()).getRemoved() || 
                            corpses.get(collideCorpse.get(j).getValue()).getIsMutated()) {
                            collideCorpse.remove(j);
                        }
                    }
                    if (collideCorpse.size() > 0) {
                        subhumans.get(i).inEnergy(collideCorpse.size());
                        for (Pair<SpecieTypes,Integer> pair : collideCorpse) {
                            corpses.get(pair.getValue()).setRemoved(true);
                            removeCorpseCount++;
                        }
                    } else {
                        /*******************************************
                        Sub-Human & Human collision
                        ********************************************/
                        List<Pair<SpecieTypes, Integer>> collideHumans = collisionDetect(subhumans.get(i).getPos(), SpecieDiameter, SpecieTypes.Human);
                        for (int j = collideHumans.size()-1; j >= 0; j--) {
                            if (humans.get(collideHumans.get(j).getValue()).getRemoved()) {
                                collideHumans.remove(j);
                            }
                        }
                        if (collideHumans.size() > 0) {
                            for (Pair<SpecieTypes, Integer> pair : collideHumans) {
                                subhumans.get(i).inEnergy(1);
                                humans.get(pair.getValue()).setRemoved(true);
                                removeHumanCount++;
                            }
                        }
                    }
                }

                /*******************************************
                Check starved sub-human
                ********************************************/
                if (!subhumans.get(i).getRemoved() && subhumans.get(i).checkStarve()) {
                    subhumans.get(i).setRemoved(true);
                    removeSubHumanCount++;
                    replaceCorpse(subhumans.get(i).getX(), subhumans.get(i).getY(), false);
                }
            }
        }

        for (int i=zombies.size()-1; i >= 0; i--) {
            if (!zombies.get(i).getRemoved()) {
                /*******************************************
                Human finding for zombie
                ********************************************/
                if (!zombies.get(i).getDirect()) {
                    List<Pair<SpecieTypes, Integer>> findPrey = collisionDetect(zombies.get(i).getPos(), zombies.get(i).getSpotRadius(), SpecieTypes.Human);
                    for (int j = findPrey.size()-1; j >= 0; j--) {
                        if (humans.get(findPrey.get(j).getValue()).getRemoved()) {
                            findPrey.remove(j);
                        }
                    }
                    Pair<SpecieTypes, Integer> typeIndex = pickNearest(findPrey, zombies.get(i).getPos());
                    if (typeIndex.getValue() != -1) {
                        zombies.get(i).setHuntedType(typeIndex.getKey());
                        zombies.get(i).setHuntedIndex(typeIndex.getValue());
                        zombies.get(i).setDirect(true);
                    } else {
                        mobs.get(i).setHuntedType(SpecieTypes.Specie);
                    }
                }
                if (zombies.get(i).getHuntedType() != SpecieTypes.Specie && !humans.get(zombies.get(i).getHuntedIndex()).getRemoved()) {
                    double angle = indexToAngle(new Pair<>(zombies.get(i).getHuntedType(), zombies.get(i).getHuntedIndex()), zombies.get(i).getPos());
                    zombies.get(i).setVelocity(angle);
                    zombies.get(i).move();
                } else {
                    zombies.get(i).setDirect(false);
                }
                /*******************************************
                Human & Zombie collision
                ********************************************/
                ArrayList<Pair<SpecieTypes, Integer>> collidePrey = collisionDetect(zombies.get(i).getPos(), SpecieDiameter, SpecieTypes.Human);
                for (int j = collidePrey.size()-1; j >= 0; j--) {
                    if (humans.get(collidePrey.get(j).getValue()).getRemoved()) {
                        collidePrey.remove(j);
                    }
                }
                for (Pair<SpecieTypes,Integer> pair : collidePrey) {
                    humans.get(pair.getValue()).setRemoved(true);
                    removeHumanCount++;
                    replaceZombie(humans.get(pair.getValue()).getX(), humans.get(pair.getValue()).getY());
                }
            }
        }

        for (int i=humans.size()-1; i >= 0; i--) {
            if (!humans.get(i).getRemoved()) {
                /*******************************************
                Create more human
                ********************************************/
                if (humans.get(i).getBirth()) {
                    humans.get(i).incrementBirthPeriod();
                    if (humans.get(i).giveBirth()) {
                        humans.get(i).resetBirthPeriod();
                        humans.get(i).setBirth(false);
                        replaceHuman(humans.get(i).getX(), humans.get(i).getY());
                    }
                }

                if (!humans.get(i).getDirect()) {
                    /*******************************************
                    Resource finding for human
                    ********************************************/
                    List<Pair<SpecieTypes, Integer>> findResources = collisionDetect(humans.get(i).getPos(), humans.get(i).getSpotRadius(), SpecieTypes.Resource);
                    for (int j = findResources.size()-1; j >= 0; j--) {
                        if (resources.get(findResources.get(j).getValue()).getRemoved()) {
                            findResources.remove(j);
                        }
                    }

                    double angle = indexToAngle(pickNearest(findResources, humans.get(i).getPos()), humans.get(i).getPos());
                    humans.get(i).setVelocity(angle);
                    humans.get(i).setDirect(true);

                    //Debugging range finding
                    // int range = humans.get(i).getSpotRadius();
                    // drawer.drawRect(humans.get(i).getX()-range, humans.get(i).getY()-range, 2*range, 2*range);
                    // for (Pair<SpecieTypes,Integer> pair : findResources) {
                    //     drawer.replaceColor(resources.get(pair.getValue()).getX(), resources.get(pair.getValue()).getY(), 7, true);
                    //     System.out.println(pair.getValue());
                    // }
                }
                humans.get(i).move();

                /*******************************************
                Resource & human collision
                ********************************************/
                ArrayList<Pair<SpecieTypes, Integer>> collideResources = collisionDetect(humans.get(i).getPos(), resourceDiameter/2+SpecieDiameter/2, SpecieTypes.Resource);
                for (int j = collideResources.size()-1; j >= 0; j--) {
                    if (resources.get(collideResources.get(j).getValue()).getRemoved()) {
                        collideResources.remove(j);
                    }
                }
                if (collideResources.size() > 0) {
                    humans.get(i).inEnergy(collideResources.size());
                    humans.get(i).setDirect(false);
                    for (Pair<SpecieTypes,Integer> pair : collideResources) {
                        resources.get(pair.getValue()).setRemoved(true);
                        removeResourceCount++;
                    }
                } else if (humans.get(i).getStarveCount() > 0) {
                    List<Pair<SpecieTypes, Integer>> collideCorpse = collisionDetect(humans.get(i).getPos(), SpecieDiameter, SpecieTypes.Corpse);
                    for (int j = collideCorpse.size()-1; j >= 0; j--) {
                        if (corpses.get(collideCorpse.get(j).getValue()).getRemoved()) {
                            collideCorpse.remove(j);
                        }
                    }
                    if (collideCorpse.size() > 0) {
                       humans.get(i).setRemoved(true);
                       corpses.get(collideCorpse.get(0).getValue()).setRemoved(true);
                       replaceSubHuman(humans.get(i).getX(), humans.get(i).getY());
                    }
                }

                //Debugging use resource
                // for (int j = 0; j < collideResources.size(); j++) {
                //     int index = collideResources.get(j).getValue();
                //     drawer.replaceColor(resources.get(index).getX(), resources.get(index).getY(), resourceDiameter, false);
                // }

                /*******************************************
                Human & human collision
                ********************************************/
                List<Pair<SpecieTypes, Integer>> collideHumans = collisionDetect(humans.get(i).getPos(), SpecieDiameter, SpecieTypes.Human);
                for (int j = collideHumans.size()-1; j >= 0; j--) {
                    if (humans.get(collideHumans.get(j).getValue()).getRemoved()) {
                        collideHumans.remove(j);
                    }
                }
                if (collideHumans.size() > 0) {
                    if (humans.get(i).getGender().equals("Female") && humans.get(i).getBirth() == false) {
                        humans.get(i).setBirth(true);
                    }
                    if (humans.get(i).getGender().equals("Male")) {
                        for (Pair<SpecieTypes,Integer> pair : collideHumans) {
                            if (humans.get(pair.getValue()).getGender().equals("Female") &&
                                humans.get(pair.getValue()).getBirth() == false) {
                                    humans.get(pair.getValue()).setBirth(true);
                            }
                        }
                    }
                }
                /*******************************************
                Check starved human
                ********************************************/
                if (!humans.get(i).getRemoved() && humans.get(i).checkStarve()) {
                    humans.get(i).setRemoved(true);
                    removeHumanCount++;
                    replaceCorpse(humans.get(i).getX(), humans.get(i).getY(), false);
                }
            }
        }
    }

    public boolean draw() {
        drawer.drawStep(resources, humans, zombies, subhumans, corpses, mobs, plants);
        return true;
    }

    public void timeStatus() {
        if (steps%24 >= 21 || steps%24 <= 9) {
            isDay = false;
        } else {
            isDay = true;
        }
        String time = String.format("Time: %02d:00", steps%24);
        MainView.setTime("Steps: " + steps, time, "Days: " + Math.floorDiv(steps, 24));
    }
}
