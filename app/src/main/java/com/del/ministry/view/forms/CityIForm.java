package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.City;
import com.del.ministry.utils.CommonException;
import com.del.ministry.view.actions.ObservableIPanel;
import com.del.ministry.view.models.CityItem;
import com.jgoodies.common.collect.LinkedListModel;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.Paddings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class CityIForm extends ObservableIPanel {

    private JList<CityItem> cityList;

    /**
     * Create the frame.
     */
    public CityIForm() {
        setMinimumSize(new Dimension(600, 400));
        setBounds(100, 100, 600, 400);

        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        cityList = new JList<>();
        cityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(cityList);

        JPanel controlPanel = new JPanel();
        add(controlPanel, BorderLayout.NORTH);
        JButton addBtn = new JButton("Создать");
        JButton editBtn = new JButton("Редактировать");
        JButton delBtn = new JButton("Удалить");
        controlPanel.add(addBtn);
        controlPanel.add(editBtn).setEnabled(false);
        controlPanel.add(delBtn).setEnabled(false);
        addBtn.addActionListener(this::addEvent);
        editBtn.addActionListener(this::editEvent);
        delBtn.addActionListener(this::delEvent);

        cityList.getSelectionModel().addListSelectionListener(e -> {
            delBtn.setEnabled(cityList.getSelectedIndex() > -1);
            editBtn.setEnabled(cityList.getSelectedIndex() > -1);
        });

        cityList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editEvent(null);
            }
        });

        initList();
    }

    @Override
    public String getTitle() {
        return "Редактирование населенных пунктов";
    }


    private void addEvent(ActionEvent e) {
        JDialog i = new JDialog(getMainFrame(), "Добавить населенный пункт", Dialog.ModalityType.APPLICATION_MODAL);
        showEditForm(null, i);
    }

    private void editEvent(ActionEvent e) {
        JDialog i = new JDialog(getMainFrame(), "Редактировать населенный пункт", Dialog.ModalityType.APPLICATION_MODAL);
        showEditForm(cityList.getSelectedValue().getCity(), i);
    }

    private void showEditForm(City city, JDialog i) {
        i.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        JButton cancelBtn = new JButton("Отмена");
        JButton saveBtn = new JButton("Сохранить");
        JTextField nameF = new JTextField();
        JPanel buttons = new JPanel();

        if (city != null) {
            nameF.setText(city.getName());
        }

        cancelBtn.addActionListener(e1 -> i.dispose());
        saveBtn.addActionListener(e -> {
            City a = city == null ? new City() : city;
            a.setName(nameF.getText());
            try {
                if (city == null) {
                    ServiceManager.getInstance().createCity(a);
                } else {
                    ServiceManager.getInstance().updateCity(a);
                }
                i.dispose();
            } catch (Exception e1) {
                ServiceManager.getInstance().clear();
                getMainFrame().setStatusError("Ошибка при сохранении населенного пункта", e1);
            }
            initList();
        });

        i.getContentPane().add(FormBuilder.create().
                columns("100, 5, 200")
                .rows("p, 5, p")
                .padding(Paddings.DIALOG)
                .addLabel("Название").xy(1, 1).add(nameF).xy(3, 1)
                .build(), BorderLayout.CENTER);
        i.getContentPane().add(buttons, BorderLayout.SOUTH);

        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        i.pack();
        i.setLocationRelativeTo(getMainFrame());
        i.setVisible(true);

    }

    private void delEvent(ActionEvent e) {
        City city = cityList.getSelectedValue().getCity();
        try {
            int answer = JOptionPane.showConfirmDialog(getMainFrame(), "Удалить населенный пункт " + city.getName() + "?", "Удаление", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                ServiceManager.getInstance().deleteCity(city.getId());
                initList();
            }
        } catch (Exception e1) {
            getMainFrame().setStatusError("Ошибка при удалении населенного пункта", e1);
        }
    }


    private void initList() {
        try {
            List<City> list = ServiceManager.getInstance().findCites();
            List<CityItem> collect = list.stream().map(CityItem::new).sorted().collect(Collectors.toList());
            cityList.setModel(new LinkedListModel<>(collect));
        } catch (CommonException e) {
            getMainFrame().setStatusError("Ошибка получения списка населенных пунктов", e);
        }

    }

}
