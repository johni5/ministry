package com.del.ministry.view;

import org.apache.log4j.Logger;

import javax.swing.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Launcher {

    final static Logger logger = Logger.getLogger(Launcher.class);

    public static MainFrame mainFrame;

    public static void main(String[] args) {

//        int a = 20, b = 1, c = 30;
//        int x = (int) ((a * b * 1.0) / (c * 1.0));
//        System.out.println(x);
//        if (1 == 1) System.exit(0);


        SwingUtilities.invokeLater(() -> {
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
        });
    }

}
