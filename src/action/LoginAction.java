/**
 * 用于验证登陆是否成功
 */
package action;

import mailutil.GetMail;
import mailutil.SendAttachMail;
import utils.EditorUtils;

public class LoginAction {
    private String POP3Host = ""; // POP3服务器
    private String SMTPHost = ""; // SMTP服务器
    private String user = ""; // 登录服务器的帐号
    private String password = ""; // 登录服务器的密码
    private GetMail getMail = null;
    private SendAttachMail sendMail = null;

    // 构造函数
    public LoginAction(String sMTPHost, String pOP3Host, String user, String password) {
        super();
        POP3Host = pOP3Host;
        SMTPHost = sMTPHost;
        this.user = user;
        this.password = password;
        // 实例化收邮件对象
        getMail = GetMail.getMailInstantiate();
        getMail.setPOP3Host(POP3Host);
        getMail.setUser(user);
        getMail.setPassword(password);
        // 实例化发邮件件对象
        sendMail = SendAttachMail.getSendMailInstantiate();
        sendMail.setSMTPHost(SMTPHost);
        sendMail.setUser(user);
        sendMail.setPassword(password);
    }

    // 判断登陆是否成功
    public boolean isLogin() {

        boolean isLogin = false;
        // 判断用户名是否为空
        if (checkUser()) {
            try {
                sendMail.connect();// 连接发件服务器（只能通过连接发件箱验证身份，否者发件时会抛异常）
                isLogin = true;
            } catch (Exception e) {
                isLogin = false;
                e.printStackTrace();
            }
        }
        return isLogin;
    }

    // 验证用户输入数据的有效性
    public boolean checkUser() {
        boolean check = false;
        boolean checkSMTP = SMTPHost.toLowerCase().startsWith("smtp");// 验证smtp服务器
        boolean checkPOP = POP3Host.toLowerCase().startsWith("pop");// 验证pop服务器
        //boolean checkPOP = true;
        boolean checkPassword = (password.length() >= 1);
        boolean checkUser = EditorUtils.checkEmailAdress(user);// 验证邮箱的有效性（正则）
		/*System.out.println("checkSMTP=" + checkSMTP);
		System.out.println("checkPOP=" + checkPOP);
		System.out.println("checkPassword=" + checkPassword);
		System.out.println("checkUser=" + checkUser);*/
        if (checkSMTP && checkPOP && checkPassword && checkUser) {
            check = true;// 验证通过
        }
        //System.out.println("check=" + check);
        return check;
    }
}
