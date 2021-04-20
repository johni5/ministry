package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.AddressType;
import com.del.ministry.db.District;
import com.del.ministry.db.DistrictAddress;
import com.del.ministry.utils.CommonException;
import com.del.ministry.utils.StringUtil;
import com.del.ministry.view.actions.ObservableIPanel;
import com.del.ministry.view.actions.ShowIFrameActionListener;
import com.del.ministry.view.models.AddressTypeItem;
import com.del.ministry.view.models.DistrictAddressItem;
import com.del.ministry.view.models.DistrictNumbers;
import com.del.ministry.view.models.SelectItemsModel;
import com.google.common.collect.Maps;
import com.jgoodies.common.collect.LinkedListModel;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.Paddings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class DistrictListIForm extends ObservableIPanel implements Observer {

    private JLabel districtSize;
    private JTextField newDistrictNumberF;
    private JComboBox<DistrictNumbers> districtNumbersF;
    private JList<DistrictAddressItem> districtAddressF;
    private JButton addBtn;
    private JButton newBtn;
    private JButton changeBtn;
    private JButton deleteBtn;
    private JButton editBtn;
    private JButton deleteAddressBtn;
    private JButton generateBtn;

    /**
     * Create the frame.
     */
    public DistrictListIForm() {
        setMinimumSize(new Dimension(600, 400));
        setLayout(new BorderLayout(0, 0));

        districtNumbersF = new JComboBox<>();
        districtAddressF = new JList<>();
        districtAddressF.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        districtAddressF.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editEvent(districtAddressF.getSelectedValue().getAddress());
                }
            }
        });
        newDistrictNumberF = new JTextField();

        addBtn = new JButton("Добавить");
        deleteBtn = new JButton("Удалить");
        editBtn = new JButton("Редактировать");
        editBtn.setEnabled(false);
        deleteAddressBtn = new JButton("Удалить");
        generateBtn = new JButton("Заполнить");
        newBtn = new JButton("Новый");
        changeBtn = new JButton("Переименовать");
        districtSize = new JLabel();
        districtSize.setHorizontalAlignment(SwingConstants.CENTER);
        printSize(0);

        deleteAddressBtn.setEnabled(false);

        add(FormBuilder.create().
                        columns("50, 5, 80, 5, 50, pref:grow, 100, 150, 5, 150")
                        .rows("p, 5, p, 5, p, 5, p, 5, p, 5, p, fill:100:grow, 5, p, p")
                        .padding(Paddings.DIALOG)
                        .add("Участок:").xy(1, 1).add(districtNumbersF).xy(3, 1).add(districtSize).xy(5, 1).add(deleteBtn).xy(10, 1)
                        .addSeparator("Адреса").xyw(1, 3, 10)
                        .add(districtAddressF).xywh(1, 5, 8, 8)
                        .add(addBtn).xy(10, 5)
                        .add(deleteAddressBtn).xy(10, 7)
                        .add(editBtn).xy(10, 9)
                        .add(generateBtn).xy(10, 11)
                        .addSeparator("Редактор").xyw(1, 14, 10)
                        .add(newDistrictNumberF).xyw(1, 15, 3).add(changeBtn).xy(8, 15).add(newBtn).xy(10, 15)
                        .build(),
                BorderLayout.CENTER);

        ShowIFrameActionListener<AddAddressIForm> addAddressFormAction = new ShowIFrameActionListener<>(AddAddressIForm.class, getMainFrame());
        ShowIFrameActionListener<GenerateAddressIForm> generateFormAction = new ShowIFrameActionListener<>(GenerateAddressIForm.class, getMainFrame());
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

        editBtn.addActionListener(e -> editEvent(districtAddressF.getSelectedValue().getAddress()));

        addBtn.addActionListener(addAddressFormAction);
        generateBtn.addActionListener(generateFormAction);
        districtAddressF.addListSelectionListener(e -> {
            deleteAddressBtn.setEnabled(districtAddressF.getSelectedIndex() > -1);
            editBtn.setEnabled(districtAddressF.getSelectedIndex() > -1);
        });
        districtNumbersF.addActionListener(e -> initAddressList());
        deleteAddressBtn.addActionListener(e -> deleteAddress());
        deleteBtn.addActionListener(e -> deleteDistrict());
        newBtn.addActionListener(e -> newDistrict());
        changeBtn.addActionListener(e -> renameDistrict());

    }

    @Override
    public void beforeShow() {
        initDistrictList();
        initAddressList();
    }

    @Override
    public String getTitle() {
        return "Редактирование участков";
    }

    private void renameDistrict() {
        if (districtNumbersF.getSelectedIndex() > -1) {
            DistrictNumbers selectedItem = districtNumbersF.getItemAt(districtNumbersF.getSelectedIndex());
            String number = newDistrictNumberF.getText();
            if (!StringUtil.isTrimmedEmpty(number)) {
                District district = selectedItem.getDistrict();
                int answer = JOptionPane.showConfirmDialog(getMainFrame(),
                        String.format("Переименовать участок %s на %s?", district.getNumber(), number),
                        "Удаление", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    district.setNumber(number);
                    try {
                        ServiceManager.getInstance().updateDistrict(district);
                        getMainFrame().setStatusText("Участок успешно переименован");
                        getMainFrame().initLeftSideTree();
                    } catch (Exception e) {
                        getMainFrame().setStatusError("Невозможно переименовать участок!", e);
                    } finally {
                        initDistrictList(district.getId());
                        newDistrictNumberF.setText("");
                    }
                }
            }
        }
    }

    private void deleteDistrict() {
        DistrictNumbers selectedItem = districtNumbersF.getItemAt(districtNumbersF.getSelectedIndex());
        if (selectedItem != null) {
            String number = selectedItem.getDistrict().getNumber();
            try {
                int answer = JOptionPane.showConfirmDialog(getMainFrame(), String.format("Удалить участок %s?", number),
                        "Удаление", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    ServiceManager.getInstance().deleteDistrict(selectedItem.getDistrict().getId());
                    getMainFrame().setStatusText(String.format("Участок %s был удален", number));
                    initDistrictList();
                    initAddressList();
                    getMainFrame().initLeftSideTree();
                }
            } catch (CommonException e1) {
                getMainFrame().setStatusError(e1.getMessage(), e1);
            } catch (Exception e1) {
                getMainFrame().setStatusError(String.format("Ошибка при удалении участка %s", number), e1);
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
                getMainFrame().setStatusText("Участок создан");
                newDistrictNumberF.setText("");
                initDistrictList(d.getId());
                getMainFrame().initLeftSideTree();
            } catch (Exception e1) {
                getMainFrame().setStatusError("Ошибка при сохранении участка", e1);
            }
        }
    }

    private void deleteAddress() {
        districtAddressF.getSelectedValuesList().forEach(item -> {
            try {
                ServiceManager.getInstance().deleteDistrictAddress(item.getAddress());
            } catch (CommonException e) {
                getMainFrame().setStatusError("Невозможно удалить адрес", e);
            }
        });
        getMainFrame().initLeftSideTree();
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
            getMainFrame().setStatusError("Нет доступа к участкам", e);
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
                districtAddressF.setModel(new LinkedListModel<>(collect));
                printSize(collect.size());
            } catch (Exception e) {
                getMainFrame().setStatusError("Нет доступа к адресам участка", e);
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

    private void editEvent(DistrictAddress da) {
        JDialog i = new JDialog(getMainFrame(), "Редактировать тип адреса", Dialog.ModalityType.APPLICATION_MODAL);
        try {
            JButton cancelBtn = new JButton("Отмена");
            JButton saveBtn = new JButton("Сохранить");
            JComboBox<AddressTypeItem> typeF = new JComboBox<>();
            List<AddressType> addressTypes = ServiceManager.getInstance().findAddressTypes();
            List<AddressTypeItem> types = addressTypes.stream().map(AddressTypeItem::new).collect(Collectors.toList());
            AddressTypeItem selectedType = types.stream().filter(p -> p.getAddressType().getId().equals(da.getType().getId())).findFirst().orElse(null);
            DefaultComboBoxModel<AddressTypeItem> model = new DefaultComboBoxModel<>(new Vector<>(types));
            model.setSelectedItem(selectedType);
            typeF.setModel(model);
            JPanel buttons = new JPanel();
            cancelBtn.addActionListener(e1 -> i.dispose());
            saveBtn.addActionListener(e1 -> {
                AddressTypeItem si = (AddressTypeItem) typeF.getSelectedItem();
                if (si != null) {
                    da.setType(si.getAddressType());
                    try {
                        ServiceManager.getInstance().updateDistrictAddress(da);
                        initAddressList();
                        i.dispose();
                    } catch (CommonException e) {
                        getMainFrame().setStatusError("Ошибка сохранения типа адреса", e);
                    }
                }
            });
            i.getContentPane().add(FormBuilder.create().
                    columns("100, 5, 200")
                    .rows("p, 5, p")
                    .padding(Paddings.DIALOG)
                    .addLabel("Тип адреса").xy(1, 1).add(typeF).xy(3, 1)
                    .build(), BorderLayout.CENTER);
            i.getContentPane().add(buttons, BorderLayout.SOUTH);

            buttons.add(saveBtn);
            buttons.add(cancelBtn);

            i.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            i.pack();
            i.setLocationRelativeTo(getMainFrame());
            i.setVisible(true);

        } catch (CommonException e1) {
            getMainFrame().setStatusError("Ошибка редактирования типа адреса", e1);
        }

    }

}
