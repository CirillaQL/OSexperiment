import java.awt.*;
import java.util.Random;
import java.util.concurrent.Semaphore;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Buffer extends Thread {
    private int countreader = 0;  //手动设置读者数
    private int countwriter = 0;  //手动设置写者数
    static int readCountNow = 0;  //等待读者数量
    static int writeCountNow = 0; //等待写者数量

    //使用Semaphore
    static Semaphore ReadCountSemaphore = new Semaphore(1);    //读者数量更改，保持原子性
    static Semaphore WriteCountSemaphore = new Semaphore(1);  //写者数量更改，保持原子性
    static Semaphore writeSemaphore = new Semaphore(1);        //写者信号量
    static Semaphore readSemaphore = new Semaphore(1);           //读者信号量
    static Semaphore first_reader_wait = new Semaphore(1);     //第一个读者，为了实现写者优先

    //GUI
    JFrame MainWindow;
    static JTextField textfield1, textfield2, textfield3;
    JPanel panel, panel1, panel2;
    JLabel jLabel1;
    static JTextPane textpane;

    Buffer(int A, int B) {
        this.countreader = A;
        this.countwriter = B;
    }

    static void insert(String str) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setUnderline(set, true);
        try {
            textpane.getDocument().insertString(textpane.getDocument().getLength(), str + "\n", set);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        MainWindow = new JFrame("显示界面");
        textpane = new JTextPane();
        jLabel1 = new JLabel("当前正在操作:");
        textfield1 = new JTextField(10);
        panel1 = new JPanel();
        panel1.add(jLabel1);
        panel1.add(textfield1);
        JLabel label2 = new JLabel("读写者进入时队列:");
        textfield3 = new JTextField(50);//显示读写者的先后顺序
        panel2 = new JPanel();
        panel2.add(label2);
        panel2.add(textfield3);
        panel = new JPanel(new BorderLayout());
        panel.add(panel1, BorderLayout.NORTH);
        panel.add(panel2, BorderLayout.SOUTH);
        panel.add(new JScrollPane(textpane), BorderLayout.CENTER);
        MainWindow.add(panel);

        int i = 1;
        int j = 1;
        while (i <= this.countreader || j <= this.countwriter) {
            //使用Boolean生成伪随机分布的序列
            if (new Random().nextBoolean()) {         //随机判断读写者
                if (i > this.countreader)
                    continue;
                insert("读者R" + i + "来了");
                textfield3.setText(textfield3.getText() + "R" + i + ",");
                new ReadThread(i);
                i++;
            } else {
                if (j > this.countwriter)
                    continue;
                insert("写者W" + j + "来了");
                textfield3.setText(textfield3.getText() + "W" + j + ",");
                new WriteThread(j);
                j++;
            }
        }
        MainWindow.setBounds(500, 300, 800, 600);
        MainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainWindow.setVisible(true);
        MainWindow.setResizable(false);
    }
}
