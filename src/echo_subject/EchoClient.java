package echo_subject;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	private static String SERVER_IP = "192.168.1.19";
	public static int SERVER_PORT  = 8000;

	public static void main(String[] args) {
		// 1. 소캣생성
		Socket socket = null;
		Scanner sc = null;
		try {
			// 1. scanner 생성 ( 표준입력, 키보드 )
			sc = new Scanner(System.in);
			// 1. 소켓생성
			socket = new Socket();

			// 3. 서버연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			log("connected");

			// 4. I/O Stream 생성
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);// true
																												// 쌓아두지말고
																												// 한번쓰면
																												// 보내라(flush).

			// 4. 쓰기
			while (true) {

				// 5. 키보드입력을 받아야한다.
				System.out.print(">>");
				String line = sc.nextLine();
				if ("quit".equals(line)) {
					break;
				}
				

				// 6. 데이터쓰기 (전송)
				pw.println(line);

				// 7. 읽기(수신)
				String data = br.readLine();
				if(data == null) {
					log("closed by client");
					break;
				}
				//8. 콘솔 출력
				System.out.println("<<"+data);
			}

		} catch (IOException e) {

		} finally {
			try {
				if(sc != null) {
					sc.close();
				}
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}

			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	private static void log(String log) {
		System.out.println("[Echo Cliet] " + log);
	}

}
