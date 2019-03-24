import java.util.concurrent.atomic.AtomicInteger;
public class MyThreadTest {
    private int x = 2;
    private int z = 0;
    private static int threadCountInt = 10;
    public static AtomicInteger threadCount = new AtomicInteger(threadCountInt);
    public static AtomicInteger countOfX = new AtomicInteger(0);
    public static AtomicInteger countOfZ = new AtomicInteger(0);
    public void setX(int x){ this.x = x; }
    public int getX() { return this.x; }
    public int getZ() { return this.z; }
    public void setZ(int z) { this.z = z; }
    public static void main(String[] args) throws Exception {
        for (int index = 0; index < threadCountInt; index++){
            MyThreadTest myThreadTest = new MyThreadTest();
            Thread setterThread1 = new Thread(new SetterRunnable(myThreadTest,0,1));
            Thread setterThread2 = new Thread(new SetterRunnable(myThreadTest,1,1));
            Thread calThread = new Thread(new CalRunnable(myThreadTest));
            calThread.join();
            setterThread1.join();
            setterThread2.join();
            calThread.start();
            setterThread1.start();
            setterThread2.start(); }
        new Thread() {
            public void run() {
                if (threadCount.get() > 0) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("===========================");
                System.out.println("x被访问：" + MyThreadTest.countOfX.get() + "次,z被访问：" + MyThreadTest.countOfZ.get() + "次");
                System.out.println("===========================");
            }
        }.start();
    }
}
class SetterRunnable implements Runnable{
    MyThreadTest myThreadTest;
    int x = 0;
    int z = 0;
    public SetterRunnable(MyThreadTest myThreadTest, int x, int z) {
        this.myThreadTest = myThreadTest;
        this.x = x;
        this.z = z;
    }

    @Override
    public void run() {
        myThreadTest.setX(x);  // l11
        MyThreadTest.countOfX.incrementAndGet();
        myThreadTest.setZ(z);  // l21
        MyThreadTest.countOfZ.incrementAndGet();
    }
}
class CalRunnable implements  Runnable{
    MyThreadTest myThreadTest;
    public CalRunnable(MyThreadTest myThreadTest) { this.myThreadTest = myThreadTest; }

    @Override
    public void run() {
        int x = myThreadTest.getX();
        int z = myThreadTest.getZ();
        int y;
        if(x != 0){  // l12
            MyThreadTest.countOfX.incrementAndGet();
            y = 2/x;  // l13
            MyThreadTest.countOfX.incrementAndGet();
            System.out.println("y=2/x=" + y);
        } else {
            y = 1/z;  // l22
            MyThreadTest.countOfZ.incrementAndGet();
            System.out.println("y=1/z=" + y);
        }
        MyThreadTest.threadCount.decrementAndGet();
    }
}