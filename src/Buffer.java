import java.awt.*;
import java.util.concurrent.Semaphore;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Buffer extends JFrame{
    public static int countreader = 0;  //手动设置读者数
    public static int countwriter = 0;  //手动设置写者数
    public static int readCountNow =0;  //等待读者数量
    public static int writeCountNow =0; //等待写者数量

    //使用Semaphore
    public static Semaphore readCountSemaphore=new Semaphore(1);    //读者数量更改，保持原子性
    public static Semaphore writeSemaphore=new Semaphore(1);        //写者信号量
    public static Semaphore reader_wait=new Semaphore(1);           //读者信号量
    public static Semaphore first_reader_wait=new Semaphore(1);     //第一个读者，为了实现写者优先
    public static Semaphore writerCountSemaphore=new Semaphore(1);  //写者数量更改，保持原子性


    //GUI
    static JTextField textfield1, textfield2, textfield3;
    JPanel panel, panel1, panel2;
    JLabel jLabel1, jLabel2;

    static JTextPane textpane;

    public Buffer(){
        JLabel Thelabel1 = new JLabel("输入读者个数：");
        JLabel Thelabel2 = new JLabel("输入写者个数：");
        JTextField j1 = new JTextField();
        JTextField j2 = new JTextField();

        textpane = new JTextPane();
        jLabel1 = new JLabel("等待读者的个数:");
        jLabel2 = new JLabel("等待写者的个数:");
        textfield1 = new JTextField(10);
        textfield2 = new JTextField(10);
        panel1 = new JPanel();
        panel1.add(jLabel1);
        panel1.add(textfield1);
        panel1.add(jLabel2);
        panel1.add(textfield2);
        JLabel label4 = new JLabel("正在等待读写者:");
        textfield3 = new JTextField(44);//显示读写者的先后顺序
        panel2 = new JPanel();
        panel2.add(label4);
        panel2.add(textfield3);

        panel = new JPanel(new BorderLayout());
        panel.add(panel1,BorderLayout.NORTH);
        panel.add(panel2,BorderLayout.SOUTH);
        panel.add(new JScrollPane(textpane),BorderLayout.CENTER);
        setTitle("问题6");//将此窗体的标题设置为指定的字符串
        setBounds(500, 300, 800, 600);
        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
    }

    public static void insert(String str)
    {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setUnderline(set, true);
        try
        {
            textpane.getDocument().insertString(textpane.getDocument().getLength(), str + "\n", set);
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }

    
}
