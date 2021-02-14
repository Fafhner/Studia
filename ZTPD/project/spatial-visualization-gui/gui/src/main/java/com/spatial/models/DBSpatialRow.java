package com.spatial.models;

import java.util.HashMap;

public class DBSpatialRow {
    public String table;
    public Integer rowID;
    public HashMap<String, String> data;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Table=").append(table).append(" | ").append("ID=").append(rowID);
        data.forEach(
                (col, val) -> {
                    if(!col.equals("geometry")) {
                        stringBuilder.append(" | ").append(col).append("=").append(val);
                    }

                }
        );
        return stringBuilder.toString();
    }
}
