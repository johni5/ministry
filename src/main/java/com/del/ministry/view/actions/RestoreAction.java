package com.del.ministry.view.actions;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.utils.StringUtil;
import com.del.ministry.view.Launcher;
import com.del.ministry.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class RestoreAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(false);
        int result = fc.showOpenDialog(Launcher.mainFrame);
        if (JFileChooser.APPROVE_OPTION == result) {
            File selectedFile = fc.getSelectedFile();
            try {
                String pwd = JOptionPane.showInputDialog(Launcher.mainFrame, "Введите пароль", "Восстановление базы данных", JOptionPane.QUESTION_MESSAGE);
                if (!StringUtil.isTrimmedEmpty(pwd)) {
                    ServiceManager.getInstance().restoreData(selectedFile.getCanonicalPath(), pwd);
                    MainFrame.setStatusText("Данные успешно восстановлены из файла '" + selectedFile.getCanonicalPath() + "'");
                    int answer = JOptionPane.showConfirmDialog(Launcher.mainFrame, "Изменения вступят в силу после перезапуска. Завершить работу программы сейчас?", "Восстановление базы данных", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (answer == JOptionPane.YES_OPTION) {
                        Launcher.mainFrame.close();
                    }
                }
            } catch (Exception e1) {
                MainFrame.setStatusError("Невозможно восстановить данные", e1);
            }
        }

    }
}
