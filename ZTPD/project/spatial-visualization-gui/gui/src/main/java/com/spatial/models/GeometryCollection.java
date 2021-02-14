package com.spatial.models;

import java.util.ArrayList;
import java.util.HashMap;

public class GeometryCollection {
    private HashMap<Integer , Geometry> geometries;

    public GeometryCollection() {
        geometries = new HashMap<>();
    }

    public void addGeometry(Integer id, Geometry geometry){
        if(geometries.containsKey(id) ){
            geometries.get(id).getPoints().addAll(geometry.getPoints());
        }
        else {
            geometries.put(id, geometry);
        }
    }

    public Geometry getGeometry(Integer id){
        return geometries.get(id);
    }

    public ArrayList<Geometry> getAll() {
        return (ArrayList<Geometry>) geometries.values();
    }

    public HashMap<Integer , Geometry> getGeometries(){
        return geometries;
    }
}
