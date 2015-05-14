package com.ink1804.gruzopoisk.jsonClases;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ink1804.gruzopoisk.Activities.InfoRequestAct;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Ink1804 on 14.05.15.
 */
public class geoTask extends AsyncTask<String,Double,String> {
    static final String LOG_TAG = "myLogs";
    public static String res;
   static GoogleMap map;
   public static Double lng=0.0,lat=0.0;
    String city;



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String responseBody="";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://maps.googleapis.com/maps/api/geocode/json?address="+params[0].toString().replace(" ","_"));
        city = params[0].toString();
        try{
        HttpResponse response = httpclient.execute(httppost);
        responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
            res = responseBody;


        }
        catch (Exception e){}
        return responseBody;
    }


    @Override
    protected void onPostExecute(String res) {
        Log.d(LOG_TAG,"завершено");
        //Double lng=0.0,lat=0.0;
        geoTask task =  new geoTask();
        //нужно получать на вход 2 города и обрабатывать их отдельно(2 раза отправлять запрос на сервер для получения данных)
        //на выход получаем координаты городов, которые в дальнейшем можно будет использовать для размещения маркеров на карте
        //пока что работает для одного города, немножно нужно подработать.
        //

        try{
            JSONObject result = new JSONObject(task.res);
            Log.d(LOG_TAG,"+++");
            JSONObject location = new JSONObject(new JSONObject((new JSONObject(new JSONArray(
                    result.getString("results")).getJSONObject(0).getString("geometry"))).toString()).getString("location"));
            lng = Double.parseDouble(location.getString("lng"));
            lat = Double.parseDouble(location.getString("lat"));
            Log.d(LOG_TAG, "lng: " + lng + "\n" + "lat: " + lat);}catch (Exception e){}
        map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(city));
        InfoRequestAct iRAct = new InfoRequestAct();
        iRAct.count++;
       iRAct.setMapCamera(map);

    }

    public void getMap(GoogleMap map){
        this.map = map;

    }
}
