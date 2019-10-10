import java.awt.BorderLayout;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class ReadAndWrite extends JFrame
{
    public static int countReader;  //手动设置读者数
    public static int countWriter;  //手动设置写者数
    public static int readCount=0;  //等待读者数量
    public static int writeCount=0; //等待写者数量
    //使用Java库中定义好的Semaphore
    public static Semaphore readCountSemaphore=new Semaphore(1);    //读者数量更改，保持原子性
    public static Semaphore writeSemaphore=new Semaphore(1);        //写者信号量
    public static Semaphore reader_wait=new Semaphore(1);           //读者信号量
    public static Semaphore first_reader_wait=new Semaphore(1);     //第一个读者，为了实现写者优先
    public static Semaphore writerCountSemaphore=new Semaphore(1);  //写者数量更改，保持原子性

    //GUI
    static JTextField textfield1, textfield2, textfield3,textfield4;
    JPanel panel, panel1, panel2;
    JLabel label1, label2, label3;
    static JTextPane textpane;
    public ReadAndWrite(){
        Scanner sc = new Scanner(System.in);//用来输入读写者个数
        System.out.println("读者个数:");
        countReader = sc.nextInt();
        System.out.println("写者个数:");
        countWriter = sc.nextInt();

        textpane = new JTextPane();//动态执行界面
        label1 = new JLabel("书:");
        label2 = new JLabel("等待读者个数:");
        label3 = new JLabel("等待写者个数:");
        textfield1 = new JTextField(10);//显示谁正在对数据操作
        textfield2 = new JTextField(10);//显示剩余读者个数
        textfield3 = new JTextField(10);//显示剩余写者个数
        panel1 = new JPanel();
        panel1.add(label1);
        panel1.add(textfield1);
        panel1.add(label2);
        panel1.add(textfield2);
        panel1.add(label3);
        panel1.add(textfield3);

        JLabel label4 = new JLabel("正在等待读写者:");
        textfield4 = new JTextField(44);//显示读写者的先后顺序
        panel2 = new JPanel();
        panel2.add(label4);
        panel2.add(textfield4);

        panel = new JPanel(new BorderLayout());
        panel.add(panel1,BorderLayout.NORTH);
        panel.add(panel2,BorderLayout.SOUTH);
        panel.add(new JScrollPane(textpane),BorderLayout.CENTER);
        setTitle("读写者问题");//将此窗体的标题设置为指定的字符串
        setBounds(300, 300, 800, 600);
        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

    }

    public static void insert(String str)//向textpane中插入动态信息
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


    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        ReadAndWrite raw = new ReadAndWrite();
        int i = 1;
        int j = 1;
        while(i<=ReadAndWrite.countReader||j<=ReadAndWrite.countWriter){
            if(new Random().nextBoolean()){         //随机判断读写者
                if(i>ReadAndWrite.countReader)
                    continue;
                ReadAndWrite.insert("读者R"+i+"来了");
                raw.textfield4.setText(raw.textfield4.getText()+"R"+i+",");
                new ReadThread(i);
                i++;
            }
            else{
                if(j>ReadAndWrite.countWriter)
                    continue;
                ReadAndWrite.insert("读者W"+j+"来了");
                raw.textfield4.setText(raw.textfield4.getText()+"W"+j+",");
                new WriteThread(j);
                j++;
            }
        }
    }
}