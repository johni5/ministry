package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.District;
import com.del.ministry.db.DistrictAddress;
import com.del.ministry.utils.CommonException;
import com.del.ministry.utils.StringUtil;
import com.del.ministry.view.Launcher;
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
        super("Участки", true, true, true, true);
        setMinimumSize(new Dimension(600, 400));
//        setMaximumSize(new Dimension(1000, 600));

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        districtNumbersF = new JComboBox<>();
        districtAddressF = new JList<>();
        districtAddressF.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        newDistrictNumberF = new JTextField();

        addBtn = new JButton("Добавить");
        deleteBtn = new JButton("Удалить");
        deleteAddressBtn = new JButton("Удалить");
        generateBtn = new JButton("Заполнить");
        newBtn = new JButton("Новый");
        changeBtn = new JButton("Переименовать");
        districtSize = new JLabel();
        districtSize.setHorizontalAlignment(SwingConstants.CENTER);
        printSize(0);

        deleteAddressBtn.setEnabled(false);

        panel.add(FormBuilder.create().
                        columns("50, 5, 80, 5, 50, pref:grow, 100, 150, 5, 100")
                        .rows("p, 5, p, 5, p, 5, p, 5, p, fill:100:grow, 5, p, p")
                        .padding(Paddings.DIALOG)
                        .add("Участок:").xy(1, 1).add(districtNumbersF).xy(3, 1).add(districtSize).xy(5, 1).add(deleteBtn).xy(10, 1)
                        .addSeparator("Адреса").xyw(1, 3, 10)
                        .add(districtAddressF).xywh(1, 5, 8, 6)
                        .add(addBtn).xy(10, 5)
                        .add(deleteAddressBtn).xy(10, 7)
                        .add(generateBtn).xy(10, 9)
                        .addSeparator("Редактор").xyw(1, 12, 10)
                        .add(newDistrictNumberF).xyw(1, 13, 3).add(changeBtn).xy(8, 13).add(newBtn).xy(10, 13)
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
        if (districtNumbersF.getSelectedIndex() > -1) {
            DistrictNumbers selectedItem = districtNumbersF.getItemAt(districtNumbersF.getSelectedIndex());
            String number = newDistrictNumberF.getText();
            if (!StringUtil.isTrimmedEmpty(number)) {
                District district = selectedItem.getDistrict();
                district.setNumber(number);
                try {
                    ServiceManager.getInstance().updateDistrict(district);
                    Launcher.mainFrame.setStatusText("Участок успешно переименован");
                    Launcher.mainFrame.initLeftSideTree();
                } catch (Exception e) {
                    Launcher.mainFrame.setStatusError("Невозможно переименовать участок!", e);
                } finally {
                    initDistrictList(district.getId());
                    newDistrictNumberF.setText("");
                }
            }
        }
    }

    private void deleteDistrict() {
        DistrictNumbers selectedItem = districtNumbersF.getItemAt(districtNumbersF.getSelectedIndex());
        if (selectedItem != null) {
            try {
                ServiceManager.getInstance().deleteDistrict(selectedItem.getDistrict().getId());
                Launcher.mainFrame.setStatusText("Запись удалена");
                initDistrictList();
                initAddressList();
                Launcher.mainFrame.initLeftSideTree();
            } catch (CommonException e1) {
                Launcher.mainFrame.setStatusError(e1.getMessage(), e1);
            } catch (Exception e1) {
                Launcher.mainFrame.setStatusError("Ошибка при удалении участка", e1);
            }
        }
    }

    private void newDistrict() {
        String number = newDistrictNumberF.getText();
        if (!StringUtil.isTrimmedEmpty(number)) {
            try {
                District d = new District();
                d.setNumber(number);
                ServiceManager.getInstance().createDistrict(d);
                Launcher.mainFrame.setStatusText("Участок создан");
                newDistrictNumberF.setText("");
                initDistrictList();
                Launcher.mainFrame.initLeftSideTree();
            } catch (Exception e1) {
                Launcher.mainFrame.setStatusError("Ошибка при сохранении участка", e1);
            }
        }
    }

    private void deleteAddress() {
        districtAddressF.getSelectedValuesList().forEach(item -> {
            try {
                ServiceManager.getInstance().deleteDistrictAddress(item.getAddress());
            } catch (CommonException e) {
                Launcher.mainFrame.setStatusError("Невозможно удалить адрес", e);
            }
        });
        Launcher.mainFrame.initLeftSideTree();
        initAddressList();
    }

    private void initDistrictList() {
        initDistrictList(null);
    }

    public void initDistrictList(Long selectedId) {
        try {
            List<District> districts = ServiceManager.getInstance().allDistricts();
            List<DistrictNumbers> items = districts.stream().map(DistrictNumbers::new).sorted().collect(Collectors.toList());
            Map<Long, DistrictNumbers> hash = Maps.uniqueIndex(items, districtNumbers -> Objects.requireNonNull(districtNumbers).getDistrict().getId());
            districtNumbersF.setModel(new SelectItemsModel<>(items));
            if (selectedId != null && hash.containsKey(selectedId)) {
                districtNumbersF.setSelectedItem(hash.get(selectedId));
            }
        } catch (CommonException e) {
            Launcher.mainFrame.setStatusError("Нет доступа к участкам", e);
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
                printSize(collect.size());
            } catch (Exception e) {
                Launcher.mainFrame.setStatusError("Нет доступа к адресам участка", e);
            }
        }
    }

    private void printSize(int size) {
        districtSize.setText(" " + size);
    }

    @Override
    public void update(Observable o, Object arg) {
        initAddressList();
    }

}
