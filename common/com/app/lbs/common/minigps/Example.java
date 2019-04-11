package com.app.lbs.common.minigps;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


//just for demo
//the cells and the wifis not the real data
public class Example {
	public static void main(String args[])
	{
		List<Cells> cells = getCellsList();
		List<WI> ws = getWifisList();
		int mt = 0; //get the std(wgs84) lat and lon
		LocRadiusPoiResponse loc = getPOIFromMinigps(cells, ws, mt);
		if(loc != null)
		{
			System.out.print(String.format("%f,%f,%d", loc.getLat(), loc.getLon(), loc.getRadius()));
		}
		else
		{
			System.out.print("getPOIFromMinigps is null, please check");
		}
	}
	
	private String getLocationSMS(String imsi, List<Cells> cells, double lat, double lon)
	{
		String sms = null;
		String crcSource = null;
		String lStr = null;
		String xStr = null;
		String zStr = null;
		String n1Str = null;
		if(cells.size() > 0)
		{
			//get the xStr zStr
			xStr = imsi.substring(0, 8);
			zStr = imsi.substring(9);
			//get the n1Str
			Cells c = cells.get(0);
			n1Str = String.format("%d-%d-%d-%d-%d", c.getMcc(), c.getMnc(), c.getLac(), c.getCellid(),
					c.getSdb());
			if(lat != 0 || lon != 0)
			{
				lStr = String.format("%f,%f", lat, lon);
			}
			if (lStr != null) {
				crcSource = String.format("%s%s%s%s", xStr, zStr, n1Str, lStr);
			} else {
				crcSource = String.format("%s%s%s", xStr, zStr, n1Str);
			}
			byte[] crcByte = crcSource.getBytes();
			char crcChar = 0;
			int crcInt = 0;
			crcChar = MyCrc16.crcTable(crcByte);
			crcInt = crcChar;
			String XParameter = getXParameter(cells);
			String.format("http://agps.co/a?x=%s-%d-%s-%s", xStr, crcInt, zStr, XParameter);
		}
		return sms;
	}
	
//	http://minigps.net/cw?x=1cc-0-6212-2F8C-AC-52EC-28AD-96-6212-2F8B-96-6212-FAD-94-6212-3FB5-93-6212-FAB-93
//	{"ws":[{"s":"xo","r":81,"m":804380873802619826},{"s":"terry","r":69,"m":2018924576320342756},{"s":"TP-LINK_3225EE","r":53,"m":674173120793097686},{"s":"loushangshengyinxiaodian","r":49,"m":44590646795096412}]}
	static public LocRadiusPoiResponse getPOIFromMinigps(List<Cells> cells, List<WI> ws, int mt)
	{
		LocRadiusPoiResponse loc = null;
		String result = null;
		String url = "http://minigps.net/cw?p=1&needaddress=0&mt=" + mt;
		String x = getXParameter(cells);
		String w = getWParameter(ws);
		if(x != null)
		{
			url = url + "&x=" + x;
		}
		if(w != null)
		{
			result = RemoteUtil.request(url, "POST", "application/json;charset=utf-8", w);
		}
		else
		{
			result = RemoteUtil.request(url, "GET", "application/json;charset=utf-8", null);
		}
		if(result != null)
		{
			System.out.print(result);
			loc = (LocRadiusPoiResponse) JsonUtil.fromJson(LocRadiusPoiResponse.class, result);
		}
		return loc;
	}
	
	static private String getXParameter(List<Cells> cells)
	{
		String x = null;
		if(cells != null && cells.size() > 0)
		{
			//mcc-mnc
			x = "%x-%x";
			Cells c = cells.get(0);
		    x = String.format(x, c.getMcc(), c.getMnc());
		    for(int i = 0; i < cells.size(); ++i)
		    {
			    c = cells.get(i);
			    x += "-%x-%x-%x";
			    x = String.format(x, c.getLac(), c.getCellid(), c.getSdb());
		    }
		}
		return x;
	}
	
	static private String getWParameter(List<WI> ws)
	{
		String w = null;
		if(ws != null && ws.size() > 0)
		{
			WifiList wlist = new WifiList();
			wlist.setWs(ws);
			w = JsonUtil.toJson(wlist);
		}
		return w;
	}
	

	private static List<WI> getWifisList() {
		List<WI> wis = new ArrayList<WI>();

		try {
			//{"s":"xo","r":81,"m":804380873802619826} //64:09:80:1F:DE:42
			String mac = "64:09:80:1F:DE:42";
			//b855100a0673
			String testmac= "b855100a0673";
			long testlong = WifiAddressConverter.HexStr2Long(testmac);
			System.out.println(testlong);
			long m = WifiAddressConverter.Str2Long(mac);
			int r = 81;
			String theRealSSID = "xo";
			String replacedSSID = new String(ReplaceBytes.replace(theRealSSID.getBytes("utf-8")), "utf-8");
			WI wi = new WI();
			wi.setM(m);
			wi.setR(r);
			wi.setS(replacedSSID);
			wis.add(wi);
			
			//{"s":"terry","r":69,"m":2018924576320342756} //FC:75:16:C5:B0:84
			mac = "FC:75:16:C5:B0:84";
			m = WifiAddressConverter.Str2Long(mac);
			r = 69;
			theRealSSID = "terry";
			replacedSSID = new String(ReplaceBytes.replace(theRealSSID.getBytes("utf-8")), "utf-8");
			wi = new WI();
			wi.setM(m);
			wi.setR(r);
			wi.setS(replacedSSID);
			wis.add(wi);
			
			//{"s":"TP-LINK_3225EE","r":53,"m":674173120793097686} //54:E6:FC:32:25:EE
			mac = "54:E6:FC:32:25:EE";
			m = WifiAddressConverter.Str2Long(mac);
			r = 53;
			theRealSSID = "TP-LINK_3225EE";
			replacedSSID = new String(ReplaceBytes.replace(theRealSSID.getBytes("utf-8")), "utf-8");
			wi = new WI();
			wi.setM(m);
			wi.setR(r);
			wi.setS(replacedSSID);
			wis.add(wi);
			
			//{"s":"loushangshengyinxiaodian","r":49,"m":44590646795096412} //0C:72:2C:CD:F1:B0
			mac = "0C:72:2C:CD:F1:B0";
			m = WifiAddressConverter.Str2Long(mac);
			r = 49;
			theRealSSID = "loushangshengyinxiaodian";
			replacedSSID = new String(ReplaceBytes.replace(theRealSSID.getBytes("utf-8")), "utf-8");
			wi = new WI();
			wi.setM(m);
			wi.setR(r);
			wi.setS(replacedSSID);
			wis.add(wi);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wis;
	}
	

	private static List<Cells> getCellsList() {
		//1cc-0-6212-2F8C-AC-52EC-28AD-96-6212-2F8B-96-6212-FAD-94-6212-3FB5-93-6212-FAB-93
		List<Cells> cells = new ArrayList<Cells>();
		//add cell info 1cc-0-6212-2F8C-AC
		Cells c = new Cells();
		c.setMcc(460);
		c.setMnc(0);
		c.setLac(0x6212);
		c.setCellid(0x2F8C);
		c.setSdb(0xAC);
		cells.add(c);
		
		//add cell info 1cc-0-52EC-28AD-96
		c = new Cells();
		c.setMcc(460);
		c.setMnc(0);
		c.setLac(0x52EC);
		c.setCellid(0x28AD);
		c.setSdb(0x96);
		cells.add(c);
		
		//add cell info 1cc-0-6212-2F8B-96
		c = new Cells();
		c.setMcc(460);
		c.setMnc(0);
		c.setLac(0x6212);
		c.setCellid(0x2F8B);
		c.setSdb(0x96);
		cells.add(c);
		
		//add cell info 1cc-0-6212-FAD-94
		c = new Cells();
		c.setMcc(460);
		c.setMnc(0);
		c.setLac(0x6212);
		c.setCellid(0xFAD);
		c.setSdb(0x94);
		cells.add(c);
		
		//add cell info 1cc-0-6212-3FB5-93
		c = new Cells();
		c.setMcc(460);
		c.setMnc(0);
		c.setLac(0x6212);
		c.setCellid(0x3FB5);
		c.setSdb(0x93);
		cells.add(c);
		
		//add cell info 1cc-0-6212-FAB-93
		c = new Cells();
		c.setMcc(460);
		c.setMnc(0);
		c.setLac(0x6212);
		c.setCellid(0xFAB);
		c.setSdb(0x93);
		cells.add(c);
		
		return cells;
	}
}
