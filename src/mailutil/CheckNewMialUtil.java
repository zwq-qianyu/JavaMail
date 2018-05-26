package mailutil;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import utils.CheckNewMail;

public class CheckNewMialUtil {
    private String POP3Host = "";   // POP3服务器
    private String user = "";       // 登录POP3服务器的帐号
    private String password = "";   // 登录POP3服务器的密码

    private Session session = null;
    private Folder folder = null;
    private Store store = null;
    private GetMail getMail = GetMail.getMailInstantiate();

    public CheckNewMialUtil() {
        POP3Host = getMail.getPOP3Host();
        user = getMail.getUser();
        password = getMail.getPassword();
    }

    // 连接邮件服务器
    public void connect() {
        //final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";//ssl加密,jdk1.8无法使用	sohu邮箱不需要使用SSL

        // 定义连接imap服务器的属性信息
        //String port = "995";	//qq邮箱因为使用ssl，所以端口号用的995
        String port = "110";
        String pop3Server = POP3Host;
        String protocol = "pop3";
        String username = user;
        String pwd = password; // QQ邮箱的授权码,sohu邮箱为真实密码

        //有些参数可能不需要
        Properties props = new Properties();    //Properties 可保存在流中或从流中加载
        //props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.transport.protocol", protocol); // 使用的协议
        props.setProperty("mail.pop3.port", port);
        props.setProperty("mail.pop3.socketFactory.port", port);

        // 获取连接
        session = Session.getInstance(props);
        session.setDebug(false);

        // 获取Store对象
        try {
            store = session.getStore(protocol);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            //System.out.println(pop3Server +" " + username + " " + pwd);
            store.connect(pop3Server,110, username, pwd);
        } catch (MessagingException e) {
            e.printStackTrace();
        } // 登陆认证
    }

    // 关闭连接
    public void closeConnect() {
        try {
            if (folder != null)
                folder.close(true);// 关闭连接时是否删除邮件，true删除邮件
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            try {
                if (store != null)
                    store.close();// 关闭收件箱连接
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isCheck = true;

    // 检测新邮件
    public int checkNewMail() {
        int count = 0;
        connect();
        try {
            folder = store.getDefaultFolder().getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            count = folder.getMessageCount();
            if (isCheck) {
                CheckNewMail.setNewMailCount(count);
                isCheck = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //closeConnect();
        }
        return count;
    }
}
