package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.Publisher;
import com.del.ministry.utils.CommonException;
import com.del.ministry.view.Launcher;
import com.del.ministry.view.MainFrame;
import com.del.ministry.view.actions.ObservableIFrame;
import com.del.ministry.view.models.YesNoList;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.google.common.collect.Maps;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.Paddings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

public class PublishersIForm extends ObservableIFrame {

    private JTable table;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public PublishersIForm() {
        super("Возвещатели", true, true, true, true);
        setMinimumSize(new Dimension(600, 200));

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(table);

        JPanel controlPanel = new JPanel();
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        JButton addBtn = new JButton("Создать");
        JButton editBtn = new JButton("Редактировать");
        JButton delBtn = new JButton("Удалить");
        controlPanel.add(addBtn);
        controlPanel.add(editBtn).setEnabled(false);
        controlPanel.add(delBtn).setEnabled(false);
        addBtn.addActionListener(this::addEvent);
        editBtn.addActionListener(this::editEvent);
        delBtn.addActionListener(this::delEvent);

        table.getSelectionModel().addListSelectionListener(e -> {
            delBtn.setEnabled(table.getSelectedRow() > -1);
            editBtn.setEnabled(table.getSelectedRow() > -1);
        });

        initTable();

        pack();
    }

    private void addEvent(ActionEvent e) {
        JInternalFrame i = new JInternalFrame("Добавить нового возвещателя", false, true, false, false);
        showPersonalForm(null, i);
    }

    private void editEvent(ActionEvent e) {
        JInternalFrame i = new JInternalFrame("Редактировать возвещателя", false, true, false, false);
        showPersonalForm(publishers.get(table.getSelectedRow()), i);
    }

    private void delEvent(ActionEvent e) {
        Publisher publisher = publishers.get(table.getSelectedRow());
        try {
            int answer = JOptionPane.showInternalConfirmDialog(this, "Удалить возвещателя " + publisher.getLastName() + "?", "Удаление", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                ServiceManager.getInstance().deletePublisher(publisher.getId());
                initTable();
            }
        } catch (Exception e1) {
            MainFrame.setStatusError("Ошибка при удалении возвещателя", e1);
        }
    }

    private void showPersonalForm(Publisher publisher, JInternalFrame i) {
        i.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        Launcher.mainFrame.desktop.add(i);
        JButton cancelBtn = new JButton("Отмена");
        JButton saveBtn = new JButton("Сохранить");
        JTextField firstNameF = new JTextField();
        JTextField secondNameF = new JTextField();
        JTextField lastNameF = new JTextField();
        DatePickerSettings s = new DatePickerSettings();
        DatePicker dp = new DatePicker(s);
        s.setFormatForDatesCommonEra("dd/MM/yyyy");
        JComboBox<YesNoList> pioneerF = new JComboBox<>(YesNoList.LIST);
        JPanel buttons = new JPanel();

        if (publisher != null) {
            firstNameF.setText(publisher.getFirstName());
            secondNameF.setText(publisher.getSecondName());
            lastNameF.setText(publisher.getLastName());
            dp.setDate(new Date(publisher.getBirthDay().getTime()).toLocalDate());
            pioneerF.setSelectedItem(publisher.getPioneer() ? YesNoList.YES : YesNoList.NO);
        }

        cancelBtn.addActionListener(e1 -> i.dispose());
        saveBtn.addActionListener(e -> {
            Publisher p = publisher == null ? new Publisher() : publisher;
            p.setFirstName(firstNameF.getText());
            p.setSecondName(secondNameF.getText());
            p.setLastName(lastNameF.getText());
            p.setBirthDay(Date.valueOf(dp.getDate()));
            p.setPioneer(pioneerF.getItemAt(pioneerF.getSelectedIndex()).isYes());
            try {
                if (publisher == null) {
                    ServiceManager.getInstance().createPublisher(p);
                } else {
                    ServiceManager.getInstance().updatePublisher(p);
                }
                initTable();
                i.dispose();
            } catch (Exception e1) {
                ServiceManager.getInstance().clear();
                MainFrame.setStatusError("Ошибка при сохранении данных возвещателя", e1);
            }
        });

        i.getContentPane().add(FormBuilder.create().
                columns("100, 5, 200")
                .rows("p, 5, p, 5, p, 5, p, 5, p")
                .padding(Paddings.DIALOG)
                .addLabel("Фамилия").xy(1, 1).add(lastNameF).xy(3, 1)
                .addLabel("Имя").xy(1, 3).add(firstNameF).xy(3, 3)
                .addLabel("Отчество").xy(1, 5).add(secondNameF).xy(3, 5)
                .addLabel("Дата рождения").xy(1, 7).add(dp).xy(3, 7)
                .addLabel("Пионер").xy(1, 9).add(pioneerF).xy(3, 9)
                .build(), BorderLayout.CENTER);
        i.getContentPane().add(buttons, BorderLayout.SOUTH);

        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        i.pack();
        i.setVisible(true);
        try {
            i.setSelected(true);
        } catch (PropertyVetoException e) {
            //
        }
    }

    private Map<Integer, Publisher> publishers;

    private void initTable() {
        try {
            DefaultTableModel tableModel = new DefaultTableModel(new Object[][]{}, new Object[]{"Фамилия", "Имя", "Отчество", "Дата рождения", "Пионер"});
            publishers = Maps.newHashMap();
            ServiceManager.getInstance().findPublishers().forEach(p -> {
                publishers.put(tableModel.getRowCount(), p);
                tableModel.addRow(new Object[]{
                        p.getLastName(),
                        p.getFirstName(),
                        p.getSecondName(),
                        SDF.format(p.getBirthDay()),
                        Boolean.TRUE.equals(p.getPioneer()) ? "Да" : "Нет"
                });
            });
            table.setModel(tableModel);
        } catch (CommonException e) {
            MainFrame.setStatusError("Ошибка доступа к данным о возвещателях", e);
        }

    }

}
