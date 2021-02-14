package com.spatial.controllers;

import com.spatial.views.MainWindow;

import java.sql.SQLException;
import java.util.Set;

public class DBDataReaderController implements Runnable {
    public MainWindow mainWindow;
    public DBOracleSpatial db;

    @Override
    public void run() {
        mainWindow.setStatusReadingData();
        mainWindow.getFooterPanel().revalidate();

        try {
            db.readSpatialDataFromDB();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        Set<String> tables = db.getTablesName();

        mainWindow.setSummaryMain(tables);
        mainWindow.setMultiPlotContent(db.getTables());

        mainWindow.setStatusConnected();
        mainWindow.getFooterPanel().revalidate();
    }
}
