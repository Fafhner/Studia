package com.spatial.models.figure;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WKTGeometry {
    private WKTType type;
    private ArrayList<WKTGeometry> geometry;
    private ArrayList<Point> points;
    private static final Pattern mainPattern;
    private static final Pattern pointPattern;
    public Color color = Color.black;

    static {
        mainPattern = Pattern.compile("(\\w*)\\s?\\((.+)\\)");
        pointPattern = Pattern.compile("([+-]?([0-9]*[.])?[0-9]+) ([+-]?([0-9]*[.])?[0-9]+)");
    }

    public  WKTGeometry() {}

    public WKTGeometry(WKTType type) {
        this.type = type;
    }

    public WKTGeometry(WKTType type, Collection<Point> points) {
        this.type = type;
        this.points = new ArrayList<Point>(points);

    }

    public WKTType getType() {
        return type;
    }

    public void setType(WKTType type) {
        this.type = type;
    }

    public ArrayList<WKTGeometry> getGeometry() {
        return geometry;
    }

    public void setGeometry(ArrayList<WKTGeometry> geometry) {
        this.geometry = geometry;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public static WKTGeometry readFromText(String text) {
        text = text.replace("\n", " ");

        Matcher matcher = mainPattern.matcher(text);
        matcher.find();
        if (matcher.groupCount() != 2) {
            throw new RuntimeException("0x001");
        }

        WKTType type = WKTType.valueOf(matcher.group(1));
        WKTGeometry figure = readDataFromText(matcher.group(2), type);


        return figure;
    }

    private static WKTGeometry readDataFromText(String text, WKTType type) {
        text = text.trim();
        ArrayList<Point> points;
        WKTGeometry figure = new WKTGeometry(type);
        WKTType nextType = Utils.getNextType(type);

        int bracketCount = 0;
        for (int it = 0; it < text.length(); ) {

            int lBracketInd = text.indexOf('(', it);

            if (lBracketInd != -1) {
                it = lBracketInd + 1;
                bracketCount++;
                do {
                    if (text.charAt(it) == '(') {
                        bracketCount += 1;
                    } else if (text.charAt(it) == ')') {
                        bracketCount -= 1;
                    }

                    it++;
                }
                while (bracketCount != 0);

                String figs = text.substring(lBracketInd + 1, it - 1);

                figure.addFigure(readDataFromText(figs, nextType));
            } else {
                points = readPointsFromText(text);
                figure.setPoints(points);
                return figure;
            }
        }

        return figure;
    }

    private static ArrayList<Point> readPointsFromText(String text) {
        Matcher matcher = pointPattern.matcher(text);
        ArrayList<Point> points = new ArrayList<>();
        while (matcher.find()) {
            Point point = new Point(
                    Float.valueOf(matcher.group(1)),
                    Float.valueOf(matcher.group(3))
            );
            points.add(point);
        }

        return points;
    }

    public void addFigure(WKTGeometry figure) {
        if(geometry == null) {
            geometry = new ArrayList<>();
        }

        geometry.add(figure);
    }

    public void setColorToAll(Color color) {
        this.color = color;

        if (this.geometry != null && this.geometry.size() > 0) {
            this.geometry.forEach(
                    (g) -> g.setColorToAll(color)
            );
        }

    }

}
