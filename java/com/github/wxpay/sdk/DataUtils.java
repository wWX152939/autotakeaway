package com.github.wxpay.sdk;

import java.io.StringReader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;



public class DataUtils {

	public static Object getObject(Class<?> class1,
			String responseBody) {
		JsonReader jsonReader = new JsonReader(new StringReader(responseBody));//其中jsonContext为String类型的Json数据    
		jsonReader.setLenient(true);
		return new Gson().fromJson(jsonReader, class1);
	}

	public static String beanToJson(Object object) {
		return new Gson().toJson(object);
	}

}
