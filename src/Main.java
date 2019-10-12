import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args){
        int Reader, Writer;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        getinfo k = new getinfo(countDownLatch);
        try{
            k.start();
            countDownLatch.await();
            Reader = k.getCountReader();
            Writer = k.getCountWriter();
            Buffer m = new Buffer(Reader,Writer);
            m.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
