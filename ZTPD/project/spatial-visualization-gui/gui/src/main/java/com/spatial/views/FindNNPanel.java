package com.spatial.views;

import com.spatial.models.DBSpatialRow;
import com.spatial.models.DBSpatialTable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FindNNPanel extends JPanel {

    DefaultListModel<DBSpatialRow> rListModel;
    JSpinner distSpinner;
    JSpinner xSpinner;
    JSpinner ySpinner;
    JButton findB;
    JButton showB;

    public FindNNPanel() {
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
        lPanel.setLayout(new GridBagLayout());

        JPanel ld = new JPanel();
        ld.setBorder(BorderFactory.createLineBorder(Color.black));
        ld.setLayout(new GridBagLayout());

        lPanel.add(ld);


        SpinnerModel xSpinnerModel = new SpinnerNumberModel(0.f, -1000f, 1000f, 0.5f);
        xSpinner = new JSpinner(xSpinnerModel);

        SpinnerModel ySpinnerModel = new SpinnerNumberModel(0.f, -1000f, 1000f, 0.5f);
        ySpinner = new JSpinner(ySpinnerModel);

        SpinnerModel distSpinnerModel = new SpinnerNumberModel(100.0f, 0, 1000f, 10.0f);
        distSpinner = new JSpinner(distSpinnerModel);


        {
            GridBagConstraints gcb = new GridBagConstraints();
            gcb.fill = GridBagConstraints.NONE;
            gcb.weighty = 0;
            gcb.weightx = 0;
            gcb.gridx = 0;
            gcb.gridy = 0;
            ld.add(new Label("Select X position"), gcb);
            gcb.gridy = 1;
            ld.add(xSpinner, gcb);
            gcb.gridy = 2;
            ld.add(new Label("Select Y position"), gcb);
            gcb.gridy = 3;
            ld.add(ySpinner, gcb);
            gcb.gridy = 4;
            ld.add(new Label("Select distance [km]"), gcb);
            gcb.gridy = 5;
            ld.add(distSpinner, gcb);
        }

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


        findB = new JButton("Find");
        JButton clearB = new JButton("Clear");
        showB = new JButton("Show");

        cPanel.setLayout(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.weightx = 0;
        c2.weighty = 1;
        c2.gridy = 0;
        cPanel.add(findB, c2);
        c2.gridy = 1;
        cPanel.add(clearB, c2);
        c2.gridy = 2;
        cPanel.add(showB, c2);



        clearB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Collection<DBSpatialRow> vals =  rList.getSelectedValuesList();
                if (vals == null || vals.size() == 0) {
                    rListModel.clear();
                }
                else {
                    vals.forEach((row) -> rListModel.removeElement(row));
                }
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


    public JButton getFindButton() {
        return findB;
    }


    public float getRange() {
        return ((Double) distSpinner.getValue()).floatValue();
    }

    public float getXValue() {
        return ((Double) xSpinner.getValue()).floatValue();
    }

    public float getYValue() {
        return ((Double) ySpinner.getValue()).floatValue();
    }

    public DefaultListModel<DBSpatialRow> getrListModel() {
        return rListModel;
    }
}
