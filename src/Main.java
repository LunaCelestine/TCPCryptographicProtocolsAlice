public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello World!");

	/* ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            WorkerThread worker = new WorkerThread(" " + i);
            executor.execute(worker);
          }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    } */

        Thread t1=new Thread(TCPClientServer::client);
        t1.start();
        Thread t2=new Thread(TCPClientServer::server);
        t2.start();
        t1.join();
        t2.join();
    }
}
