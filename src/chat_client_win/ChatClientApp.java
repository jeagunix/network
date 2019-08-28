package chat_client_win;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import chat.ChatClientThread;

public class ChatClientApp {
	private static String SERVER_IP = "127.0.0.1";
	private static int SERVER_PORT = 8222;

	public static void main(String[] args) {

		// 1. 소캣생성
		Socket socket = null;
		Scanner sc = null;
		String name = null;

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
			while (true) {

				System.out.println("대화명을 입력하세요.");
				System.out.print(">>> ");
				name = sc.nextLine();

				if (name.isEmpty() == false) {
					break;
				}

				System.out.println("대화명은 한글자 이상 입력해야 합니다.\n");
			}
			pw.println("join:" + name);

			String ack = br.readLine();

			if ("join".equals(ack)) {

				new ChatWindow(name, socket).show();
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
