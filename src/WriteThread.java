import java.util.Random;

public class WriteThread extends Thread
{
    private int id;
    //创建后直接运行
    WriteThread(int id)
    {
        this.id=id;
        this.start();
    }
    public void run()
    {
        try{
            //锁操作，保证原子性
            synchronized(this){
                if(Buffer.writeSemaphore.availablePermits()>0)//判断是否有写者正在操作
                    Buffer.insert("当前的写者W"+this.id+"可以写");
                else
                    Buffer.insert("当前的写者W"+this.id+"不可以写");
                Buffer.WriteCountSemaphore.acquire();//保持writeCount的原子性
                if(Buffer.writeCountNow == 0)
                    Buffer.first_reader_wait.acquire();	//如果写者数量为0，第一个读者申请访问
                Buffer.writeCountNow++;
                Buffer.WriteCountSemaphore.release();
                Buffer.writeSemaphore.acquire();//写者开始操作，与其他操作互斥
                Buffer.insert("写者W"+this.id+"正在写");               //界面显示
                Buffer.textfield1.setText(" "+ "写者W"+ id + "正在写");
                Thread.sleep((long) (new Random().nextFloat()*5000));
                Buffer.insert("写者W"+this.id+"写完了");
                Buffer.writeSemaphore.release();
                Buffer.WriteCountSemaphore.acquire();
                Buffer.writeCountNow--;
                if(Buffer.writeCountNow ==0)//如果写者数量为0，唤醒第一个等待的读者
                    Buffer.first_reader_wait.release();
                Buffer.WriteCountSemaphore.release();
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}