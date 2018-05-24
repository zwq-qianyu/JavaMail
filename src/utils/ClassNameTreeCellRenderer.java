package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

// 树形节点的实现
/**
 * 重新渲染树形节点
 */
public class ClassNameTreeCellRenderer extends DefaultTreeCellRenderer {
    private static final long serialVersionUID = 1L;
    Font f = UIManager.getFont("Label.font");
    Font bf = new Font(f.getName(), Font.BOLD, f.getSize());
    Font bif = new Font(f.getName(), Font.BOLD | Font.ITALIC, f.getSize());

    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded,
                leaf, row, hasFocus);
        if (row == 0) {
            this.setIcon(EditorUtils.createIcon("home.gif"));
            this.setFont(bf);
        } else if (value.toString().equals("收件夹")) {
            this.setIcon(EditorUtils.createIcon("receive.png"));
            this.setFont(bif);
        } else if (value.toString().equals("发件夹")) {
            this.setIcon(EditorUtils.createIcon("send.png"));
            this.setFont(bif);
        } else if (value.toString().equals("已发送邮件")) {
            this.setIcon(EditorUtils.createIcon("sended.png"));
            this.setFont(bif);
        } else if (value.toString().equals("已删除邮件")) {
            this.setIcon(EditorUtils.createIcon("deleted.png"));
            this.setFont(bif);
        } else {
            this.setIcon(EditorUtils.createIcon("send6.gif"));
            this.setFont(bif);
        }
        if (!sel) {
            if (row == 0) {
                this.setForeground(Color.red);
            }
        }
        return this;
    }
}
