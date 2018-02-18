package com.ismael.fastrecipes.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import com.ismael.fastrecipes.R;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorMapUtils {

    private static Map<String, String> map = null;
    private static int hashMapResId = R.xml.map_error;

    public static Map<String, String> getErrorMap(Context c) {
        if (map == null) {
            XmlResourceParser parser = c.getResources().getXml(hashMapResId);

            String key = null, value = null;

            try {
                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        Log.d("utils", "Start document");
                    } else if (eventType == XmlPullParser.START_TAG) {
                        if (parser.getName().equals("map")) {
                            boolean isLinked = parser.getAttributeBooleanValue(null, "linked", false);

                            map = isLinked
                                    ? new LinkedHashMap<String, String>()
                                    : new HashMap<String, String>();
                        } else if (parser.getName().equals("entry")) {
                            key = parser.getAttributeValue(null, "key");

                            if (null == key) {
                                parser.close();
                                return null;
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (parser.getName().equals("entry")) {
                            map.put(key, value);
                            key = null;
                            value = null;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (null != key) {
                            value = parser.getText();
                        }
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return map;
    }
}
