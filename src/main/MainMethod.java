package main;

import javax.swing.UIManager;

import frame.LoginFrame;

public class MainMethod {

    /**
     * 主程序的入口
     */
    public static void main(String[] args) {
        // 设置界面为本地模式
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
                // new MainFrame().setVisible(true);
            }
        });

    }

}
