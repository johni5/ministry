package com.del.ministry.view.forms;

import com.del.ministry.utils.Utils;
import com.del.ministry.view.actions.ObservableIFrame;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.Paddings;

import javax.swing.*;
import java.awt.*;

public class AboutIForm extends ObservableIFrame {

    public AboutIForm() {
        super("О программе", false, true, false, false);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        JLabel info = new JLabel(Utils.getBundle().getString("version.info"));
        JLabel build = new JLabel(Utils.getBundle().getString("version.date"));

        panel.add(FormBuilder.create().
                        columns("p, 5, p")
                        .rows("p, 5, p")
                        .padding(Paddings.DIALOG)
                        .add("Версия:").xy(1, 1).add(info).xy(3, 1)
                        .add("Сборка:").xy(1, 3).add(build).xy(3, 3)
                        .build(),
                BorderLayout.CENTER);

        pack();

    }
}
