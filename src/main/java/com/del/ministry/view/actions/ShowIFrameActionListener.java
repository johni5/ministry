package com.del.ministry.view.actions;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.utils.Utils;
import com.del.ministry.view.Launcher;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by DodolinEL
 * date: 26.09.2018
 */
public class ShowIFrameActionListener<E extends JInternalFrame> implements ActionListener {

    private Class<E> eClass;
    private E instance;
    private OpenIFrameListener<E> listener;

    public ShowIFrameActionListener(Class<E> eClass) {
        this.eClass = eClass;
    }

    public void setListener(OpenIFrameListener<E> listener) {
        this.listener = listener;
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (instance == null || instance.isClosed() || !instance.isVisible()) {

                instance = eClass.newInstance();
                instance.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                Launcher.mainFrame.desktop.add(instance);
            }
            instance.setVisible(true);
            instance.setSelected(true);
            if (listener != null) {
                listener.invoke(instance);
            }
            ServiceManager.getInstance().clear();
        } catch (Exception e) {
            Utils.getLogger().error(e.getMessage(), e);
        }
    }

    public E getInstance() {
        return instance;
    }
}
