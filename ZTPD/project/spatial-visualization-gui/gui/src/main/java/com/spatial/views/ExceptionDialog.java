package com.spatial.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExceptionDialog extends JDialog {
    public JLabel errMsg;

    public ExceptionDialog(String msg) {
        errMsg = new JLabel();
        errMsg.setText(msg);

        setModal(true);
        setTitle("Exception");
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout(10, 10));
        getContentPane().add(content);

        JButton ok = new JButton("Ok");
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttons.add(ok);

        content.add(errMsg, BorderLayout.NORTH);
        content.add(buttons, BorderLayout.SOUTH);
        content.setBorder(new EmptyBorder(15, 15, 15, 15));
        pack();

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        getRootPane().setDefaultButton(ok);

    }

    String getExceptionMsg(){
        return errMsg.getText();
    }
}
