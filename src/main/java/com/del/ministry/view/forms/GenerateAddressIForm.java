package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.*;
import com.del.ministry.utils.CommonException;
import com.del.ministry.utils.DistrictGenerator;
import com.del.ministry.utils.ListUtil;
import com.del.ministry.view.Launcher;
import com.del.ministry.view.MainFrame;
import com.del.ministry.view.actions.ObservableIFrame;
import com.del.ministry.view.filters.BuildingFilter;
import com.del.ministry.view.models.AreaItem;
import com.del.ministry.view.models.NumberItem;
import com.del.ministry.view.models.SelectItemsModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jgoodies.common.base.Objects;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.Paddings;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateAddressIForm extends ObservableIFrame {

    private District district;

    private JSpinner maxDoorsF;
    private JList<AreaItem> areaListF;
    private JComboBox<NumberItem> doorsPerBuildingF;
    private JComboBox<NumberItem> maxFloorF;
    private JComboBox<NumberItem> intervalF;
    private JButton makeBtn;
    private JLabel buildingsL;

    /**
     * Create the frame.
     */
    public GenerateAddressIForm() {
        super("Заполнение участка случайными адресами", true, true, true, true);
        setMinimumSize(new Dimension(550, 200));

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        buildingsL = new JLabel("");
        maxDoorsF = new JSpinner(new SpinnerNumberModel(70, 1, 200, 1));
        maxFloorF = new JComboBox<>();
        List<NumberItem> items = Stream.of(10, 20, 30, 40, 50, 60, 70).
                map(NumberItem::new).
                collect(Collectors.toList());
        items.add(0, new NumberItem(0, "Не задан", true));
        intervalF = new JComboBox<>(new SelectItemsModel<>(items));
        intervalF.setEnabled(false);
        items = Stream.of(1, 2, 3, 4, 5, 10, 15).
                map(i -> new NumberItem(i, i + " кв./дом")).
                collect(Collectors.toList());
        items.add(0, new NumberItem(0, "Не огранич.", true));
        doorsPerBuildingF = new JComboBox<>(new SelectItemsModel<>(items));
        areaListF = new JList<>();
        areaListF.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        areaListF.addListSelectionListener(e -> initMaxFloors());
        areaListF.addListSelectionListener(e -> initBuildingsCount());

        makeBtn = new JButton("Заполнить");
        makeBtn.addActionListener(e -> generate());

        panel.add(FormBuilder.create().
                        columns("250:grow, 5, 100, 5, 120")
                        .rows("p, 5, p, 5, p, 5, p, fill:0:grow, 5, p")
                        .padding(Paddings.DIALOG)
                        .add("Укажите районы").xy(1, 1).add("Квартир").xy(3, 1).add(maxDoorsF).xy(5, 1)
                        .add(areaListF).xywh(1, 3, 1, 6)
                        .add("Этажей").xy(3, 3).add(maxFloorF).xy(5, 3)
                        .add("Расброс").xy(3, 5).add(intervalF).xy(5, 5)
                        .add("Плотность").xy(3, 7).add(doorsPerBuildingF).xy(5, 7)
                        .add(buildingsL).xy(1, 10).add(makeBtn).xyw(3, 10, 3)
                        .build(),
                BorderLayout.CENTER);

        initAreaList();
        initMaxFloors();
        initBuildingsCount();

        pack();
    }

    private void initMaxFloors() {
        List<Long> areas = areaListF.getSelectedValuesList().
                stream().map(areaItem -> areaItem.getArea().getId()).
                collect(Collectors.toList());
        List<NumberItem> floorItems = Lists.newArrayList();
        floorItems.add(new NumberItem(0, "Не задан", true));
        try {
            int maxFloor = ServiceManager.getInstance().getMaxFloor(areas);
            floorItems.addAll(Stream.iterate(1, i -> i + 1).limit(maxFloor).map(NumberItem::new).collect(Collectors.toList()));
        } catch (Exception e) {
            MainFrame.setStatusError("Ошибка получения данных!", e);
        }
        maxFloorF.setModel(new SelectItemsModel<>(floorItems));
    }

    private void initAreaList() {
        try {
            List<Area> areas = ServiceManager.getInstance().findAreas();
            areaListF.setModel(new SelectItemsModel<AreaItem>(areas.stream().map(AreaItem::new).sorted().collect(Collectors.toList())));
        } catch (Exception e) {
            MainFrame.setStatusError("Ошибка получения данных!", e);
        }
    }

    private void generate() {
        BuildingFilter filter = new BuildingFilter();
        List<Long> areas = areaListF.getSelectedValuesList().
                stream().map(areaItem -> areaItem.getArea().getId()).
                collect(Collectors.toList());
        filter.setAreaIds(areas);
        ServiceManager serviceManager = ServiceManager.getInstance();
        Map<Long, List<Integer>> usedDoorsAtAll = Maps.newHashMap();
        Map<Long, AtomicInteger> doorsCounter = Maps.newHashMap();
        try {
            AddressType defaultAddressType = serviceManager.getDefaultAddressType();
            int districtSize = ListUtil.size(serviceManager.findDistrictAddresses(getDistrict().getId()));
            List<Building> buildings = serviceManager.findBuildings(filter);
            for (Building building : buildings) {
                usedDoorsAtAll.put(building.getId(), serviceManager.getUsedDoors(building.getId()));
            }
            List<Building> readyBuildings = buildings.stream().
                    filter(building -> building.getDoors() > usedDoorsAtAll.get(building.getId()).size()).
                    sorted(Comparator.comparingInt(o -> usedDoorsAtAll.get(o.getId()).size())).
                    collect(Collectors.toList());
            if (ListUtil.isEmpty(readyBuildings)) {
                MainFrame.setStatusError("Нет свободных адресов!");
                return;
            }
            NumberItem manualMaxFloors = maxFloorF.getItemAt(maxFloorF.getSelectedIndex());
            NumberItem manualMaxDoors = doorsPerBuildingF.getItemAt(doorsPerBuildingF.getSelectedIndex());
            int ready = 0;
            int index = 0;
            while (ready < ((Integer) maxDoorsF.getValue()) - districtSize) {
                Building building = readyBuildings.get(index);
                List<Integer> usedDoors = usedDoorsAtAll.get(building.getId());
                if (usedDoors.size() < building.getDoors()) {
                    Integer rndDoor = DistrictGenerator.rndDoor(
                            building,
                            manualMaxFloors.getNumber(building.getFloors()).intValue(),
                            Sets.newHashSet(usedDoors)
                    );
                    if (rndDoor > 0) {
                        usedDoors.add(rndDoor);
                        if (!doorsCounter.containsKey(building.getId())) {
                            doorsCounter.put(building.getId(), new AtomicInteger(0));
                        }
                        if (doorsCounter.get(building.getId()).incrementAndGet() >= manualMaxDoors.getNumber(building.getDoors()).intValue()) {
                            // ограничение по плотности квартир на дом больше не позволяет присваивать адреса
                            readyBuildings.remove(index);
                            index--;
                        }

//                        System.out.println(ready + " -> " + building + " -> " + rndDoor + "(" + doorsCounter.get(building.getId()).get() + ")");

                        DistrictAddress da = new DistrictAddress();
                        da.setDistrict(getDistrict());
                        da.setBuilding(building);
                        da.setType(defaultAddressType);
                        da.setNumber(rndDoor);
                        ServiceManager.getInstance().createDistrictAddress(da);

                        ready++;
                    } else {
                        // ограничение по этажам больше не позволяет присваивать адреса
                        readyBuildings.remove(index);
                        index--;
                    }
                } else {
                    // адреса закончились
                    readyBuildings.remove(index);
                    index--;
                }
                if (ListUtil.isEmpty(readyBuildings)) break;
                index = (index + 1) % readyBuildings.size();
            }

            if (ready > 0) {
                MainFrame.setStatusText("К участку было привязано адресов: " + ready);
                Launcher.mainFrame.initLeftSideTree();
                notifyObservers();
            } else MainFrame.setStatusError("Не был привязан ни один адрес");

        } catch (Exception e) {
            MainFrame.setStatusError("Ошибка получения/сохранения данных!", e);
        }

    }

    private void initBuildingsCount() {
        List<Long> areas = areaListF.getSelectedValuesList().
                stream().map(areaItem -> areaItem.getArea().getId()).
                collect(Collectors.toList());
        try {
            int count = ServiceManager.getInstance().countAvailableBuildings(areas);
            buildingsL.setText("Доступно адресов: " + count);
        } catch (CommonException e) {
            MainFrame.setStatusError("Ошибка получения данных!", e);
        }
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
        setTitle("Заполнение участка '" + district.getNumber() + "' случайными адресами");
    }
}
