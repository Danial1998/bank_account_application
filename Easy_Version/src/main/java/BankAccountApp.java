import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BankAccountApp {
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount();
        Depositor depositor = new Depositor(account);
        Withdrawer withdrawer = new Withdrawer(account);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.submit(depositor);
        executorService.submit(withdrawer);
        executorService.submit(withdrawer);

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }
}
