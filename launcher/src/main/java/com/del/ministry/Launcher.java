package com.del.ministry;

import com.del.ministry.view.MainFrame;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.Locale;
import java.awt.*;
import java.lang.reflect.Field;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Launcher {

    final static Logger logger = Logger.getLogger(Launcher.class);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.
                        getSystemLookAndFeelClassName());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            JFrame.setDefaultLookAndFeelDecorated(true);
            MainFrame mainFrame = new MainFrame();
            mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            mainFrame.setVisible(true);
            mainFrame.setLocale(new Locale("RU"));
            mainFrame.setStatusText("Идет загрузка, подождите...");
        });

        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            try {
                Toolkit xToolkit = Toolkit.getDefaultToolkit();
                Field awtAppClassNameField = xToolkit.getClass().getDeclaredField("awtAppClassName");
                awtAppClassNameField.setAccessible(true);
                awtAppClassNameField.set(xToolkit, "Welcome");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

    }

}
