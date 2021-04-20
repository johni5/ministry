package com.del.ministry.view.forms;

import com.del.ministry.utils.Utils;
import com.del.ministry.view.actions.ObservableIFrame;
import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.factories.Paddings;

import javax.swing.*;
import java.awt.*;

public class AboutIForm extends ObservableIFrame {

    public AboutIForm() {
        super("О программе");

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        JLabel info = new JLabel(Utils.getBundle().getString("version.info"));
        JLabel build = new JLabel(Utils.getBundle().getString("version.date"));

        Image img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/img/ico_64x64.png"));
        JLabel imgLabel = new JLabel(new ImageIcon(img));

        panel.add(FormBuilder.create().
                        columns("64, 10, p, 5, p")
                        .rows("5, p, 5, p, 30")
                        .padding(Paddings.DIALOG).add(imgLabel).xywh(1, 1, 2, 5)
                        .add("Версия:").xy(3, 2).add(info).xy(5, 2)
                        .add("Сборка:").xy(3, 4).add(build).xy(5, 4)
                        .build(),
                BorderLayout.CENTER);

        pack();

    }
}
