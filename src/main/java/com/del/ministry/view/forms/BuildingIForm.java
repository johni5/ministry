package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.*;
import com.del.ministry.utils.CommonException;
import com.del.ministry.utils.Utils;
import com.del.ministry.view.MainFrame;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;

public class BuildingIForm extends JInternalFrame {

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

    private JButton commitButton;
    private JButton revertButton;
    private JButton deleteButton;

    /**
     * Create the frame.
     */
    public BuildingIForm() {
        super("Строение", true, true, true, true);
        setMinimumSize(new Dimension(900, 400));
        setBounds(100, 100, 900, 400);

        try {
            JPanel panel = new JPanel();
            getContentPane().add(panel, BorderLayout.CENTER);
            panel.setLayout(new BorderLayout(0, 0));

            JScrollPane scrollPane = new JScrollPane();
            panel.add(scrollPane, BorderLayout.CENTER);

            table = new JTable();
            scrollPane.setViewportView(table);

            JPanel filterPanel = new JPanel();
            GridBagLayout filterPanelLayout = new GridBagLayout();
            filterPanelLayout.columnWidths = new int[]{100, 200};
            filterPanel.setLayout(filterPanelLayout);

            JPanel panel_1 = new JPanel();
            panel.add(panel_1, BorderLayout.SOUTH);
            GridBagLayout gbl_panel_1 = new GridBagLayout();
            gbl_panel_1.columnWidths = new int[]{100, 200, 20, 100, 200, 80};
            panel_1.setLayout(gbl_panel_1);

            numberTF = new JTextField();
            doorsTF = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
            entersTF = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
            floorsTF = new JSpinner(new SpinnerNumberModel(1, -2, 100, 1));
            List<Street> streets = ServiceManager.getInstance().findStreets();
            List<City> cites = ServiceManager.getInstance().findCites();
            List<Area> areas = ServiceManager.getInstance().findAreas();
            List<BuildingType> buildingTypes = ServiceManager.getInstance().findBuildingTypes();

            cityCB = new JComboBox<>(new Vector<>(cites));
            areaCB = new JComboBox<>(new Vector<>(areas));
            streetCB = new JComboBox<>(new Vector<>(streets));
            bTypeCB = new JComboBox<>(new Vector<>(buildingTypes));
            areaFilterCB = new JComboBox<>(new Vector<>(areas));
            Area item = new Area();
            item.setName("...");
            areaFilterCB.addItem(item);
            areaFilterCB.setSelectedItem(item);
            streetFilterCB = new JComboBox<>(new Vector<>(streets));
            Street street = new Street();
            street.setName("...");
            streetFilterCB.addItem(street);
            streetFilterCB.setSelectedItem(street);

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
            panel.add(panel_2, BorderLayout.NORTH);
            panel_2.add(filterPanel);

            commitButton = new JButton("Сохранить");
            revertButton = new JButton("Отменить");
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

            table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JComboBox<>(new Vector<>(cites))));
            table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox<>(new Vector<>(areas))));
            table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JComboBox<>(new Vector<>(streets))));
            table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JComboBox<>(new Vector<>(buildingTypes))));

            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    deleteButton.setEnabled(true);
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buildingTableModel.removeItems(table.getSelectedRows());
                }
            });
            commitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buildingTableModel.commitChanges();
                }
            });
            revertButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buildingTableModel.refresh();
                }
            });
            btnNewButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = numberTF.getText();
                    if (!Utils.isTrimmedEmpty(name)) {
                        Building item = new Building();
                        item.setNumber(name);
                        item.setDoors((Integer) doorsTF.getValue());
                        item.setFloors((Integer) floorsTF.getValue());
                        item.setEntrances((Integer) entersTF.getValue());
                        item.setCity((City) cityCB.getSelectedItem());
                        item.setArea((Area) areaCB.getSelectedItem());
                        item.setStreet((Street) streetCB.getSelectedItem());
                        item.setType((BuildingType) bTypeCB.getSelectedItem());
                        try {
                            ServiceManager.getInstance().createBuilding(item);
                            numberTF.setText("");
                            buildingTableModel.refresh();
                        } catch (Exception e1) {
                            MainFrame.setStatusError("Ошибка сохранения записи!", e1);
                        }
                    }
                }
            });
        } catch (CommonException e) {
            MainFrame.setStatusError("Ошибка получения списка данных!", e);
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
                    data = ServiceManager.getInstance().findBuildings(
                            ((Area) areaFilterCB.getSelectedItem()).getId(),
                            ((Street) streetFilterCB.getSelectedItem()).getId()

                    );
                    commitButton.setEnabled(false);
                } catch (Exception e) {
                    MainFrame.setStatusError("Ошибка получения данных!", e);
                }
            }
            return data;
        }

        public void commitChanges() {
            if (changed.isEmpty()) return;
            for (Integer index : changed) {
                try {
                    ServiceManager.getInstance().updateBuilding(getData().get(index));
                } catch (Exception e) {
                    MainFrame.setStatusError("Ошибка сохранения записи!", e);
                }
            }
            refresh();
        }

        public void removeItems(int[] rows) {
            if (rows == null || getData().isEmpty()) return;
            for (int row : rows) {
                try {
                    ServiceManager.getInstance().deleteBuilding(getData().get(row).getId());
                } catch (Exception e) {
                    MainFrame.setStatusError("Ошибка удаления записи!", e);
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

}
