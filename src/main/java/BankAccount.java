import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private double balance;
    private final Lock lock = new ReentrantLock(true);  // Fair lock
    private final Condition sufficientFunds = lock.newCondition();

    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
            System.out.println(Thread.currentThread().getName() + " deposited: " + amount + ", Current Balance: " + balance);
            sufficientFunds.signalAll();  // Notify waiting threads
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (balance < amount) {
                System.out.println(Thread.currentThread().getName() + " waiting for sufficient funds.");
                sufficientFunds.await();
            }
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " withdrew: " + amount + ", Current Balance: " + balance);
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount();
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Create Runnable tasks for depositing and withdrawing
        Runnable depositor = () -> {
            for (int i = 0; i < 5; i++) {
                account.deposit(100);
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        };

        Runnable withdrawer = () -> {
            try {
                for (int i = 0; i < 5; i++) {
                    account.withdraw(150);
                    try { Thread.sleep(150); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Submit tasks to the ExecutorService
        executor.submit(depositor);
        executor.submit(withdrawer);
        executor.submit(withdrawer);

        // Initiate a graceful shutdown
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}
