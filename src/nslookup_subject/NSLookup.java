package nslookup_subject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		InetAddress[] inetAddresses = null;
		while (true) {
			System.out.print(">");
			String domain = sc.nextLine();
			try {
				if ("exit".equals(domain)) {
					break;
				}
				inetAddresses = InetAddress.getAllByName(domain);
				for(InetAddress inetAddress : inetAddresses) {
					System.out.println(inetAddress.getHostName()+" : "+inetAddress.getHostAddress());
				}
				
			} catch (UnknownHostException e) {
				
				e.printStackTrace();
			} 
			

			
		}

	}

}
