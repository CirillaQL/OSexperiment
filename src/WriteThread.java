import java.util.Random;



public class WriteThread extends Thread
{
    public int id;
    public WriteThread(int id)
    {
        this.id=id;
        this.start();
    }
    public void run()
    {
        try{
            synchronized(this){
                if(ReadAndWrite.writeSemaphore.availablePermits()>0)//判断是否有写者正在操作
                    ReadAndWrite.insert("写者W"+this.id+"可以写");
                else
                    ReadAndWrite.insert("写者W"+this.id+"不可以写");
                ReadAndWrite.writerCountSemaphore.acquire();//保持writeCount的原子性
                if(ReadAndWrite.writeCount==0)
                    ReadAndWrite.first_reader_wait.acquire();	//如果写者数量为0，第一个读者申请访问
                ReadAndWrite.writeCount++;
                ReadAndWrite.textfield3.setText(" "+ReadAndWrite.writeCount);//GUI显示
                ReadAndWrite.writerCountSemaphore.release();
                ReadAndWrite.writeSemaphore.acquire();//写者开始操作，与其他操作互斥

                ReadAndWrite.insert("写者W"+this.id+"正在写");//GUI显示
                ReadAndWrite.textfield1.setText("写者W"+id);

                Thread.sleep((long) (new Random().nextFloat()*2000));
                ReadAndWrite.insert("写者W"+this.id+"写完了");
                ReadAndWrite.writeSemaphore.release();
                ReadAndWrite.writerCountSemaphore.acquire();
                ReadAndWrite.writeCount--;
                ReadAndWrite.textfield3.setText(" "+ReadAndWrite.writeCount);
                if(ReadAndWrite.writeCount==0)//如果写者数量为0，唤醒第一个等待的读者
                    ReadAndWrite.first_reader_wait.release();
                ReadAndWrite.writerCountSemaphore.release();
            }
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}