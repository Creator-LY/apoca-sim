package com.model;

public class Boundary {
    double xmin;
    double xmax;
    double ymin;
    double ymax;
    Boundary(double xmin, double xmax, double ymin, double ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    public double getXmin() {
        return xmin;
    }
    public double getXmax() {
        return xmax;
    }
    public double getYmin() {
        return ymin;
    }
    public double getYmax() {
        return ymax;
    }
    public boolean contains(double pointX, double PointY) {
        return pointX >= xmin && pointX <=xmax
               && PointY >= ymin && PointY <= ymax;
    }
}