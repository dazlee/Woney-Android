package com.woney.data;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by houan on 2016/12/6.
 */

public abstract class CoreData {

    protected ContentValues values;
    protected boolean isUpdate;

    public CoreData() {
        this.values = new ContentValues();
    }

    public void setValuesByKey(String key, String value) {
        values.put(key, value);
    }

    public String getStringByKey(String key) {
        String value = values.getAsString(key);
        return value != null ? value : "";
    }

    public Integer getIntegerByKey(String key) {
        Integer value = values.getAsInteger(key);
        return value != null ? value : 0;
    }

    public void updateValue(String key, String arg1, String arg2) {
        if (arg1 == null || !arg1.equals(arg2)) {
            isUpdate = true;
            setValuesByKey(key, arg2);
        }
    }

    public void updateValueByJson(String []keyArray, JSONObject jsonObject) {
        Iterator<String> it = jsonObject.keys();
        List<String> keyList = Arrays.asList(keyArray);
        try {
            while (it.hasNext()) {
                String key = it.next();
                if (keyList.contains(key)) {
                    updateValue(key, getStringByKey(key), jsonObject.getString(key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getReqJson(String []keyArray) {
        JSONObject jsonObject = new JSONObject();
        List<String> keyList = Arrays.asList(keyArray);

        try {
            for (String key : values.keySet()) {
                if (keyList.contains(key)) {
                    jsonObject.put(key, values.getAsString(key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
