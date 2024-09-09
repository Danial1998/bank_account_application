import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceExample implements Runnable{
    @Override
    public void run() {
        for(int i=0;i<5;i++){
            System.out.println(i);
            try{
                Thread.sleep(500);
            } catch(InterruptedException e){
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) {
        ExecutorServiceExample numberThread = new ExecutorServiceExample();

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(numberThread);
        executorService.execute(numberThread);
        executorService.execute(numberThread);
        executorService.execute(numberThread);
    }
}
