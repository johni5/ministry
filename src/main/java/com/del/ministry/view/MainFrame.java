package com.del.ministry.view;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.utils.Utils;
import com.del.ministry.view.actions.BackupAction;
import com.del.ministry.view.actions.MainFrameActions;
import com.del.ministry.view.actions.RestoreAction;
import com.del.ministry.view.actions.ShowIFrameActionListener;
import com.del.ministry.view.forms.*;
import com.del.ministry.view.models.tree.DistrictNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    public final JDesktopPane desktop = new JDesktopPane();

    private JMenu menuTerritory;
    private JMenu menuMinistry;
    private JTree leftSideTree;
    private ShowIFrameActionListener<AppointmentsIForm> appointmentsActionListener;

    private final static JLabel STATUS_TEXT = new JLabel();

    /**
     * Create the frame.
     */
    public MainFrame() {
        setTitle("Система учета территории");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("Файл");

        JMenuItem menuItem = new JMenuItem("Выход");
        menuItem.addActionListener(arg0 -> {
            if (JOptionPane.showConfirmDialog(MainFrame.this, "Вы действительно хотите выйти?", "Завершение работы",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                close();
            }
        });
        menu.add(menuItem);

        menuTerritory = new JMenu("Территория");
        JMenuItem menuItem_2 = new JMenuItem("Район");
        JMenuItem menuItem_1 = new JMenuItem("Улица");
        JMenuItem menuItem_3 = new JMenuItem("Населенный пункт");
        JMenuItem menuItem_4 = new JMenuItem("Дом");
        JMenuItem menuItem_5 = new JMenuItem("Тип адреса");
        JMenuItem menuItem_6 = new JMenuItem("Тип дома");
        menuTerritory.add(menuItem_2);
        menuTerritory.add(menuItem_1);
        menuTerritory.add(menuItem_3);
        menuTerritory.add(menuItem_4);
        menuTerritory.add(new JSeparator());
        menuTerritory.add(menuItem_5);
        menuTerritory.add(menuItem_6);
        menuItem_2.addActionListener(new ShowIFrameActionListener<>(AreaIForm.class));
        menuItem_1.addActionListener(new ShowIFrameActionListener<>(StreetIForm.class));
        menuItem_3.addActionListener(new ShowIFrameActionListener<>(CityIForm.class));
        menuItem_4.addActionListener(new ShowIFrameActionListener<>(BuildingIForm.class));
        menuItem_5.addActionListener(new ShowIFrameActionListener<>(AddressTypeIForm.class));
        menuItem_6.addActionListener(new ShowIFrameActionListener<>(BuildingTypeIForm.class));

        menuMinistry = new JMenu("Служение");
        JMenuItem menuItem_2_1 = new JMenuItem("Участки");
        JMenuItem menuItemPublishers = new JMenuItem("Возвещатели");
        JMenuItem menuItemAppointments = new JMenuItem("Назначения участков");
        menuMinistry.add(menuItem_2_1);
        menuMinistry.add(menuItemPublishers);
        menuMinistry.add(menuItemAppointments);
        menuItem_2_1.addActionListener(new ShowIFrameActionListener<>(DistrictListIForm.class));
        menuItemPublishers.addActionListener(new ShowIFrameActionListener<>(PublishersIForm.class));

        appointmentsActionListener = new ShowIFrameActionListener<>(AppointmentsIForm.class);
        menuItemAppointments.addActionListener(appointmentsActionListener);

        JMenu menuService = new JMenu("Сервис");
        JMenuItem menuItemRestore = new JMenuItem("Восстановление");
        JMenuItem menuItemBackup = new JMenuItem("Резервное копирование");
        menuItemBackup.addActionListener(new BackupAction());
        menuItemRestore.addActionListener(new RestoreAction());
        menuService.add(menuItemBackup);
        menuService.add(menuItemRestore);

        JMenu menuHelp = new JMenu("Помощь");
        JMenuItem menuItemAbout = new JMenuItem("О программе");
        menuHelp.add(menuItemAbout);
        menuItemAbout.addActionListener(new ShowIFrameActionListener<>(AboutIForm.class));

        menuBar.add(menu);
        menuBar.add(menuTerritory);
        menuBar.add(menuMinistry);
        menuBar.add(menuService);
        menuBar.add(menuHelp);

        JPanel statusBar = new JPanel(new BorderLayout(0, 0));
        STATUS_TEXT.setFont(new Font("Tahoma", Font.PLAIN, 12));
        STATUS_TEXT.setPreferredSize(new Dimension(100, 20));
        statusBar.add(STATUS_TEXT);

        desktop.setBackground(Color.LIGHT_GRAY);
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        JPanel leftSide = new JPanel(new BorderLayout());

        getContentPane().add(statusBar, BorderLayout.SOUTH);
        leftSideTree = new JTree();
        JScrollPane sp = new JScrollPane();
        sp.setViewportView(leftSideTree);
        leftSide.add(sp);


        JSplitPane p = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSide, desktop);
        getContentPane().add(p, BorderLayout.CENTER);

        addWindowListener(new MainFrameActions(this));

        initLeftSideTree();
    }

    public void initLeftSideTree() {
        try {
            DefaultTreeModel model = new DefaultTreeModel(ServiceManager.getInstance().getDistrictTree());
            leftSideTree.setModel(model);
            expandAllNodes(leftSideTree, 0, leftSideTree.getRowCount());
            MouseListener ml = new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    int selRow = leftSideTree.getRowForLocation(e.getX(), e.getY());
                    TreePath selPath = leftSideTree.getPathForLocation(e.getX(), e.getY());
                    if (selRow != -1) {
                        if (e.getClickCount() == 1) {
//                            mySingleClick(selRow, selPath);
                        } else if (e.getClickCount() == 2) {
//                            myDoubleClick(selRow, selPath);
                            if (selPath != null && selPath.getLastPathComponent() instanceof DistrictNode) {
                                DistrictNode node = (DistrictNode) selPath.getLastPathComponent();
                                appointmentsActionListener.safeOpen();
                                appointmentsActionListener.getInstance().selectDistrict(node.getDistrictId());
                            }
                        }
                    }
                }
            };
            leftSideTree.addMouseListener(ml);
        } catch (Exception e) {
            setStatusError("Ошибка построения дерева участков", e);
        }
    }

    private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
        for (int i = startingIndex; i < rowCount; ++i) {
            tree.expandRow(i);
        }
        if (tree.getRowCount() != rowCount) {
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    public void setStatusDB_ERROR() {
        menuTerritory.setEnabled(false);
        setStatusText("Нет соединения с базой данных");
    }

    public void setStatusDB_OK() {
        setStatusText("Соединение с базой данных установлено");
    }

    public static void setStatusText(String message) {
        STATUS_TEXT.setText(message);
        STATUS_TEXT.setForeground(Color.BLUE);
    }

    public static void setStatusError(String message, Exception e) {
        STATUS_TEXT.setText(message);
        STATUS_TEXT.setForeground(Color.RED);
        Utils.getLogger().error(e.getMessage(), e);
    }

    public static void setStatusError(String message) {
        STATUS_TEXT.setText(message);
        STATUS_TEXT.setForeground(Color.RED);
    }

    public void close() {
        dispatchEvent(new WindowEvent(MainFrame.this, WindowEvent.WINDOW_CLOSING));
    }

}
