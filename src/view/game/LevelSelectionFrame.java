package view.game;

import tool.tool;
import view.FrameUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LevelSelectionFrame extends JDialog {
    private final GameFrame parent;
    private JComboBox<String> levelCombo;
    private JButton confirmBtn;

    public LevelSelectionFrame(GameFrame parent) {
        super(parent, "选择关卡", true);
        this.parent = parent;
        initUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // todo替换为自定义背景面板或贴图
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("请选择关卡："));
        String[] levels = {
                "横刀立马", "指挥若定", "将拥曹营", "齐头并进", "兵分三路",
                "捷足先登", "左右布兵", "围而不坚", "插翅难飞", "守口如瓶",
                "近在咫尺", "五将逼供"
        };
        levelCombo = new JComboBox<>(levels);
        levelCombo.setPreferredSize(new Dimension(200, 30));
        topPanel.add(levelCombo);
        add(topPanel, BorderLayout.CENTER);

        confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) levelCombo.getSelectedItem();
                parent.loadLevelByName(selected);
                dispose();
            }
        });
        JPanel btnPanel = new JPanel();
        btnPanel.add(confirmBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }
}

