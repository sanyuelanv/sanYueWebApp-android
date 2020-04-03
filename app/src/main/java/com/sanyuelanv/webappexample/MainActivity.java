package com.sanyuelanv.webappexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sanyuelanv.sanwebapp.SanYueWebApp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener   {

    private TextView cacheText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button openBtn =  findViewById(R.id.btn);
        cacheText = findViewById(R.id.cache);
        Button clearBtn =  findViewById(R.id.clear);
        openBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        cacheText.setText(SanYueWebApp.getCacheSize(this));
    }
    @Override
    protected void onResume() {
        super.onResume();
        cacheText.setText(SanYueWebApp.getCacheSize(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:{
                SanYueWebApp sanYuewebApp = SanYueWebApp.initInstance(this);
                sanYuewebApp.setTimeout(20);
                sanYuewebApp.setDebug(true);
                sanYuewebApp.create(this,"http://192.168.50.197:3000/app.zip");
                break;
            }
            case R.id.clear:{
                cacheText.setText(SanYueWebApp.removeCache(this));
            }
        }
    }
}
