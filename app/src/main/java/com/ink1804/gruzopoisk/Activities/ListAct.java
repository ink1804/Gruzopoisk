package com.ink1804.gruzopoisk.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Toolbar;

import com.ink1804.gruzopoisk.R;

import java.net.CookieHandler;
import java.net.CookieManager;

public class ListAct extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ActionBar actionBar = this.getActionBar();
        Spanned title = Html.fromHtml(("<font color=\"#ffffff\">Грузопоиск </font>"));
        actionBar.setTitle(title);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.toolBarColor));

    }

    @Override
    public void onBackPressed() {

    }
    protected void onPause(){
        super.onPause();
    }
}


