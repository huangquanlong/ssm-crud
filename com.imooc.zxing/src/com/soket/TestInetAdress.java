package com.soket;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestInetAdress {

	public static void main(String[] args) throws UnknownHostException {
		InetAddress inetAddress=InetAddress.getByName("www.atguigu.com");
		String hostName=inetAddress.getHostAddress();
		String adress=inetAddress.getHostName();
		System.out.println(adress);
		System.out.println(hostName);
		System.out.println(inetAddress);
	}

}
