package com.spatial.models;

public class SpatialTabelInfo {
    String owner;
    String tableName;
    String columnName;
    String srid;

    public SpatialTabelInfo() {
    }

    public SpatialTabelInfo(String owner, String tableName, String columnName, String srid) {
        this.owner = owner;
        this.tableName = tableName;
        this.columnName = columnName;
        this.srid = srid;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getSrid() {
        return srid;
    }

    public void setSrid(String srid) {
        this.srid = srid;
    }
}
