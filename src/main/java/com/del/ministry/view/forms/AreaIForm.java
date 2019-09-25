package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.Area;
import com.del.ministry.utils.CommonException;
import com.del.ministry.view.Launcher;
import com.del.ministry.view.actions.ObservableIPanel;
import com.del.ministry.view.models.AreaItem;
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

public class AreaIForm extends ObservableIPanel {

    private JList<AreaItem> areasList;

    /**
     * Create the frame.
     */
    public AreaIForm() {
        setMinimumSize(new Dimension(600, 400));
        setBounds(100, 100, 600, 400);

        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        areasList = new JList<>();
        areasList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(areasList);

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

        areasList.getSelectionModel().addListSelectionListener(e -> {
            delBtn.setEnabled(areasList.getSelectedIndex() > -1);
            editBtn.setEnabled(areasList.getSelectedIndex() > -1);
        });

        areasList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) editEvent(null);
            }
        });

        initList();
    }

    @Override
    public String getTitle() {
        return "Редактирование районов";
    }


    private void addEvent(ActionEvent e) {
        JDialog i = new JDialog(Launcher.mainFrame, "Добавить район", Dialog.ModalityType.APPLICATION_MODAL);
        showEditForm(null, i);
    }

    private void editEvent(ActionEvent e) {
        JDialog i = new JDialog(Launcher.mainFrame, "Редактировать район", Dialog.ModalityType.APPLICATION_MODAL);
        showEditForm(areasList.getSelectedValue().getArea(), i);
    }

    private void showEditForm(Area area, JDialog i) {
        i.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        JButton cancelBtn = new JButton("Отмена");
        JButton saveBtn = new JButton("Сохранить");
        JTextField nameF = new JTextField();
        JPanel buttons = new JPanel();

        if (area != null) {
            nameF.setText(area.getName());
        }

        cancelBtn.addActionListener(e1 -> i.dispose());
        saveBtn.addActionListener(e -> {
            Area a = area == null ? new Area() : area;
            a.setName(nameF.getText());
            try {
                if (area == null) {
                    ServiceManager.getInstance().createArea(a);
                } else {
                    ServiceManager.getInstance().updateArea(a);
                }
                i.dispose();
            } catch (Exception e1) {
                ServiceManager.getInstance().clear();
                Launcher.mainFrame.setStatusError("Ошибка при сохранении района", e1);
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
        i.setLocationRelativeTo(Launcher.mainFrame);
        i.setVisible(true);

    }

    private void delEvent(ActionEvent e) {
        Area area = areasList.getSelectedValue().getArea();
        try {
            int answer = JOptionPane.showConfirmDialog(Launcher.mainFrame, "Удалить район " + area.getName() + "?", "Удаление", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                ServiceManager.getInstance().deleteArea(area.getId());
                initList();
            }
        } catch (Exception e1) {
            Launcher.mainFrame.setStatusError("Ошибка при удалении района", e1);
        }
    }


    private void initList() {
        try {
            List<Area> areas = ServiceManager.getInstance().findAreas();
            List<AreaItem> collect = areas.stream().map(AreaItem::new).sorted().collect(Collectors.toList());
            areasList.setModel(new LinkedListModel<>(collect));
        } catch (CommonException e) {
            Launcher.mainFrame.setStatusError("Ошибка получения списка районов", e);
        }

    }

}
