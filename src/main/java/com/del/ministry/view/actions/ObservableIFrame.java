package com.del.ministry.view.actions;

import javax.swing.*;
import java.util.Observer;

abstract public class ObservableIFrame extends JFrame {

    private IFrameObservable observable = new IFrameObservable();

    public ObservableIFrame() {
    }

    public ObservableIFrame(String title) {
        super(title);
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

}
