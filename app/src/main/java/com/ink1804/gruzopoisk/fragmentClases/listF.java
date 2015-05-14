package com.ink1804.gruzopoisk.fragmentClases;

import android.app.ActionBar;
import android.app.ListFragment;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.*;
import android.widget.ListView;
import android.widget.Toast;

import com.ink1804.gruzopoisk.Activities.AuthorizationAct;
import com.ink1804.gruzopoisk.Activities.InfoRequestAct;
import com.ink1804.gruzopoisk.R;
import com.ink1804.gruzopoisk.jsonClases.HTTPPost;
import com.ink1804.gruzopoisk.jsonClases.ParseJSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ink1804 on 15.02.15.
 */
public class listF extends ListFragment {
    private SwipeRefreshLayout swUpdate;
    static final String LOG_TAG = "myLogs";
    private ContentValues cv_object = new ContentValues();
    private ListView myList;
    private Timer timerUpdate;
    private TimerTask timerTaskUpdate;

    /////////Блок переменных/////////
    public int size = 15,NumofPost=1;// количество первичных записей,номер запроса на расширение списка
    public static String A[][] = new String[17][500];
    public static String JsonBase="";
    private String direction="direction=up";
    public static boolean dataInArray=false;//проверка, нужно ли докачивать данные(если количество записей больше чем Numofdata*30)


//все, что происходит в данном классе - продаелки всевышнего! никто не знает как это работает, главное что
//работает! если что-то не работает, то оно и не должно работать


    private JSONAnalysis JAnalysClass = new JSONAnalysis();

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swUpdate = (SwipeRefreshLayout) getActivity().findViewById(R.id.refresh);

        swUpdate.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(LOG_TAG," "+JAnalysClass.newCount);
                if (JAnalysClass.newCount!="0") {//если есть обновления, то активируется обновлялка
                    direction="direction=down";     //при обновлении делаем максимум в 30 записей, сбрасываем счетчик запросов
                    if(size>=30){NumofPost=1;size=30;}//выполняем запрос на обновление листа и убираем надпись
                    dataInArray=false;
                    JSONListTask jListTask = new JSONListTask();
                    jListTask.execute(AuthorizationAct.log, AuthorizationAct.pass, "1",AuthorizationAct.type);
                    Spanned news = Html.fromHtml("<font color=\"#ffffff\">Грузопоиск </font>");
                    ActionBar actionBar = getActivity().getActionBar();
                    actionBar.setTitle(news);
                } else {
                    swUpdate.setRefreshing(false);
            }
            }
        });
        JSONListTask jListTask = new JSONListTask();
        jListTask.execute(AuthorizationAct.log, AuthorizationAct.pass, "1",AuthorizationAct.type);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);//переходим на новую активность с полной инфой, если это последняя строчка, то добавить 30записей
        if(position!=size){
        Intent infoInt = new Intent(getActivity(), InfoRequestAct.class);
        infoInt.putExtra("position",position);
        startActivity(infoInt);
        getActivity().overridePendingTransition(R.anim.rl_slide_enter,R.anim.hl_alpha_exit);}

        else{ size +=5;dataInArray=false;direction="direction=down";
            if(size>=30*NumofPost){
                NumofPost++;
                JSONListTask jListTask = new JSONListTask();
                jListTask.execute(AuthorizationAct.log, AuthorizationAct.pass, "1",AuthorizationAct.type);
            }else{
            adapter myAdapter = new adapter(getActivity(), dataList());
            myList.setAdapter(myAdapter);
            getListView().setSelection(size-10);}
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.listfragment_fon, null);
        myList = (ListView) root.findViewById(android.R.id.list);
        return root;
    }

    private ArrayList<struct> dataList() {
        ArrayList<struct> data = new ArrayList<>();
        for (int i = 0; i < size+1; i++) {
            data.add(new struct("", "", "", "", ""));
        }
        return data;
    }

    //////////////////////Поток для обновления адаптера листа(при старте и при обновлениияяя)///////////////
    public class JSONListTask extends AsyncTask<String, Void, ContentValues> {

        @Override
        protected void onPreExecute() {
            Log.d(LOG_TAG, "Поток Листа открыт");
            super.onPreExecute();
        }

        @Override
        protected ContentValues doInBackground(String... params) {
            String lastId="1";
            JSONObject obj;

            if(NumofPost>1){direction="direction=down";
                try{obj = (new JSONArray(new JSONObject(JsonBase).getString("result")).getJSONObject(29));
                    lastId=obj.getString("lastId");}catch (Exception e){}}else{lastId="0";}

            String url = "https://gruzopoisk.ru/cargo/get.json?"+direction+"&lastId="+lastId+"&"+"v=70391fe";
            Log.d(LOG_TAG,url);
          //  String url3 = "https://gruzopoisk.ru/cargo/get.json?direction=up&lastId=0&v=70391fe&;
            cv_object = HTTPPost.GetResponse(url, params);
            return cv_object;
        }

        @Override
        protected void onPostExecute(ContentValues cv_object) {
            // выполняет после doInBackground.
            Log.d(LOG_TAG, "Поток листа закрыт");
            try {JsonBase = ParseJSON.jsonString;}catch (Exception e){}
            if (ParseJSON.jsonString != null) {
                adapter myAdapter = new adapter(getActivity(), dataList());
                myList.setAdapter(myAdapter);
            } else {Toast.makeText(getActivity(), "Не удалось получить данные", Toast.LENGTH_SHORT).show();}
            swUpdate.setRefreshing(false);
            if (timerUpdate != null) {
                timerUpdate.cancel();}
            timerUpdate = new Timer();
            timerTaskUpdate = new myTimerTask();
            timerUpdate.schedule(timerTaskUpdate, 5000, 60000);

        }
    }

    //////////////////Поток запускаемый для определени новых заказов(для таймера)//////////////////
    public class JSON_TIMER_TASK extends AsyncTask<String, Void, ContentValues> {
        @Override
        protected void onPreExecute() {
            Log.d(LOG_TAG, "Поток таймера открыт");
            super.onPreExecute();
        }

        @Override
        protected ContentValues doInBackground(String... params) {

            String getLastID = A[13][0];
            String url2 = "https://gruzopoisk.ru/cargo/get_count.json?direction=up&lastId="+getLastID;

            Log.d(LOG_TAG,"POST "+url2);
            cv_object = HTTPPost.GetResponse(url2, params);
            return cv_object;
        }

        @Override
        protected void onPostExecute(ContentValues cv_object) {
            Log.d(LOG_TAG, "Поток таймера зыкрыт");
            JAnalysClass.Json_listUpdate();
            Log.d(LOG_TAG,JAnalysClass.newCount);
            Spanned news;
            ActionBar actionBar = getActivity().getActionBar();
            if(Integer.parseInt(JAnalysClass.newCount)!=0){
            news = Html.fromHtml(("<font color=\"#ffffff\">Грузопоиск </font>" +"<font color=\"#808080\">(Новых: "+ JAnalysClass.newCount)+")</font>");}else
            { news = Html.fromHtml("<font color=\"#ffffff\">Грузопоиск </font>");}
            actionBar.setTitle(news);
        }
    }

    class myTimerTask extends TimerTask {
        @Override
        public void run() {
            JSON_TIMER_TASK jTimerTask =  new JSON_TIMER_TASK();
            jTimerTask.execute(AuthorizationAct.log, AuthorizationAct.pass, "1",AuthorizationAct.type);
        }
    }
}