package com.gps.test.GPS;
/**
 * GPS消息通信涉及到的常量
 * @author lenovo
 *
 */
public class msgConstant {

	/**
	 * 标示位
	 */
	public static final byte flag = 0x7e;
	
	
	
	/**
	 * 消息ID
	 * 目前消息有：
	 * 终端通用应答：0x0001
	 * 平台通用应答：0x8001
	 * 终端心跳:0x0002
	 * 终端注册:0x0100
	 * 终端注册应答:0x8100
	 * 应答鉴权 0x0102
	 * 终端注销:0x0003
	 * 终端鉴权：0x0102
	 * 查询终端参数：0x8104
	 * 查询终端指定参数：0x8106
	 * 查询终端参数应答:0x0104
	 * 位置信息汇报:0x0200
	 * 位置信息查询：0x8201
	 * 位置信息查询消息应答：0x0201
	 * 位置信息批量上传：0x0704
	 * 
	 * 设置终端参数  回传位置速率：0x8103
	 */
	
	/**
	 * 终端通用应答：0x0001
	 */
	public static final char devCommonResID = 0x0001;
	/**
	 * 平台通用应答：0x8001
	 */
	public static final char serverCommonResID =  0x8001;
	/**
	 * 终端心跳:0x0002
	 */
	public static final char devHeartBeatID = 0x0002;
	/**
	 * 终端注册:0x0100
	 */
	public static final char devRegisterID =  0x0100;
	/**
	 * 终端注册应答:0x8100
	 */
	public static final char resDevRegID =  0x8100;
	/**
	 * 终端注销:0x0003
	 */
	public static final char devLogoutID = 0x0003;
	/**
	 * 终端鉴权：0x0102
	 */
	public static final char devAuthID =  0x0102;
	/**
	 * 查询终端参数：0x8104
	 */
	public static final char queryDevParaID =  0x8104;//
	/**
	 * 查询终端指定参数：0x8106
	 */
	public static final char queryDevPointParaID =  0x8106;//
	/**
	 * 查询终端参数应答:0x0104
	 */
	public static final char resQueryDevParaID =  0x0104;
	/**
	 * 位置信息汇报:0x0200
	 */
	public static final char addReportID =  0x0200;
	/**
	 * 位置信息查询：0x8201
	 */
	public static final char queryAddID =  0x8201;//
	/**
	 * 位置信息查询消息应答：0x0201
	 */
	public static final char resQueryAddID =  0x0201;
	/**
	 * 位置信息批量上传汇报
	 */
	public static final char addBatchReportID = 0x0704;
	/**
	 * 设置终端参数0x8103  回传位置速率：
	 */
	public static final char setDevPara = 0x8103;
	public enum msgID {
		devCommonResID,serverCommonResID,devHeartBeat,devRegister,resDevReg,devLogout,
		devAuth,queryDevPara,queryDevPointPara,resQueryDevPara,addReport,queryAdd,
		resQueryAdd,addBatchReportID,setDevPara	
	};
	
	/**
	 * 需要组装的消息类型
	 * 应答消息:应答注册，应答鉴权（平台通用应答）
	 * 查询地址
	 * 查询设备参数
	 * 查询设备指定参数
	 */
	
	/**
	 * 应答注册
	 */
	public static final String resDevReg = "resDevReg";
	/**
	 * 应答鉴权（平台通用应答）
	 */
	public static final String serverCommonRes = "serverCommonRes";
	/**
	 * 查询位置
	 */
	public static final String queryAdd = "queryAdd";
	/**
	 * 查询设备参数
	 */
	public static final String queryDevPara = "queryDevPara";
	/**
	 * 查询设备指定参数
	 */
	public static final String queryDevPointPara = "queryDevPointPara";
	
	private enum msgType{
		resDevReg,serverCommonRes,queryAdd,queryDevPara,queryDevPointPara
	}
	
}
