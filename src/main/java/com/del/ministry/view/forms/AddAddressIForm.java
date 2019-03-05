package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.AddressType;
import com.del.ministry.db.Building;
import com.del.ministry.db.District;
import com.del.ministry.db.DistrictAddress;
import com.del.ministry.utils.CommonException;
import com.del.ministry.utils.StringUtil;
import com.del.ministry.view.Launcher;
import com.del.ministry.view.MainFrame;
import com.del.ministry.view.actions.ObservableIFrame;
import com.del.ministry.view.filters.BuildingFilter;
import com.del.ministry.view.models.AddressTypeItem;
import com.del.ministry.view.models.DistrictAddAddress;
import com.google.common.collect.Lists;
import com.jgoodies.common.collect.LinkedListModel;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.Paddings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddAddressIForm extends ObservableIFrame {

    private District district;
    private List<DistrictAddAddress> addressList;

    private JTextField maskF;
    private JList<DistrictAddAddress> districtAddAddressF;
    private JComboBox<Integer> doorsF;
    private JComboBox<AddressTypeItem> typeF;
    private JButton addBtn;

    private final Predicate<DistrictAddAddress> FILTER = new Predicate<DistrictAddAddress>() {
        @Override
        public boolean test(DistrictAddAddress item) {
            return StringUtil.isTrimmedEmpty(maskF.getText()) ||
                    item.toString().toLowerCase().trim().contains(maskF.getText().trim().toLowerCase());
        }
    };

    /**
     * Create the frame.
     */
    public AddAddressIForm() {
        super("Редактировать участок", true, true, true, true);
        setMinimumSize(new Dimension(500, 200));

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        maskF = new JTextField();
        doorsF = new JComboBox<>();
        typeF = new JComboBox<>();
        districtAddAddressF = new JList<>();
        districtAddAddressF.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addBtn = new JButton("Добавить");
        addBtn.setEnabled(false);

        panel.add(FormBuilder.create().
                        columns("50, 5, pref:grow, 5, 80, 5, 120")
                        .rows("p, 5, p, 5, p, fill:0:grow, p")
                        .padding(Paddings.DIALOG)
                        .add("Поиск:").xy(1, 1).add(maskF).xyw(3, 1, 5)
                        .add(districtAddAddressF).xywh(1, 3, 3, 5).add("Дверь").xy(5, 3).add(doorsF).xy(7, 3)
                        .add("Тип").xy(5, 5).add(typeF).xy(7, 5)
                        .add(addBtn).xy(7, 7)
                        .build(),
                BorderLayout.CENTER);

        districtAddAddressF.addListSelectionListener(e -> initDoors());


        maskF.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent keyEvent) {
                //
            }

            public void keyReleased(KeyEvent keyEvent) {
                filterAddressList();
            }

            public void keyTyped(KeyEvent keyEvent) {
                //
            }
        });

        addBtn.addActionListener(e -> add());

        init();
        pack();
    }

    private void add() {
        int selectedIndex = districtAddAddressF.getSelectedIndex();
        if (selectedIndex > -1) {
            DistrictAddAddress selectedValue = districtAddAddressF.getSelectedValue();
            Integer door = doorsF.getItemAt(doorsF.getSelectedIndex());
            DistrictAddress da = new DistrictAddress();
            da.setBuilding(selectedValue.getBuilding());
            da.setDistrict(getDistrict());
            da.setType(typeF.getItemAt(typeF.getSelectedIndex()).getAddressType());
            da.setNumber(door);
            try {
                ServiceManager.getInstance().createDistrictAddress(da);
                initDoors();
                Launcher.mainFrame.initLeftSideTree();
                notifyObservers();
            } catch (Exception e1) {
                Launcher.mainFrame.setStatusError("Не удалось привязить адрес", e1);
            }
        }
    }

    private void init() {
        try {
            List<AddressType> addressTypes = ServiceManager.getInstance().findAddressTypes();
            List<AddressTypeItem> types = addressTypes.stream().map(AddressTypeItem::new).collect(Collectors.toList());
            typeF.setModel(new DefaultComboBoxModel<>(new Vector<>(types)));
        } catch (CommonException e) {
            Launcher.mainFrame.setStatusError("Ошибка чтения типов адресов", e);
        }
    }

    private void initDoors() {
        int selectedIndex = districtAddAddressF.getSelectedIndex();
        addBtn.setEnabled(selectedIndex > -1);
        if (selectedIndex > -1) {
            DistrictAddAddress selectedValue = districtAddAddressF.getSelectedValue();
            List<Integer> items = Lists.newArrayList();
            try {
                List<Integer> usedDoors = ServiceManager.getInstance().getUsedDoors(selectedValue.getBuilding().getId());
                Stream.iterate(1, i -> i + 1).limit(selectedValue.getBuilding().getDoors()).forEach(door -> {
                    if (!usedDoors.contains(door)) {
                        items.add(door);
                    }
                });
                doorsF.setModel(new DefaultComboBoxModel<>(new Vector<>(items)));
            } catch (CommonException e1) {
                Launcher.mainFrame.setStatusError("Нет доступа к данным", e1);
            }
        }
    }

    public District getDistrict() {
        return district;
    }

    private void filterAddressList() {
        LinkedListModel<DistrictAddAddress> model = addressList.stream().filter(FILTER).collect(Collectors.toCollection(LinkedListModel::new));
        districtAddAddressF.setModel(model);
    }

    public void setDistrict(District district) {
        this.district = district;
        setTitle("Редактировать участок '" + district.getNumber() + "'");
        try {
            BuildingFilter f = new BuildingFilter();
            f.setOnlyAvailableDoors(true);
            List<Building> buildings = ServiceManager.getInstance().findBuildings(f);
            addressList = buildings.stream().map(DistrictAddAddress::new).collect(Collectors.toList());
            filterAddressList();
        } catch (Exception e) {
            Launcher.mainFrame.setStatusError("Нет доступа к адресам", e);
        }
    }
}
