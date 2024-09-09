public class RunnableExample implements Runnable{
    @Override
    public void run() {
        for(int i=1;i<=5;i++){
            System.out.println(i);
            try{
                Thread.sleep(1000);
            } catch(InterruptedException e){
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) {
        RunnableExample numberThread1 = new RunnableExample();
        RunnableExample numberThread2 = new RunnableExample();

        Thread thread1 = new Thread(numberThread1);
        Thread thread2= new Thread(numberThread2);
    }
}
