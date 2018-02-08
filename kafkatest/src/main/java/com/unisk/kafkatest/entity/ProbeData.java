package com.unisk.kafkatest.entity;

import java.util.List;

public class ProbeData {

	private String deviceMac ;
	private String shopId ;
	private List<UserData> data ;
	
	public List<UserData> getData() {
		return data;
	}
	public void setData(List<UserData> data) {
		this.data = data;
	}
	public String getDeviceMac() {
		return deviceMac;
	}
	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
}
