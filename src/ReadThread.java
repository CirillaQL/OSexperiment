import java.util.Random;


public class ReadThread extends Thread
{
    public int id;

    public ReadThread(int id)
    {
        this.id = id;
        this.start();
    }

    public void run()
    {
        try{
            synchronized(this){
                if(Buffer.writeSemaphore.availablePermits()>0)    //判断是否有写者正在操作
                    Buffer.insert("读者R"+id+"可以读");
                else
                    Buffer.insert("有写者在写操作,所以读者R"+id+"正在等待读");
                //判断部分，如果writeSemaphore>0则可以进行读操作，availablePermits()返回此Semaphore对象中当前可用的许可数

                Buffer.reader_wait.acquire();//阻塞第一个之后的读者

                Buffer.first_reader_wait.acquire();//第一个读者申请资源，为了写者优先

                Buffer.readCountSemaphore.acquire();//保持writeCount的原子性
                if(Buffer.readCountNow ==0)
                    Buffer.writeSemaphore.acquire();
                Buffer.readCountNow++;
                Buffer.textfield1.setText(" "+ Buffer.readCountNow);        //界面显示
                Buffer.readCountSemaphore.release();
                Buffer.first_reader_wait.release();
                Buffer.reader_wait.release();

                Buffer.insert("读者R"+id+"读文件");      //界面显示
                Buffer.textfield1.setText("读者R"+id);

                Thread.sleep((long) (new Random().nextFloat()*7000));
                Buffer.insert("读者R"+id+"读完了");
                Buffer.readCountSemaphore.acquire();
                Buffer.readCountNow--;
                Buffer.textfield1.setText(" "+ Buffer.readCountNow);
                if(Buffer.readCountNow ==0)
                    Buffer.writeSemaphore.release();
                Buffer.readCountSemaphore.release();
            }

        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}