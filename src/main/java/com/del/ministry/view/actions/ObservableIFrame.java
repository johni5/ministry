package com.del.ministry.view.actions;

import javax.swing.*;
import java.util.Observer;

abstract public class ObservableIFrame extends JInternalFrame {

    private IFrameObservable observable = new IFrameObservable();

    public ObservableIFrame() {
    }

    public ObservableIFrame(String title) {
        super(title);
    }

    public ObservableIFrame(String title, boolean resizable) {
        super(title, resizable);
    }

    public ObservableIFrame(String title, boolean resizable, boolean closable) {
        super(title, resizable, closable);
    }

    public ObservableIFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
        super(title, resizable, closable, maximizable);
    }

    public ObservableIFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
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
