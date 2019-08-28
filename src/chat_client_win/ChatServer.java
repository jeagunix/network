package chat_client_win;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	public static final int PORT = 8222;

	public static void main(String[] args) {
		// 0. ListWriter 생성
		List<Writer> listWriters = new ArrayList<Writer>();

		// 1. 서버소캣 생성
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			
			// 2.Binding : Socket에 SocketAddress(IpAddres + port) 바인딩한다.
			serverSocket.bind(new InetSocketAddress("127.0.0.1", PORT));
		
			// 3.클라이언트로부터 연결요청(connect)를 기다린다.
			while (true) {
				Socket socket = serverSocket.accept(); // blocking
				new ChatServerThread(socket, listWriters).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 8. Server Socket 자원정리
			try {
				if (serverSocket != null && serverSocket.isClosed() == false)
					serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void log(String log) {
		System.out.println("[Echo Server#" + Thread.currentThread().getId() + "] " + log);
	}

}
