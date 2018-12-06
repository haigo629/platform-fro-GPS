package com.gps.test.TCP;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 平台服务端TCPserver初始化
 * @author lenovo
 *
 */
public class tcpServer {
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	//记录客户端数量
	private static int count = 0;
	public void initServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("****************服务启动，等待客户端连接：****************");
			while(true) {
				socket = serverSocket.accept();
				ServerThread  serverThread = new ServerThread(socket);
				serverThread.start();
				count++;
				System.out.println("客户端的数量: " + count);
                InetAddress address = socket.getInetAddress();
                System.out.println("当前客户端的IP ： " + address.getHostAddress());
                
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				socket.close();
				serverSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("通信连接错误");
			}
			
		}
		
	}
}
