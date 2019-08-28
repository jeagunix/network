package echo_subject;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	public static final int PORT = 8000;

	public static void main(String[] args) {
		// 1. 서버소캣 생성
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();

			// 2.Binding : Socket에 SocketAddress(IpAddres + port) 바인딩한다.
			InetAddress inetAddress = InetAddress.getLocalHost();
			
			InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, PORT);
			serverSocket.bind(inetSocketAddress);
			System.out.println(inetAddress.getHostAddress());
			log("[TCPServer] binding " + inetAddress.getHostAddress() + ":" + PORT);

			// 3.클라이언트로부터 연결요청(connect)를 기다린다.
			while (true) {
				Socket socket = serverSocket.accept(); // blocking
				new EchoServerRecieveThread(socket).start();
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
		System.out.println("[Echo Server#" +Thread.currentThread().getId()+"] " + log);
	}
}
