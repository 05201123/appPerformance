package com.jh.util;

import java.lang.reflect.Type;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jh.exception.JHException;
/**
 * json数据处理类
 * @author jhzhangnan1
 *
 */
public class GsonUtil {
	 /** 空的 {@code JSON} 数据 - <code>"{}"</code>。 */
    public static final String EMPTY_JSON = "{}";
    /** 空的 {@code JSON} 数组(集合)数据 - {@code "[]"}。 */
    public static final String EMPTY_JSON_ARRAY = "[]";
    /** 默认的 {@code JSON} 日期/时间字段的格式化模式。 */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
	/**
	 * Gson对象反序列化
	 * @param str 字符串
	 * @param value 类对象
	 * @return 反序列化后对象
	 * @throws JSONException
	 */
	public static <T> T parseMessage(String str, Class<T> value)
			throws JSONException {
		try {
//			Gson gson = new Gson();
			Gson gson = dealDateWithDotNet();
			return gson.fromJson(str, value);
		} catch (JsonSyntaxException e) {
			throw new JSONException();
		}
	}
	/**
	 * GSon反序列化为列表
	 * @param str
	 * @param value
	 * @return
	 * @throws JSONException
	 */
	public static <T> List<T> parseList(String str,Class<T> classItem)throws JSONException
	{
		
		//Log.e("value", value.getGenericSuperclass().toString());
		Gson gson = dealDateWithDotNet();
		JsonParser parser = new JsonParser();
        Type listType = new TypeToken<ArrayList>(){}.getType();
        JsonElement element = parser.parse(str);
        JsonArray array = element.getAsJsonArray();
        LinkedList result = new LinkedList();
        for(int ii=0;ii<array.size();ii++){
        	result.add(gson.fromJson(array.get(ii), classItem));
        }
		return result;
	}
	/**
	 * GSon反序列化为列表
	 * @param str
	 * @param value
	 * @return
	 * @throws JSONException
	 */
	public static <T> List<T> parseList(String str,TypeToken<List<T>> token)throws JSONException
	{
		
		//Log.e("value", value.getGenericSuperclass().toString());
		Gson gson = dealDateWithDotNet();
		

		List<T> results = gson.fromJson(str, token.getType());
		return results;
	}
	/**
	 * Json 转换为对象 某平台下自己的实现
	 * @param str
	 * @param value
	 * @param hashMap 
	 * @return
	 * @throws JSONException
	 */
	public static <T> T parseMessage(String str, Class<T> value,HashMap<Type, Object> hashMap)
	        throws JSONException {
	    try {
//			Gson gson = new Gson();
	        Gson gson = dealDateWithDotNet();
	        return getJson(hashMap).fromJson(str, value);
	    } catch (JsonSyntaxException e) {
	        throw new JSONException();
	    }
	}
	/**
	 * json序列化
	 * @param value 待序列化对象
	 * @return
	 * @throws JSONException
	 */
	public static String format(Object value) throws JSONException {
		try {
//			Gson gson = new Gson();
			Gson gson = dealDateWithDotNet();
			return gson.toJson(value);
		} catch (JsonSyntaxException e) {
			throw new JSONException();
		}
	}

	
	private static Gson dealDateWithDotNet() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new WcfDateJsonAdapter());
		return builder.create();
	} 
	
	/**
	 * 对象转换为JSON， hashMap中加入对应的需要转换的类型 和该平台下的实现对象。
	 * 
	 * @param value
	 * @param hashMap
	 * @return
	 * @throws JSONException
	 */
	public static String format(Object value, HashMap<Type, Object> hashMap)
			throws JSONException {
		try {
			return getJson(hashMap).toJson(value);

		} catch (JsonSyntaxException e) {
			throw new JSONException();
		}
	}

    /**
     * @param hashMap
     * @return
     */
    private static Gson getJson(HashMap<Type, Object> hashMap) {
        GsonBuilder builder = new GsonBuilder();
        if (hashMap != null) {
        	Iterator<Entry<Type, Object>> iterator = hashMap.entrySet()
        			.iterator();
        	while (iterator.hasNext()) {
        		Entry<Type, Object> entry = iterator.next();
        		builder.registerTypeAdapter(entry.getKey(),
        				entry.getValue());
        	}
        }
        builder.registerTypeAdapter(Date.class, new WcfDateJsonAdapter());
        return builder.create();
    }

    /**
     * 将给定的目标对象根据指定的条件参数转换成 {@code JSON} 格式的字符串。
     * <p />
     * <strong>该方法转换发生错误时，不会抛出任何异常。若发生错误时，曾通对象返回 <code>"{}"</code>； 集合或数组对象返回 <code>"[]"</code>
     * </strong>
     * 
     * @param target 目标对象。
     * @param targetType 目标对象的类型。
     * @param isSerializeNulls 是否序列化 {@code null} 值字段。
     * @param version 字段的版本号注解。
     * @param datePattern 日期字段的格式化模式。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, boolean isSerializeNulls, Double version,
            String datePattern, boolean excludesFieldsWithoutExpose) {
        if (target == null) return EMPTY_JSON;
        GsonBuilder builder = new GsonBuilder();
        if (isSerializeNulls) builder.serializeNulls();
        if (version != null) builder.setVersion(version.doubleValue());
        if (TextUtils.isEmpty(datePattern)) datePattern = DEFAULT_DATE_PATTERN;
        builder.setDateFormat(datePattern);
        if (excludesFieldsWithoutExpose) builder.excludeFieldsWithoutExposeAnnotation();
        return toJson(target, targetType, builder);
    }
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target) {
        return toJson(target, null, false, null, null, true);
    }
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param datePattern 日期字段的格式化模式。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, String datePattern) {
        return toJson(target, null, false, null, datePattern, true);
    }
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param version 字段的版本号注解({@literal @Since})。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Double version) {
        return toJson(target, null, false, version, null, true);
    }
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, boolean excludesFieldsWithoutExpose) {
        return toJson(target, null, false, null, null, excludesFieldsWithoutExpose);
    }
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean} 对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param version 字段的版本号注解({@literal @Since})。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Double version, boolean excludesFieldsWithoutExpose) {
        return toJson(target, null, false, version, null, excludesFieldsWithoutExpose);
    }
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSSS}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType) {
        return toJson(target, targetType, false, null, null, true);
    }
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSSS}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @param version 字段的版本号注解({@literal @Since})。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, Double version) {
        return toJson(target, targetType, false, version, null, true);
    }
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, null, null, excludesFieldsWithoutExpose);
    }
    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss SSS}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @param version 字段的版本号注解({@literal @Since})。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, Double version, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, version, null, excludesFieldsWithoutExpose);
    }
    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。
     * 
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param token {@code com.google.gson.reflect.TypeToken} 的类型指示类对象。
     * @param datePattern 日期格式模式。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, TypeToken<T> token, String datePattern) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        if (TextUtils.isEmpty(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        Gson gson = builder.create();
        try {
            return (T)gson.fromJson(json, token.getType());
        } catch (Exception ex) {
           
            return null;
        }
    }
    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。
     * 
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param token {@code com.google.gson.reflect.TypeToken} 的类型指示类对象。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, TypeToken<T> token) {
        return fromJson(json, token, null);
    }
    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean} 对象。</strong>
     * 
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param clazz 要转换的目标类。
     * @param datePattern 日期格式模式。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, Class<T> clazz, String datePattern) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        if (TextUtils.isEmpty(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        Gson gson = builder.create();
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception ex) {
           
            return null;
        }
    }
    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean} 对象。</strong>
     * 
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param clazz 要转换的目标类。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(json, clazz, null);
    }
    /**
     * 将给定的目标对象根据{@code GsonBuilder} 所指定的条件参数转换成 {@code JSON} 格式的字符串。
     * <p />
     * 该方法转换发生错误时，不会抛出任何异常。若发生错误时，{@code JavaBean} 对象返回 <code>"{}"</code>； 集合或数组对象返回
     * <code>"[]"</code>。 其本基本类型，返回相应的基本值。
     * 
     * @param target 目标对象。
     * @param targetType 目标对象的类型。
     * @param builder 可定制的{@code Gson} 构建器。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.1
     */
    public static String toJson(Object target, Type targetType, GsonBuilder builder) {
        if (target == null) return EMPTY_JSON;
        Gson gson = null;
        if (builder == null) {
            gson = new Gson();
        } else {
            gson = builder.create();
        }
        String result = EMPTY_JSON;
        try {
            if (targetType == null) {
                result = gson.toJson(target);
            } else {
                result = gson.toJson(target, targetType);
            }
        } catch (Exception ex) {
          
            if (target instanceof Collection<?> || target instanceof Iterator<?> || target instanceof Enumeration<?>
                    || target.getClass().isArray()) {
                result = EMPTY_JSON_ARRAY;
            }
        }
        return result;
    }
	/**
	 * 日期格式的互转换
	 * @author xiongyt
	 *
	 */
	 public static class WcfDateJsonAdapter implements JsonDeserializer<Date>, JsonSerializer<Date> {
	        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
	                                throws JsonParseException {
	            String JSONDateToMilliseconds = "\\/(Date\\((.*?)(\\+.*)?\\))\\/";
	            Pattern pattern = Pattern.compile(JSONDateToMilliseconds);
	            Matcher matcher = pattern.matcher(json.getAsJsonPrimitive().getAsString());
	            matcher.matches();
	            String tzone = matcher.group(3);
	            String result = matcher.replaceAll("$2");

	            Calendar calendar = new GregorianCalendar();
	            calendar.setTimeZone(TimeZone.getTimeZone("GMT" + tzone));
	            calendar.setTimeInMillis(Long.valueOf(result));

	            return calendar.getTime();
	        }

	        @Override
	        public JsonElement serialize(Date dt, Type arg1, JsonSerializationContext arg2) {
	            TimeZone tz = TimeZone.getDefault();
	            
	            //TODO timezone
	            String dateStr = "/Date(" + dt.getTime() + "+0800)/";
	            return new JsonPrimitive(dateStr);
	        }
	    }
	 public static class JSONException extends JHException {

			/**
			 * 序列化
			 */
			private static final long serialVersionUID = 3643487842618312404L;

			@Override
			public String getMessage() {
				// TODO Auto-generated method stub
				return "JSON解析异常";
			}
		}
}

