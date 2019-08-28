package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {

	private static final int PORT = 5000; // final이 붙은 변수명은 대문자로 ! ! !

	public static void main(String[] args) {
		// 1. 서버소켓 생성
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			// 1-1 time wait 상태에서 서버 소캣을 즉시 사용하기 위해서...
			serverSocket.setReuseAddress(true);

			// 2. Binding : 소켓에 SocketAddress(IP Address + Port)를 바인딩 하다.
			InetAddress inetAddress = InetAddress.getLocalHost();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, PORT);
			serverSocket.bind(inetSocketAddress);
			System.out.println("[TCPServer] binding " + inetAddress.getHostAddress() + ":" + PORT);

			// 3. accept: 클라이언트로 부터 연결요청(connect)을 기다린다.
			Socket socket = serverSocket.accept(); // Blocking
			InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress(); // IP
			int remoteHostPort = inetRemoteSocketAddress.getPort();
			System.out.println("[TCPServer] connected form client " + remoteHostAddress + ":" + remoteHostPort);

			try {
				// 4. IOStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				while (true) {
					// 5. 데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer); // Blocking
					if (readByteCount == -1) {
						// 정상종료 : remote socket이 close()메소드를 통해서 정상적으로 소켓을 닫은 경우
						System.out.println("[TCPServer] closed by client");
						break;
					}

					String data = new String(buffer, 0, readByteCount, "UTF-8");
					System.out.println("[TCPServer] received : " + data);

					// 6. 데이터 쓰기
					os.write(data.getBytes("UTF-8"));
				}

			} catch (SocketException e) { // 정상적인 종료가 아닌 서버를 종료하지 않고 닫은경우
				System.out.println("[TCPServer] abnormal closed by client (비정상적인종료)");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 7. socket 자원정리
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 8. Server Socket자원정리
			try {
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}