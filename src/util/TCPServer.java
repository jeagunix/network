package util;

import java.io.IOException;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {
	private static final int PORT = 5002;

	public static void main(String[] args) {
		// 1. 서버소캣 생성
		ServerSocket server = null;
		try {
			server = new ServerSocket();

			// 2. Binding :Socket에 SocketAddress(IPAddress + port) 바인딩한다.
			InetAddress inetAddress = InetAddress.getLocalHost();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, PORT); // 모든 시스템의 자기자신
			server.bind(inetSocketAddress);
			System.out.println("[TCPServer] binding " + inetAddress.getHostAddress() + ":" + PORT);
			// 3.accept :
			// 클라이언트로부터 연결요청(connect)을 기다린다.
			Socket socket = server.accept(); // Blocking
			InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			String remoteHostAddress = inetRemoteSocketAddress.getAddress().getHostAddress();
			int remoteHostPort = inetRemoteSocketAddress.getPort();

			System.out.println("[TCPServer] connected from client[" + remoteHostAddress + ":" + remoteHostPort + "]");

			try {
				// 4. IOSream 받아오기 //밑에들은 굳이 닫을 필요 없다. socket이 닫히기 떄문에
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				while (true) {
					
					//5. 데이터 읽기
					byte[] buffer = new byte[256]; // 버퍼를 넣는다.
					int readByteCount = is.read(buffer); // 느려질 것 같지만 blocking이 되므로 걱정 ㄴㄴ //버퍼를 선을 그어줘야한다.
					if (readByteCount == -1) {
						// 정상종료: remote socket이 close()
						// 메소드를 통해서 정상적으로 소켓을 닫은 경우
						System.out.println("[TCPServer] closed by client");
						break;
					}
					
					String data = new String(buffer, 0, readByteCount, "UTF-8");
					System.out.println("[TCPServer] received:" + data);
					//6. 데이터 쓰기
					os.write(data.getBytes("UTF-8"));
				}
			} catch (SocketException e) {
				System.out.println("[TCPServer] abnormal closed by client");
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//7. Socket 자원정리
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}

			}
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			//8. Server Socket 자원정리
			try {
				if (server != null && server.isClosed() == false)
					server.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

}
