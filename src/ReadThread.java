import java.util.Random;
//import java.util.concurrent.Semaphore;

public class ReadThread extends Thread
{
    public int id;

    //线程创建时直接运行
    public ReadThread(int id)
    {
        this.id = id;
        this.start();
    }

    public void run()
    {
        try{
            synchronized(this){
                if(ReadAndWrite.writeSemaphore.availablePermits()>0)    //判断是否有写者正在操作
                    ReadAndWrite.insert("读者R"+id+"可以读");
                else
                    ReadAndWrite.insert("有写者在写操作,读者R"+id+"等待读");
                //
                ReadAndWrite.reader_wait.acquire();//阻塞第一个之后的读者
                ReadAndWrite.first_reader_wait.acquire();//第一个读者申请资源，为了写者优先
                ReadAndWrite.readCountSemaphore.acquire();//保持writeCount的原子性
                if(ReadAndWrite.readCount==0)
                    ReadAndWrite.writeSemaphore.acquire();
                ReadAndWrite.readCount++;
                ReadAndWrite.textfield2.setText(" "+ReadAndWrite.readCount);//GUI显示
                ReadAndWrite.readCountSemaphore.release();
                ReadAndWrite.first_reader_wait.release();
                ReadAndWrite.reader_wait.release();

                ReadAndWrite.insert("R"+id+"读文件");//GUI显示
                ReadAndWrite.textfield1.setText("读者R"+id);

                Thread.sleep((long) (new Random().nextFloat()*2000));
                ReadAndWrite.insert("读者R"+id+"读完了");
                ReadAndWrite.readCountSemaphore.acquire();
                ReadAndWrite.readCount--;
                ReadAndWrite.textfield2.setText(" "+ReadAndWrite.readCount);
                if(ReadAndWrite.readCount==0)
                    ReadAndWrite.writeSemaphore.release();
                ReadAndWrite.readCountSemaphore.release();
            }

        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}