package com.gps.test.TCP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.gps.test.GPS.msgConstant;
import com.gps.test.GPS.msgToServer;
import com.gps.test.Redis.redis;
import com.gps.test.model.device;
import com.gps.test.util.util;
/**
 * 新起线程处理客户端数据
 * @author lenovo
 *
 */
public class ServerThread extends Thread{
	
	private Socket socket = null;
	private BufferedOutputStream bos = null;
	private BufferedInputStream bis =null;
	private msgToServer mts ;
	//位置汇报  5s一次
	private byte[] resp2 = new byte[] {0x7E, (byte) 0x81, 0x03, 0x00, 0x0A,
			0x01, 0x40, 0x10, 0x00, 0x10, 0x11, 0x00, 0x00, 0x01,
			0x00, 0x00, 0x00, 0x29, 0x04, 0x00, 0x00, 0x00, 0x05, (byte) 0xF1, 0x7E};
	
	public ServerThread(Socket s) throws IOException {
		this.socket = s;
		this.mts = new msgToServer();
		bis = new  BufferedInputStream(socket.getInputStream());
		bos = new BufferedOutputStream(socket.getOutputStream());
		sendMessageToClient(resp2);
		lastHeartbeat = System.currentTimeMillis();
	}
	private boolean isRunning = true;
	
	// 最近的心跳时间
    private long lastHeartbeat;
    // 心跳间隔时间
    private long heartBeatInterval = 3 * 30000;

	public void run() {
		try {
			while(isRunning) {
				if(null==socket||null==bis||null==bos)
					break;
				if(socket.isClosed())
					break;
				if(!socket.isConnected())
					break;
				//现在时间
				long startTime = System.currentTimeMillis();
				if (startTime - lastHeartbeat > heartBeatInterval) {
                    System.out.println("心跳失联，断开连接");
                    isRunning = false;
				}
				byte [] data = getMessageFromClient();
				if(data!=null) {
					//转义还原
					byte[] receive = util.resEscapeCode(data);
					
//					System.out.println("收到数据：");//默认为心跳
//		          	util.printByteData(data);
					//处理心跳
					char msgID = util.byteToChar(new byte[] {receive[1],receive[2]});
					if(msgID==msgConstant.devHeartBeatID)
						lastHeartbeat = System.currentTimeMillis();
					
					byte[] resp = mts.analyzeData(receive);
					sendMessageToClient(resp);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			try {
				System.out.println("通信错误");
				if(bos!=null)
	        		bos.close();
		    	if(bis!=null)
		    		bis.close();
		    	if(socket!=null)
		    		socket.close();
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		finally {
			try {
				System.out.println("线程结束");
				if(bos!=null)
	        		bos.close();
		    	if(bis!=null)
		    		bis.close();
		    	if(socket!=null)
		    		socket.close();
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 获取客户端数据
	 * @return
	 */
	public byte[] getMessageFromClient() {
		 
		byte [] data = null;
        try {
             data = new byte[2048];
             
             int i = bis.read(data);
             if(i<=0) {
            	 return null;
             }
             //非正常数据
             if(data[0]!=0x7e) {
             	System.out.println("收到非正常数据数据：");//默认为心跳
             	util.printByteData(data);
             }
        } catch (Exception e) {
            e.printStackTrace();  //出现报错  
            //org.apache.catalina.loader.WebappClassLoaderBase.clearReferencesThreads 
            //The web application [testGps] appears to have started a thread named [Thread-7] 
            //but has failed to stop it. This is very likely to create a memory leak. 
            //Stack trace of thread:
            //线程回收不及时，重启tomcat发现存在的线程但是无法关闭，为了防止出现溢出 ，强制注销掉了这个线程
            
            try {
				System.out.println("接收数据异常");
				util.printByteData(data);
				if(bos!=null)
	        		bos.close();
		    	if(bis!=null)
		    		bis.close();
		    	if(socket!=null)
		    		socket.close();
			}catch (Exception e1) {
				e1.printStackTrace();
			}
        }//这里关闭流会导致socket关闭	
		return data;
	}
	
	/**
	 * 发送指令给客户端
	 * @param message
	 */
	public void sendMessageToClient(byte[] message) {
		try {
			
			bos.write(message);
            bos.flush();
//            System.out.println("回复：");
//            util.printByteData(message);
		} catch (IOException e) {
			e.printStackTrace();
			
			try {
				System.out.println("发送数据异常");
				util.printByteData(message);
				if(bos!=null)
	        		bos.close();
		    	if(bis!=null)
		    		bis.close();
		    	if(socket!=null)
		    		socket.close();
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
