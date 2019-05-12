package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.*;
import com.del.ministry.utils.CommonException;
import com.del.ministry.utils.Utils;
import com.del.ministry.view.Launcher;
import com.del.ministry.view.actions.ObservableIPanel;
import com.del.ministry.view.filters.BuildingFilter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;

public class BuildingIForm extends ObservableIPanel {

    private JTable table;
    private BuildingTableModel buildingTableModel;

    private JTextField numberTF;
    private JSpinner doorsTF;
    private JSpinner entersTF;
    private JSpinner floorsTF;
    private JComboBox<City> cityCB;
    private JComboBox<Area> areaCB;
    private JComboBox<Street> streetCB;
    private JComboBox<BuildingType> bTypeCB;
    private JComboBox<Area> areaFilterCB;
    private JComboBox<Street> streetFilterCB;
    private JScrollPane scrollPane;

    private JButton commitButton;
    private JButton revertButton;
    private JButton deleteButton;

    /**
     * Create the frame.
     */
    public BuildingIForm() {
        setMinimumSize(new Dimension(900, 400));
        setBounds(100, 100, 900, 400);

        setLayout(new BorderLayout(0, 0));

        scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        scrollPane.setViewportView(table);

        JPanel filterPanel = new JPanel();
        GridBagLayout filterPanelLayout = new GridBagLayout();
        filterPanelLayout.columnWidths = new int[]{100, 200};
        filterPanel.setLayout(filterPanelLayout);

        JPanel panel_1 = new JPanel();
        add(panel_1, BorderLayout.SOUTH);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[]{100, 200, 20, 100, 200, 80};
        panel_1.setLayout(gbl_panel_1);

        numberTF = new JTextField();
        doorsTF = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        entersTF = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        floorsTF = new JSpinner(new SpinnerNumberModel(1, -2, 100, 1));

        cityCB = new JComboBox<>();
        areaCB = new JComboBox<>();
        streetCB = new JComboBox<>();
        bTypeCB = new JComboBox<>();
        areaFilterCB = new JComboBox<>();
        streetFilterCB = new JComboBox<>();

        panel_1.add(new JLabel("Номер дома"), createGridBagConstraints(0, 0));
        panel_1.add(numberTF, createGridBagConstraints(1, 0));

        panel_1.add(new JLabel("Дверей"), createGridBagConstraints(3, 0));
        panel_1.add(doorsTF, createGridBagConstraints(4, 0));

        panel_1.add(new JLabel("Подъездов"), createGridBagConstraints(0, 1));
        panel_1.add(entersTF, createGridBagConstraints(1, 1));

        panel_1.add(new JLabel("Этажей"), createGridBagConstraints(3, 1));
        panel_1.add(floorsTF, createGridBagConstraints(4, 1));

        panel_1.add(new JLabel("Город"), createGridBagConstraints(0, 2));
        panel_1.add(cityCB, createGridBagConstraints(1, 2));

        panel_1.add(new JLabel("Район"), createGridBagConstraints(3, 2));
        panel_1.add(areaCB, createGridBagConstraints(4, 2));

        panel_1.add(new JLabel("Улица"), createGridBagConstraints(0, 3));
        panel_1.add(streetCB, createGridBagConstraints(1, 3));

        panel_1.add(new JLabel("Тип"), createGridBagConstraints(3, 3));
        panel_1.add(bTypeCB, createGridBagConstraints(4, 3));

        JButton btnNewButton = new JButton("Создать");
        panel_1.add(btnNewButton, createGridBagConstraints(3, 4));

        filterPanel.add(new JLabel("Район"), createGridBagConstraints(0, 0));
        filterPanel.add(areaFilterCB, createGridBagConstraints(1, 0));
        filterPanel.add(new JLabel("Улица"), createGridBagConstraints(0, 1));
        filterPanel.add(streetFilterCB, createGridBagConstraints(1, 1));

        JPanel panel_2 = new JPanel();
        add(panel_2, BorderLayout.NORTH);
        panel_2.add(filterPanel);

        commitButton = new JButton("Сохранить");
        revertButton = new JButton("Обновить");
        deleteButton = new JButton("Удалить");
        panel_2.add(commitButton);
        panel_2.add(revertButton);
        panel_2.add(deleteButton);
        deleteButton.setEnabled(false);

        buildingTableModel = new BuildingTableModel();
        table.setModel(buildingTableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(80);
        table.getColumnModel().getColumn(7).setPreferredWidth(80);
        table.getColumnModel().getColumn(8).setPreferredWidth(80);
        table.getColumnModel().getColumn(9).setPreferredWidth(80);

        table.getSelectionModel().addListSelectionListener(e -> deleteButton.setEnabled(true));

        deleteButton.addActionListener(e -> buildingTableModel.removeItems(table.getSelectedRows()));
        commitButton.addActionListener(e -> buildingTableModel.commitChanges());
        revertButton.addActionListener(e -> buildingTableModel.refresh());
        btnNewButton.addActionListener(e -> {
            String name = numberTF.getText();
            if (!Utils.isTrimmedEmpty(name)) {
                Building item1 = new Building();
                item1.setNumber(name);
                item1.setDoors((Integer) doorsTF.getValue());
                item1.setFloors((Integer) floorsTF.getValue());
                item1.setEntrances((Integer) entersTF.getValue());
                item1.setCity((City) cityCB.getSelectedItem());
                item1.setArea((Area) areaCB.getSelectedItem());
                item1.setStreet((Street) streetCB.getSelectedItem());
                item1.setType((BuildingType) bTypeCB.getSelectedItem());
                try {
                    ServiceManager.getInstance().createBuilding(item1);
                    numberTF.setText("");
                    buildingTableModel.refresh();
                    Launcher.mainFrame.initLeftSideTree();
                } catch (Exception e1) {
                    Launcher.mainFrame.setStatusError("Ошибка сохранения записи!", e1);
                }
            }
        });
    }

    @Override
    public void beforeShow() {
        try {
            ServiceManager serviceManager = ServiceManager.getInstance();
            List<Street> streets = serviceManager.findStreets();
            List<City> cites = serviceManager.findCites();
            List<Area> areas = serviceManager.findAreas();
            List<BuildingType> buildingTypes = serviceManager.findBuildingTypes();

            Area item = new Area();
            item.setName("...");
            areas.add(0, item);
            Street street = new Street();
            street.setName("...");
            streets.add(0, street);

            cityCB.setModel(new DefaultComboBoxModel<>(new Vector<>(cites)));
            areaCB.setModel(new DefaultComboBoxModel<>(new Vector<>(areas)));
            streetCB.setModel(new DefaultComboBoxModel<>(new Vector<>(streets)));
            bTypeCB.setModel(new DefaultComboBoxModel<>(new Vector<>(buildingTypes)));
            areaFilterCB.setModel(new DefaultComboBoxModel<>(new Vector<>(areas)));
            streetFilterCB.setModel(new DefaultComboBoxModel<>(new Vector<>(streets)));

            areaFilterCB.setSelectedItem(item);
            streetFilterCB.setSelectedItem(street);

            table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JComboBox<>(new Vector<>(cites))));
            table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox<>(new Vector<>(areas))));
            table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JComboBox<>(new Vector<>(streets))));
            table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JComboBox<>(new Vector<>(buildingTypes))));

            buildingTableModel.refresh();
        } catch (CommonException e) {
            Launcher.mainFrame.setStatusError("Ошибка получения списка данных!", e);
        }

    }

    private GridBagConstraints createGridBagConstraints(int x, int y) {
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(2, 2, 2, 2);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = x;
        gbc_textField.gridy = y;
        return gbc_textField;
    }

    class BuildingTableModel implements TableModel {

        private List<String> header = Lists.newArrayList("ИД", "Город", "Район", "Улица", "Номер", "Тип", "Квартир", "Подъездов", "Этажей", "Изменено");
        private List<Building> data;
        private Set<Integer> changed = Sets.newHashSet();

        @Override
        public int getRowCount() {
            return getData().size();
        }

        @Override
        public int getColumnCount() {
            return header.size();
        }

        @Override
        public String getColumnName(int columnIndex) {
            return header.get(columnIndex);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 1:
                    return City.class;
                case 2:
                    return Area.class;
                case 3:
                    return Street.class;
                case 5:
                    return BuildingType.class;
                case 6:
                case 7:
                case 8:
                    return Integer.class;
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex != 0 && columnIndex != header.size() - 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Building item = getData().get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return item.getId().toString();
                case 1:
                    return item.getCity().getName();
                case 2:
                    return item.getArea().getName();
                case 3:
                    return item.getStreet().getName();
                case 4:
                    return item.getNumber();
                case 5:
                    return item.getType().getName();
                case 6:
                    return item.getDoors();
                case 7:
                    return item.getEntrances();
                case 8:
                    return item.getFloors();
                case 9:
                    return changed.contains(rowIndex) ? "Да" : "";
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Building item = getData().get(rowIndex);
            if (!Utils.isTrimmedEmpty(aValue)) {
                switch (columnIndex) {
                    case 1:
                        if (!Objects.deepEquals(aValue, item.getCity())) {
                            item.setCity((City) aValue);
                            wasChanged(rowIndex);
                        }
                        break;
                    case 2:
                        if (!Objects.deepEquals(aValue, item.getArea())) {
                            item.setArea((Area) aValue);
                            wasChanged(rowIndex);
                        }
                        break;
                    case 3:
                        if (!Objects.deepEquals(aValue, item.getStreet())) {
                            item.setStreet((Street) aValue);
                            wasChanged(rowIndex);
                        }
                        break;
                    case 4:
                        if (!Objects.deepEquals(aValue, item.getNumber())) {
                            item.setNumber(aValue.toString());
                            wasChanged(rowIndex);
                        }
                        break;
                    case 5:
                        if (!Objects.deepEquals(aValue, item.getType())) {
                            item.setType((BuildingType) aValue);
                            wasChanged(rowIndex);
                        }
                        break;
                    case 6:
                        if (!Objects.deepEquals(aValue, item.getDoors())) {
                            item.setDoors(((Number) aValue).intValue());
                            wasChanged(rowIndex);
                        }
                        break;
                    case 7:
                        if (!Objects.deepEquals(aValue, item.getEntrances())) {
                            item.setEntrances(((Number) aValue).intValue());
                            wasChanged(rowIndex);
                        }
                        break;
                    case 8:
                        if (!Objects.deepEquals(aValue, item.getFloors())) {
                            item.setFloors(((Number) aValue).intValue());
                            wasChanged(rowIndex);
                        }
                        break;
                }
            }
        }

        private void wasChanged(int rowIndex) {
            changed.add(rowIndex);
            commitButton.setEnabled(true);
            table.updateUI();
        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }

        public List<Building> getData() {
            if (data == null) {
                try {
                    BuildingFilter filter = new BuildingFilter();
                    filter.setAreaIds(Lists.newArrayList(((Area) Objects.requireNonNull(areaFilterCB.getSelectedItem())).getId()));
                    filter.setStreetIds(Lists.newArrayList(((Street) Objects.requireNonNull(streetFilterCB.getSelectedItem())).getId()));
                    data = ServiceManager.getInstance().findBuildings(filter);
                    commitButton.setEnabled(false);
                } catch (Exception e) {
                    Launcher.mainFrame.setStatusError("Ошибка получения данных!", e);
                }
            }
            return data;
        }

        public void commitChanges() {
            if (changed.isEmpty()) return;
            for (Integer index : changed) {
                try {
                    ServiceManager.getInstance().updateBuilding(getData().get(index));
                    Launcher.mainFrame.initLeftSideTree();
                } catch (Exception e) {
                    Launcher.mainFrame.setStatusError("Ошибка сохранения записи!", e);
                }
            }
            refresh();
        }

        public void removeItems(int[] rows) {
            if (rows == null || getData().isEmpty()) return;
            for (int row : rows) {
                try {
                    ServiceManager.getInstance().deleteBuilding(getData().get(row).getId());
                    Launcher.mainFrame.initLeftSideTree();
                } catch (Exception e) {
                    Launcher.mainFrame.setStatusError("Ошибка удаления записи!", e);
                }
            }
            refresh();
        }


        public void refresh() {
            data = null;
            changed.clear();
            table.updateUI();
        }
    }

    public void setSelectedRow(Long buildingId) {
        table.getSelectionModel().clearSelection();
        for (int i = 0; i < table.getRowCount(); i++) {
            Object id = table.getValueAt(i, 0);
            if (Objects.deepEquals(id.toString(), buildingId.toString())) {
                table.changeSelection(i, i, false, false);
                break;
            }
        }

    }

}
