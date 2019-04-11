package com.app.lbs.common.minigps;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


public class JsonUtil {
	private static ObjectMapper mapper = null;
	private static ObjectMapper getMapper()
	{
		if(mapper == null)
		{
			mapper = new ObjectMapper();
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_FIELDS, true);
			mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, true);
			mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		}
		return mapper;
	}
	public static String toJson(Object o)
	{
		String result = null;
		
		try {
			result = getMapper().writeValueAsString(o);
		} catch (JsonGenerationException e) {
			
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.gc();
			e.printStackTrace();
		}
		return result;
	}
	
	public static Object fromJson(Class<?> cls, String jsonStr)
	{
		Object result = null;
		 
		try {
			if((jsonStr.indexOf('{') >=0) && jsonStr.length() > 0)
			{
				result =  getMapper().readValue(jsonStr, cls);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
		return result;
	}
}
