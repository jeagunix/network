package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient {
	private static String SERVER_IP = "192.168.1.19";
	private static int SERVER_PORT = 5002;

	public static void main(String[] args) {
		Socket socket = null;
		try {
			//1. 소켓생성
			socket = new Socket();
		
			//2. 서버연결
			InetSocketAddress inetSocketAddress = new InetSocketAddress(SERVER_IP, SERVER_PORT);
			socket.connect(inetSocketAddress);
			
			System.out.println("[TCPClient connected]");
		
			// 3. IOSream 받아오기 //밑에들은 굳이 닫을 필요 없다. socket이 닫히기 떄문에
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			//4. 쓰기
			String data = "안녕하세요\n";
			os.write(data.getBytes("UTF-8"));
		
			//5. 읽기
			byte[] buffer = new byte[256]; // 버퍼를 넣는다.
			int readByteCount = is.read(buffer); // 느려질 것 같지만 blocking이 되므로 걱정 ㄴㄴ //버퍼를 선을 그어줘야한다.
			if (readByteCount == -1) {
				// 정상종료: remote socket이 close()
				// 메소드를 통해서 정상적으로 소켓을 닫은 경우
				System.out.println("[TCPClient] closed by client");
				return;
			}
			data = new String(buffer,0,readByteCount, "UTF-8");
			System.out.println("[TCPClient] recevied:"+data);
		} catch (IOException e) {
			
		}finally {
			try {
				if(socket != null && socket.isClosed() == false) {
					socket.close();	
				}
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
	}

}
