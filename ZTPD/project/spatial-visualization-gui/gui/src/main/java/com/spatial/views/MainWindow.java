package com.spatial.views;

import com.spatial.models.DBSpatialTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainWindow extends JFrame {
    private SummaryPanel summaryPanel;
    private MultiPlotPanel multiPlotPanel;
    private RelatePanel relatePanel;
    private JPanel footerPanel;
    private FindNNPanel findNNPanel;

    public MainWindow() {
        createMainWindow();
    }

    private void createMainWindow() {
        this.setTitle("Oracle Spatial");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addMenu();
        addPanels();

        addFooter();
    }

    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu menu = new JMenu("Options");
        menuBar.add(menu, 0);


    }

    private void addPanels() {
        JPanel mainPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 1);
        mainPanel.setLayout(gridLayout);
        JTabbedPane tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane);

        summaryPanel = new SummaryPanel();
        tabbedPane.addTab("Summary",  summaryPanel);

        multiPlotPanel = new MultiPlotPanel();
        tabbedPane.addTab("Show",  multiPlotPanel);

        relatePanel = new RelatePanel();
        tabbedPane.addTab("Relate-find", relatePanel);

        findNNPanel = new FindNNPanel();
        tabbedPane.addTab("NN-find", findNNPanel);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void addFooter() {
        this.add(new JSeparator(), BorderLayout.SOUTH);
        footerPanel = new JPanel();
        this.add(footerPanel, BorderLayout.SOUTH);

        footerPanel.setLayout(new GridBagLayout());
        footerPanel.setBorder(BorderFactory.createTitledBorder( "Info"));

    }

    public void setFooterData(Map<String, String> footerData) {

        AtomicInteger ypos = new AtomicInteger();
        GridBagConstraints c = new GridBagConstraints();
        footerPanel.removeAll();
        footerData.forEach(
                (key, info) -> {
                    c.gridy = ypos.get();

                    c.gridx = 0;
                    c.weightx = 0.25;
                    footerPanel.add(new JLabel(key + ": ", JLabel.LEFT), c);

                    c.gridx = 1;
                    c.weightx = 0.75;
                    footerPanel.add(new JLabel(info, JLabel.LEFT), c);

                    ypos.addAndGet(1);
                }
        );
    }

    public void setStatusConnected() {
        HashMap<String, String> footer = new HashMap<>();
        footer.put("Status", "Connected");
        setFooterData(footer);
    }

    public void setStatusReadingData() {
        HashMap<String, String> footer = new HashMap<>();
        footer.put("Status", "Reading data...");
        setFooterData(footer);
    }

    public void setMultiPlotContent(HashMap<String, DBSpatialTable> tables){
        multiPlotPanel.setTables(tables);
        relatePanel.setTables(tables);
    }

    public  void addMPPShowListener(ActionListener listener) {
        multiPlotPanel.showB.addActionListener(listener);
    }

    public  void addRPShowListener(ActionListener listener) {
        relatePanel.showB.addActionListener(listener);
    }

    public  void addFNNPShowListener(ActionListener listener) {
        findNNPanel.showB.addActionListener(listener);
    }

    public void setSummaryMain(Collection<String> coll) {
        summaryPanel.setSummaryListContent(0, coll);
    }

    public void setSummaryContent(Collection<String> content) {
        summaryPanel.setSummaryListContent(1, content);
    }

    public SummaryPanel getSummaryPanel() {
        return summaryPanel;
    }

    public MultiPlotPanel getMultiPlotPanel() {
        return multiPlotPanel;
    }

    public void setMultiPlotPanel(MultiPlotPanel multiPlotPanel) {
        this.multiPlotPanel = multiPlotPanel;
    }

    public JPanel getFooterPanel() {
        return footerPanel;
    }

    public RelatePanel getRelatePanel() {
        return relatePanel;
    }

    public FindNNPanel getFindNNPanel() {
        return findNNPanel;
    }
}