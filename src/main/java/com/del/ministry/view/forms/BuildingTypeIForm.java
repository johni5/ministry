package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.BuildingType;
import com.del.ministry.utils.CommonException;
import com.del.ministry.utils.Utils;
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

public class BuildingTypeIForm extends JInternalFrame {

    private JTable table;
    private BuildingTypeTableModel buildingTypeTableModel;
    private JTextField nameTF;
    private JTextField shortNameTF;
    private JButton commitButton;
    private JButton revertButton;
    private JButton deleteButton;

    /**
     * Create the frame.
     */
    public BuildingTypeIForm() {
        super("Тип строения", true, true, true, true);
        setMinimumSize(new Dimension(600, 400));
        setBounds(100, 100, 600, 400);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        scrollPane.setViewportView(table);

        JPanel panel_1 = new JPanel();
        panel.add(panel_1, BorderLayout.SOUTH);
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

        nameTF = new JTextField();
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(2, 2, 2, 2);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 0;
        panel_1.add(nameTF, gbc_textField);

        JLabel lblNewLabel1 = new JLabel("Код ");
        GridBagConstraints gbc_lblNewLabel1 = new GridBagConstraints();
        gbc_lblNewLabel1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel1.insets = new Insets(2, 2, 2, 2);
        gbc_lblNewLabel1.gridx = 0;
        gbc_lblNewLabel1.gridy = 1;
        panel_1.add(lblNewLabel1, gbc_lblNewLabel1);

        shortNameTF = new JTextField();
        GridBagConstraints gbc_textField2 = new GridBagConstraints();
        gbc_textField2.insets = new Insets(2, 2, 2, 2);
        gbc_textField2.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField2.gridx = 1;
        gbc_textField2.gridy = 1;
        panel_1.add(shortNameTF, gbc_textField2);

        JButton btnNewButton = new JButton("Создать");
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnNewButton.insets = new Insets(2, 2, 2, 2);
        gbc_btnNewButton.gridx = 2;
        gbc_btnNewButton.gridy = 1;
        panel_1.add(btnNewButton, gbc_btnNewButton);

        JPanel panel_2 = new JPanel();
        panel.add(panel_2, BorderLayout.NORTH);

        commitButton = new JButton("Сохранить");
        revertButton = new JButton("Отменить");
        deleteButton = new JButton("Удалить");
        panel_2.add(commitButton);
        panel_2.add(revertButton);
        panel_2.add(deleteButton);
        deleteButton.setEnabled(false);

        buildingTypeTableModel = new BuildingTypeTableModel();
        table.setModel(buildingTypeTableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                deleteButton.setEnabled(true);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buildingTypeTableModel.removeItems(table.getSelectedRows());
            }
        });
        commitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buildingTypeTableModel.commitChanges();
            }
        });
        revertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buildingTypeTableModel.refresh();
            }
        });
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTF.getText();
                String shortName = shortNameTF.getText();
                if (!Utils.isTrimmedEmpty(name) && !Utils.isTrimmedEmpty(shortName)) {
                    BuildingType item = new BuildingType();
                    item.setName(name);
                    item.setShortName(shortName);
                    try {
                        ServiceManager.getInstance().createBuildingType(item);
                        nameTF.setText("");
                        shortNameTF.setText("");
                        buildingTypeTableModel.refresh();
                    } catch (CommonException e1) {
                        Utils.getLogger().error(e1.getMessage(), e1);
                    }
                }
            }
        });
    }

    class BuildingTypeTableModel implements TableModel {

        private List<String> header = Lists.newArrayList("ИД", "Название", "Код", "Изменено");
        private List<BuildingType> data;
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
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1 || columnIndex == 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            BuildingType item = getData().get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return item.getId().toString();
                case 1:
                    return item.getName();
                case 2:
                    return item.getShortName();
                case 3:
                    return changed.contains(rowIndex) ? "Да" : "";
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (!Utils.isTrimmedEmpty(aValue)) {
                BuildingType item = getData().get(rowIndex);
                if (columnIndex == 1 && !Objects.deepEquals(aValue, item.getName())) {
                    item.setName(aValue.toString());
                    wasChanged(rowIndex);
                }
                if (columnIndex == 2 && !Objects.deepEquals(aValue, item.getShortName())) {
                    item.setShortName(aValue.toString());
                    wasChanged(rowIndex);
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

        public List<BuildingType> getData() {
            if (data == null) {
                try {
                    data = ServiceManager.getInstance().findBuildingTypes();
                    commitButton.setEnabled(false);
                } catch (CommonException e) {
                    Utils.getLogger().error(e.getMessage(), e);
                }
            }
            return data;
        }

        public void commitChanges() {
            if (changed.isEmpty()) return;
            for (Integer index : changed) {
                try {
                    ServiceManager.getInstance().updateBuildingType(getData().get(index));
                } catch (CommonException e) {
                    Utils.getLogger().error(e.getMessage(), e);
                }
            }
            refresh();
        }

        public void removeItems(int[] rows) {
            if (rows == null || getData().isEmpty()) return;
            for (int row : rows) {
                try {
                    ServiceManager.getInstance().deleteBuildingType(getData().get(row).getId());
                } catch (CommonException e) {
                    Utils.getLogger().error(e.getMessage(), e);
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
