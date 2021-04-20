package com.del.ministry.view.actions;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.utils.SystemEnv;
import com.del.ministry.view.MainFrame;
import org.apache.log4j.Logger;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrameActions implements WindowListener {

    final static private Logger logger = Logger.getLogger(MainFrameActions.class);

    private MainFrame mainFrame;

    public MainFrameActions(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        logger.info("================================= WINDOW OPENED =================================");
        logger.info("Loading system variables...");
        for (SystemEnv value : SystemEnv.values()) {
            logger.info("\t\t" + value.getName() + "=" + value.read());
        }
        logger.info("... success.");
        logger.info("Init DB connection...");
        try {
            ServiceManager.getInstance();
            mainFrame.setStatusText("Соединение с базой данных установлено");
            logger.info("... success.");
        } catch (Exception e1) {
            logger.info("... error connection to data base", e1);
            mainFrame.setStatusDB_ERROR();
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            ServiceManager.close();
        } catch (Exception e1) {
            //
        }
        logger.info("================================= WINDOW CLOSING =================================");
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
