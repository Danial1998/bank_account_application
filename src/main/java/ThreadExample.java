public class ThreadExample extends Thread {

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
        ThreadExample numberThread1 = new ThreadExample();
        ThreadExample numberThread2 = new ThreadExample();

        numberThread1.start();
        numberThread2.start();
    }
}
