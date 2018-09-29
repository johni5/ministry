package com.del.ministry.view;

import org.apache.log4j.Logger;

import javax.swing.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Launcher {

    final static Logger logger = Logger.getLogger(Launcher.class);

    public static MainFrame mainFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.
                            getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                JFrame.setDefaultLookAndFeelDecorated(true);
                mainFrame = new MainFrame();
                mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                mainFrame.setVisible(true);
                MainFrame.setStatusText("Идет загрузка, подождите...");
            }
        });
    }

}
