package com.gps.test.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * 消息处理帮助类
 * @author lenovo
 *
 */
public class util {
	
	public static void printByteData(byte[] data) {
		for (byte b : data) {		
			System.out.print("0x");
			
			if((b&0xF0)>>4==10)
				System.out.print("A");
			else if((b&0xF0)>>4==11)
				System.out.print("B");
			else if((b&0xF0)>>4==12)
				System.out.print("C");
			else if((b&0xF0)>>4==13)
				System.out.print("D");
			else if((b&0xF0)>>4==14)
				System.out.print("E");
			else if((b&0xF0)>>4==15)
				System.out.print("F");
			else
				System.out.print((b&0xF0)>>4);
			
			if((b&0x0F)==10)
				System.out.print("A");
			else if((b&0x0F)==11)
				System.out.print("B");
			else if((b&0x0F)==12)
				System.out.print("C");
			else if((b&0x0F)==13)
				System.out.print("D");
			else if((b&0x0F)==14)
				System.out.print("E");
			else if((b&0x0F)==15)
				System.out.print("F");
			else
				System.out.print(b&0x0F);
			
			System.out.print(" ");
			
		}
		System.out.println();
	}
	/**
	 * 获取经纬度byte[4]转换成实际数
	 * @param bytes
	 */
	public static double bytesToDouble(byte[] bytes) {
		int b0 = bytes[0] & 0xFF;  
        int b1 = bytes[1] & 0xFF;  
        int b2 = bytes[2] & 0xFF;  
        int b3 = bytes[3] & 0xFF;  
        long a= (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
        return  (double)a/(double)1000000; 
	}
	//序列化 
    public static byte[] serialize(Object obj){
        ObjectOutputStream obi=null;
        ByteArrayOutputStream bai=null;
        try {
            bai=new ByteArrayOutputStream();
            obi=new ObjectOutputStream(bai);
            obi.writeObject(obj);
            byte[] byt=bai.toByteArray();
            return byt;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        	try {
        		obi.close();
        		bai.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return null;
    }
    
    //反序列化
    public static Object unserizlize(byte[] data){
        ObjectInputStream oii=null;
        ByteArrayInputStream bis=null;
        Object obj = null;
        try {
        	bis=new ByteArrayInputStream(data);
            oii=new ObjectInputStream(bis);
            obj=oii.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        	try {
				oii.close();
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
        return obj;
    }
	
	/**
	 * 12位 字符串电话转BCD[6]
	 * @param phone
	 * @return
	 */
	public static byte[] phoneToBytes(String phone) {
		byte[] phoneBytes = new byte[6];
		if (phone.length() != 12) return phoneBytes;
        for (int i = 0; i < 12; i++)
        {
        	int high = Integer.parseInt(phone.substring(i, 1));
        	i++;
        	int low = Integer.parseInt(phone.substring(i, 1));
        	
        	phoneBytes[i] =(byte)(high*16+low);
        }
        return phoneBytes;
	}
	/**
	 * BCD[6] 转12位 String电话
	 * @param bytes
	 * @return
	 */
	public static String bytesToPhone(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bytes) {
			int high = (int) ((b & 0xF0) >> 4);
			sb.append(high);
	        int low = (int) (b & 0x0F);
	        sb.append(low);
		}
		return sb.toString();
	}
	
	public static String bytesToTime(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bytes) {
			int high = (int) ((b & 0xF0) >> 4);
			sb.append(high);
	        int low = (int) (b & 0x0F);
	        sb.append(low);
	        sb.append(":");
		}
		return sb.toString();
	}
	
	/**
	 * 获取消息体
	 * @param msg
	 * @return
	 */
	public static byte[] getMsgBody(byte[] msg) {
		//标示位1 消息头12      校验码1 标示位1
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for(int i=13;i<msg.length-2;i++) {
			out.write(msg[i]);
		}
		return out.toByteArray();
	}
	/**
	 * 随机生成六位鉴权码
	 * @return
	 */
	public static int createRandom() {
		return (int)((Math.random()*9+1)*100000);
	}
	
	
	/**
	 * 一个字符转两个byte变成byte数组
	 * @param c
	 * @return
	 */
	public static byte[] charToByte(char c) {
		byte[] b = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(out);
		try {
			dataOut.writeChar(c);
			b = out.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	//char转化为byte
    public static byte[] charToByte2(char c) {
    	byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }
 
	//byte转换为char
    public static char byteToChar(byte[] b) {
	    char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
	    return c;
    }
	/**
	 * 两个byte[]数组合并
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static byte[] addBytes(byte[] data1, byte[] data2) {
		byte[] data3 = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, data3, 0, data1.length);
		System.arraycopy(data2, 0, data3, data1.length, data2.length);
		return data3;
 
	}
	/**
	 * 对消息里出现的特殊byte 0x7e 0x7d进行转义
	 * @param msg 消息
	 * @return
	 */
	public static byte[] escapeCode(byte[] msg) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for(int i=0;i<msg.length;i++) {
			out.write(msg[i]);
			if(i>0&&i<msg.length-1) {
				if(msg[i]==0x7e) {
					out.write(0x02);
				}
				if(msg[i]==0x7d) {
					out.write(0x01);
				}
			}
		}
		return out.toByteArray();
	}
	/**
	 * 转义还原,并获取消息
	 * @param msg
	 * @return
	 */
	public static byte[] resEscapeCode(byte[] msg) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] data = null;
		//获取消息，去除无用byte
		try {
			out.write(msg[0]);
			int j =1;
			while(true) {
				out.write(msg[j]);
				
				if(msg[j]==0x7e) {
					//0x7e本来就是最后一个byte
					if(j==msg.length-1)
						break;
					//出现0x7e非转义 
					if(msg[j+1]!=0x02)
						break;
				}
				j++;
			}
			byte[] newMsg = out.toByteArray();
			//System.out.println("去除0x00:");
			//util.printByteData(newMsg);
			out.reset();
			
			//转义还原
			out.write(newMsg[0]);
			for(int i=1;i<newMsg.length-2;i++) {
				out.write(newMsg[i]);
				if(newMsg[i]==0x7e&&newMsg[i+1]==0x02) {
					i++;
				}
				if(newMsg[i]==0x7d&&newMsg[i+1]==0x01) {
					i++;
				}
			}
			out.write(newMsg[newMsg.length-2]);
			out.write(newMsg[newMsg.length-1]);
			data = out.toByteArray();
			
			//System.out.println("转义还原:");
			//util.printByteData(newMsg);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 计算设备回传消息校验码
	 * @param msg
	 * @return
	 */
	public static byte DevCode(byte[] msg) {
		byte result=0;
		//去掉头尾和校验码
		for(int i=1;i<msg.length-2;i++) {
			if(i==0) result = msg[0];
			result^=msg[i];
		}
		return result;
	}
}
