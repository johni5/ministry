package com.del.ministry.view.actions;

import javax.swing.*;
import java.util.Observer;

abstract public class ObservableIPanel extends JPanel {

    private IFrameObservable observable = new IFrameObservable();

    public ObservableIPanel() {
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

    public void beforeShow() {
        //
    }

    public abstract String getTitle();
}
