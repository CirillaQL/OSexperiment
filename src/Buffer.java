import java.awt.*;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Buffer extends Thread{
    public int countreader = 0;  //手动设置读者数
    public int countwriter = 0;  //手动设置写者数
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

    public Buffer(int A, int B){
        this.countreader = A;
        this.countwriter = B;
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

    public void run(){
        JFrame MainWindow = new JFrame("显示界面");
        textpane = new JTextPane();
        jLabel1 = new JLabel("等待读者的个数:");
        jLabel2 = new JLabel("等待写者的个数:");
        textfield1 = new JTextField(10);
        textfield2 = new JTextField(10);
        panel1 = new JPanel();
        //panel1.add(jLabel1);
        //panel1.add(textfield1);
        //panel1.add(jLabel2);
        //panel1.add(textfield2);
        JLabel label4 = new JLabel("读写者进入时队列:");
        textfield3 = new JTextField(44);//显示读写者的先后顺序
        panel2 = new JPanel();
        panel2.add(label4);
        panel2.add(textfield3);

        panel = new JPanel(new BorderLayout());
        panel.add(panel1,BorderLayout.NORTH);
        panel.add(panel2,BorderLayout.SOUTH);
        panel.add(new JScrollPane(textpane),BorderLayout.CENTER);

        MainWindow.setBounds(500, 300, 800, 600);
        MainWindow.add(panel);
        MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainWindow.setVisible(true);
        MainWindow.setResizable(false);



        int i = 1;
        int j = 1;

        while(i<= Buffer.countreader ||j<= Buffer.countwriter){
            if(new Random().nextBoolean()){         //随机判断读写者
                if(i> Buffer.countreader)
                    continue;
                Buffer.insert("读者R"+i+"来了");
                textfield3.setText(textfield3.getText()+"R"+i+",");
                new ReadThread(i);
                i++;
            }
            else{
                if(j> Buffer.countwriter)
                    continue;
                Buffer.insert("写者W"+j+"来了");
                textfield3.setText(textfield3.getText()+"W"+j+",");
                new WriteThread(j);
                j++;
            }
        }
    }
    
}
