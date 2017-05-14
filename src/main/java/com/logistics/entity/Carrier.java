package com.logistics.entity;
/**
 * 承运商实体类
 * @author zys
 *
 */
public class Carrier {
	
	private int carrierID;
	private String carrierName;
	private String carrierAddress;
	private String carrierTelephone;
	
	public int getCarrierID() {
		return carrierID;
	}
	public void setCarrierID(int carrierID) {
		this.carrierID = carrierID;
	}
	public String getCarrierName() {
		return carrierName;
	}
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	public String getCarrierAddress() {
		return carrierAddress;
	}
	public void setCarrierAddress(String carrierAddress) {
		this.carrierAddress = carrierAddress;
	}
	public String getCarrierTelephhone() {
		return carrierTelephone;
	}
	public void setCarrierTelephhone(String carrierTelephone) {
		this.carrierTelephone = carrierTelephone;
	}
	
}
