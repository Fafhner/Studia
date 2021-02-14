package com.spatial.models;

import com.spatial.models.figure.WKTType;

import java.util.ArrayList;

public class Geometry {
    private Integer dim;
    private Integer lrs;
    private WKTType type;
    private Integer srid;
    private ArrayList<Point> points;

    public Geometry(){
        points = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Geometry{" +
                "dim=" + dim +
                ", lrs=" + lrs +
                ", type=" + type +
                ", srid=" + srid +
                ", points=" + points +
                '}';
    }

    public Integer getDim() {
        return dim;
    }

    public void setDim(Integer dim) {
        this.dim = dim;
    }

    public Integer getLrs() {
        return lrs;
    }

    public void setLrs(Integer lrs) {
        this.lrs = lrs;
    }

    public WKTType getType() {
        return type;
    }

    public void setType(WKTType type) {
        this.type = type;
    }

    public Integer getSrid() {
        return srid;
    }

    public void setSrid(Integer srid) {
        this.srid = srid;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public static class Point {
        public Double x;
        public Double y;
        public Double z;

        public Point(Double x, Double y, Double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");
            stringBuilder.append(x).append(", ").append(y);
            if (z != null) {
                stringBuilder.append(", ").append(z);
            }
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }
}


