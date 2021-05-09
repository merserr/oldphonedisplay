package com.example.oldphonedisplay;

import android.app.PendingIntent;
import android.content.Intent;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ConnectionWorker implements Runnable
{
	PendingIntent pi;
	private Socket clientSocket = null;	         // Сокет для взаимодействия с клиентом
	private InputStream inputStream = null;	     // Входной поток получения данных из сокета
	Intent intent = new Intent(MainActivity.BROADCAST_ACTION);

	public ConnectionWorker(Socket socket) {
		clientSocket = socket;
	}
	@Override
	public void run() {
		try {
			// Определение входного потока
			inputStream = clientSocket.getInputStream();
		} catch (IOException e) {
			System.err.println("===Can't get input stream===");
		}

		byte[] data = new byte[1024*4];		 // Буфер для чтения информации
		while(true) {
			try {
				// Получение информации :
				// count - количество полученных байт

				int count;
				count = inputStream.read(data,0,data.length);

				if (count > 0) {
					String msg = new String(data, 0, count);
					// Вывод в консоль сообщения
					System.out.println("--- "+msg);

					intent.putExtra("msg", msg);
					MainActivity.sendfromConnectionWorker(intent); //=====================================
					//sendBroadcast(intent);



				} else if (count == -1 ) {
					// Если count=-1, то поток прерван
					System.out.println("===socket is closed===");
					clientSocket.close();
					break;
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		System.out.println("===ConnectionWorker stoped===");
	}
}
