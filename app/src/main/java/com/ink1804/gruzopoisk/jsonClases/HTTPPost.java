package com.ink1804.gruzopoisk.jsonClases;

import android.content.ContentValues;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

// Класс отправки POST запроса на сервер.
// после получения ответа отправляет результат в класс ParseJSON. в нем парсится строка и возвращает объект ContentValues
// для работы с сетью нужно добавить в манифест     <uses-permission android:name="android.permission.INTERNET" />
//<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
public class HTTPPost {
    static final String LOG_TAG = "myLogs";

    public static ContentValues GetResponse(String HTTPUrl,String... params) {
        ContentValues cv_object = new ContentValues();
        HttpClient httpclient = new DefaultHttpClient();
       // String HTTPUrl = "https://gruzopoisk.ru/account/auth.json";
        HttpPost httppost = new HttpPost(HTTPUrl);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        if(params[3].toString().equals("cell"))
        nameValuePairs.add(new BasicNameValuePair("cell", params[0].toString()));
        else
        nameValuePairs.add(new BasicNameValuePair("email", params[0].toString()));
        nameValuePairs.add(new BasicNameValuePair("password", params[1].toString()));
        nameValuePairs.add(new BasicNameValuePair("remember", params[2].toString()));
        nameValuePairs.add(new BasicNameValuePair("type", params[3].toString()));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {

            Log.d(LOG_TAG, "text = " + e.toString());
            e.printStackTrace();
        }
        try {
            HttpResponse response = httpclient.execute(httppost);
            String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

            cv_object = ParseJSON.ParseObjectJSON(responseBody);
            Log.d(LOG_TAG, "response = " + responseBody);
        } catch (ClientProtocolException e) {

            Log.d(LOG_TAG, "text = " + e.toString());
            e.printStackTrace();
        } catch (IOException e) {

            Log.d(LOG_TAG, "text = " + e.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return cv_object;
    }
}
