package com.spatial.models;

import java.util.ArrayList;
import java.util.HashMap;

public class DBSpatialTable {
    String tableName;
    HashMap<Integer, DBSpatialRow> rows;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public HashMap<Integer, DBSpatialRow> getRows() {
        return rows;
    }

    public void setRows(HashMap<Integer, DBSpatialRow>  rows) {
        this.rows = rows;
    }

    public void addRow(Integer rowId, HashMap<String, String> row) {
        DBSpatialRow r = new DBSpatialRow();
        if(rows == null) {
            rows = new HashMap<>();
        }
        r.table = tableName;
        r.data = row;
        r.rowID = rowId;
        rows.put(rowId, r);
    }

    public ArrayList<String> entriesToString() {
        ArrayList<String> entries = new ArrayList<>();

        rows.forEach(
                (id, entry) -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("ID: ").append(id).append(" | ");
                    entry.data.forEach(
                            (name, value) -> {
                                if(!name.equals("geometry")) {
                                    stringBuilder.append(name).append(": ").append(value).append(" | ");
                                }
                            }
                    );
                    entries.add(stringBuilder.toString());
                }
        );
        return entries;
    }

    public ArrayList<String> getGeometries() {
        ArrayList<String> entries = new ArrayList<>();
        rows.forEach(
                (id, entry) -> {
                    entries.add(entry.data.get("geometry"));
                }
        );
        return entries;
    }

    public String getGeometry(Integer rowID) {
        return rows.get(rowID).data.get("geometry");
    }
}
