package com.del.ministry.view.forms;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.db.*;
import com.del.ministry.utils.CommonException;
import com.del.ministry.utils.DateUtilz;
import com.del.ministry.utils.ListUtil;
import com.del.ministry.view.Launcher;
import com.del.ministry.view.MainFrame;
import com.del.ministry.view.actions.ObservableIFrame;
import com.del.ministry.view.filters.AppointmentsFilter;
import com.del.ministry.view.models.DistrictNumbers;
import com.del.ministry.view.models.PublisherItem;
import com.del.ministry.view.models.SelectItemsModel;
import com.del.ministry.view.models.table.ReadOnlyTableModel;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.google.common.collect.Maps;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.Paddings;
import net.sf.jasperreports.data.cache.ColumnValuesDataSource;
import net.sf.jasperreports.data.cache.ObjectArrayValues;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentsIForm extends ObservableIFrame {

    final static Logger logger = Logger.getLogger(AppointmentsIForm.class);

    private final Object[] HEADER = {"ФИО", "Номер участка", "Взял", "Сдал"};
    private Map<Integer, Appointment> appointmentCache = Maps.newHashMap();
    private Map<Long, DistrictNumbers> districtNumbers;

    private JTable table;
    private DatePicker dpFrom, dpTo;
    private JTextField publisherFilterF, districtFilterF;
    private JCheckBox filterOnlyActive;
    private JComboBox<DistrictNumbers> districtNumbersF;

    public AppointmentsIForm() {
        super("Назначения участков", true, true, true, true);
        setMinimumSize(new Dimension(600, 400));

        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane();
        table = new JTable();
        JPanel controlPanel = new JPanel(new BorderLayout(0, 0));
        JPanel filterPanel = new JPanel();
        JButton searchBtn = new JButton("Поиск");
        JButton pdfBtn = new JButton("PDF");
        pdfBtn.setEnabled(false);
        JButton takeBtn = new JButton("Назначить");
        JButton returnBtn = new JButton("Сдать");
        returnBtn.setEnabled(false);
        DatePickerSettings s1 = new DatePickerSettings();
        DatePickerSettings s2 = new DatePickerSettings();
        DatePickerSettings s3 = new DatePickerSettings();
        s1.setFormatForDatesCommonEra("dd/MM/yyyy");
        s2.setFormatForDatesCommonEra("dd/MM/yyyy");
        s3.setFormatForDatesCommonEra("dd/MM/yyyy");
        dpFrom = new DatePicker(s1);
        dpTo = new DatePicker(s2);
        DatePicker dpReturn = new DatePicker(s3);
        dpReturn.setEnabled(false);
        publisherFilterF = new JTextField();
        districtFilterF = new JTextField();
        districtNumbersF = new JComboBox<>();
        JComboBox<PublisherItem> publishersF = new JComboBox<>();
        filterOnlyActive = new JCheckBox("Назначен в настоящее время", true);

        panel.setLayout(new BorderLayout(0, 0));
        panel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(table);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        getContentPane().add(filterPanel, BorderLayout.NORTH);

        filterPanel.add(FormBuilder.create().
                columns("100, 5, 20, 5, 150, 5, 20, 5, 150")
                .rows("p, 5, p, 5, p, 5, p")
                .padding(Paddings.DIALOG)
                .addLabel("Период").xy(1, 1).addLabel("с").xy(3, 1).add(dpFrom).xy(5, 1).addLabel("по").xy(7, 1).add(dpTo).xy(9, 1)
                .addLabel("Фамилия").xy(1, 3).add(publisherFilterF).xyw(3, 3, 7)
                .addLabel("Участок").xy(1, 5).add(districtFilterF).xyw(3, 5, 3).add(searchBtn).xywh(9, 5, 1, 3)
                .add(filterOnlyActive).xyw(1, 7, 5)
                .build());
        controlPanel.add(FormBuilder.create().
                columns("150, 5, 200, 5, 100, 200, pref:grow, 100")
                .rows("p, 5, p")
                .padding(Paddings.DIALOG)
                .add(districtNumbersF).xy(1, 1).add(takeBtn).xy(3, 1).add(publishersF).xyw(5, 1, 2).add(pdfBtn).xywh(8, 1, 1, 3)
                .add(dpReturn).xy(1, 3).add(returnBtn).xy(3, 3)
                .build());

        table.getSelectionModel().addListSelectionListener(e -> {
            Appointment selectedApp = table.getSelectedRow() > -1 ? appointmentCache.get(table.getSelectedRow()) : null;
            boolean allowEdit = selectedApp != null && appointmentCache.get(table.getSelectedRow()).getCompleted() == null;
            returnBtn.setEnabled(allowEdit);
            pdfBtn.setEnabled(allowEdit);
            dpReturn.setEnabled(allowEdit);
            if (selectedApp != null) {
                districtNumbersF.setSelectedItem(districtNumbers.get(selectedApp.getDistrict().getId()));
            }
        });

        takeBtn.addActionListener(e -> {
            try {
                District district = districtNumbersF.getItemAt(districtNumbersF.getSelectedIndex()).getDistrict();
                Publisher publisher = publishersF.getItemAt(publishersF.getSelectedIndex()).getPublisher();
                Appointment appointment = ServiceManager.getInstance().findActiveAppointment(district.getId());
                if (appointment == null) {
                    appointment = new Appointment();
                    appointment.setDistrict(district);
                    appointment.setAssigned(DateUtilz.today());
                } else {
                    if (Objects.deepEquals(appointment.getPublisher().getId(), publisher.getId())
                            ||
                            JOptionPane.showConfirmDialog(null,
                                    String.format("Вы действительно хотите передать участок '%s' возвещателю %s ?", district.getNumber(), publisher.getFIO()),
                                    "Переназначение усастка",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                appointment.setPublisher(publisher);
                ServiceManager.getInstance().createAppointment(appointment);
                initTable();
                MainFrame.setStatusText("Возвещатель " + publisher.getFIO() + " теперь обрабатывает участок " + district.getNumber());
                Launcher.mainFrame.initLeftSideTree();
            } catch (Exception e1) {
                MainFrame.setStatusError("Не удалось назначить участок", e1);
            }
        });

        returnBtn.addActionListener(e -> {
            Appointment appointment = appointmentCache.get(table.getSelectedRow());
            if (appointment != null && dpReturn.getDate() != null) {
                Date completed = DateUtilz.fromLocalDate(dpReturn.getDate());
                if (DateUtilz.between(completed, appointment.getAssigned(), DateUtilz.today())) {
                    String comment = JOptionPane.showInputDialog(this, "Примечания возвещателя", "Сдать участок", JOptionPane.INFORMATION_MESSAGE);
                    if (comment != null) {
                        appointment.setCompleted(completed);
                        appointment.setDescription(comment);
                        try {
                            ServiceManager.getInstance().updateAppointment(appointment);
                            initTable();
                            MainFrame.setStatusText("Возвещатель " + appointment.getPublisher().getFIO() + " сдал участок " + appointment.getDistrict().getNumber());
                            Launcher.mainFrame.initLeftSideTree();
                        } catch (CommonException e1) {
                            MainFrame.setStatusError("Не удалось сдать участок", e1);
                        }
                    }
                } else {
                    MainFrame.setStatusError(
                            String.format("Дата должна быть между %s и %s",
                                    DateUtilz.formatDate(appointment.getAssigned()),
                                    DateUtilz.formatDate(DateUtilz.today()))
                    );
                }
            }
        });

        searchBtn.addActionListener(e -> initTable());
        pdfBtn.addActionListener(e -> makePDF());

        initDistrictList();
        initPublishersList(publishersF);
        initTable();

        pack();

    }

    private void initDistrictList() {
        try {
            List<District> districts = ServiceManager.getInstance().allDistricts();
            List<DistrictNumbers> items = districts.stream().map(DistrictNumbers::new).sorted().collect(Collectors.toList());
            this.districtNumbers = Maps.uniqueIndex(items, d -> d != null ? d.getDistrict().getId() : null);
            districtNumbersF.setModel(new SelectItemsModel<>(items));
        } catch (CommonException e) {
            MainFrame.setStatusError("Нет доступа к участкам", e);
        }
    }

    private void initPublishersList(JComboBox<PublisherItem> publishersF) {
        try {
            List<Publisher> publishers = ServiceManager.getInstance().findPublishers();
            List<PublisherItem> items = publishers.stream().map(publisher -> new PublisherItem(publisher, true)).collect(Collectors.toList());
            publishersF.setModel(new SelectItemsModel<>(items));
        } catch (Exception e) {
            MainFrame.setStatusError("Нет доступа к возвещателям", e);
        }
    }


    private void initTable() {
        try {
            DefaultTableModel tableModel = new ReadOnlyTableModel(new Object[][]{}, HEADER);
            appointmentCache.clear();
            ServiceManager.getInstance().findAppointments(
                    new AppointmentsFilter(
                            DateUtilz.fromLocalDate(dpFrom.getDate()),
                            DateUtilz.fromLocalDate(dpTo.getDate()),
                            publisherFilterF.getText(),
                            districtFilterF.getText(),
                            filterOnlyActive.isSelected()
                    )
            ).forEach(p -> {
                appointmentCache.put(tableModel.getRowCount(), p);
                String period = DateUtilz.formatPeriod(p.getAssigned(), DateUtilz.today(), getLocale());
                tableModel.addRow(new Object[]{
                        p.getPublisher().getFIO(),
                        p.getDistrict().getNumber(),
                        DateUtilz.formatDate(p.getAssigned()) + " (" + period + ")",
                        p.getCompleted() != null ? DateUtilz.formatDate(p.getCompleted()) : ""
                });
            });
            table.setModel(tableModel);
            Set<Long> ids = appointmentCache.values().stream().
                    filter(a -> a.getCompleted() == null).
                    map(a -> a.getDistrict().getId()).
                    collect(Collectors.toSet());
            MyListCellRenderer renderer = new MyListCellRenderer(ids);
            districtNumbersF.setRenderer(renderer);
        } catch (Exception e) {
            MainFrame.setStatusError("Ошибка доступа к данным о назначениях", e);
        }
    }

    private void makePDF() {
        try {
            Appointment appointment = appointmentCache.get(table.getSelectedRow());
            District district = appointment.getDistrict();
            List<DistrictAddress> addresses = ServiceManager.getInstance().findDistrictAddresses(district.getId());
            if (!ListUtil.isEmpty(addresses)) {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("META-INF/print.jrxml");
                JasperDesign jd = JRXmlLoader.load(inputStream);
                JasperReport jasperReport = JasperCompileManager.compileReport(jd);
                Map p = Maps.newHashMap();
                p.put(JRParameter.REPORT_LOCALE, Locale.getDefault());
                p.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
                p.put("assigned", appointment.getAssigned());
                p.put("publisher", appointment.getPublisher().getFullFIO());
                p.put("districtNumber", district.getNumber());
                int max = addresses.size();
                Object[] header = new Object[max];
                Object[] data = new Object[max];
                for (int i = 1; i <= max; i++) {
                    header[i - 1] = i;
                    data[i - 1] = getAddressString(addresses.get(i - 1));
                }
                JRDataSource ds = new ColumnValuesDataSource(new String[]{"number", "address"}, max, new ObjectArrayValues[]{
                        new ObjectArrayValues(header),
                        new ObjectArrayValues(data)
                });
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, p, ds);
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                JFileChooser fc = new JFileChooser();
                fc.setMultiSelectionEnabled(false);
                fc.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
                int result = fc.showSaveDialog(Launcher.mainFrame);
                if (JFileChooser.APPROVE_OPTION == result) {
                    File selectedFile = fc.getSelectedFile();
                    if (!selectedFile.getName().endsWith(".pdf")) {
                        selectedFile = new File(selectedFile.getAbsolutePath() + ".pdf");
                    }
                    if (selectedFile.exists()) {
                        if (JOptionPane.showConfirmDialog(null,
                                "Перезаписать существующий файл?",
                                "Сохранение",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE) !=
                                JOptionPane.YES_OPTION) {
                            return;
                        }
                    }
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(selectedFile));
                    SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
                    exporter.setConfiguration(configuration);
                    exporter.exportReport();
                    MainFrame.setStatusText("Файл сохранен: " + selectedFile.getCanonicalPath());
                }
            } else {
                MainFrame.setStatusError("Участок не имеет адресов");
            }
        } catch (Exception e) {
            MainFrame.setStatusError("Ошибка формирования документа", e);
        }

    }

    public String getAddressString(DistrictAddress address) {
        Building building = address.getBuilding();
        return building.getCity().getName() +
                ", ул." + building.getStreet().getName() +
                ", д." + building.getNumber() +
                ", кв." + address.getNumber();
    }

    public void selectDistrict(Long id) {
        districtNumbersF.setSelectedItem(districtNumbers.get(id));
        table.getSelectionModel().clearSelection();
        appointmentCache.forEach((i, app) -> {
            if (app.getDistrict().getId().equals(id)) table.getSelectionModel().setSelectionInterval(i, i);
        });
    }

    /**
     * Раскрасит указанные ИД в красный цвет
     */
    class MyListCellRenderer extends DefaultListCellRenderer {

        private Set<Long> assignedIds;

        public MyListCellRenderer(Set<Long> assignedIds) {
            this.assignedIds = assignedIds;
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList jc, Object val, int idx,
                                                      boolean isSelected, boolean cellHasFocus) {
            setText(val.toString());
            DistrictNumbers v = (DistrictNumbers) val;
            setForeground(assignedIds.contains(v.getDistrict().getId()) ? Color.RED : Color.BLACK);
            if (isSelected)
                setBackground(Color.LIGHT_GRAY);
            else
                setBackground(Color.WHITE);
            return this;
        }
    }


}
