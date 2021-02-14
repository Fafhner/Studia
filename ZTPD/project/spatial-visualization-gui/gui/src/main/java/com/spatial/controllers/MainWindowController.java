package com.spatial.controllers;

import com.spatial.models.DBSpatialRow;
import com.spatial.models.figure.WKTGeometry;
import com.spatial.models.figure.WKTType;
import com.spatial.views.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainWindowController {
    MainWindow mainWindow;
    DBOracleSpatial db;

    public MainWindowController() {
        mainWindow = new MainWindow();
        db = new DBOracleSpatial();

        initMainWindowMenu();
        initMainWindowFooter();
        addSummaryListener();
        addShowListener();
        addFindListener();
        addFindNNListener();

    }

    private void initMainWindowMenu() {
        JMenu menu = mainWindow.getJMenuBar().getMenu(0);

        JMenuItem item = new JMenuItem(new AbstractAction("Connect") {
            public void actionPerformed(ActionEvent e) {
                DatabaseConnWindow dbConnWin = new DatabaseConnWindow();
                dbConnWin.setOkActionListener(null);

                dbConnWin.setOkActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (!dbConnWin.allFilled()) {
                            dbConnWin.setErrorMsg("You must fill all fields.");
                        } else {
                            HashMap<String, String> data = dbConnWin.getValues();
                            db.setJdbcUrl(
                                    "jdbc:oracle:thin:@//" +
                                            data.get("hostName") + ":" +
                                            data.get("port") + "/" +
                                            data.get("serviceName")
                            );
                            db.setUserid(data.get("user"));
                            db.setPassword(data.get("password"));


                            try {
                                db.connect();
                                dbConnWin.dispose();

                                updateOnDBConnection();


                            } catch (SQLException exception) {
                                dbConnWin.setErrorMsg("Unable to connect: State:" + exception.getSQLState() + " " + exception.getMessage());
                            }
                        }
                    }
                });
                dbConnWin.setVisible(true);
            }
        });


        menu.add(item, 0);
    }

    public void initMainWindowFooter() {
        HashMap<String, String> footer = new HashMap<>();
        footer.put("Status", "Disconnected");
        mainWindow.setFooterData(footer);
    }

    public void show() {
        mainWindow.setVisible(true);
    }

    private void updateOnDBConnection() {
        try {
            db.readSpatialDataFromDB();

            DBDataReaderController rdc = new DBDataReaderController();
            rdc.mainWindow = mainWindow;
            rdc.db = db;

            Thread thread = new Thread(rdc);
            thread.start();

        } catch (SQLException sqlE) {
            System.out.println("Error: " + sqlE.getMessage());
        }
    }


    private void addSummaryListener() {
        SummaryPanel summaryPanel = mainWindow.getSummaryPanel();

        summaryPanel.addMouseListener(
                new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 1) {

                            String tableName = summaryPanel.getjList0().getSelectedValue();
                            ArrayList<String> geoms = db.getTable(tableName).entriesToString();
                            mainWindow.setSummaryContent(geoms);
                        } else if (e.getClickCount() == 2) {
                            String tableName = summaryPanel.getjList0().getSelectedValue();
                            ArrayList<String> geoms = db.getTable(tableName).getGeometries();

                            WKTGeometry mainFigure = new WKTGeometry();
                            mainFigure.setType(WKTType.MULTIPOLYGON);

                            for (String geom : geoms) {
                                mainFigure.addFigure(
                                        WKTGeometry.readFromText(geom)
                                );
                            }

                            JFrame frame = DrawPanelUI.createFrame(mainFigure);
                            frame.setVisible(true);
                        }
                    }
                }
        );


    }

    private void addShowListener() {
        Color[] colors = new Color[]{
                Color.BLACK, Color.BLUE, Color.RED,
                Color.GREEN, Color.MAGENTA, Color.CYAN, Color.ORANGE};


        mainWindow.addMPPShowListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<DBSpatialRow> entries = new ArrayList<>();

                        for (int i = 0; i < mainWindow.getMultiPlotPanel().getSelectRight().size(); i++) {
                            entries.add(mainWindow.getMultiPlotPanel().getSelectRight().get(i));
                        }

                        setUpDrawPanel(entries, colors);
                    }
                });

        mainWindow.addRPShowListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<DBSpatialRow> entries = new ArrayList<>();
                for (int i = 0; i < mainWindow.getRelatePanel().getrListModel().size(); i++) {
                    entries.add(mainWindow.getRelatePanel().getrListModel().get(i));
                }

                setUpDrawPanel(entries, colors);
            }

        });

        mainWindow.addFNNPShowListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<DBSpatialRow> entries = new ArrayList<>();

                for (int i = 0; i < mainWindow.getFindNNPanel().getrListModel().size(); i++) {
                    entries.add(mainWindow.getFindNNPanel().getrListModel().get(i));
                }

                setUpDrawPanel(entries, colors);
            }
        });

    }


    private void addFindListener() {
        mainWindow.getRelatePanel().getFindButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sdo_func = mainWindow.getRelatePanel().getSDOFunction();
                DBSpatialRow selectedRow = mainWindow.getRelatePanel().getSelectedRow();
                boolean isReversed = mainWindow.getRelatePanel().isReversed();
                if (selectedRow != null) {
                    ArrayList<DBSpatialRow> rows = db.findByRelation(selectedRow, sdo_func, isReversed);

                    rows.forEach(
                            (row) -> {
                                if(!mainWindow.getRelatePanel().getrListModel().contains(row)) {
                                    mainWindow.getRelatePanel().getrListModel().addElement(row);
                                }
                            }
                    );
                }
            }
        });

    }

    private void addFindNNListener() {
        mainWindow.getFindNNPanel().getFindButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float range = mainWindow.getFindNNPanel().getRange();

                ArrayList<DBSpatialRow> rows = db.findNearest(
                        mainWindow.getFindNNPanel().getXValue(),
                        mainWindow.getFindNNPanel().getYValue(),
                        range);

                rows.forEach(
                        (row) -> {
                            if(!mainWindow.getFindNNPanel().getrListModel().contains(row)) {
                                mainWindow.getFindNNPanel().getrListModel().addElement(row);
                            }
                        }
                );

            }
        });

    }

    private void setUpDrawPanel(ArrayList<DBSpatialRow> entries, Color[] colors) {
        WKTGeometry mainFigure = new WKTGeometry();
        mainFigure.setType(WKTType.MULTIPOLYGON);

        if (entries.size() > 0) {
            HashMap<String, ArrayList<String>> groupedGeometries = new HashMap<>();

            for (DBSpatialRow row : entries) {
                ArrayList<String> geoms = groupedGeometries.computeIfAbsent(
                        row.table, k -> new ArrayList<>());

                geoms.add(db.getTable(row.table).getGeometry(row.rowID));
            }

            final int[] ri = {0};
            groupedGeometries.forEach(
                    (table, rows) -> {
                        WKTGeometry fig = new WKTGeometry();
                        fig.setType(WKTType.MULTIPOLYGON);
                        for (String g : rows) {
                            WKTGeometry f = WKTGeometry.readFromText(g);
                            fig.addFigure(f);
                        }
                        fig.setColorToAll(colors[ri[0] % colors.length]);
                        ri[0]++;
                        mainFigure.addFigure(fig);
                    }
            );


            JFrame frame = DrawPanelUI.createFrame(mainFigure);
            frame.setVisible(true);
        }
    }


}
