package com.app.lbs.common.minigps;


public class LocPoiResponse {
	int Status = 0;
	String Cause = "";
	String Map = "";
	Double Lat = 0.0;
	Double Lon = 0.0;
	String Address = "";
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public String getCause() {
		return Cause;
	}
	public void setCause(String cause) {
		Cause = cause;
	}
	public String getMap() {
		return Map;
	}
	public void setMap(String map) {
		Map = map;
	}
	public Double getLat() {
		return Lat;
	}
	public void setLat(Double lat) {
		Lat = lat;
	}
	public Double getLon() {
		return Lon;
	}
	public void setLon(Double lon) {
		Lon = lon;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
}
