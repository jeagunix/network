package echo_subject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerRecieveThread extends Thread {
	private Socket socket;
	
	public EchoServerRecieveThread(Socket socket) {
		this.socket = socket;
	}
	@Override
	public void run() {
		InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
		EchoServer.log("[TCPServer] connected from client[" + inetRemoteSocketAddress.getAddress().getHostAddress() + ":"
				+ inetRemoteSocketAddress.getPort() + "]");

		try {
			// 4. I/O Stream 생성
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"),true);//true 쌓아두지말고 한번쓰면 보내라(flush).
			while (true) {

				// 5. 데이터 읽기(수신)
				String data = br.readLine();
				if(data == null) {
					EchoServer.log("closed by client");
				}
				
				EchoServer.log("[TCPServer] received:" + data);
				// 6. 데이터 쓰기(송신)
				pw.println(data);
			}
		} catch (SocketException e) {
			EchoServer.log("[TCPServer] abnormal closed by client");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 7. Socket 자원정리
			if (socket != null && socket.isClosed() == false) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
