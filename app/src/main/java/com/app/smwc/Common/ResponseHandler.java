package com.app.smwc.Common;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResponseHandler {
    Context context;
    public ResponseHandler(Context context) {
        this.context = context;
    }
    public static ArrayList<String> getListFromJSon(JSONArray food_types) {
        ArrayList<String> strings = new ArrayList<>();

        if (food_types != null) {
            for (int i = 0; i < food_types.length(); i++) {
                try {
                    strings.add(food_types.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return strings;
        } else {

            return null;
        }

    }

    public static boolean isSuccess(String strResponse, JSONObject jsonResponse) {
        if (strResponse != null) {
            JSONObject jsonObject = createJsonObject(strResponse);
            if (jsonObject != null) {
                return getBool(jsonObject, "status");
            }
        } else if (jsonResponse != null) {
            return getBool(jsonResponse, "status");
        }
        return false;
    }

    public static JSONObject createJsonObject(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getString(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? jObj.getString(strKey) : "";
        } catch (JSONException e) {
            return "";
        }
    }

    public static int getInt(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? jObj.getInt(strKey) : 0;
        } catch (JSONException e) {
            return 0;
        }
    }

    public static float getFloat(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? (float) jObj.getDouble(strKey) : 0;
        } catch (JSONException e) {
            return 0;
        }
    }

    public static boolean getBool(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) && jObj.getBoolean(strKey);
        } catch (JSONException e) {
            return false;
        }
    }

    public static JSONObject getJSONObject(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? jObj.getJSONObject(strKey) : new JSONObject();
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public static JSONArray getJSONArray(JSONObject jObj, String strKey) {
        try {
            return (jObj.has(strKey) && !jObj.isNull(strKey)) ? jObj.getJSONArray(strKey) : new JSONArray();
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

}

