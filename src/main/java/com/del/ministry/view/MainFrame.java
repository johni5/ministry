package com.del.ministry.view;

import com.del.ministry.utils.Utils;
import com.del.ministry.view.actions.MainFrameActions;
import com.del.ministry.view.actions.ShowIFrameActionListener;
import com.del.ministry.view.forms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    public final JDesktopPane desktop = new JDesktopPane();

    private JMenu menu_1;
    private JMenu menu_2;

    private final static JLabel STATUS_TEXT = new JLabel();

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

        JMenuItem menuItem = new JMenuItem("Выход");
        menuItem.addActionListener(arg0 -> {
            if (JOptionPane.showConfirmDialog(MainFrame.this, "Вы действительно хотите выйти?", "Завершение работы",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dispatchEvent(new WindowEvent(MainFrame.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        menu.add(menuItem);

        menu_1 = new JMenu("Адреса");

        JMenuItem menuItem_2 = new JMenuItem("Район");
        menuItem_2.addActionListener(new ShowIFrameActionListener<>(AreaIForm.class));
        menu_1.add(menuItem_2);

        JMenuItem menuItem_1 = new JMenuItem("Улица");
        menuItem_1.addActionListener(new ShowIFrameActionListener<>(StreetIForm.class));
        menu_1.add(menuItem_1);

        JMenuItem menuItem_3 = new JMenuItem("Город");
        menuItem_3.addActionListener(new ShowIFrameActionListener<>(CityIForm.class));
        menu_1.add(menuItem_3);

        JMenuItem menuItem_4 = new JMenuItem("Дом");
        menuItem_4.addActionListener(new ShowIFrameActionListener<>(BuildingIForm.class));
        menu_1.add(menuItem_4);

        JSeparator separator = new JSeparator();
        menu_1.add(separator);

        JMenuItem menuItem_5 = new JMenuItem("Тип адреса");
        menuItem_5.addActionListener(new ShowIFrameActionListener<>(AddressTypeIForm.class));
        menu_1.add(menuItem_5);

        JMenuItem menuItem_6 = new JMenuItem("Тип дома");
        menuItem_6.addActionListener(new ShowIFrameActionListener<>(BuildingTypeIForm.class));
        menu_1.add(menuItem_6);

        menu_2 = new JMenu("Участки");
        JMenuItem menuItem_2_1 = new JMenuItem("Список");
        menuItem_2_1.addActionListener(new ShowIFrameActionListener<>(DistrictListIForm.class));
        menu_2.add(menuItem_2_1);

        menuBar.add(menu);
        menuBar.add(menu_1);
        menuBar.add(menu_2);

        JPanel statusBar = new JPanel(new BorderLayout(0, 0));
        STATUS_TEXT.setFont(new Font("Tahoma", Font.PLAIN, 12));
        STATUS_TEXT.setPreferredSize(new Dimension(100, 20));
        statusBar.add(STATUS_TEXT);

        desktop.setBackground(Color.LIGHT_GRAY);
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        getContentPane().add(desktop, BorderLayout.CENTER);
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        addWindowListener(new MainFrameActions(this));
    }

    public void setStatusDB_ERROR() {
        menu_1.setEnabled(false);
        setStatusText("Нет соединения с базой данных");
    }

    public void setStatusDB_OK() {
        setStatusText("Соединение с базой данных установлено");
    }

    public static void setStatusText(String message) {
        STATUS_TEXT.setText(message);
        STATUS_TEXT.setForeground(Color.BLUE);
    }

    public static void setStatusError(String message, Exception e) {
        STATUS_TEXT.setText(message);
        STATUS_TEXT.setForeground(Color.RED);
        Utils.getLogger().error(e.getMessage(), e);
    }

    public static void setStatusError(String message) {
        STATUS_TEXT.setText(message);
        STATUS_TEXT.setForeground(Color.RED);
    }

}
