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
import com.del.ministry.view.models.SelectItemsModel;
import com.google.common.collect.Maps;
import com.jgoodies.common.collect.LinkedListModel;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.Paddings;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class DistrictListIForm extends ObservableIFrame implements Observer {

    private JLabel districtSize;
    private JTextField newDistrictNumberF;
    private JComboBox<DistrictNumbers> districtNumbersF;
    private JList<DistrictAddressItem> districtAddressF;
    private JButton addBtn;
    private JButton newBtn;
    private JButton changeBtn;
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
        changeBtn = new JButton("Переименовать");
        districtSize = new JLabel("0");
        districtSize.setHorizontalAlignment(SwingConstants.CENTER);

        deleteAddressBtn.setEnabled(false);

        panel.add(FormBuilder.create().
                        columns("50, 5, 80, 5, 50, 5, 120, 5, 100")
                        .rows("p, 5, p, 5, p, 5, p, 5, p, 5, p, p")
                        .padding(Paddings.DIALOG)
                        .add("Участок:").xy(1, 1).add(districtNumbersF).xy(3, 1).add(districtSize).xy(5, 1).add(deleteBtn).xy(9, 1)
                        .addSeparator("Адреса").xyw(1, 3, 9)
                        .add(districtAddressF).xywh(1, 5, 7, 5)
                        .add(addBtn).xy(9, 5)
                        .add(deleteAddressBtn).xy(9, 7)
                        .add(generateBtn).xy(9, 9)
                        .addSeparator("Создать").xyw(1, 11, 9)
                        .add(newDistrictNumberF).xyw(1, 12, 3).add(changeBtn).xy(7, 12).add(newBtn).xy(9, 12)
                        .build(),
                BorderLayout.CENTER);

        ShowIFrameActionListener<AddAddressIForm> addAddressFormAction = new ShowIFrameActionListener<>(AddAddressIForm.class);
        ShowIFrameActionListener<GenerateAddressIForm> generateFormAction = new ShowIFrameActionListener<>(GenerateAddressIForm.class);
        addAddressFormAction.addObserver(this);
        generateFormAction.addObserver(this);

        addAddressFormAction.setListener(instance -> {
            DistrictNumbers selectedDistrict = districtNumbersF.getItemAt(districtNumbersF.getSelectedIndex());
            instance.setDistrict(selectedDistrict.getDistrict());
        });

        generateFormAction.setListener(instance -> {
            DistrictNumbers selectedDistrict = districtNumbersF.getItemAt(districtNumbersF.getSelectedIndex());
            instance.setDistrict(selectedDistrict.getDistrict());
        });

        addBtn.addActionListener(addAddressFormAction);
        generateBtn.addActionListener(generateFormAction);
        districtAddressF.addListSelectionListener(e -> deleteAddressBtn.setEnabled(districtAddressF.getSelectedIndex() > -1));
        districtNumbersF.addActionListener(e -> initAddressList());
        deleteAddressBtn.addActionListener(e -> deleteAddress());
        deleteBtn.addActionListener(e -> deleteDistrict());
        newBtn.addActionListener(e -> newDistrict());
        changeBtn.addActionListener(e -> renameDistrict());

        init();
        pack();
    }

    private void init() {
        initDistrictList();
        initAddressList();
    }

    private void renameDistrict() {
        DistrictNumbers selectedItem = districtNumbersF.getItemAt(districtNumbersF.getSelectedIndex());
        String number = newDistrictNumberF.getText();
        if (number != null) {
            District district = selectedItem.getDistrict();
            district.setNumber(number);
            try {
                ServiceManager.getInstance().updateDistrict(district);
                MainFrame.setStatusText("Участок успешно переименован");
            } catch (Exception e) {
                MainFrame.setStatusError("Невозможно переименовать участок!", e);
            } finally {
                initDistrictList(district.getId());
                newDistrictNumberF.setText("");
            }
        }
    }

    private void deleteDistrict() {
        DistrictNumbers selectedItem = districtNumbersF.getItemAt(districtNumbersF.getSelectedIndex());
        if (selectedItem != null) {
            try {
                ServiceManager.getInstance().deleteDistrict(selectedItem.getDistrict().getId());
                MainFrame.setStatusText("Запись удалена");
                initDistrictList();
                initAddressList();
            } catch (CommonException e1) {
                MainFrame.setStatusError(e1.getMessage(), e1);
            } catch (Exception e1) {
                MainFrame.setStatusError("Ошибка при удалении участка", e1);
            }
        }
    }

    private void newDistrict() {
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
    }

    private void deleteAddress() {
        try {
            ServiceManager.getInstance().deleteDistrictAddress(districtAddressF.getSelectedValue().getAddress().getId());
            initAddressList();
        } catch (CommonException e1) {
            MainFrame.setStatusError("Невозможно удалить адрес", e1);
        }
    }

    private void initDistrictList() {
        initDistrictList(null);
    }

    private void initDistrictList(Long selectedId) {
        try {
            List<District> districts = ServiceManager.getInstance().allDistricts();
            List<DistrictNumbers> items = districts.stream().map(DistrictNumbers::new).collect(Collectors.toList());
            Map<Long, DistrictNumbers> hash = Maps.uniqueIndex(items, districtNumbers -> Objects.requireNonNull(districtNumbers).getDistrict().getId());
            districtNumbersF.setModel(new SelectItemsModel<>(items));
            if (selectedId != null && hash.containsKey(selectedId)) {
                districtNumbersF.setSelectedItem(hash.get(selectedId));
            }
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
                districtSize.setText(String.valueOf(collect.size()));
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
