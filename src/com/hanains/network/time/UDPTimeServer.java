package com.hanains.network.time;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPTimeServer {
	public static final int PORT = 8000;
	   public static final int BUFFER_SIZE = 1024;
	   
	   public static void main(String[] args) {
	      DatagramSocket socket = null;
	      SimpleDateFormat format = null;
	      String time = null;
	      try {
	         //1. socket 생성
	         socket = new DatagramSocket(PORT);
	         format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
	        
	         while(true){
	            //2. data 수신
	            DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
	            socket.receive(receivePacket); //blocking
	            
	            //3. data 처리(확인)
	            byte[] data = receivePacket.getData();
	            int length = receivePacket.getLength();
	            String message = new String(data, 0, length, "UTF-8");
	            time = format.format(new Date());
	            System.out.println("[UDP Echo Server] received:" + message);
	            
	            //4. data 전송
	            if("".equals(message)) {
	            	byte[] sendData = time.getBytes("UTF-8");
		            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
		            socket.send(sendPacket);
	            }
	            
	         }
	      } catch (SocketException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }finally {
	         if(socket != null && socket.isClosed() == false) {
	            socket.close();
	         }
	      }
	      
	   }


}
