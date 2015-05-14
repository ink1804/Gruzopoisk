package com.ink1804.gruzopoisk.Activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ink1804.gruzopoisk.jsonClases.HTTPPost;
import com.ink1804.gruzopoisk.fragmentClases.JSONAnalysis;
import com.ink1804.gruzopoisk.R;

import org.w3c.dom.Attr;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Timer;
import java.util.TimerTask;
import java.util.jar.Attributes;

public class AuthorizationAct extends Activity {
    static final String LOG_TAG = "myLogs";
    public static final String APP_PREFERENCES = "My Settings";
    private SharedPreferences mSettings;

    private RelativeLayout myRel;
    private JSONTask authTask = null;
    private ContentValues cv_object = new ContentValues();
    private ProgressBar newpb;
    private TextView tvError;
    private EditText etLogin,etPassword;
    private Button btnLogIn;
    public static String log,pass,type;
    private Timer timerAuth;
    private TimerTask timerTaskAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorization);
        myRel = (RelativeLayout)findViewById(R.id.relAuth);
        etLogin = (EditText)findViewById(R.id.loginET); etPassword = (EditText)findViewById(R.id.passwordET);
        btnLogIn = (Button)findViewById(R.id.logInBTN);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);



    }
    private void createProgBar(){
        newpb=new ProgressBar(this,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams pbParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams
                .WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        pbParams.addRule(RelativeLayout.BELOW, R.id.logInBTN);
        pbParams.setMargins(0, 25, 0, 0);
        pbParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        newpb.setLayoutParams(pbParams);
        myRel.addView(newpb);
    }
    public void Log_in_Click(View v) {
        createProgBar();
        tvError = (TextView) findViewById(R.id.errorTV);
        log = etLogin.getText().toString();
        btnLogIn.setClickable(false);
        pass = etPassword.getText().toString();
        if(log.contains(".")&log.contains("@")){type = "email";}else{ type = "cell";}
            Log.d(LOG_TAG, "Запрос на сервер");
            authTask = new JSONTask();
            // нужно указать логин и пароль.
            authTask.execute(log, pass, "1",type);
    }

    public class JSONTask extends AsyncTask<String, Void, ContentValues> {

        @Override
        protected void onPreExecute() {
            if (timerAuth != null) {
                timerAuth.cancel();
            }
            timerAuth = new Timer();
            timerTaskAuth = new myTimerTask();
            timerAuth.schedule(timerTaskAuth, 15000, 7000);
            super.onPreExecute();
        }
        @Override
        protected ContentValues doInBackground(String... params) {
            String url = "https://gruzopoisk.ru/account/auth.json";
           // String url2 = "https://gruzopoisk.ru/cargo/get_count.json?direction=up&lastId=9056220";
           // String url3 = "https://gruzopoisk.ru/cargo/get.json?direction=up&lastId=0&v=70391fe&firstId=0";
            cv_object = HTTPPost.GetResponse(url, params);

            if (isCancelled())
                return null;
            return cv_object;
        }

        @Override
        protected void onPostExecute(ContentValues cv_object) {
            Log.d(LOG_TAG, "Поток авторизации закрыт");
            myRel.removeView(newpb);
            JSONAnalysis j = new JSONAnalysis();
            btnLogIn.setClickable(true);
            timerAuth.cancel();
            j.JsonAuth();
        switch (j.JSON_Success){
            case "true":Intent iListAct = new Intent(getApplicationContext(),ListAct.class);
                startActivity(iListAct);break;
            case "false":
                Toast.makeText(getApplicationContext(), j.JsonAuth(), Toast.LENGTH_SHORT).show();break;
            default:break;
        }
        }
    }

    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("SP_login", etLogin.getText().toString());
        editor.putString("SP_password", etPassword.getText().toString());
        editor.apply();
    }
    protected void onResume(){
        super.onResume();
        if (mSettings.contains("SP_login")) {
            etLogin.setText(mSettings.getString("SP_login", ""));}
        if (mSettings.contains("SP_password")) {
            etPassword.setText(mSettings.getString("SP_password", ""));}
       // btnLogIn.callOnClick();

    }


    class myTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                     authTask.cancel(true);
                    myRel.removeView(newpb);
                    btnLogIn.setClickable(true);
                    Toast.makeText(getApplicationContext(), "Ошибка авторизации. Проверьте ваше интернет-соединение",
                            Toast.LENGTH_SHORT).show();
                    timerAuth.cancel();
                }
            });
        }
    }
}
