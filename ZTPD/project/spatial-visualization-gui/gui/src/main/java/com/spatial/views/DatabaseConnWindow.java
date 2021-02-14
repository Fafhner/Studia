package com.spatial.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class DatabaseConnWindow extends JDialog {
    private JTextField hostName;
    private JTextField port;
    private JTextField serviceName;
    private JTextField user;
    private JPasswordField password;
    private JLabel err;
    private JButton ok;
    private JButton cancel;
    private boolean cancelled = true;

    public DatabaseConnWindow() {
        setModal(true);
        this.setLocation(100, 100);
        this.setSize(new Dimension(300, 500));
        setTitle("Connect to database");
        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        getContentPane().add(content);

        hostName = new JTextField("");
        port = new JTextField("");
        serviceName = new JTextField("");
        user = new JTextField("");
        password = new JPasswordField("");
        err = new JLabel();

        err.setVisible(false);

        ok = new JButton("Accept");
        cancel = new JButton("Cancel");
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelButtons.add(ok);
        panelButtons.add(cancel);

        JPanel panelData = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridx = 0;

        panelData.add(new JLabel("hostname: "), gbc);
        gbc.gridx = 1;
        panelData.add(hostName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelData.add(new JLabel("port: "), gbc);
        gbc.gridx = 1;
        panelData.add(port, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelData.add(new JLabel("service name: "), gbc);
        gbc.gridx = 1;
        panelData.add(serviceName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelData.add(new JLabel("user: "), gbc);
        gbc.gridx = 1;
        panelData.add(user, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panelData.add(new JLabel("password: "), gbc);
        gbc.gridx = 1;
        panelData.add(password, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JPanel panelErr = new JPanel();
        panelErr.add(err, gbc);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        content.add(panelData, c);
        c.gridy = 1;
        c.weighty = 0;
        content.add(panelErr, c);
        c.gridy = 2;
        c.weighty = 0;
        content.add(panelButtons, c);

        cancel.addActionListener(e -> {
            cancelled = true;
            dispose();
        });

        getRootPane().setDefaultButton(ok);
    }

    public HashMap<String, String> getValues() {
        HashMap<String, String> values = new HashMap<>();
        values.put("hostName", hostName.getText());
        values.put("port", port.getText());
        values.put("serviceName", serviceName.getText());
        values.put("user", user.getText());
        values.put("password", new String(password.getPassword()));

        return values;
    }

    public void setErrorMsg(String msg) {
        this.err.setText(msg);
        err.setVisible(true);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean allFilled() {
        return !( hostName.getText().equals("") ||
                port.getText().equals("")||
                serviceName.getText().equals("") ||
                user.getText().equals("") ||
                (new String(password.getPassword())).equals("") );
    }

    public void setOkActionListener(ActionListener action) {
        this.ok.addActionListener(action);
    }

    public void setCancelActionListener(AbstractAction action) {
        this.ok.addActionListener(action);
    }
}
