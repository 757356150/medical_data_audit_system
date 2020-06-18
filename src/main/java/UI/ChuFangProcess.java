package UI;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import javax.swing.*;

public class ChuFangProcess {
    static JFrame jf = new JFrame("居民医疗保险数据分析系统");
    static void firtPage() {
        // 1.设置窗体大小和标题
        jf.setPreferredSize(new Dimension(700, 700));
        // 2.设置关闭窗口就是关闭程序
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 最精准的布局模式空布局
        jf.setLayout(null);
        // 设置定位
        JLabel jl = new JLabel("《处方集》导入", JLabel.CENTER);
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


        Container c= jf.getContentPane();
        JButton exe=new JButton("打开文件");
        c.add(exe);
        exe.setBounds(300,150,100,20);
        exe.setBackground(Color.GREEN);
        //jb.setBackground(Color.GREEN);//设置按钮颜色
        JButton process=new JButton("进行处理");
        c.add(process);
        process.setBounds(300, 200, 100, 20);
        process.setBackground(Color.GREEN);

        JLabel choose = new JLabel();
        c.add(choose);
        choose.setBounds(30, 80, 670, 200);


        exe.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser jf = new JFileChooser();
                    jf.showOpenDialog(null);//显示打开的文件对话框
                    File f =  jf.getSelectedFile();//使用文件类获取选择器选择的文件
                    String s = f.getAbsolutePath();//返回路径名
                    choose.setText("文件选择成功，路径为："+s);
                    System.out.println(s);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

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
