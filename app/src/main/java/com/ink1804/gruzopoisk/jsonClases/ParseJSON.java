package com.ink1804.gruzopoisk.jsonClases;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ink1804 on 28.02.15.
 */
public class ParseJSON {
    static final String LOG_TAG = "myLogs";
    public static String jsonString;

    public static ContentValues ParseObjectJSON(String JSONText) throws JSONException {
        ContentValues cv_object = new ContentValues();
        Log.d(LOG_TAG, "ParseObjectJSON = " + JSONText);
        jsonString = JSONText;
        JSONObject jObject = new JSONObject(JSONText);
        cv_object.put("success", jObject.getString("success"));
        cv_object.put("data", jObject.getString("data"));
        cv_object.put("errors",jObject.getString("errors"));

        return cv_object;



        // официальная документация http://developer.android.com/reference/org/json/package-summary.html
        // почитать можно сдесь http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android
        // почитать можно сдесь http://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/
    }
}
