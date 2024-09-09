import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        ExecutorServiceCallableExample obj1 = new ExecutorServiceCallableExample();
        ExecutorServiceCallableExample obj2 = new ExecutorServiceCallableExample();

        Future<Integer> futureX = executorService.submit(obj1);
        try{
            int x = futureX.get();
            System.out.println(x);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Future<Integer> futureY = executorService.submit(obj2);
        try{
            int y = futureY.get();
            System.out.println(y);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
