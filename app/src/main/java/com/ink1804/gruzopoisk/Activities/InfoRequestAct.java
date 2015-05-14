package com.ink1804.gruzopoisk.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.*;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.ink1804.gruzopoisk.R;
import com.ink1804.gruzopoisk.fragmentClases.JSONAnalysis;
import com.ink1804.gruzopoisk.fragmentClases.listF;
import com.ink1804.gruzopoisk.jsonClases.HTTPPost;
import com.ink1804.gruzopoisk.jsonClases.geoTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;
import java.util.Timer;


public class InfoRequestAct extends FragmentActivity {
    static final String LOG_TAG = "myLogs";
    private listF listClass = new listF();
    private ContentValues cv_object = new ContentValues();
    private JSONAnalysis JAnalysClass = new JSONAnalysis();
    int pos;//позиция заявки в списке
    static Double lat,lng,d=0.0,s=0.0;// широта и долгота(средняя)
    public static int count=0;
    private TextView from_toTV,nameTV,pricesTV,volumeTV,about_firmTV,load_dateTV,user_infoTV,noteTV,pre_price;
    private ImageView firm_logo,user_avatar;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_request);
        Intent fromList = getIntent();
        pos = fromList.getIntExtra("position",0);
        ActionBar actionBar = this.getActionBar();
        Spannable title = new SpannableString(listClass.A[0][pos] + " → " + listClass.A[1][pos]);
        title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)),0,title.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(title);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.toolBarColor));

        PersonDataTask personDataTask = new PersonDataTask();
        personDataTask.execute(AuthorizationAct.log, AuthorizationAct.pass, "1",AuthorizationAct.type);
        mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        mMap.getUiSettings().setAllGesturesEnabled(false);

    }

    private void init(){
        from_toTV=(TextView)findViewById(R.id.from_to_km);
        nameTV=(TextView)findViewById(R.id.name);
        pricesTV=(TextView)findViewById(R.id.prices);
        volumeTV=(TextView)findViewById(R.id.volume);
        load_dateTV=(TextView)findViewById(R.id.load_dateTv);
        about_firmTV=(TextView)findViewById(R.id.about_firm);
        noteTV=(TextView)findViewById(R.id.note);
        user_infoTV=(TextView)findViewById(R.id.user_info);
        firm_logo=(ImageView)findViewById(R.id.firm_logo);
        user_avatar=(ImageView)findViewById(R.id.user_avatar);

        try {

            from_toTV.setText(listClass.A[6][pos]);
            nameTV.setText(listClass.A[2][pos]);
            Spanned notesS = Html.fromHtml(("<b>Примечание: </b>"+listClass.A[4][pos]));
            noteTV.setText(notesS);
            Spanned load_dateS = Html.fromHtml(("<b>Дата загрузки: </b>"+listClass.A[15][pos]));
            load_dateTV.setText(load_dateS);
            Spanned prices = Html.fromHtml(("<font color=\"#6abf6c\">"+listClass.A[7][pos]+"</font>, "+"<font color=\"#808080\">"+listClass.A[10][pos]+"</font>"));
            pricesTV.setText(prices);
            volumeTV.setText(listClass.A[8][pos]);
            about_firmTV.setText(listClass.A[9][pos]);
           // user_infoTV.setText(OneJsonObj.getString("user_fio")+"\n"+OneJsonObj.getString("cell")+
             //       "\n"+OneJsonObj.getString("mail"));
            Picasso.with(this).load(listClass.A[11][pos]).into(firm_logo);
            Picasso.with(this).load(listClass.A[12][pos]).into(user_avatar);

        }catch (Exception e){}
        geoTask task =  new geoTask();
        geoTask task2 = new geoTask();
        task.getMap(mMap);
        task.execute(listClass.A[0][pos]);
        task2.getMap(mMap);
        task2.execute(listClass.A[1][pos]);

    }

    public void setMapCamera(GoogleMap map){
        Double vekt=0.0;
        int zoom=0;
        geoTask task =  new geoTask();
        if(count==1){
                lat = task.lat;d=task.lat;
                lng  = task.lng;s=task.lng;}else{
            vekt = Math.sqrt((d-task.lat)*(d-task.lat)+(s-task.lng)*(s-task.lng));
            Log.d(LOG_TAG,"!!"+d+"!!"+s+"!!"+vekt);
            //Toast.makeText(getApplicationContext(),"!!"+d+"!!"+s+"!!"+vekt,Toast.LENGTH_LONG).show();
            lat +=task.lat;
            lng +=task.lng;
        }
        if(count==2){
            if(vekt>0&vekt<=6)zoom = 5;
            if(vekt>6&vekt<=9)zoom = 5;
            if(vekt>9&vekt<=17)zoom = 4;
            if(vekt>17&vekt<=28)zoom = 3;
            if(vekt>28&vekt<=52)zoom = 2;
            if(vekt>52&vekt<=85)zoom = 1;
            if(vekt>85&vekt<=150)zoom = 0;
            LatLng myTarget = new LatLng(lat/2, lng/2);
            //(65.689676, 90.446609) - center of Russia
            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(myTarget)
                    .zoom(zoom)
                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),2000,null);
            count=0;
        }


    }
    /////////////////////поток получений персональных данных//////////////////////////////
    public class PersonDataTask extends AsyncTask<String, Void, ContentValues> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected ContentValues doInBackground(String... params) {
            String url = "https://gruzopoisk.ru/cargo/get_contacts.json?id="+listClass.A[14][pos];
            Log.d(LOG_TAG, url);

            cv_object = HTTPPost.GetResponse(url, params);

            if (isCancelled())
                return null;
            return cv_object;
        }

        @Override
        protected void onPostExecute(ContentValues cv_object) {
            Log.d(LOG_TAG, "Поток авторизации закрыт");
            JAnalysClass.getPersonData();
            }

    }/////////////////////////////////////////////////////////////////////////////////////

    protected void onResume(){
        Intent fromList = getIntent();
        pos = fromList.getIntExtra("position",0);
        init();
        super.onResume();

    }
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.lh_alpha_enter,R.anim.lr_slide_exit);finish();
    }

}


/*Карта
<fragment
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:id="@+id/map"
            tools:context="com.ink1804.gruzopoisk.Activities.InfoRequestAct"
            android:name="com.google.android.gms.maps.SupportMapFragment" />
 */
