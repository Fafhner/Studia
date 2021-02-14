package com.spatial.views;

import com.spatial.models.DBSpatialRow;
import com.spatial.models.DBSpatialTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class RelatePanel extends JPanel {
    HashMap<String, DBSpatialTable> tables;
    DefaultListModel<DBSpatialRow> rListModel;
    DefaultListModel<DBSpatialRow> lListModel;
    JList<DBSpatialRow> lList;
    JComboBox<String> selectTable;
    JComboBox<String> spatialFunc;
    JCheckBox reverse;
    JButton findB;
    JButton showB;

    public RelatePanel() {
        init();
    }

    private void init() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);

        JPanel lPanel = new JPanel();
        JPanel cPanel = new JPanel();
        JPanel rPanel = new JPanel();

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;



        // Left
        selectTable= new JComboBox<>();
        lListModel = new DefaultListModel<>();
        lList = new JList<>(lListModel);
        lList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lList.setLayoutOrientation(JList.VERTICAL);
        lList.setVisibleRowCount(-1);

        JScrollPane lScrollPane = new JScrollPane();
        lScrollPane.setViewportView(lList);
        lPanel.setLayout(new GridBagLayout());
        GridBagConstraints gcb = new GridBagConstraints();
        gcb.fill = GridBagConstraints.BOTH;
        gcb.weighty = 0;
        gcb.weightx = 1;
        gcb.gridx = 0;
        gcb.gridy = 0;
        lPanel.add(selectTable, gcb);
        gcb.weighty = 1;
        gcb.gridy = 1;
        lPanel.add(lScrollPane, gcb);


        // Right
        rListModel = new DefaultListModel<DBSpatialRow>();

        JList<DBSpatialRow> rList = new JList<>(rListModel);
        rList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        rList.setLayoutOrientation(JList.VERTICAL);
        rList.setVisibleRowCount(-1);

        JScrollPane rScrollPane = new JScrollPane();
        rScrollPane.setViewportView(rList);
        rPanel.setLayout(new BorderLayout());
        rPanel.add(rScrollPane);

        // Center
        spatialFunc = new JComboBox<String>();
        reverse = new JCheckBox("Reverse ");
        findB = new JButton("Find");
        JButton clearB = new JButton("Clear");
        showB = new JButton("Show");

        cPanel.setLayout(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 0;
        c2.weighty = 1;
        c2.gridy = 0;
        cPanel.add(spatialFunc, c2);
        c2.gridy = 1;
        cPanel.add(reverse, c2);
        c2.gridy = 2;
        cPanel.add(findB, c2);
        c2.gridy = 3;
        cPanel.add(clearB, c2);
        c2.gridy = 4;
        cPanel.add(showB, c2);



        clearB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Collection<DBSpatialRow> vals = rList.getSelectedValuesList();
                if (vals == null || vals.size() == 0) {
                    rListModel.clear();
                }
                else {
                    vals.forEach((row) -> rListModel.removeElement(row)
                    );
                }
            }

        });

        selectTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = (String) selectTable.getSelectedItem();
                HashMap<Integer, DBSpatialRow> rows = tables.get(tableName).getRows();
                lListModel.clear();
                rows.forEach(
                        (id, row) -> {
                            lListModel.addElement(row);
                        }
                );

            }
        });

        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.WEST;
        this.add(lPanel, c);
        c.gridx = 1;
        c.weighty = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        this.add(cPanel, c);
        c.gridx= 2;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        this.add(rPanel, c);

        spatialFunc.addItem("ANYINTERACT");
        spatialFunc.addItem("CONTAINS");
        spatialFunc.addItem("COVEREDBY");
        spatialFunc.addItem("COVERS");
        spatialFunc.addItem("EQUAL");
        spatialFunc.addItem("INSIDE");
        spatialFunc.addItem("OVERLAPBDYDISJOINT");
        spatialFunc.addItem("OVERLAPBDYINTERSECT");
        spatialFunc.addItem("TOUCH");
        spatialFunc.addItem("ON");
    }

    public DefaultListModel<DBSpatialRow> getrListModel() {
        return rListModel;
    }

    public void setTables(HashMap<String, DBSpatialTable> tables) {
        this.tables = tables;

        this.tables.forEach(
                (tableName, table) -> {
                    selectTable.addItem(
                            table.getTableName()
                    );
                }
        );
    }

    public String getSDOFunction() {
        return (String) spatialFunc.getSelectedItem();
    }

    public boolean isReversed() {
        return reverse.isSelected();
    }

    public JButton getFindButton() {
        return findB;
    }

    public DBSpatialRow getSelectedRow() {
        return lList.getSelectedValue();
    }
}
