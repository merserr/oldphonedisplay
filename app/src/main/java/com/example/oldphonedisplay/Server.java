package com.example.oldphonedisplay;

import java.net.ServerSocket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Server
{
	// Открываемый сервером порт для клиентов
	private final int SERVER_PORT = 5556;

	// Сокет соединения с клиентами
	private ServerSocket          serverSoket = null;

	// Поток контроля времени работы сервера
	private CallableDelay         callable1  = null;
	// Поток соединения с клиентами
	private CallableServer        callable2  = null;

	// Список задач
	private FutureTask<String>[]  futureTask = null;
	// Исполнитель задач
	private ExecutorService       executor   = null;

	// Класс
	class CallableDelay implements Callable<String>
    {
        private int cycle;

        public CallableDelay(int cycle)
        {
            this.cycle = cycle;
        }
        @Override
        public String call() throws Exception
        {
            Thread.sleep(100);
         //   while (cycle > 0) {
         //   	System.out.println("" + cycle);
         //
         //       cycle--;
         //   }
            // Останов 2-ой задачи
            futureTask[1].cancel(true);
            // Закрытие серверного сокета
         //   serverSoket.close();

			System.out.println("Thread '" + Thread.currentThread().getName() + "' stoped" );
             // Наименование потока, выполняющего задачу
            return "" + Thread.currentThread().getName();
        }
    }
	class CallableServer implements Callable<String>
    {
        private  int      port;
        private  boolean  started;

        public CallableServer(int port)
        {
            this.port    = port;
            this.started = true;
        }
        public void stopTask()
        {
        	started = false;
        }
        @Override
        public String call() throws Exception
        {
			// Создание серверного сокета
			serverSoket = new ServerSocket(port);
			System.out.println("Server start on port : " + port);

			// Цикл ожидания соединений клиентов с сервером
			while(started) {
				ConnectionWorker worker = null;
				try {
					// Ожидание соединения с клиентом
					worker = new ConnectionWorker(serverSoket.accept());

					System.out.println("Get client connection : " + (worker != null));

					// Обработка соединения в отдельном потоке
					Thread t = new Thread(worker);
					t.start();
				} catch (Exception e) {
					System.err.println("Connection error : " + e.getMessage());
					// Завершение цикла.
					if (serverSoket.isClosed())
						break;
				}
			}
			System.out.println("Thread '" + Thread.currentThread().getName() + "' stoped" );

			futureTask[1].cancel(true);
             // Наименование потока, выполняющего задачу
            return "" + Thread.currentThread().getName();
        }
    }

	Server()
	{
		// 1-ый поток контролирует задержку работы сервера
		callable1  = new CallableDelay (50);
		// 2-йй поток открывает соединение
		callable2  = new CallableServer(SERVER_PORT);

		// Создание задач
        futureTask = new FutureTask[2];

        futureTask[0] = new FutureTask<String>(callable1);
        futureTask[1] = new FutureTask<String>(callable2);

        // Выполнение задач
        executor = Executors.newFixedThreadPool(2);
        executor.execute(futureTask[0]);
        executor.execute(futureTask[1]);

        // Цикл работы executor'а
        while (true) {
//            try {
        	if (isTasksDone()) {
        		// Завершение работы executor'а
        		executor.shutdown();
        		System.out.println("\nexecutor shutdown");

//        		System.out.println("0. canceled = " + futureTask[0].isCancelled());
//        		System.out.println("1. canceled = " + futureTask[1].isCancelled());
        		break;
        	}
        }
	}
	//-----------------------------------------------------
    private boolean isTasksDone()
    {
        return futureTask[0].isDone() && futureTask[1].isDone();
    }
    //-----------------------------------------------------
    public static void main(String[] args)
    {
        new Server();
    }
}