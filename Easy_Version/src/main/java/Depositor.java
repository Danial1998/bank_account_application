public class Depositor implements Runnable{

    private final BankAccount bankAccount;

    public Depositor(BankAccount bankAccount){
        this.bankAccount=bankAccount;
    }

    @Override
    public void run() {
        for(int i=0;i<5;i++){
            bankAccount.deposit(100);
            try{
                Thread.sleep(100);
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println(e);
            }
        }
    }
}
