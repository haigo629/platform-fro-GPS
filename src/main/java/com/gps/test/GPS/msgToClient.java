package com.gps.test.GPS;

import java.nio.charset.StandardCharsets;

import com.gps.test.util.util;

/**
 * 组装平台向设备发送的消息
 * @author lenovo
 *
 */
public class msgToClient {

	/**
	 * 消息头
	 * @param sum 流水号
	 * @param msgID 消息ID
	 * @param phoneNum 设备终端手机号
	 * @param body 消息体
	 * @return
	 */
	private static byte[] msgHead(long sum,char msgID,byte[] phoneNum,byte[] body) {
		byte[] b = new byte[10];
		byte[] num = util.charToByte((char)sum);
		byte[] id = util.charToByte(msgID);
		byte[] len = util.charToByte((char) body.length);
		b = util.addBytes(util.addBytes(util.addBytes(id, len), phoneNum), num);
		return b;
		
	}
	
	/**
	 * 消息体
	 * @param type 具体对应消息
	 * @param sum 流水号
	 * @param msgID 消息ID
	 * @param result 结果  0：成功/确认；1：失败；2：消息有误；3：不支持 
	 * @param authCode 
	 * @param oldMsgID 
	 * @return
	 */
	public static byte[] msgBody(String type,long sum,char msgID,int result, String authCode, char oldMsgID) {
		byte[] body = null;
		byte[] num = util.charToByte((char)--sum);
		byte[] id = util.charToByte(oldMsgID);
		byte res = (byte)result;
		switch(type) {
			case msgConstant.serverCommonRes:
				body = util.addBytes(util.addBytes(num, id), new byte[] {res});
				break;
			case msgConstant.resDevReg:
				body = util.addBytes(util.addBytes(num,new byte[] {res}), authCode.getBytes(StandardCharsets.UTF_8));
				break;
			case msgConstant.queryAdd:
				body = null;
				break;
			case msgConstant.queryDevPara:
				body = null;
				break;
			case msgConstant.queryDevPointPara:
				break;
			
			
		}
		return body;
	}
	
	/**
	 * 生成检验码
	 * @param head 消息头
	 * @param body 消息体
	 * @return 校验码
	 */
	private static byte msgAuthCode(byte[] head,byte[] body) {
		byte[] msg = util.addBytes(head, body);
		byte result=0;
		for(int i=0;i<msg.length;i++) {
			if(i==0) result = msg[0];
			else result^=msg[i];
		}
		return result;
	}
	
	/**
	 * 组装消息
	 * @param type 具体消息
	 * @param sum 设备消息流水号，每个设备一个流水号，从0开始
	 * @param phone 设备手机号码
	 * @param result 应答消息的结果 0：成功/确认；1：失败；2：消息有误；3：不支持 
	 * @param authCode  应答注册时的鉴权码
	 * @param oldMsgID  应答时的上个消息ID
	 * @return
	 */
	public static byte[] getMsg(String type,long sum,byte[] phone,int result, String authCode, char oldMsgID) {
		byte[] msg = null;
		byte[] head=null;
		byte[] body=null;
		char msgid = 0;
		switch(type) {
			case msgConstant.serverCommonRes:
				msgid = msgConstant.serverCommonResID;
				break;
			case msgConstant.resDevReg:
				msgid=msgConstant.resDevRegID;
				break;
			case msgConstant.queryAdd:
				msgid = msgConstant.queryAddID;
				break;
			case msgConstant.queryDevPara:
				msgid = msgConstant.queryDevParaID;
				break;
			case msgConstant.queryDevPointPara:
				msgid = msgConstant.queryDevPointParaID;
				break;
		}
		
		body = msgBody(type,sum,msgid,result,authCode,oldMsgID);
		head = msgHead(sum, msgid, phone, body);
		byte code = msgAuthCode(head,body);
		
		byte[] c = new byte[] {code};
		byte[] f = new byte[] {msgConstant.flag};
		//对特殊字符转义
		byte[] smallMsg = util.escapeCode(util.addBytes(head,body));
		msg = util.addBytes(util.addBytes(util.addBytes(f, smallMsg), c), f);
		
		return msg;
		
	}
}
