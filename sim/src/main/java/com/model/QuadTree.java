package com.model;

import java.util.ArrayList;
import javafx.util.Pair;

public class QuadTree {
    private Node root;

    // helper node data type
    private class Node {
        double x, y;              // x- and y- coordinates
        Node NW, NE, SE, SW;   // four subtrees
        Pair<SpecieTypes, Integer> values;           // associated data

        Node(double x, double y, Pair<SpecieTypes, Integer> values) {
            this.x = x;
            this.y = y;
            this.values = values;
        }
    }

     /***********************************************************************
    *  Insert (x, y) into appropriate quadrant
    ***************************************************************************/

    public void insert(double x, double y, Pair<SpecieTypes, Integer> values) {
        root = insert(root, x, y, values);
    }

    private Node insert(Node h, double x, double y, Pair<SpecieTypes, Integer> values) {
        if (h == null) return new Node(x, y, values);
        //// if (eq(x, h.x) && eq(y, h.y)) h.value = value;  // duplicate
        else if ( less(x, h.x) &&  less(y, h.y)) h.SW = insert(h.SW, x, y, values);
        else if ( less(x, h.x) && !less(y, h.y)) h.NW = insert(h.NW, x, y, values);
        else if (!less(x, h.x) &&  less(y, h.y)) h.SE = insert(h.SE, x, y, values);
        else if (!less(x, h.x) && !less(y, h.y)) h.NE = insert(h.NE, x, y, values);
        return h;
    }

    /***********************************************************************
    *  Comparison function
    ***************************************************************************/

    private boolean less(double k1, double k2) { return k1 < k2; }

    /***********************************************************************
    *  Range search.
    ***************************************************************************/

    public ArrayList<Pair<SpecieTypes, Integer>> query2D(Boundary rect, SpecieTypes allowed) {
        return query2D(root, rect, new ArrayList<Pair<SpecieTypes, Integer>>(), allowed);
    }

    private ArrayList<Pair<SpecieTypes, Integer>> query2D(Node h, Boundary rect, ArrayList<Pair<SpecieTypes, Integer>> pairs, SpecieTypes allowed) {
        if (h == null) return pairs;
        double xmin = rect.getXmin();
        double ymin = rect.getYmin();
        double xmax = rect.getXmax();
        double ymax = rect.getYmax();
        if (allowed == SpecieTypes.Specie && rect.contains(h.x, h.y)) {
            pairs.add(h.values);
        }
        else if (rect.contains(h.x, h.y) && h.values.getKey() == allowed) {
            pairs.add(h.values);
        }
        if ( less(xmin, h.x) &&  less(ymin, h.y)) query2D(h.SW, rect, pairs, allowed);
        if ( less(xmin, h.x) && !less(ymax, h.y)) query2D(h.NW, rect, pairs, allowed);
        if (!less(xmax, h.x) &&  less(ymin, h.y)) query2D(h.SE, rect, pairs, allowed);
        if (!less(xmax, h.x) && !less(ymax, h.y)) query2D(h.NE, rect, pairs, allowed);

        return pairs;
    }
}
