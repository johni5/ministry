package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.District;
import com.del.ministry.db.DistrictAddress;
import com.del.ministry.utils.CommonException;
import com.del.ministry.view.MainFrame;
import com.del.ministry.view.actions.ObservableIFrame;
import com.del.ministry.view.actions.ShowIFrameActionListener;
import com.del.ministry.view.models.DistrictAddressItem;
import com.del.ministry.view.models.DistrictNumbers;
import com.jgoodies.common.collect.LinkedListModel;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.Paddings;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class DistrictListIForm extends ObservableIFrame implements Observer {

    private JTextField newDistrictNumberF;
    private JComboBox<DistrictNumbers> districtNumbersF;
    private JList<DistrictAddressItem> districtAddressF;
    private JButton addBtn;
    private JButton newBtn;
    private JButton deleteBtn;
    private JButton deleteAddressBtn;
    private JButton generateBtn;

    /**
     * Create the frame.
     */
    public DistrictListIForm() {
        super("Участки", false, true, false, true);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        districtNumbersF = new JComboBox<>();
        districtAddressF = new JList<>();
        districtAddressF.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        newDistrictNumberF = new JTextField();

        addBtn = new JButton("Добавить");
        deleteBtn = new JButton("Удалить");
        deleteAddressBtn = new JButton("Удалить");
        generateBtn = new JButton("Заполнить");
        newBtn = new JButton("Новый");

        deleteAddressBtn.setEnabled(false);

        panel.add(FormBuilder.create().
                        columns("50, 5, 80, 5, 150, 5, 100")
                        .rows("p, 5, p, 5, p, 5, p, 5, p, 5, p, p")
                        .padding(Paddings.DIALOG)
                        .add("Участок:").xy(1, 1).add(districtNumbersF).xy(3, 1).add(deleteBtn).xy(7, 1)
                        .addSeparator("Адреса").xyw(1, 3, 7)
                        .add(districtAddressF).xywh(1, 5, 5, 5)
                        .add(addBtn).xy(7, 5)
                        .add(deleteAddressBtn).xy(7, 7)
                        .add(generateBtn).xy(7, 9)
                        .addSeparator("Создать").xyw(1, 11, 7)
                        .add(newDistrictNumberF).xyw(1, 12, 3).add(newBtn).xy(7, 12)
                        .build(),
                BorderLayout.CENTER);

        deleteBtn.addActionListener(e -> {
            DistrictNumbers selectedItem = districtNumbersF.getItemAt(districtNumbersF.getSelectedIndex());
            if (selectedItem != null) {
                try {
                    ServiceManager.getInstance().deleteDistrict(selectedItem.getDistrict().getId());
                    MainFrame.setStatusText("Запись удалена");
                    initDistrictList();
                } catch (CommonException e1) {
                    MainFrame.setStatusError(e1.getMessage(), e1);
                } catch (Exception e1) {
                    MainFrame.setStatusError("Ошибка при удалении участка", e1);
                }
            }
        });

        newBtn.addActionListener(e -> {
            String number = newDistrictNumberF.getText();
            if (number != null) {
                try {
                    District d = new District();
                    d.setNumber(number);
                    ServiceManager.getInstance().createDistrict(d);
                    MainFrame.setStatusText("Участок создан");
                    newDistrictNumberF.setText("");
                    initDistrictList();
                } catch (Exception e1) {
                    MainFrame.setStatusError("Ошибка при сохранении участка", e1);
                }
            }
        });

        ShowIFrameActionListener<AddAddressIForm> addAddressFormAction = new ShowIFrameActionListener<>(AddAddressIForm.class);
        ShowIFrameActionListener<GenerateAddressIForm> generateFormAction = new ShowIFrameActionListener<>(GenerateAddressIForm.class);
        addAddressFormAction.addObserver(this);

        addBtn.addActionListener(addAddressFormAction);
        generateBtn.addActionListener(generateFormAction);

        addAddressFormAction.setListener(instance -> {
            DistrictNumbers selectedDistrict = districtNumbersF.getItemAt(districtNumbersF.getSelectedIndex());
            instance.setDistrict(selectedDistrict.getDistrict());
        });

        districtNumbersF.addActionListener(e -> initAddressList());
        districtAddressF.addListSelectionListener(e -> deleteAddressBtn.setEnabled(districtAddressF.getSelectedIndex() > -1));
        deleteAddressBtn.addActionListener(e -> {
            try {
                ServiceManager.getInstance().deleteDistrictAddress(districtAddressF.getSelectedValue().getAddress().getId());
                initAddressList();
            } catch (CommonException e1) {
                MainFrame.setStatusError("Невозможно удалить адрес", e1);
            }
        });

        init();
        pack();
    }

    private void init() {
        initDistrictList();
        initAddressList();
    }

    private void initDistrictList() {
        try {
            List<District> districts = ServiceManager.getInstance().allDistricts();
            districtNumbersF.setModel(new DefaultComboBoxModel<>(new Vector<>(
                    districts.stream().map(DistrictNumbers::new).collect(Collectors.toList()))
            ));
        } catch (CommonException e) {
            MainFrame.setStatusError("Нет доступа к участкам", e);
        }
    }

    private void initAddressList() {
        DistrictNumbers selectedDistrict = districtNumbersF.getItemAt(districtNumbersF.getSelectedIndex());
        if (selectedDistrict != null) {
            District district = selectedDistrict.getDistrict();
            try {
                List<DistrictAddress> addresses = ServiceManager.getInstance().findDistrictAddresses(district.getId());
                List<DistrictAddressItem> collect = addresses.stream().
                        map(DistrictAddressItem::new).sorted().collect(Collectors.toList());
                districtAddressF.setModel(new LinkedListModel<DistrictAddressItem>(collect));
            } catch (Exception e) {
                MainFrame.setStatusError("Нет доступа к адресам участка", e);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        initAddressList();
    }

}
