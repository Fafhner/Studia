package com.spatial.controllers;

import java.sql.ResultSet;
import java.sql.Statement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.spatial.models.DBSpatialRow;
import com.spatial.models.DBSpatialTable;

public class DBOracleSpatial extends DBConnection {
    HashMap<String, DBSpatialTable> tables;
    Statement stmt;
    ResultSet rset;
    String query;

    public DBOracleSpatial(){
        tables= new HashMap<>();
    }
    public DBOracleSpatial(String jdbcUrl, String userid, String password){
        super(jdbcUrl, userid, password);
        tables= new HashMap<>();
    }

    public void readSpatialDataFromDB() throws SQLException {
        ArrayList<String> tableNames = getSpatialTablesNamesFromDB();

        for(String tableName: tableNames) {
            DBSpatialTable table = new DBSpatialTable();
            table.setTableName(tableName);
            table.setRows(readTableEntriesFromDB(tableName));

            tables.put(tableName, table);
        }
    }

    public ArrayList<String> getSpatialTablesNamesFromDB() throws SQLException {
        ArrayList<String> tables = new ArrayList<>();

        this.connect();
        stmt = this.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        query = " select TABLE_NAME from ALL_SDO_GEOM_METADATA where owner = 'ZSBD_TOOLS' ";

        rset = stmt.executeQuery(query);

        while(rset.next()) {
            String table = rset.getString(1);
            tables.add(table);


        }

        return tables;
    }


    public HashMap<Integer, DBSpatialRow> readTableEntriesFromDB(String table) throws SQLException {

        HashMap<Integer, String>  g = getWKTGeometryFromDB(table);
        HashMap<Integer, DBSpatialRow> entries = new HashMap<>();

        this.connect();
        stmt = this.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        query = " select * from " + table + " ";

        rset = stmt.executeQuery(query);
        int colNum = rset.getMetaData().getColumnCount();

        while(rset.next()) {
            Integer id = rset.getInt("ID");
            DBSpatialRow r = new DBSpatialRow();
            HashMap<String, String> entry = new HashMap<>();

            for(int i = 1; i <= colNum; i++) {
                String colName = rset.getMetaData().getColumnName(i);

                if(!colName.toLowerCase().equals("geom") && !colName.toLowerCase().equals("id")) {
                    entry.put(colName, rset.getString(i));
                }
            }

            entry.put("geometry", g.get(id));
            r.data = entry;
            r.table = table;
            r.rowID = id;
            entries.put(id, r);
        }

        return entries;
    }


    public HashMap<Integer, String> getWKTGeometryFromDB(String table) throws SQLException {
        HashMap<Integer, String> geoms = new HashMap<>();

        this.connect();
        stmt = this.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        query = " select id, SDO_UTIL.TO_WKTGEOMETRY(geom) from " + table + " ";

        rset = stmt.executeQuery(query);

        while(rset.next()) {
            String geom = rset.getString(2);
            geoms.put(rset.getInt(1), geom);
        }

        return geoms;
    }

    public Set<String> getTablesName() {
       return tables.keySet();
    }

    public DBSpatialTable getTable(String tbName) {
        return tables.get(tbName);
    }

    public HashMap<String, DBSpatialTable> getTables() {
        return tables;
    }

    private String getSDORelateString(String ta, String tb, String mask, boolean reversed) {
        if(reversed) {
            String buff = ta;
            ta = tb;
            tb = buff;
        }

        return "SDO_RELATE(" + ta + ", " + tb + ", 'mask=" + mask + "') = 'TRUE'";
    }

    public ArrayList<DBSpatialRow> runSDOFuncQuery(DBSpatialRow row, String tableName ,String relation, boolean reversed) throws SQLException {
        this.connect();
        stmt = this.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        query = " select r.id " +
                " from " + tableName + " r, " + row.table + " f " +
                " where f.id=" + row.rowID + " and " +
                getSDORelateString("r.geom", "f.geom", relation, reversed);

        rset = stmt.executeQuery(query);

        ArrayList<DBSpatialRow> rtnRows = new ArrayList<>();

        while(rset.next()) {
            int rowId = rset.getInt(1);

            rtnRows.add(tables.get(tableName).getRows().get(rowId));
        }

        return rtnRows;
    }

    public ArrayList<DBSpatialRow> findByRelation(DBSpatialRow row, String relation, boolean reversed) {
        ArrayList<DBSpatialRow> rtnRows = new ArrayList<>();

        tables.forEach(
                (tableName, table) -> {
                    try {
                        rtnRows.addAll(runSDOFuncQuery(row, tableName, relation, reversed));
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
        );

        return rtnRows;
    }

    public ArrayList<DBSpatialRow> findNearest(float x, float y, float range) {
        ArrayList<DBSpatialRow> rtnRows = new ArrayList<>();

        tables.forEach(
                (tableName, table) -> {
                    try {
                        rtnRows.addAll(findNearestForTable(x, y, range, tableName));
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
        );

        return rtnRows;
    }

    public ArrayList<DBSpatialRow> findNearestForTable(float x, float y, float range, String table) throws SQLException {
        ArrayList<DBSpatialRow> rtnRows = new ArrayList<>();

        this.connect();
        stmt = this.getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        query = "select c.id from " + table +" c " +
                " where  SDO_WITHIN_DISTANCE(" +
                " c.GEOM, SDO_GEOMETRY(2001,8307,null,MDSYS.SDO_ELEM_INFO_ARRAY(1, 1, 1), MDSYS.SDO_ORDINATE_ARRAY("+ x +", " + y +"))," +
                " 'distance="+range+ " unit=km') = 'TRUE' ";

        rset = stmt.executeQuery(query);
        while(rset.next()) {
            int rowId = rset.getInt(1);
            rtnRows.add(tables.get(table).getRows().get(rowId));
        }

        return  rtnRows;
    }
}
