package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost(); // InetAddress는 인터넷 관련된 것
			String hostName = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
			byte[] ipAddress = inetAddress.getAddress(); //4바이트
			
			System.out.println(hostName);
			System.out.println(hostAddress);
			
			for(byte ipAddresses : ipAddress) {
				System.out.print((int)ipAddresses & 0x0000000ff);
				System.out.print(".");
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
