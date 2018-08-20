package com.del.ministry.view;

import com.del.ministry.utils.Utils;
import com.del.ministry.view.actions.MainFrameActions;
import com.del.ministry.view.forms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    private JDesktopPane desktop;

    private IFormActionListener<AreaIForm> areaFormListener;
    private IFormActionListener<DistrictIForm> districtFormListener;
    private IFormActionListener<StreetIForm> streetFormListener;
    private IFormActionListener<AddressTypeIForm> atFormListener;
    private IFormActionListener<BuildingIForm> buildingFormListener;
    private IFormActionListener<BuildingTypeIForm> btFormListener;
    private IFormActionListener<CityIForm> cityFormListener;

    /**
     * Create the frame.
     */
    public MainFrame() {
        setTitle("Система учета территории");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Файл");
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("Выход");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (JOptionPane.showConfirmDialog(MainFrame.this, "Вы действительно хотите выйти?", "Завершение работы",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    dispatchEvent(new WindowEvent(MainFrame.this, WindowEvent.WINDOW_CLOSING));
                }
            }
        });
        menu.add(menuItem);

        JMenu menu_1 = new JMenu("Данные");
        menuBar.add(menu_1);

        JMenuItem menuItem_1 = new JMenuItem("Улица");
        streetFormListener = new IFormActionListener<>(StreetIForm.class);
        menuItem_1.addActionListener(streetFormListener);
        menu_1.add(menuItem_1);

        JMenuItem menuItem_2 = new JMenuItem("Район");
        areaFormListener = new IFormActionListener<>(AreaIForm.class);
        menuItem_2.addActionListener(areaFormListener);
        menu_1.add(menuItem_2);

        JMenuItem menuItem_3 = new JMenuItem("Город");
        cityFormListener = new IFormActionListener<>(CityIForm.class);
        menuItem_3.addActionListener(cityFormListener);
        menu_1.add(menuItem_3);

        JMenuItem menuItem_4 = new JMenuItem("Дом");
        buildingFormListener = new IFormActionListener<>(BuildingIForm.class);
        menuItem_4.addActionListener(buildingFormListener);
        menu_1.add(menuItem_4);

        JSeparator separator = new JSeparator();
        menu_1.add(separator);

        JMenuItem menuItem_5 = new JMenuItem("Тип адреса");
        atFormListener = new IFormActionListener<>(AddressTypeIForm.class);
        menuItem_5.addActionListener(atFormListener);
        menu_1.add(menuItem_5);

        JMenuItem menuItem_6 = new JMenuItem("Тип дома");
        btFormListener = new IFormActionListener<>(BuildingTypeIForm.class);
        menuItem_6.addActionListener(btFormListener);
        menu_1.add(menuItem_6);

        desktop = new JDesktopPane();
        desktop.setBackground(Color.LIGHT_GRAY);
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        setContentPane(desktop);

        addWindowListener(new MainFrameActions());
    }

    class IFormActionListener<E extends JInternalFrame> implements ActionListener {

        private Class<E> eClass;
        private E instance;

        public IFormActionListener(Class<E> eClass) {
            this.eClass = eClass;
        }

        public void actionPerformed(ActionEvent ae) {
            try {
                if (instance == null) {
                    instance = eClass.newInstance();
                    instance.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                    getContentPane().add(instance);
                }
                instance.setVisible(true);
                instance.setSelected(true);
            } catch (Exception e) {
                Utils.getLogger().error(e.getMessage(), e);
            }
        }

        public E getInstance() {
            return instance;
        }
    }

}
