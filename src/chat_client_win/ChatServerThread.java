package chat_client_win;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import echo_subject.EchoServer;

public class ChatServerThread extends Thread {
	private Socket socket;
	private String nickname;
	private List<Writer> listWriters;

	public ChatServerThread(Socket socket, List<Writer> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;
	}

	@Override
	public void run() {
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			// 4. I/O Stream 생성
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);// true
																												// 쌓아두지말고
																												// 한번쓰면
																												// 보내라(flush).
			while (true) {

				// 5. 데이터 읽기(수신)
				String request = br.readLine();
				if (request == null) {
					ChatServer.log("클라언트로부터 연결 끊김");
					doQuit(pw);
					break;
				}

				String[] tokens = request.split(":");

				if ("join".equals(tokens[0])) {
					
					doJoin(tokens[1], pw);
				} else if ("message".equals(tokens[0])) {
					doMessage(tokens[1]);

				} else if ("quit".equals(tokens[0])) {
					doQuit(pw);
				} else {
					ChatServer.log("에러 : 알 수 없는 요청(" + tokens[0] + ")");
				}

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

	private void doJoin(String nickname, Writer writer) {
		this.nickname = nickname;
		
		String data = nickname + "님이 참여하였습니다.";
		broadcast(data);
		// writer pool에 저장
		addWriter(writer);

		// ack
		PrintWriter printWriter = (PrintWriter) writer;
		printWriter.println("join");
		printWriter.flush();
	}

	private void addWriter(Writer writer) {
		synchronized (listWriters) {
			listWriters.add(writer);
		}
	}

	private void broadcast(String data) {
		synchronized (listWriters) {
			for (Writer writer : listWriters) {
				PrintWriter printWriter = (PrintWriter) writer;
				printWriter.println(data);
				printWriter.flush();

			}
		}
	}

	private void doMessage(String message) {
		broadcast(message);
	}

	private void doQuit(Writer writer) {
		removeWriter(writer);

		String data = nickname + "님이 퇴장하셨습니다";
		broadcast(data);

	}

	private void removeWriter(Writer writer) {
		synchronized (listWriters) {
			listWriters.remove(writer);
		}
	}
}
