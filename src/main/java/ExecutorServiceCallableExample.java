import java.util.concurrent.Callable;

public class ExecutorServiceCallableExample implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return 5;
    }
}
