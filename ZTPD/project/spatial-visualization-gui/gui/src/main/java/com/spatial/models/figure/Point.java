package com.spatial.models.figure;

public class Point {
    public Float x;
    public Float y;
    public Float z;

    public Point(Float x, Float y, Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(Float x, Float y) {
        this.x = x;
        this.y = y;
        this.z = null;
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