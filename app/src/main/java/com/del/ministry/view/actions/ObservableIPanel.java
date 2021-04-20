package com.del.ministry.view.actions;

import com.del.ministry.view.MainFrame;

import javax.swing.*;
import java.util.Observer;

abstract public class ObservableIPanel extends JPanel {

    private IFrameObservable observable = new IFrameObservable();
    private MainFrame mainFrame;

    public ObservableIPanel() {
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    protected void notifyObservers() {
        observable.setChanged();
        observable.notifyObservers();
    }

    protected void notifyObservers(Object arg) {
        observable.setChanged();
        observable.notifyObservers(arg);
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void beforeShow() {
        //
    }

    public abstract String getTitle();
}
