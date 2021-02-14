package com.spatial.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

public class SummaryPanel extends JPanel {
    JList<String> jList0;
    JList<String> jList1;
    DefaultListModel<String> summaryList0;
    DefaultListModel<String> summaryList1;

    public SummaryPanel(){
        createSummaryPanelContent();
    }

    private void createSummaryPanelContent() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        summaryList0 = new  DefaultListModel<String>();
        jList0 = new JList<String>(summaryList0);
        this.add(createJList("Found", jList0), 0);

        summaryList1 = new  DefaultListModel<String>();
        jList1 = new JList<String>(summaryList1);
        this.add(createJList("Content", jList1), 1);
    }

    private JPanel createJList(String title, JList<String> list) {
        JPanel panelList = new JPanel();
        panelList.setLayout(new BorderLayout());
        panelList.setBorder(BorderFactory.createTitledBorder(title));

        JScrollPane scrollPane = new JScrollPane();

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        scrollPane.setViewportView(list);
        panelList.add(scrollPane);

        return panelList;
    }

    public void setSummaryListContent(int listIdx, Collection<String> content){
        if(listIdx == 0) {
            summaryList0.clear();
            content.forEach(summaryList0::addElement);
        }
        else {
            summaryList1.clear();
            content.forEach(summaryList1::addElement);
        }
    }

    public void addMouseListener(MouseListener mouseListener) {
        jList0.addMouseListener(mouseListener);
    }

    public JList<String> getjList0() {
        return jList0;
    }

    public void setjList0(JList<String> jList0) {
        this.jList0 = jList0;
    }

    public JList<String> getjList1() {
        return jList1;
    }

    public void setjList1(JList<String> jList1) {
        this.jList1 = jList1;
    }

    public DefaultListModel<String> getSummaryList0() {
        return summaryList0;
    }

    public void setSummaryList0(DefaultListModel<String> summaryList0) {
        this.summaryList0 = summaryList0;
    }

    public DefaultListModel<String> getSummaryList1() {
        return summaryList1;
    }

    public void setSummaryList1(DefaultListModel<String> summaryList1) {
        this.summaryList1 = summaryList1;
    }
}
