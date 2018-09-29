package com.del.ministry.view.forms;

import com.del.ministry.view.actions.ObservableIFrame;
import com.del.ministry.view.models.AreaItem;
import com.del.ministry.view.models.DistrictAddAddress;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.Paddings;

import javax.swing.*;
import java.awt.*;

public class GenerateAddressIForm extends ObservableIFrame {

    private JTextField maxDoorsF;
    private JList<AreaItem> areaListF;
    private JComboBox<String> maxFloorF;
    private JComboBox<String> intervalF;
    private JButton makeBtn;

    /**
     * Create the frame.
     */
    public GenerateAddressIForm() {
        super("Выбрать адрес", false, true, false, true);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        maxDoorsF = new JTextField();
        maxFloorF = new JComboBox<>();
        intervalF = new JComboBox<>();
        areaListF = new JList<>();
        areaListF.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        makeBtn = new JButton("Заполнить");

        panel.add(FormBuilder.create().
                        columns("200, 5, 80, 5, 80")
                        .rows("p, 5, p, 5, p, 5, p")
                        .padding(Paddings.DIALOG)
                        .add("Укажите районы").xy(1, 1).add("Квартир").xy(3, 1).add(maxDoorsF).xy(5, 1)
                        .add(areaListF).xywh(1, 3, 1, 5)
                        .add("Этажей").xy(3, 3).add(maxFloorF).xy(5, 3)
                        .add("Расброс").xy(3, 5).add(intervalF).xy(5, 5)
                        .add(makeBtn).xyw(3, 7, 3)
                        .build(),
                BorderLayout.CENTER);
        pack();
    }

}
