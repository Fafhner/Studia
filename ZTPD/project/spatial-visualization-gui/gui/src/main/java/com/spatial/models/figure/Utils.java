package com.spatial.models.figure;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Utils {

    static WKTType getNextType(WKTType type) {
        WKTType nextType = WKTType.POINT;
        switch (type){
            case LINESTRING:
            case MULTIPOINT:
            case POINT:
                break;
            case MULTILINESTRING:
            case POLYGON:
                nextType = WKTType.LINESTRING;
                break;
            case MULTIPOLYGON:
                nextType = WKTType.POLYGON;
                break;
        }

        return nextType;
    }


    public static void draw(Graphics2D graphic, WKTGeometry geometry, float scale) {
        ArrayList<Point> points=geometry.getPoints();
        ArrayList<WKTGeometry> figures = geometry.getGeometry();
        switch (geometry.getType()){
            case LINESTRING:
                graphic.setColor(geometry.color);
                for(int i=1; i<points.size(); i++) {
                    Point a = points.get(i-1);
                    Point b = points.get(i);
                    graphic.drawLine(
                            Math.round(a.x*scale),
                            Math.round(a.y*scale),
                            Math.round(b.x*scale),
                            Math.round(b.y*scale)
                    );
                }
                break;
            case MULTIPOINT:
            case POINT:
                graphic.setColor(geometry.color);
                for (Point a : points) {
                    graphic.drawOval(
                            Math.round(a.x * scale), Math.round(a.y * scale), 3, 3);
                }
                break;
            case MULTILINESTRING:
            case MULTIPOLYGON:
                for (WKTGeometry wktGeometry : figures) {
                    draw(graphic, wktGeometry, scale);
                }
                break;
            case POLYGON:
                graphic.setColor(geometry.color);
                for (WKTGeometry figure : figures) {
                    int pointsSize = figure.getPoints().size();
                    int[] xs = new int[pointsSize];
                    int[] ys = new int[pointsSize];

                    for (int j = 0; j < pointsSize; j++) {
                        Point a = figure.getPoints().get(j);
                        xs[j] = Math.round(a.x * scale);
                        ys[j] = Math.round(a.y * scale);
                    }
                    graphic.drawPolygon(xs, ys, pointsSize);
                }
                break;
        }
    }

    public static ArrayList<Point> getAllPoints(WKTGeometry geometry) {

        if(geometry.getPoints() != null) {
            return geometry.getPoints();
        }

        ArrayList<Point> points = new ArrayList<>();

        for(WKTGeometry wktGeometry: geometry.getGeometry()) {
            points.addAll(getAllPoints(wktGeometry));
        }

        return points;
    }

    public static Float[] getRectBoundary(ArrayList<Point> points) {
        float maxX = Float.MIN_VALUE;
        float minX = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;

        for(Point point: points) {
            if(point.x > maxX) {
                maxX = point.x;
            }
            else if (point.x < minX) {
                minX = point.x;
            }

            if(point.y > maxY) {
                maxY = point.y;
            }
            else if (point.y < minY) {
                minY = point.y;
            }
        }

        return new Float[] {minX, maxX, minY, maxY};
    }

    public static Point getCenter(ArrayList<Point> points) {
        int n = points.size();
        float xMean = 0;
        float yMean = 0;

        for(Point point: points) {
            xMean += point.x;
            yMean += point.y;
        }

        return new Point( xMean/n,  yMean/n);
    }

    public static void flipData(ArrayList<Point> points) {
        for(Point point: points) {
            point.y *= -1;
        }
    }

    public static ImageIcon createImageIcon(Color color, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(color);
        graphics.fillRect (0, 0, width, height);
        return new ImageIcon(image);
    }
}
