package com.spatial.views;

import com.spatial.models.DBSpatialRow;
import com.spatial.models.DBSpatialTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.*;
import java.util.List;

public class MultiPlotPanel extends JPanel {
    HashMap<String, DBSpatialTable> tables;
    DefaultListModel<DBSpatialRow> selectLeft;
    DefaultListModel<DBSpatialRow> selectRight;
    JList<DBSpatialRow> showList;
    JComboBox<String> selectTable;
    JButton showB;

    public MultiPlotPanel() {
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
        selectTable = new JComboBox<>();
        selectLeft = new DefaultListModel<>();
        JList<DBSpatialRow> lList= new JList<>(selectLeft);
        lList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
        selectRight = new DefaultListModel<DBSpatialRow>();

        showList = new JList<>(selectRight);
        showList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        showList.setLayoutOrientation(JList.VERTICAL);
        showList.setVisibleRowCount(-1);

        JScrollPane rScrollPane = new JScrollPane();
        rScrollPane.setViewportView(showList);
        rPanel.setLayout(new BorderLayout());
        rPanel.add(rScrollPane);

        // Center
        JButton leftB = new JButton("<<");
        JButton rightB = new JButton(">>");
        showB = new JButton("Show");

        cPanel.setLayout(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 0;
        c2.weighty = 1;
        c2.gridy = 0;
        cPanel.add(rightB, c2);
        c2.gridy = 1;
        cPanel.add(leftB, c2);
        c2.gridy = 2;
        cPanel.add(showB, c2);



        rightB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<DBSpatialRow> vals =  lList.getSelectedValuesList();
                if(vals.isEmpty()) {
                    Enumeration<DBSpatialRow> eRows =  selectLeft.elements();
                    ArrayList<DBSpatialRow> v = new ArrayList<>();
                    while(eRows.hasMoreElements()) {
                        v.add(eRows.nextElement());
                    }
                    vals = v;
                }
                for(DBSpatialRow val: vals) {
                    selectLeft.removeElement(val);
                    selectRight.addElement(val);
                }
            }


        });

        leftB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<DBSpatialRow> vals =  showList.getSelectedValuesList();
                String tableName = (String) selectTable.getSelectedItem();
                if(vals.isEmpty()) {
                    Enumeration<DBSpatialRow> eRows =  selectRight.elements();
                    ArrayList<DBSpatialRow> v = new ArrayList<>();
                    while(eRows.hasMoreElements()) {
                        v.add(eRows.nextElement());
                    }
                    vals = v;
                }

                for(DBSpatialRow val: vals) {
                    selectRight.removeElement(val);
                    if(val.table.equals(tableName)) {
                        selectLeft.addElement(val);
                    }

                }

            }
        });

        selectTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = (String) selectTable.getSelectedItem();
                HashMap<Integer, DBSpatialRow> rows = tables.get(tableName).getRows();
                selectLeft.clear();
                rows.forEach(
                        (id, row) -> {
                            if(!selectRight.contains(row)) {
                                selectLeft.addElement(row);
                            }
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
    }

    public DefaultListModel<DBSpatialRow> getSelectRight() {
        return selectRight;
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


}
