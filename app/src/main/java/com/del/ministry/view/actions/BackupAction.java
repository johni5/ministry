package com.del.ministry.view.actions;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.utils.StringUtil;
import com.del.ministry.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class BackupAction implements ActionListener {

    private MainFrame mainFrame;

    public BackupAction(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(false);
        int result = fc.showSaveDialog(mainFrame);
        if (JFileChooser.APPROVE_OPTION == result) {
            File selectedFile = fc.getSelectedFile();
            try {
                String pwd = JOptionPane.showInputDialog(mainFrame, "Задайте пароль", "Резервная копия базы данных", JOptionPane.QUESTION_MESSAGE);
                if (!StringUtil.isTrimmedEmpty(pwd)) {
                    ServiceManager.getInstance().backupData(selectedFile.getCanonicalPath(), pwd);
                    mainFrame.setStatusText("Данные успешно сохранены в файл '" + selectedFile.getCanonicalPath() + "'");
                }
            } catch (Exception e1) {
                mainFrame.setStatusError("Невозможно сохранить данные", e1);
            }
        }
    }
}
