package com.mango.libs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.phoneutils.crosspromotion.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startLoader("http://theorycrosspromotion.appspot.com/getapps","main");
    }
}
