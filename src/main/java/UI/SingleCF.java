package UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SingleCF {
    static JFrame jf = new JFrame("居民医疗保险数据分析系统");
    static void firtPage() {
        // 1.设置窗体大小和标题
        jf.setPreferredSize(new Dimension(700, 700));
        // 2.设置关闭窗口就是关闭程序
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 最精准的布局模式空布局
        jf.setLayout(null);
        // 设置定位
        JLabel jl = new JLabel("手动添加处方", JLabel.CENTER);
        jl.setPreferredSize(new Dimension(680, 30));
        jf.add(jl);
        jl.setBounds(0, 0, 690, 30);
        jl.setFont(new Font("宋体", Font.BOLD, 24));
        jl.setForeground(Color.decode("#375a7f"));
        // 菜单栏
        // 新建一个菜单条
        JMenuBar jb = new JMenuBar();
        jf.add(jb);
        jb.setBounds(0, 40, 690, 30);
        jb.setBackground(Color.decode("#65991a"));
        // 新建一个菜单选项
        JMenu jmenu = new JMenu("首页");
        jmenu.setPreferredSize(new Dimension(100, 30));
        jmenu.setForeground(Color.white);
        jb.add(jmenu);
        JMenuItem jm0 = new JMenuItem("回到主页");
        jmenu.add(jm0);
        // 新建一个菜单项
        JMenu jmenu0 = new JMenu("处方集处理");
        jmenu0.setPreferredSize(new Dimension(100, 30));
        jmenu0.setForeground(Color.white);
        jb.add(jmenu0);

        // 新建一个菜单项
        JMenuItem jm1 = new JMenuItem("导入处方集文件");
        JMenuItem jm2 = new JMenuItem("手动添加");
        jmenu0.add(jm1);
        jmenu0.add(jm2);

        // 新建一个菜单选项
        JMenu jmenu1 = new JMenu("医疗数据审核");
        jmenu1.setForeground(Color.white);
        jmenu1.setPreferredSize(new Dimension(100, 30));
        jb.add(jmenu1);
        // 新建一个菜单项
        JMenuItem jm3 = new JMenuItem("单条数据审核");
        JMenuItem jm4 = new JMenuItem("批量数据审核");
        jmenu1.add(jm3);
        jmenu1.add(jm4);


        // 以下是显示位移的地方
        // 放置图片
        Container c= jf.getContentPane();

        JLabel cn = new JLabel("中文名：");
        c.add(cn);
        cn.setBounds(0, -200, 700, 600);

        JLabel en = new JLabel("英文名：");
        c.add(en);
        en.setBounds(0, -150, 700, 600);

        JLabel sym = new JLabel("适应症：");
        c.add(sym);
        sym.setBounds(0, -100, 700, 600);

        JLabel attention = new JLabel("注意事项：");
        c.add(attention);
        attention.setBounds(0, -50, 700, 600);

        JLabel fbd = new JLabel("禁忌症：");
        c.add(fbd);
        fbd.setBounds(0, 0, 700, 600);

        JLabel side = new JLabel("不良反应：");
        c.add(side);
        side.setBounds(0, 50, 700, 600);

        JLabel use = new JLabel("用法和用量：");
        c.add(use);
        use.setBounds(0, 100, 700, 600);

        JLabel formation = new JLabel("规格和制剂：");
        c.add(formation);
        formation.setBounds(0, 150, 700, 600);

        JTextField txt1=new JTextField(40);
        c.add(txt1);
        txt1.setBounds(100,92,200,20);

        JTextField txt2=new JTextField(40);
        c.add(txt2);
        txt2.setBounds(100,142,200,20);

        JTextField txt3=new JTextField(100);
        c.add(txt3);
        txt3.setBounds(100,192,500,20);

        JTextField txt4=new JTextField(100);
        c.add(txt4);
        txt4.setBounds(100,242,500,20);

        JTextField txt5=new JTextField(100);
        c.add(txt5);
        txt5.setBounds(100,292,500,20);

        JTextField txt6=new JTextField(100);
        c.add(txt6);
        txt6.setBounds(100,342,500,20);

        JTextField txt7=new JTextField(100);
        c.add(txt7);
        txt7.setBounds(100,392,500,20);

        JTextField txt8=new JTextField(100);
        c.add(txt8);
        txt8.setBounds(100,442,500,20);

        JButton exe=new JButton("导入");
        c.add(exe);
        exe.setBounds(500,550,100,20);

        JLabel done=new JLabel();
        c.add(done);
        done.setBounds(300,550,100,20);

        exe.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DAO.Process.symProcess(txt1.getText(),txt2.getText(),txt3.getText());
                    DAO.Process.fbdProcess(txt1.getText(),txt2.getText(),txt5.getText());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                done.setText("完成");
            }
        });


        //开始监听事件
        jm0.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //销毁当前页面
                closeThis();
                //打开一个新的页面
                new IndexPage().firtPage();
            }
        });
        jm1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //销毁当前页面
                closeThis();
                //打开一个新的页面
                new ChuFangProcess().firtPage();
            }
        });
        jm2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //销毁当前页面
                closeThis();
                //打开一个新的页面
                new SingleCF().firtPage();
            }
        });
        jm3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //销毁当前页面
                closeThis();
                //打开一个新的页面
                new SingleAudit().firtPage();
            }
        });
        jm4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //销毁当前页面
                closeThis();
                //打开一个新的页面
                new BatchAudit().firtPage();
            }
        });
        // 3.设置窗体可见
        jf.pack();
        jf.setVisible(true);
    }
    public static void closeThis(){
        jf.dispose();
    }
    public static void main(String[] args) {
        firtPage();
    }
}
