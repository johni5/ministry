package com.del.ministry.view;

import com.del.ministry.dao.ServiceManager;
import com.del.ministry.utils.Utils;
import com.del.ministry.view.actions.BackupAction;
import com.del.ministry.view.actions.MainFrameActions;
import com.del.ministry.view.actions.RestoreAction;
import com.del.ministry.view.actions.ShowIFrameActionListener;
import com.del.ministry.view.forms.*;
import com.del.ministry.view.models.tree.pub.DistrictNode;
import com.del.ministry.view.models.tree.stat.BuildingNode;
import com.del.ministry.view.models.tree.stat.StreetNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFrame extends JFrame {

    private JScrollPane mainPane;
    private JMenu menuTerritory;
    private JMenu menuMinistry;
    private JTree leftSideTree;
    private JTree leftSideStat;

    private JTextPane logArea;
    private JScrollPane logScroll;

    private MainForms mainForms;

    /**
     * Create the frame.
     */
    public MainFrame() {
        setTitle("Система учета территории");
        setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/img/ico_32x32.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1024, 800);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mainPane = new JScrollPane();
        mainForms = new MainForms(mainPane, this);

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
        menuItem_2.addActionListener(e -> mainForms.show(AreaIForm.class));
        menuItem_1.addActionListener(e -> mainForms.show(StreetIForm.class));
        menuItem_3.addActionListener(e -> mainForms.show(CityIForm.class));
        menuItem_4.addActionListener(e -> mainForms.show(BuildingIForm.class));
        menuItem_5.addActionListener(e -> mainForms.show(AddressTypeIForm.class));
        menuItem_6.addActionListener(e -> mainForms.show(BuildingTypeIForm.class));

        menuMinistry = new JMenu("Служение");
        JMenuItem menuItem_2_1 = new JMenuItem("Участки");
        JMenuItem menuItemPublishers = new JMenuItem("Возвещатели");
        JMenuItem menuItemAppointments = new JMenuItem("Назначения участков");
        menuMinistry.add(menuItem_2_1);
        menuMinistry.add(menuItemPublishers);
        menuMinistry.add(menuItemAppointments);
        menuItem_2_1.addActionListener(e -> mainForms.show(DistrictListIForm.class));
        menuItemPublishers.addActionListener(e -> mainForms.show(PublishersIForm.class));

        menuItemAppointments.addActionListener(e -> mainForms.show(AppointmentsIForm.class));

        JMenu menuService = new JMenu("Сервис");
        JMenuItem menuItemRestore = new JMenuItem("Восстановление");
        JMenuItem menuItemBackup = new JMenuItem("Резервное копирование");
        menuItemBackup.addActionListener(new BackupAction(this));
        menuItemRestore.addActionListener(new RestoreAction(this));
        menuService.add(menuItemBackup);
        menuService.add(menuItemRestore);

        JMenu menuHelp = new JMenu("Помощь");
        JMenuItem menuItemAbout = new JMenuItem("О программе");
        menuHelp.add(menuItemAbout);
        menuItemAbout.addActionListener(new ShowIFrameActionListener<>(AboutIForm.class, this));

        menuBar.add(menu);
        menuBar.add(menuTerritory);
        menuBar.add(menuMinistry);
        menuBar.add(menuService);
        menuBar.add(menuHelp);

        logArea = new JTextPane();
        logArea.setContentType("text/html");
        logArea.setBackground(Color.DARK_GRAY);
        logArea.setEditable(false);
        logArea.setPreferredSize(new Dimension(logArea.getWidth(), 100));

        logScroll = new JScrollPane();
        logScroll.setViewportView(logArea);

        JPanel leftSide = new JPanel(new BorderLayout());

        leftSideTree = new JTree();
        leftSideStat = new JTree();

        JScrollPane sp1 = new JScrollPane();
        JScrollPane sp2 = new JScrollPane();
        sp1.setViewportView(leftSideTree);
        sp2.setViewportView(leftSideStat);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Участки", sp1);
        tabbedPane.addTab("Адреса", sp2);
        leftSide.add(tabbedPane);

        JSplitPane p1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSide, mainPane);
        p1.setBorder(null);
        JSplitPane p2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, p1, logScroll);
        p2.setResizeWeight(1);
        getContentPane().add(p2, BorderLayout.CENTER);

        addWindowListener(new MainFrameActions(this));

        initLeftSideTree();
    }

    public void initLeftSideTree() {
        try {
            DefaultTreeModel model1 = new DefaultTreeModel(ServiceManager.getInstance().getDistrictTree());
            DefaultTreeModel model2 = new DefaultTreeModel(ServiceManager.getInstance().getBuildingTree());
            leftSideTree.setModel(model1);
            leftSideStat.setModel(model2);
            expandAllNodes(leftSideStat, 0, leftSideStat.getRowCount());
            expandAllNodes(leftSideTree, 0, leftSideTree.getRowCount());
            leftSideTree.addMouseListener(new MouseAdapter() {
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
                                mainForms.show(AppointmentsIForm.class).selectDistrict(node.getDistrictId());
                            }
                        }
                    }
                }
            });
            leftSideStat.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    int selRow = leftSideStat.getRowForLocation(e.getX(), e.getY());
                    TreePath selPath = leftSideStat.getPathForLocation(e.getX(), e.getY());
                    if (selRow != -1) {
                        if (e.getClickCount() == 1) {
//                            mySingleClick(selRow, selPath);
                        } else if (e.getClickCount() == 2) {
//                            myDoubleClick(selRow, selPath);
                            if (selPath != null && selPath.getLastPathComponent() instanceof BuildingNode) {
                                BuildingNode node = (BuildingNode) selPath.getLastPathComponent();
                                mainForms.show(BuildingIForm.class).setSelectedRow(node.getBuilding().getId());
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            setStatusError("Ошибка построения дерева участков", e);
        }
    }

    private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
        for (int i = startingIndex; i < rowCount; ++i) {
            if (!(tree.getPathForRow(i).getLastPathComponent() instanceof StreetNode)) {
                tree.expandRow(i);
            }
        }
        if (tree.getRowCount() != rowCount) {
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    public void setStatusDB_ERROR() {
        menuTerritory.setEnabled(false);
        menuMinistry.setEnabled(false);
        setStatusText("Нет соединения с базой данных");
    }

    public void setStatusText(String message) {
        log(message, "white");
    }

    public void setStatusError(String message, Exception e) {
        log(message, "red");
        Utils.getLogger().error(e.getMessage(), e);
    }

    public void setStatusError(String message) {
        log(message, "red");
    }

    private void log(String message, String color) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        logArea.setText("<font color=\"" + color + "\" size=\"3\" face=\"Tahoma\">" +
                sdf.format(new Date()) + ": " +
                message +
                "</font><br/>" + logArea.getText());
        logArea.setCaretPosition(0);
    }

    public void close() {
        dispatchEvent(new WindowEvent(MainFrame.this, WindowEvent.WINDOW_CLOSING));
    }

}
