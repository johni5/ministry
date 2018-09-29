package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.District;
import com.del.ministry.utils.CommonException;
import com.del.ministry.utils.Utils;
import com.google.common.collect.Lists;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DistrictIForm extends JInternalFrame {

    private JTable table;
    private JTextField textField;

    /**
     * Create the frame.
     */
    public DistrictIForm() {
        super("Участки", true, true, true, true);
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
        gbl_panel_1.columnWidths = new int[]{150, 300, 200};
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

        renderTable();

        btnNewButton.addActionListener(e -> {
            String number = textField.getText();
            if (!Utils.isTrimmedEmpty(number)) {
                District district = new District();
                district.setNumber(number);
                try {
                    ServiceManager.getInstance().createDistrict(district);
                    textField.setText("");
                    renderTable();
                } catch (CommonException e1) {
                    Utils.getLogger().error(e1.getMessage(), e1);
                }
            }
        });
    }

    private void renderTable() {
        try {
            List<District> districts = ServiceManager.getInstance().allDistricts();
            table.setModel(new DistrictTableModel(districts));
        } catch (CommonException e) {
            Utils.getLogger().error(e.getMessage(), e);
        }
    }


    class DistrictTableModel implements TableModel {

        private List<String> header = Lists.newArrayList("ИД", "Название", "");
        private List<District> districtList;

        public DistrictTableModel(List<District> districtList) {
            this.districtList = districtList;
        }

        @Override
        public int getRowCount() {
            return districtList.size();
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
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            District district = districtList.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return district.getId().toString();
                case 1:
                    return district.getNumber();
                case 2:
                    return "-";
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }

}
