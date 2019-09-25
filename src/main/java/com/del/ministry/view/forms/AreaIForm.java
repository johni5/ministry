package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.Area;
import com.del.ministry.utils.CommonException;
import com.del.ministry.utils.Utils;
import com.del.ministry.view.actions.ObservableIPanel;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class AreaIForm extends ObservableIPanel {

    private JTable table;
    private AreaTableModel areaTableModel;
    private JTextField textField;
    private JButton commitButton;
    private JButton revertButton;
    private JButton deleteButton;

    /**
     * Create the frame.
     */
    public AreaIForm() {
        setMinimumSize(new Dimension(600, 400));
        setBounds(100, 100, 600, 400);

        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        scrollPane.setViewportView(table);

        JPanel panel_1 = new JPanel();
        add(panel_1, BorderLayout.SOUTH);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[]{100, 400, 100};
        panel_1.setLayout(gbl_panel_1);

        JLabel lblNewLabel = new JLabel("Название ");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.insets = new Insets(2, 2, 2, 2);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 0;
        panel_1.add(lblNewLabel, gbc_lblNewLabel);

        textField = new JTextField();
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(2, 2, 2, 2);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 0;
        panel_1.add(textField, gbc_textField);

        JButton btnNewButton = new JButton("Создать");
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton.insets = new Insets(2, 2, 2, 2);
        gbc_btnNewButton.gridx = 2;
        gbc_btnNewButton.gridy = 0;
        panel_1.add(btnNewButton, gbc_btnNewButton);

        JPanel panel_2 = new JPanel();
        add(panel_2, BorderLayout.NORTH);

        commitButton = new JButton("Сохранить");
        revertButton = new JButton("Отменить");
        deleteButton = new JButton("Удалить");
        panel_2.add(commitButton);
        panel_2.add(revertButton);
        panel_2.add(deleteButton);
        deleteButton.setEnabled(false);

        areaTableModel = new AreaTableModel();
        table.setModel(areaTableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(400);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);

        table.getSelectionModel().addListSelectionListener(e -> deleteButton.setEnabled(true));

        deleteButton.addActionListener(e -> areaTableModel.removeItems(table.getSelectedRows()));
        commitButton.addActionListener(e -> areaTableModel.commitChanges());
        revertButton.addActionListener(e -> areaTableModel.refresh());
        btnNewButton.addActionListener(e -> {
            String name = textField.getText();
            if (!Utils.isTrimmedEmpty(name)) {
                Area area = new Area();
                area.setName(name);
                try {
                    ServiceManager.getInstance().createArea(area);
                    textField.setText("");
                    areaTableModel.refresh();
                } catch (CommonException e1) {
                    Utils.getLogger().error(e1.getMessage(), e1);
                }
            }
        });
    }

    @Override
    public String getTitle() {
        return "Редактирование районов";
    }

    class AreaTableModel implements TableModel {

        private List<String> header = Lists.newArrayList("ИД", "Название", "Изменено");
        private List<Area> areaList;
        private Set<Integer> changed = Sets.newHashSet();

        @Override
        public int getRowCount() {
            return getAreaList().size();
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
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Area item = getAreaList().get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return item.getId().toString();
                case 1:
                    return item.getName();
                case 2:
                    return changed.contains(rowIndex) ? "Да" : "";
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (!Utils.isTrimmedEmpty(aValue) && columnIndex == 1) {
                Area area = getAreaList().get(rowIndex);
                if (!Objects.deepEquals(aValue, area.getName())) {
                    area.setName(aValue.toString());
                    changed.add(rowIndex);
                    commitButton.setEnabled(true);
                    table.updateUI();
                }
            }
        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }

        public List<Area> getAreaList() {
            if (areaList == null) {
                try {
                    areaList = ServiceManager.getInstance().findAreas();
                    commitButton.setEnabled(false);
                } catch (CommonException e) {
                    Utils.getLogger().error(e.getMessage(), e);
                }
            }
            return areaList;
        }

        public void commitChanges() {
            if (changed.isEmpty()) return;
            for (Integer index : changed) {
                try {
                    ServiceManager.getInstance().updateArea(getAreaList().get(index));
                } catch (CommonException e) {
                    Utils.getLogger().error(e.getMessage(), e);
                }
            }
            refresh();
        }

        public void removeItems(int[] rows) {
            if (rows == null || getAreaList().isEmpty()) return;
            for (int row : rows) {
                try {
                    ServiceManager.getInstance().deleteArea(getAreaList().get(row).getId());
                } catch (CommonException e) {
                    Utils.getLogger().error(e.getMessage(), e);
                }
            }
            refresh();
        }

        public void refresh() {
            areaList = null;
            changed.clear();
            table.updateUI();
        }
    }

}
