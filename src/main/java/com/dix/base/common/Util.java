package com.dix.base.common;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by dd on 9/8/15.
 */
public class Util {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(Util.class);

    public static Long time()
    {
        return (System.currentTimeMillis() / 1000);
    }

    public static String timeFormat(long time)
    {
        DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time * 1000);
        return formatter.format(date);
    }

    public static String timeFormat(long time, String format)
    {
        DateFormat formatter= new SimpleDateFormat(format);
        Date date = new Date(time * 1000);
        return formatter.format(date);
    }

    public static Integer parseInt(Object obj, Integer optional)
    {
        if (obj instanceof Long)
        {
            return ((Long) obj).intValue();
        }

        if (obj instanceof Integer)
        {
            return (Integer)obj;
        }

        try
        {
            return (int)Double.parseDouble(obj.toString());
        }
        catch (Exception e)
        {
            logger.error("parse Integer error: {}, source: \n{}", e.getMessage(), obj);
        }

        return optional;
    }

    public static Integer parseInt(Object obj)
    {
        return parseInt(obj, 0);
    }

    public static Long parseLong(Object obj, Long optional)
    {
        if (obj instanceof Long)
        {
            return (Long) obj;
        }

        if (obj instanceof Integer)
        {
            return ((Integer)obj).longValue();
        }

        try
        {
            return (long)Double.parseDouble(obj.toString());
        }
        catch (Exception e)
        {
            logger.error("parse Long error: {}, source: \n{}", e.getMessage(), obj);
        }

        return optional;
    }

    public static Long parseLong(Object obj)
    {
        return parseLong(obj, 0L);
    }

    public static Double parseDouble(Object obj, Double optional)
    {
        if (obj instanceof Double)
        {
            return (Double) obj;
        }

        try
        {
            return Double.parseDouble(obj.toString());
        }
        catch (Exception e)
        {
            logger.error("parse Double error: {}, source: \n{}", e.getMessage(), obj);
        }

        return optional;
    }

    public static Double parseDouble(Object obj)
    {
        return parseDouble(obj, (double) 0);
    }

    public static String generateRandomString(int length)
    {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABSDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        Random r = new Random(System.currentTimeMillis());
        char[] id = new char[length];
        for (int i = 0;  i < length;  i++) {
            id[i] = chars[r.nextInt(chars.length)];
        }
        return new String(id);
    }

    public static String generateRandomNumberString(int length)
    {
        char[] chars = "1234567890".toCharArray();
        Random r = new Random(System.currentTimeMillis());
        char[] id = new char[length];
        for (int i = 0;  i < length;  i++) {
            id[i] = chars[r.nextInt(chars.length)];
        }
        return new String(id);
    }

    public static String implode(List list, String glue)
    {
        if (list==null || list.size() == 0) {
            return null;
        }

        StringBuilder result=new StringBuilder();
        boolean flag = false;
        for (Object o : list) {
            if (flag) {
                result.append(glue);
            }else {
                flag=true;
            }
            result.append(o.toString());
        }
        return result.toString();
    }

    public static Map<String, Integer> convertDecToIntValueByKeys(Long dec, List<String> keys) {
        Map<String, Integer> map = new HashMap<>();
        int length = keys.size();
        int i = 0;
        for (; i < length; i++) {
            String key = keys.get(i);
            int bitValue = (int) Math.pow(2, i);
            if ((dec&bitValue) == bitValue) {
                map.put(key, 1);
            }else {
                map.put(key, 0);
            }
        }
        return map;
    }

    public static String filterIdString(String idString)
    {
        HashSet<String> idStringSet = Sets.newHashSet(Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split(idString));
        return Joiner.on(",").join(idStringSet);
    }



    private static Gson gson = new Gson();

    public static String jsonEncode(Object object)
    {
        return gson.toJson(object);
    }

    public static <T> T jsonDecode(String jsonString, Type type)
    {
        if (jsonString == null) return null;

        T object = null;
        try
        {
            object = gson.fromJson(jsonString, type);
        }
        catch (Exception e)
        {
            logger.error("json decode error: {}, source: \n{}", e.getMessage(), jsonString);
        }

        return object;
    }


    public static boolean containsKey(Map map, Object key) {
        return map != null && key != null && map.containsKey(key);
    }

    public static boolean containsKey(Map map, Object... keys) {
        if (map == null || keys == null)
        {
            return false;
        }

        for (Object key : keys)
        {
            if (!map.containsKey(key))
            {
                return false;
            }
        }

        return true;
    }

    public static String getStackTrace(final Throwable throwable) {
        return getStackTrace(throwable, 5);
    }

    public static String getStackTrace(final Throwable throwable, int maxLines) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        String traces = sw.getBuffer().toString();
        List traceList = Lists.newArrayList(Splitter.on("\n").trimResults().omitEmptyStrings().split(traces));
        maxLines = maxLines > traceList.size() ? traceList.size() : maxLines;
        return Joiner.on("\n    ").join(traceList.subList(0, maxLines));
    }




    // 手机号验证
    private static Pattern patternPhoneNumber = Pattern.compile("^1[3458][0-9]\\d{8}$");
    public static Boolean isPhoneNumber(String content)
    {
        return patternPhoneNumber.matcher(content).matches();
    }

    // 邮箱验证
    private static Pattern patternEmail = Pattern.compile("^[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}$");
    public static Boolean isEmail(String content)
    {
        return patternEmail.matcher(content).matches();
    }

    // 身份证验证
    private static Pattern patternIDNumber = Pattern.compile("^(\\d{6})(18|19|20)?(\\d{2})([01]\\d)([0123]\\d)(\\d{3})(\\d|X|x)?$");
    public static Boolean isIDCard(String content)
    {
        return patternIDNumber.matcher(content).matches();
    }

    // 邮编验证
    private static Pattern patternPostcode = Pattern.compile("^[1-9]\\d{5}$");
    public static Boolean isPostcode(String content)
    {
        return patternPostcode.matcher(content).matches();
    }

    /**
     * url验证
     * https://mathiasbynens.be/demo/url-regex
     */
    private static Pattern patternUrl = Pattern.compile("_^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!10(?:\\.\\d{1,3}){3})(?!127(?:\\.\\d{1,3}){3})(?!169\\.254(?:\\.\\d{1,3}){2})(?!192\\.168(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)*(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}]{2,})))(?::\\d{2,5})?(?:/[^\\s]*)?$_iuS");
    public static Boolean isUrl(String content)
    {
        return patternUrl.matcher(content).matches();
    }


}
