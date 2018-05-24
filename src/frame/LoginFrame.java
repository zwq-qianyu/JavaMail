package frame;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import utils.CheckNewMail;
import utils.EditorUtils;
import action.LoginAction;

/**
 * 登录页面
 */
public class LoginFrame extends JFrame implements ActionListener, ItemListener {

    private static final long serialVersionUID = 1L;
    private JComboBox pop3CB;   //收邮件服务器下拉列表
    private JComboBox smtpCB;   //发邮件服务器下拉列表
    private JTextField nameTF;
    private JPasswordField passwordTF;
    private JButton loginButton = null, resetButton = null;
    private String username = null, password = null, popHost = null,
            smtpHost = null;
    private JProgressBarFrame progressBar = null;   //进度条实例

    public LoginFrame() {
        super();
        this.setIconImage(EditorUtils.createIcon("email.png").getImage());
        //getContentPane().setLayout(null);
        jFrameValidate();
        setTitle("浅鱼邮箱");
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 768, 540);
        backgroundLabel.setText("<html><img width=776 height=574 src='"
                + this.getClass().getResource("/loginBg.jpg") + "'></html>");
        backgroundLabel.setLayout(null);

        final JLabel smtpLable = new JLabel();
        smtpLable.setText("SMTP 服务器：");
        smtpLable.setBounds(230, 203, 100, 18);
        backgroundLabel.add(smtpLable);

        final JLabel pop3Label = new JLabel();
        pop3Label.setText("POP3 服务器：");
        pop3Label.setBounds(230, 243, 100, 18);
        backgroundLabel.add(pop3Label);

        final JLabel nameLabel = new JLabel();
        nameLabel.setText("邮箱名称：");
        nameLabel.setBounds(230, 283, 100, 18);
        backgroundLabel.add(nameLabel);

        final JLabel passwordLable = new JLabel();
        passwordLable.setText("密码：");
        passwordLable.setBounds(230, 323, 100, 18);
        backgroundLabel.add(passwordLable);

        // 发件箱服务器地址列表
        String[] smtpAdd = {"smtp.sohu.com","smtp.qq.com"};
        smtpCB = new JComboBox(smtpAdd);
        smtpCB.setSelectedIndex(0);
        smtpCB.setEditable(true);
        smtpCB.addItemListener(this);
        smtpCB.setBounds(370, 203, 150, 22);
        backgroundLabel.add(smtpCB);

        // 收件箱服务器地址列表
        String[] pop3Add = {"pop3.sohu.com","pop.qq.com"};
        pop3CB = new JComboBox(pop3Add);
        pop3CB.setSelectedIndex(0);
        pop3CB.addItemListener(this);
        pop3CB.setEditable(true);
        pop3CB.setBounds(370, 243, 150, 22);
        backgroundLabel.add(pop3CB);

        nameTF = new JTextField();
        nameTF.setBounds(370, 283, 150, 22);
        backgroundLabel.add(nameTF);

        passwordTF = new JPasswordField();
        passwordTF.setBounds(370, 323, 150, 22);
        backgroundLabel.add(passwordTF);

        loginButton = new JButton("登录");
        resetButton = new JButton("重置");
        loginButton.setBounds(280, 360, 80, 30);
        resetButton.setBounds(400, 360, 80, 30);
        backgroundLabel.add(loginButton);
        backgroundLabel.add(resetButton);
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        getContentPane().add(backgroundLabel);  //Container javax.swing.JFrame.getContentPane() 返回一个container对象

        progressBar = new JProgressBarFrame(this, "登录", "登录中，请稍后...");
        reset();    //设置默认初始值
    }

    public void jFrameValidate() {
        //初始化界面
        Toolkit tk = getToolkit();  //获得屏幕的宽和高
        Dimension dim = tk.getScreenSize();
        this.setResizable(false);
        this.setBounds(dim.width / 2 - 380, dim.height / 2 - 270, 776, 574);
        //validate();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // 处理登录和重置事件
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) { //登录
            progressBar.setVisible(true);   //设置进度条可见
            new Thread() {
                public void run() {
                    getValues();    //得到界面中的所有项的值
                    //System.out.println(smtpHost+"   "+popHost +"   " +username+"   "+password);
                    checkUser();    //登录验证
                }
            }.start();
        } else if (e.getSource() == resetButton) {  //重置
            reset();    //重新设置各项的值
        }
    }

    // 得到界面中的所有项的值
    private void getValues() {
        smtpHost = (String) smtpCB.getSelectedItem();
        popHost = (String) pop3CB.getSelectedItem();
        username = nameTF.getText().trim();
        password = passwordTF.getText().trim();
    }

    // 重新设置各项的值
    private void reset() {
        smtpCB.setSelectedIndex(0);
        pop3CB.setSelectedIndex(0);
        nameTF.setText("zwq_test@sohu.com");
        passwordTF.setText("123456zwq");
    }

    // 登录验证
    private void checkUser() {
        System.out.println(smtpHost+"   "+popHost +"   " +username+"   "+password);
        LoginAction login = new LoginAction(smtpHost, popHost, username, password);
        if (login.isLogin()) {  //登录成功
            progressBar.dispose();
            new CheckNewMail().start(); //开始检测新邮件
            this.dispose();     //释放本窗口资源
            new MainFrame().setVisible(true);
        } else {                //登录失败
            progressBar.setVisible(false);
            JOptionPane.showMessageDialog(this, "<html><h4>"
                            + "登录失败，请检查主机、用户名、密码是否正确！" + "<html><h4>", "警告",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // 下拉列表改变时的事件处理
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == smtpCB) {
            if (e.getStateChange() == ItemEvent.SELECTED
                    && smtpCB.getSelectedIndex() != -1)
                smtpCB.setSelectedIndex(smtpCB.getSelectedIndex());
        } else if (e.getSource() == pop3CB) {
            if (e.getStateChange() == ItemEvent.SELECTED
                    && pop3CB.getSelectedIndex() != -1)
                pop3CB.setSelectedIndex(pop3CB.getSelectedIndex());
        }

    }
}

