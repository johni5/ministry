package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.utils.Unchecked;
import com.del.ministry.view.actions.ObservableIPanel;
import com.google.common.collect.Maps;

import javax.swing.*;
import java.util.Map;

/**
 * Created by DodolinEL
 * date: 25.03.2019
 */
public class MainForms {

    private Map<Class<? extends ObservableIPanel>, ObservableIPanel> formsIndex = Maps.newHashMap();

    private JScrollPane rootView;

    public MainForms(JScrollPane rootView) {
        this.rootView = rootView;
        init(AddressTypeIForm.class);
        init(AppointmentsIForm.class);
        init(AreaIForm.class);
        init(BuildingIForm.class);
        init(BuildingTypeIForm.class);
        init(CityIForm.class);
        init(DistrictListIForm.class);
        init(PublishersIForm.class);
        init(StreetIForm.class);
    }

    private void init(Class<? extends ObservableIPanel> aClass) {
        try {
            this.formsIndex.put(aClass, aClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T extends ObservableIPanel> T show(Class<T> aClass) {
        T view = Unchecked.cast(formsIndex.get(aClass));
        view.beforeShow();
        rootView.setViewportView(view);
        JFrame frame = (JFrame) SwingUtilities.getRoot(rootView);
        if (frame != null) frame.setTitle(view.getTitle());
        ServiceManager.getInstance().clear();
        return view;
    }
}
