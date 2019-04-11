package com.app.lbs.common.minigps;

public class WI {
	String s; //ssid
	long m;   //mac
	int r;    //rxlev
	public String getS() {
		return s;
	}
	public void setS(String s) {
		if(s != null)
		{
			if(s.length() > 32)
			{
				this.s = s.substring(0, 31);
			}
			else
			{
				this.s = s;
			}
		}
		else
		{
			this.s = s;
		}
		
	}
	public long getM() {
		return m;
	}
	public void setM(long m) {
		this.m = m;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	
}
