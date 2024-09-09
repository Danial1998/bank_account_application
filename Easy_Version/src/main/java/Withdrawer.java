public class Withdrawer implements Runnable{

    private final BankAccount bankAccount;

    public Withdrawer(BankAccount bankAccount){
        this.bankAccount = bankAccount;
    }

    @Override
    public void run() {
        try{
            for(int i=0;i<5;i++){
                bankAccount.withdraw(150);
                try{
                    Thread.sleep(150);
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
