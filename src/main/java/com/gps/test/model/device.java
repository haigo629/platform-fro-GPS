package com.gps.test.model;

import java.io.Serializable;

public class device implements Serializable{
	/**
	 * 设备ID
	 */
	private int deviceID;
	
	public int getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}
	
	/**
	 * 设备消息流水号
	 */
	private static long sum = 0;
	
	public static long getSum() {
		return sum;
	}

	public static void setSum(long sum) {
		device.sum = sum;
	}
	/**
	 * 纬度
	 */
	private double lat;
	
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * 经度
	 */
	private double lng;
	
	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	/**
	 * 鉴权码
	 */
	private String authID;
	
	public String getAuthID() {
		return authID;
	}

	public void setAuthID(String authID) {
		this.authID = authID;
	}
	/**
	 * 当我位置时间
	 */
	private String nowTime;
	
	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	/**
	 * 设备终端手机号
	 */
	private String phone = "017366282715";
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
