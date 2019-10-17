import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

public class getinfo extends Thread {
    private int CountReader = 0, CountWriter = 0;
    public CountDownLatch countDownLatch;
    public getinfo(CountDownLatch getcountDownLatch){
        this.countDownLatch = getcountDownLatch;
    }
    public int getCountReader(){
        return this.CountReader;
    }
    public int getCountWriter(){
        return this.CountWriter;
    }
    @Override
    public void run(){
        JFrame MainWindow = new JFrame("输入界面");
        JPanel JP1 = new JPanel();
        JPanel JP2 = new JPanel();
        JLabel JL1 = new JLabel("输入读者个数:");
        JLabel JL2 = new JLabel("输入写者个数:");
        JTextField JT1 = new JTextField();
        JTextField JT2 = new JTextField();
        JButton OK = new JButton("确认");
        JT1.setColumns(10);
        JT2.setColumns(10);
        JP1.add(JL1);
        JP1.add(JT1);
        JP2.add(JL2);
        JP2.add(JT2);
        MainWindow.add(JP1);
        MainWindow.add(JP2);
        MainWindow.add(OK);
        OK.addActionListener(actionEvent -> {
            while (CountWriter <= 0 || CountReader <= 0) {
                CountReader = Integer.parseInt(JT1.getText());
                CountWriter = Integer.parseInt(JT2.getText());
                if (CountWriter <= 0 || CountReader <= 0) {
                    JOptionPane.showMessageDialog(MainWindow, "输入错误", "错误", JOptionPane.WARNING_MESSAGE);
                    break;\\
                } else {
                    countDownLatch.countDown();
                }
            }
        });
        MainWindow.setLayout(new FlowLayout());
        MainWindow.setBounds(800,300,250,140);
        MainWindow.setVisible(true);
        MainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
