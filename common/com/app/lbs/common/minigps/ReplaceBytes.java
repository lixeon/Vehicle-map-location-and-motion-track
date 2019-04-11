package com.app.lbs.common.minigps;

public class ReplaceBytes {
	static public byte[] replace(byte[] obytes)
	{
		//printable 32-126
		//need except:
		//34 "
		//92 \
		byte[] destbytes = new byte[obytes.length];
		for(int i = 0; i < obytes.length; ++i)
		{
			byte b = obytes[i];
			if(b > 126)
			{
				b = (byte)(b - 126);
				if(b < 32)
				{
					b = (byte)(b+32);
				}
			}
			else if(b < 32)
			{
				b = (byte)(b + 126);
				if(b < 32)
				{
					b = (byte)(b+32);
				}
			}
			//except code:
			if(b == 34 || b== 92)
			{
				b += 1;
			}
			destbytes[i] = b;
		}
		return destbytes;
	}
}
