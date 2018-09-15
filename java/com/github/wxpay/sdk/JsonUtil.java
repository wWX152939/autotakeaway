package com.github.wxpay.sdk;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.crypto.Data;

import org.apache.http.client.utils.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON解析封装
 * 
 * @author Andy
 * 
 */
public class JsonUtil {

    private static JSONObject mJsonObject;
    private static JSONArray mJsonArray;
    private static final boolean DEBUG = true;

    private enum JSON_TYPE {
	JSON_TYPE_OBJECT, JSON_TYPE_ARRAY, JSON_TYPE_ERROR
    }

    public static JSONArray resultSetToJsonArry(ResultSet rs) throws SQLException, JSONException {
	JSONArray array = new JSONArray();
	ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
	int columnCount = metaData.getColumnCount();
	while (rs.next()) {
	    JSONObject jsonObj = new JSONObject();
	    for (int i = 1; i <= columnCount; i++) {
		String columnName = metaData.getColumnLabel(i);
		String value = rs.getString(columnName);
		jsonObj.put(columnName, value);
	    }
	    array.put(jsonObj);
	}
	return array;
    }

    /**
     * 将传入的JSON字符串转换成反射调用方法的参数数组
     * 
     * @param jsonObject
     * @return
     */
    public static Object[] getJsonArgs(JSONObject jsonObject) {
	Object[] args = null;
	String arg = "";
	String tmp = "";
	int i = 0;
	while (true) {
	    try {
		tmp = jsonObject.getString("p" + i);
		arg = arg + tmp + "@@";
	    } catch (JSONException e) {
		tmp = "";
	    }
	    if (("".equals(tmp)) || (tmp == null)) {
		args = new Object[i];
		break;
	    }
	    i++;
	}
	args = arg.split("@@");
	return args;
    }


    public static JSONArray retAddResult(Connection conn,PreparedStatement pstat, ResultSet rs, String tab, String id_key, int id_value) {
	JSONArray json = new JSONArray();
	String sql = "SELECT * FROM " + tab + " WHERE " + id_key + "=?";
	try {
	    pstat = conn.prepareStatement(sql);
	    pstat.setInt(1, id_value);
	    rs = pstat.executeQuery();
	    json = resultSetToJsonObject(rs);
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return json;
    }

    public static int getGeneratedKey(PreparedStatement pstat, ResultSet rs) {
	int id = 0;
	try {
	    rs = pstat.getGeneratedKeys();
	    if (rs.next()) {
		id = rs.getInt(1);
	    }
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return id;
    }

    public static JSONArray resultSetToJsonObject(ResultSet rs) throws SQLException {
	JSONArray json = new JSONArray();
	ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
	ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
	int columnCount = md.getColumnCount();
	while (rs.next()) {
	    Map rowData = new HashMap();
	    for (int i = 1; i <= columnCount; i++) {
		// System.out.println(md.getColumnTypeName(i) + "   " +
		// md.getColumnName(i) + "   " + rs.getObject(i));
		rowData.put(md.getColumnLabel(i), formatResValue(md, rs, i));
	    }
	    json.put(rowData);
	}
	return json;
    }
    

    public static JSONArray stringToJsonObject(Map map) throws SQLException {
	JSONArray json = new JSONArray();
	json.put(map);
	return json;
    }

    public static Map resultSetToMap(ResultSet rs) throws SQLException {
	JSONArray json = new JSONArray();
	Map rowData = new HashMap();
	while (rs.next()) {
	    ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
	    ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
	    int columnCount = md.getColumnCount();
	    for (int i = 1; i <= columnCount; i++) {
		// System.out.println(md.getColumnTypeName(i) + "   " +
		// md.getColumnName(i) + "   " + rs.getObject(i));
		rowData.put(md.getColumnLabel(i), formatResValue(md, rs, i));
	    }
	}
	return rowData;
    }

    public static Map resultSetToMapObject(ResultSet rs) throws SQLException {
	Map rowData = new HashMap();
	ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
	ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
	int columnCount = md.getColumnCount();
	while (rs.next()) {
	    rowData = new HashMap();
	    for (int i = 1; i <= columnCount; i++) {
		// System.out.println(md.getColumnTypeName(i) + "   " +
		// md.getColumnName(i) + "   " + rs.getObject(i));
		rowData.put(md.getColumnLabel(i), formatResValue(md, rs, i));
	    }
	}
	return rowData;
    }

    public static JSONArray retUploadJSON(List<Files> fileslist) throws SQLException {
	JSONArray json = new JSONArray();
	json.put(fileslist);
	return json;
    }

    /**
     * 将Java对象转换为json字符串，常用接口
     * 
     * @param bean
     * @return
     */
    public static String toJson(Object bean) {
	return beanToJson(bean);
    }

    /**
     * 将Object转换为Json字符串
     * 
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj) {
	StringBuilder json = new StringBuilder();

	if (obj == null) {
	    json.append("\"\"");
	} else if (obj instanceof String || obj instanceof Float || obj instanceof Boolean || obj instanceof Short
	        || obj instanceof Double || obj instanceof Long || obj instanceof BigDecimal || obj instanceof BigInteger || obj instanceof Byte) {
	    json.append("\"").append(stringToJson(obj.toString())).append("\"");
//	     System.out.println("Name1 = ");
	} else if (obj instanceof Integer) {
	    json.append(stringToJson(obj.toString()));
//	     System.out.println("Name2 = ");
	} else if (obj instanceof Object[]) {
	    json.append(arrayToJson((Object[]) obj));
	} else if (obj instanceof List) {
	    json.append(listToJson((List<?>) obj));
	} else if (obj instanceof Map) {
	    json.append(mapToJson((Map<?, ?>) obj));
	} else if (obj instanceof Set) {
	    json.append(setToJson((Set<?>) obj));
	    // } else if (obj instanceof BasicNameValuePair) {
	    // json.append(nameValueToJson((BasicNameValuePair) obj));
	} else if (obj instanceof Date) {
	    json.append(dateToJson((Date) obj));
	} else {
	    json.append(beanToJson(obj));
	}

	return json.toString();
    }

    // /**
    // * 将BasicNameValuePair对象转换为Json字符串
    // * @param map
    // * @return
    // */
    // public static String nameValueToJson(BasicNameValuePair pair) {
    // StringBuilder json = new StringBuilder();
    // json.append("{");
    //
    // if (pair != null) {
    // json.append(objectToJson(pair.getName()));
    // json.append(":");
    // json.append(objectToJson(pair.getValue()));
    // }
    // json.append("}");
    //
    // return json.toString();
    // }
    /**
     * 将Date转换为Json字符串
     * 
     * @param obj
     * @return
     */
    public static String dateToJson(Date date) {
	return null;
    }

    /**
     * 将Bean转换为Json字符串
     * 
     * @param bean
     * @return
     */
    public static String beanToJson(Object bean) {
	StringBuilder json = new StringBuilder();
	json.append("{");

	Field fs[] = bean.getClass().getDeclaredFields();
	// Log.e(TAG, "bean.getClass = " + bean.getClass().getName());
	if (fs != null) {
	    for (int i = 0; i < fs.length; i++) {
		try {
		    Field field = fs[i];
		    field.setAccessible(true);
		    String name = objectToJson(fs[i].getName());
//		     System.out.println("Name = " + fs[i].getName());
		    String value = objectToJson(fs[i].get(bean));
//		    System.out.println("value = " + fs[i].get(bean));
		    json.append(name);
		    json.append(":");
		    json.append(value);
		    json.append(",");
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    json.setCharAt(json.length() - 1, '}');
	} else {
	    json.append("}");
	}

	return json.toString();
    }

    /**
     * 将list对象列表转换为Json字符串
     * 
     * @param list
     * @return
     */
    public static String listToJson(List<?> list) {
	StringBuilder json = new StringBuilder();
	json.append("[");

	if (list != null && list.size() > 0) {
	    for (Object obj : list) {
		json.append(objectToJson(obj));
		json.append(",");
	    }
	    json.setCharAt(json.length() - 1, ']');
	} else {
	    json.append("]");
	}

	return json.toString();
    }

    // /**
    // * 将list对象列表转换为Json字符串
    // *
    // * @param list
    // * @return
    // */
    // public static String listToJson_(List<?> list) {
    // StringBuilder json = new StringBuilder();
    // json.append("[[");
    //
    // if (list != null && list.size() > 0) {
    // for (Object obj : list) {
    // json.append(objectToJson(obj));
    // json.append(",");
    // }
    // json.setCharAt(json.length() - 1, ']');
    // json.append("]");
    // } else {
    // json.append("]]");
    // }
    //
    // return json.toString();
    // }

    /**
     * 将数组转换为Json字符串
     * 
     * @param array
     * @return
     */
    public static String arrayToJson(Object[] array) {
	StringBuilder json = new StringBuilder();
	json.append("[");

	if (array != null && array.length > 0) {
	    for (Object obj : array) {
		json.append(objectToJson(obj));
		json.append(",");
	    }
	    json.setCharAt(json.length() - 1, ']');
	} else {
	    json.append("]");
	}

	return json.toString();
    }

    /**
     * 将map对象转换为Json字符串
     * 
     * @param map
     * @return
     */
    public static String mapToJson(Map<?, ?> map) {
	StringBuilder json = new StringBuilder();
	json.append("{");

	if (map != null && map.size() > 0) {
	    for (Object key : map.keySet()) {
		json.append(objectToJson(key));
		json.append(":");
		json.append(objectToJson(map.get(key)));
		json.append(",");
	    }
	    json.setCharAt(json.length() - 1, '}');
	} else {
	    json.append("}");
	}

	return json.toString();
    }

    // /**
    // * 将BasicNameValuePair对象转换为Json字符串
    // * @param map
    // * @return
    // */
    // public static String nameValueToJson(BasicNameValuePair pair) {
    // StringBuilder json = new StringBuilder();
    // json.append("{");
    //
    // if (pair != null) {
    // json.append(objectToJson(pair.getName()));
    // json.append(":");
    // json.append(objectToJson(pair.getValue()));
    // }
    // json.append("}");
    //
    // return json.toString();
    // }

    /**
     * 将集合对象转换为Json字符串，很少使用
     * 
     * @param set
     * @return
     */
    public static String setToJson(Set<?> set) {
	StringBuilder json = new StringBuilder();
	json.append("[");

	if (set != null && set.size() > 0) {
	    for (Object obj : set) {
		json.append(objectToJson(obj));
		json.append(",");
	    }
	    json.setCharAt(json.length() - 1, ']');
	} else {
	    json.append("]");
	}

	return json.toString();
    }

    /**
     * 将字符串转换为Json格式字符串
     * 
     * @param s
     * @return
     */
    public static String stringToJson(String s) {
	if (s == null)
	    return "";

	StringBuilder sb = new StringBuilder();

	for (int i = 0; i < s.length(); i++) {
	    char ch = s.charAt(i);
	    switch (ch) {
	    case '"':
		sb.append("\\\"");
		break;
	    case '\\':
		sb.append("\\\\");
		break;
	    case '\b':
		sb.append("\\b");
		break;
	    case '\f':
		sb.append("\\f");
		break;
	    case '\n':
		sb.append("\\n");
		break;
	    case '\r':
		sb.append("\\r");
		break;
	    case '\t':
		sb.append("\\t");
		break;
	    case '/':
		sb.append("\\/");
		break;
	    default:
		if (ch >= '\u0000' && ch <= '\u001F') {
		    String ss = Integer.toHexString(ch);
		    sb.append("\\u");
		    for (int k = 0; k < 4 - ss.length(); k++) {
			sb.append('0');
		    }
		    sb.append(ss.toUpperCase());
		} else {
		    sb.append(ch);
		}
	    }
	}

	return sb.toString();
    }

    private static Object formatResValue(ResultSetMetaData md, ResultSet rs, int i) {
	Object objValue = null;
	try {
	    String type = md.getColumnTypeName(i);
	    objValue = rs.getObject(i);
	    if (type.equals("BLOB")) {
		Blob blob = rs.getBlob(md.getColumnName(i));
		if (null == blob) {
		    return "";
		}
		int bolblen = (int) blob.length();
		byte[] data = blob.getBytes(1, bolblen);
		objValue = new String(data, "UTF-8");
	    }
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return objValue;
    }

    /*
     * 设置内部JSONObject对象，这里不做空指针处理，请在保证json不为null的情况下调用
     */
    public static void setJson(String json) {
	if (DEBUG)
	    System.out.println("json = " + json);
	JSON_TYPE jsonType = JSON_TYPE.JSON_TYPE_ERROR;
	jsonType = getJSONType(json);

	if (DEBUG)
	    System.out.println("jsonType = " + jsonType);

	try {
	    if (jsonType == JSON_TYPE.JSON_TYPE_OBJECT) {
		mJsonObject = new JSONObject(json);
	    } else if (jsonType == JSON_TYPE.JSON_TYPE_ARRAY) {
		mJsonArray = new JSONArray(json);
		if (DEBUG)
		    System.out.println("jsonArray" + mJsonArray);
	    } else {
		if (DEBUG)
		    System.out.println(json + "Json字符串为空或无效！");
	    }
	} catch (JSONException e) {
	    if (DEBUG)
		System.out.println("create json exception");
	    e.printStackTrace();
	}
    }

    /**
     * 静态方式获取对象列表
     * 
     * @param c
     * @param jsonString
     * @return
     */
    public static List<?> getList(Class<?> c, String jsonString) {
	JsonUtil jsonUtil = JsonUtil.getInstance(jsonString);
	return jsonUtil.getList(c);
    }
    
    public static List<?> getList(String key, Class<?> c) {
		List<?> list = null;
		try {
			list = getList(mJsonArray, key, c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

    /**
     * 获取JsonUtis实例对象
     * 
     * @param json
     * @return
     */
    public static JsonUtil getInstance(String json) {
	JsonUtil util = new JsonUtil(json);
	return util;
    }

    public JsonUtil(String json) {
	this.setJson(json);
    }

    /**
     * 获取列表
     * 
     * @param c
     * @param mc
     * @return
     */
    public static List<?> getList(Class<?> c) {
	List<?> list = null;
	try {
	    list = getList(mJsonArray, null, c);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return list;
    }

    /**
     * This method use in jsonObject get list object
     * 
     * @param key
     * @param objectKey
     * @param c
     * @param mc
     * @return list
     * @throws Exception
     */
    public static List<?> getList(JSONArray jsonArray, String key, Class<?> c) throws Exception {
	List<Object> list = new ArrayList<Object>();
	// 空字符串或无效的json字符串
	if (jsonArray == null) {
	    return list;
	}

	if (mJsonObject != null && key != null) {
	    // 获取key对应的json数组对象
	    jsonArray = mJsonObject.getJSONArray(key);
	}
	// jsonArray有有效数据
	if (jsonArray != null && !jsonArray.isNull(0)) {
	    // 列表成员是Java自定义对象
	    if (JSON_TYPE.JSON_TYPE_OBJECT == getJSONType(jsonArray.get(0).toString())) {
		// 循环解析每一个数组成员
		for (int i = 0; i < jsonArray.length(); i++) {
		    JSONObject jsObject = jsonArray.getJSONObject(i);
		    // if (DEBUG)
		    // Log.d(TAG, "jsObject" + jsObject);
		    Object object = getObject(jsObject, null, c);
		    list.add(object);
		}
		// 列表元素还是列表
	    } else if (JSON_TYPE.JSON_TYPE_ARRAY == getJSONType(jsonArray.get(0).toString())) {
		for (int i = 0; i < jsonArray.length(); i++) {
		    JSONArray jsonArray2 = jsonArray.getJSONArray(i);
		    // if (DEBUG)
		    // Log.d(TAG, "jsObject" + jsonArray.getJSONArray(i));
		    list.add(getList(jsonArray2, null, c));
		}
		// 列表元素是普通对象
	    } else {
		for (int i = 0; i < jsonArray.length(); i++) {
		    String element = jsonArray.getString(i);
		    if (!element.equals("")) {
			if (c.equals(Integer.class)) {
			    list.add(Integer.parseInt(jsonArray.getString(i)));
			} else if (c.equals(Double.class)) {
			    list.add(Double.parseDouble(jsonArray.getString(i)));
			} else if (c.equals(String.class)) {
			    list.add(jsonArray.getString(i));
			}
		    }
		}
	    }
	}
	return list;
    }

    /**
     * 获取对象的简单调用
     * 
     * @param c
     * @return
     */
    public static Object getObject(Class<?> c, String json) {
	setJson(json);
	return getObject(null, c);
    }

    /**
     * This Method use in jsonObject get current class with object
     * 
     * @param jsonObject
     * @param key
     *            query key
     * @param c
     *            class
     * @return object
     * @throws Exception
     */
    public static Object getObject(String key, Class<?> c) {
	try {
	    return getObject(mJsonObject, key, c);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * This Method use in jsonObject get current class with object
     * 
     * @param jsonObject
     * @param key
     *            query key
     * @param c
     *            class
     * @param mc
     * @return object
     * @throws Exception
     */
    public static Object getObject(JSONObject jsonObject, String key, Class<?> c) throws Exception {
	if (DEBUG)
	    System.out.println("key == " + key);

	JSONObject jsonObj = null;

	if (jsonObject == null) {
	    if (DEBUG)
		System.out.println("current param jsonobject is null");
	    return null;
	}

	if (key != null) {
	    jsonObj = jsonObject.getJSONObject(key);
	} else {
	    jsonObj = jsonObject;
	}

	if (jsonObj == null) {
	    if (DEBUG)
		System.out.println("in jsonobject not key ");
	    return null;
	}

	Object bean = null;
	if (!c.equals(null)) {
	    if (DEBUG)
		System.out.println("jsonObj = " + jsonObj.toString());

	    // 创建Class<?>类型的实例
	    bean = c.newInstance();

	    // 获取Class<?>的类名
	    String className = c.getName();
	    if (DEBUG)
		System.out.println("classname = " + className);

	    // Java反映射解析Json字符串
	    Field[] fs = c.getDeclaredFields();
	    for (int i = 0; i < fs.length; i++) {
		Field field = fs[i];
		field.setAccessible(true);

		String keyString;
		keyString = field.getName();
		// System.out.println("111111111111      "+ keyString);

		String value = jsonObj.optString(keyString);
		if (DEBUG)
		    System.out.println(field.getName() + "=" + value);

		// 获取字域类型
		Class<?> type = field.getType();
		// 如果value为"null"，或者为""但所属类型不是字符串类型，则直接跳过继续下一循环
		if (value == "null" || (value == "" && !type.equals(String.class)))
		    continue;
		// System.out.println("sssssssssss      "+ value);
		if (type.equals(String.class)) {
		    field.set(bean, value);
		} else if (type.equals(int.class)) {
		    field.setInt(bean, Integer.valueOf(value));
		} else if (type.equals(double.class)) {
		    field.setDouble(bean, Double.valueOf(value));
		} else if (JSON_TYPE.JSON_TYPE_OBJECT == getJSONType(value)) {
		    field.set(bean, getObject(new JSONObject(value), null, type));
		} else if (JSON_TYPE.JSON_TYPE_ARRAY == getJSONType(value)) {
		    // Object中包含list，获取泛型类型
		    Type type2 = field.getGenericType();
		    // 转换为参数化的类型
		    ParameterizedType parameterizedType = (ParameterizedType) type2;
		    // 获取参数类型
		    Type[] types = parameterizedType.getActualTypeArguments();
		    // 打印所有参数化类型
		    if (DEBUG) {
			for (int j = 0; j < types.length; j++)
			    System.out.println("type[" + j + "] = " + types[j]);
		    }
		    // 如果types[0]是class<?>，继续解析
		     if (types[0] instanceof Class<?>)
		     field.set(bean, getList((Class<?>)
		     types[0], value));
		}
	    }
	} else {
	    if (DEBUG)
		System.out.println("class is null");
	    bean = jsonObj.opt(key);
	}

	return bean;
    }

    /**
     * 获取Json字符串的类型
     * 
     * @param jsonString
     * @return
     */
    public static JSON_TYPE getJSONType(String jsonString) {
	if (jsonString == null || jsonString.isEmpty()) {
	    return JSON_TYPE.JSON_TYPE_ERROR;
	}

	char firstChar = jsonString.charAt(0);
	if (firstChar == '{') {
	    return JSON_TYPE.JSON_TYPE_OBJECT;
	} else if (firstChar == '[') {
	    return JSON_TYPE.JSON_TYPE_ARRAY;
	} else {
	    return JSON_TYPE.JSON_TYPE_ERROR;
	}
    }
}
