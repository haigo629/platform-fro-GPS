package com.gps.test.GPS;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Date;

import com.gps.test.Redis.redis;
import com.gps.test.model.device;
import com.gps.test.util.util;

/**
 * 解析设备向服务器发送的消息并返回应答
 * @author lenovo
 *
 */
public class msgToServer {
	private byte[] msg;
	private BufferedOutputStream bos;
	private redis redis = new redis();
	/**
	 * 解析客户端消息 并应答
	 * @param msg 客户端消息：
	 * @param os 
	 * @param redis 
	 */
	public byte[] analyzeData(byte[] msgInfo) {
		//System.out.println("开始解析消息");
		msg = msgInfo;
		//获取消息ID
		char msgID = util.byteToChar(new byte[] {msg[1],msg[2]});
		//获取消息流水号
		char sum = util.byteToChar(new byte[] {msg[11],msg[12]});
		//设备号/电话
		byte[] phoneByte =new byte[] {msg[5],msg[6],msg[7],msg[8],msg[9],msg[10]};
		//设备号
		String devNum = util.bytesToPhone(phoneByte);
		//获取消息体
		byte[] body = util.getMsgBody(msg);
		//应答消息
		byte[] resp = null;
		device dev = new device();
		dev.setPhone(devNum);
		String time="";
		switch(msgID) {
			//终端通用应答：
			case msgConstant.devCommonResID:
				//System.out.println("收到号码"+devNum+"终端通用应答：");
				time = new Date().toString();
				//System.out.println("系统时间："+time);
				break;
			//终端心跳：
			case msgConstant.devHeartBeatID:
				//回应通用应答
				resp = msgToClient.getMsg(msgConstant.serverCommonRes, ++sum, phoneByte, 0,"",msgID);
				//System.out.println("收到号码"+devNum+"心跳");
				//System.out.println("回应终端心跳 应答完毕");
				time = new Date().toString();
				//System.out.println("系统时间："+time);
				break;
			//终端注册
			case msgConstant.devRegisterID:
				String authCode = util.createRandom()+"";//鉴权码
				resp = msgToClient.getMsg(msgConstant.resDevReg, ++sum, phoneByte, 0,authCode,msgID);
				//System.out.println("收到号码"+devNum+"注册");
				//System.out.println("回应终端注册 应答完毕");
				time = new Date().toString();
				//System.out.println("系统时间："+time);
				break;
			//终端注销
			case msgConstant.devLogoutID:
				//删除redis数据
				try {
					redis.del(devNum);
				}catch(Exception e) {
					e.printStackTrace();
				}
				//回应通用应答
				resp = msgToClient.getMsg(msgConstant.serverCommonRes, ++sum, phoneByte, 0,"",msgID);
				//System.out.println("收到号码"+devNum+"注销");
				//System.out.println("回应终端注销 应答完毕");
				time = new Date().toString();
				//System.out.println("系统时间："+time);
				break;
			//终端鉴权
			case msgConstant.devAuthID:
				//获得鉴权码，对比
				resp = msgToClient.getMsg(msgConstant.serverCommonRes, ++sum, phoneByte, 0,"",msgID);
				//System.out.println("收到号码"+devNum+"鉴权");
				//System.out.println("终端鉴权应答完毕");
				time = new Date().toString();
				//System.out.println("系统时间："+time);
				break;
			//终端位置信息汇报
			case msgConstant.addReportID:
				//保存位置
				double lat = util.bytesToDouble(new byte[] {body[8],body[9],body[10],body[11]});
				double lng = util.bytesToDouble(new byte[] {body[12],body[13],body[14],body[15]});
//				char high = util.byteToChar(new byte[] {body[16],body[17]});
//				char speed = util.byteToChar(new byte[] {body[18],body[19]});
//				char direction = util.byteToChar(new byte[] {body[20],body[21]});
				time = util.bytesToTime(new byte[] {body[22],body[23],body[24],body[25],body[26],body[27]});
				dev.setLat(lat);
				dev.setLng(lng);
				dev.setNowTime(time);
				redis.saveToRedis(devNum, util.serialize(dev));
				//回应通用应答
				resp = msgToClient.getMsg(msgConstant.serverCommonRes, ++sum, phoneByte, 0,"",msgID);
				//System.out.println("收到号码"+devNum+"位置：");
				//System.out.println("lat:"+lat+",lng:"+lng+"");
				//System.out.println("时间："+time);
				time = new Date().toString();
				//System.out.println("系统时间："+time);
				//System.out.println("终端位置信息汇报应答完毕");
				//System.out.println("1");
				break;
			//位置信息查询应答
			case msgConstant.resQueryAddID:
				double lat2 = util.bytesToDouble(new byte[] {body[10],body[11],body[12],body[13]});
				double lng2 = util.bytesToDouble(new byte[] {body[14],body[15],body[16],body[17]});				
				time = util.bytesToTime(new byte[] {body[24],body[25],body[26],body[27],body[28],body[29]});
				//保存位置
				dev.setLat(lat2);
				dev.setLng(lng2);
				dev.setNowTime(time);
				redis.saveToRedis(devNum, util.serialize(dev));
				//System.out.println("查询，收到号码"+devNum+"位置：");
				//System.out.println("lat:"+lat2+",lng:"+lng2+"");
				//System.out.println("时间："+time);
				time = new Date().toString();
				//System.out.println("系统时间："+time);
				//System.out.println("1");
				break;
			case msgConstant.addBatchReportID:
				//System.out.println("收到号码"+devNum+"批量汇报位置：");
				
				if(body[2]==0) {
					//System.out.println("正常汇报位置：");
				}
				if(body[2]==1) {
					//System.out.println("盲区补报：");
				}
				char dataNum = util.byteToChar(new byte[] {body[0],body[1]});
				if(dataNum>0) {
					double lat3 = util.bytesToDouble(new byte[] {body[13],body[14],body[15],body[16]});
					double lng3 = util.bytesToDouble(new byte[] {body[17],body[18],body[19],body[20]});	
					time = util.bytesToTime(new byte[] {body[27],body[28],body[29],body[30],body[31],body[32]});
					
					//System.out.println("时间："+time);
					//System.out.println("lat:"+lat3+",lng:"+lng3+"");
					dev.setLat(lat3);
					dev.setLng(lng3);
					dev.setNowTime(time);
					redis.saveToRedis(devNum, util.serialize(dev));
				}
				resp = msgToClient.getMsg(msgConstant.serverCommonRes, ++sum, phoneByte, 0,"",msgID);
				time = new Date().toString();
				//System.out.println("系统时间："+time);
				break;
				
			default :
				//System.out.println("收到号码"+devNum+"消息默认应答：");
				resp = msgToClient.getMsg(msgConstant.serverCommonRes, ++sum, phoneByte, 0,"",msgID);
				time = new Date().toString();
				//System.out.println("系统时间："+time);
				break;
				
		}
		//util.printByteData(resp);
		return resp;
	}	
}