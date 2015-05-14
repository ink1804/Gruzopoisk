package com.ink1804.gruzopoisk.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ink1804.gruzopoisk.R;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread logoTimer = new Thread()
        {
            public void run()
            {
                try
                {
                    int logoTimer = 0;
                    while(logoTimer < 5000)
                    {
                        sleep(100);
                        logoTimer = logoTimer +100;
                    };
                    startActivity(new Intent(getApplicationContext(),AuthorizationAct.class));
                    //overridePendingTransition(R.anim.sim,R.anim.simple_slide_toleft);
                }
                catch (InterruptedException e)
                {
                    // TODO: автоматически сгенерированный блок catch.
                    e.printStackTrace();
                }
                finally
                {
                    finish();
                }
            }
        };
        logoTimer.start();
    }


}
