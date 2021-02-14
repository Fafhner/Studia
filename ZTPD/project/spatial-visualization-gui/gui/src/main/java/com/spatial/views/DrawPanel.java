package com.spatial.views;


import com.spatial.models.figure.Utils;
import com.spatial.models.figure.WKTGeometry;
import com.spatial.models.figure.Point;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;



public class DrawPanel extends JPanel {
    WKTGeometry geometry;
    float staticScale = 0;
    float scale = 1;
    Integer xPos = 0;
    Integer yPos = 0;
    Float[] boundary;
    boolean center = true;
    boolean scaled = false;

    public DrawPanel() {
    }

    public DrawPanel(WKTGeometry geometry) {
        this.geometry = geometry;
        ArrayList<Point> points = Utils.getAllPoints(geometry);
        Utils.flipData(points);
        boundary = Utils.getRectBoundary(points);

    }

    public WKTGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(WKTGeometry geometry) {
        this.geometry = geometry;
    }

    private void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        translate(g);
        Utils.draw(g2d, this.geometry, staticScale*scale);

    }

    private void translate(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if(center) {
            xPos = -Math.round(boundary[0]*staticScale*scale);
            yPos = -Math.round(boundary[2]*staticScale*scale);
            center = false;
        }

        g2d.translate(xPos, yPos);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!scaled) {
            staticScale = findScale();
            scaled = true;
        }
        draw(g);
    }


    public void doCentering(){
        center = true;
    }

    public float findScale() {
        return getWidth()/(boundary[1]-boundary[0]);
    }

    public float getScale() {
        return this.staticScale*this.scale;
    }

    public Integer getXPos() {
        return xPos;
    }

    public Integer getYPos() {
        return yPos;
    }


}