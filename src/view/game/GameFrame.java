package view.game;

import controller.GameController;
import model.MapModel;
import model.UserManager;
import model.UserManager.GameState;
import tool.tool;
import view.FrameUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {
    private GameController controller;
    private GamePanel gamePanel;
    private JLabel stepLabel;
    private JComboBox<String> levelSelector;
    private JButton restartBtn;
    private JButton loadBtn;
    private JButton saveBtn;
    private final String currentUser;
    private final UserManager userManager;
    private MapModel mapModel;
    private int currentSteps;
    private int[][] originalMatrix;

    // 保留无用户构造器，游客模式下不显示存档控件
    public GameFrame(int width, int height, MapModel mapModel) {
        this(width, height, mapModel, null, null, 0);
    }

    public GameFrame(int width, int height, MapModel model, String user, UserManager um, int initialSteps) {
        super("2025 CS109 Project Demo");
        this.mapModel = model;
        this.currentUser = user;
        this.userManager = um;
        this.currentSteps = initialSteps;
        initUI(width, height);
        initController();
    }

    private void initUI(int width, int height) {
        setLayout(null);
        setSize(width, height);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        // 关卡选择
        /*String[] levels = {
                "横刀立马", "指挥若定", "将拥曹营", "齐头并进", "兵分三路",
                "捷足先登", "左右布兵", "围而不坚", "插翅难飞", "守口如瓶",
                "近在咫尺", "五将逼供"
        };
        levelSelector = new JComboBox<>(levels);
        levelSelector.setBounds(20, 20, 120, 30);
        add(levelSelector);
        levelSelector.addActionListener(e -> loadLevelByName((String) levelSelector.getSelectedItem()));*/

        // 游戏面板
        gamePanel = new GamePanel(mapModel);
        gamePanel.setLocation(tool.GRID_SIZE, tool.GRID_SIZE);
        gamePanel.setStepCount(currentSteps);
        add(gamePanel);

        // 步数标签
        stepLabel = FrameUtil.createJLabel(this,
                "Step: " + currentSteps,
                new Font("serif", Font.ITALIC, 22),
                new Point(gamePanel.getPanelWidth() + 80, 70),
                180, 50);
        gamePanel.setStepLabel(stepLabel);

        // 按钮：Restart
        restartBtn = FrameUtil.createButton(this,
                "Restart",
                new Point(gamePanel.getPanelWidth() + 80, 120),
                150, 50);
        /*restartBtn.addActionListener(e -> {
            mapModel.setMatrix(originalMatrix);
            totallyReset();
            gamePanel.requestFocusInWindow();
        });*/
        restartBtn.addActionListener(e -> {
            new LevelSelectionFrame(this).setVisible(true);
            // 对话框关闭后，GameFrame会自动根据选择加载和重置
        });
        add(restartBtn);

        // 仅登录用户模式下，显示 Load/Save
        if (userManager != null && currentUser != null) {
            loadBtn = FrameUtil.createButton(this,
                    "Load",
                    new Point(gamePanel.getPanelWidth() + 80, 190),
                    150, 50);
            loadBtn.addActionListener(e -> {
                GameState gs = userManager.loadState(currentUser);
                if (gs != null && gs.matrix != null) {
                    mapModel.setMatrix(gs.matrix);
                    resetCurrentLevel();
                    currentSteps = gs.steps;
                    updateStepLabel();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No saved state found.",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }
                gamePanel.requestFocusInWindow();
            });
            add(loadBtn);

            saveBtn = FrameUtil.createButton(this,
                    "Save",
                    new Point(gamePanel.getPanelWidth() + 80, 260),
                    150, 50);
            saveBtn.addActionListener(e -> {
                userManager.saveState(currentUser,
                        gamePanel.getSteps(),
                        mapModel.getMatrix());
                JOptionPane.showMessageDialog(this,
                        "Game saved!",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                gamePanel.requestFocusInWindow();
            });
            add(saveBtn);
        }

        // 自动保存（仅登录）
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (userManager != null && currentUser != null) {
                    userManager.saveState(currentUser,
                            gamePanel.getSteps(),
                            mapModel.getMatrix());
                }
                super.windowClosing(e);
            }
        });

        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initController() {
        controller = new GameController(gamePanel, mapModel);
        gamePanel.setController(controller);
    }

    void loadLevelByName(String name) {
        int[][] matrix;
        switch (name) {
            case "横刀立马": matrix = tool.hengdaolima_1; break;
            case "指挥若定": matrix = tool.zhihuiruoding_1; break;
            case "将拥曹营": matrix = tool.jiangyongcaoying_1; break;
            case "齐头并进": matrix = tool.qitoubingjin_1; break;
            case "兵分三路": matrix = tool.bingfensanlu_1; break;
            case "捷足先登": matrix = tool.jiezuxiandeng_1; break;
            case "左右布兵": matrix = tool.zuoyoububing_1; break;
            case "围而不坚": matrix = tool.weierbujian_1; break;
            case "插翅难飞": matrix = tool.chachinanfei_2; break;
            case "守口如瓶": matrix = tool.shoukouruping_2; break;
            case "近在咫尺": matrix = tool.jinzaizhichi_2; break;
            case "五将逼供": matrix = tool.wujiangbigong_3; break;
            default: matrix = tool.hengdaolima_1;
        }
        mapModel.setMatrix(matrix);
        currentSteps = 0;
        totallyReset();
        updateStepLabel();
        originalMatrix=matrix;
    }

    private void resetCurrentLevel() {
        controller.restartGame();
    }
    private void totallyReset(){
        controller.totallyRestart();
    }

    private void updateStepLabel() {
        stepLabel.setText("Step: " + currentSteps);
        gamePanel.setStepCount(currentSteps);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
