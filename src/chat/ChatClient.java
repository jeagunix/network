package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
	private static String SERVER_IP = "127.0.0.1";
	private static int SERVER_PORT = 8222;

	public static void main(String[] args) {
		// 1. 소캣생성
		Socket socket = null;
		Scanner sc = null;
		try {
			// 1. scanner 생성 ( 표준입력, 키보드 )
			sc = new Scanner(System.in);
			// 2. 소켓생성
			socket = new Socket();

			// 3. 서버연결
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			log("connected");

			// 4. I/O Stream 생성
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);// true쌓아두지말고한번쓰면보내라(flush).

			// 5. join 프로토콜
			System.out.print("닉네임>>");
			String nickname = sc.nextLine();
			pw.println("join:" + nickname);

			String ack = br.readLine();
			if ("join".equals(ack)) {
				System.out.println("입장하였습니다.");
			}
			// 6. ChatClientReceiveThread 시작
			new ChatClientThread(socket).start();
			// 7. 키보드 입력처리
			while (true) {

				String input = sc.nextLine();
				if ("quit".equals(input)) {
					pw.println("quit");
					// 8. quit프로토콜 시작
					break;
				}
				if ("".equals(input) == false) {
					pw.println("message:" + input);
				}

			}

		} catch (IOException e) {
			log("error:" + e);
		} finally {
			try {
				if (sc != null) {
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
