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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            System.out.println("Thread was interrupted during withdrawal.");
        } finally {
            lock.unlock();
        }
    }


    //CLarification on what is thrownInterruptedException and how does the flow happen ?
    //so basically what i understood is that the withdraw can be interrupted by a customer by hitting cancel, in that scenario since the lock is lockInteruptible, the withdraw thread will be interrupted and the code in catch block will execute in that case. And then Thread.currentThread().interrupt() will be executed
    //ChatGPT said:
    //ChatGPT
    //
    //Yes, you’ve got it! The idea is that if the withdraw method is interrupted (for example, if a customer cancels the transaction), the lock.lockInterruptibly() method allows the thread to be interrupted while waiting to acquire the lock. When this interruption occurs:
    //
    //    Interrupted Exception Thrown: An InterruptedException is thrown, which is caught in the catch block.
    //
    //    Handle Interruption: Inside the catch block, Thread.currentThread().interrupt() is called to restore the interrupted status of the thread. This is important because catching the InterruptedException clears the thread’s interrupted status, and by calling Thread.currentThread().interrupt(), you ensure that the interruption signal is not lost, which can be important for higher-level interrupt handling.
    //
    //    Graceful Exit: Any necessary clean-up(unlocking) or logging can also be done in the catch block.
    //
    //So, the method gracefully handles the interruption, ensuring that resources like locks are properly managed even if the operation is interrupted.

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



