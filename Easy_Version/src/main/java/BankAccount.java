import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private double balance;
    private final Lock lock = new ReentrantLock(true);
    private final Condition sufficientFunds = lock.newCondition();

    public void deposit(double amount){
        lock.lock();
        try{
            balance+=amount;
            System.out.println(Thread.currentThread().getName()+" deposited: "+amount+" ,Balance: "+balance);
            sufficientFunds.signalAll();
        } catch(Exception e){
            System.out.println(e);
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(double amount) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while(balance<amount){
                System.out.println(Thread.currentThread().getName()+" waiting for sufficient funds");
                sufficientFunds.await();
            }
            balance-=amount;
            System.out.println(Thread.currentThread().getName()+" withdrawn amount: "+amount+" ,CurrentBalance: "+balance);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println(e);
        } finally {
            lock.unlock();
        }
    }
}
