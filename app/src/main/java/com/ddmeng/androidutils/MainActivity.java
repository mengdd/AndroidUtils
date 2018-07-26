package com.ddmeng.androidutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean deviceRooted = new RootUtils(this).isDeviceRooted();
        TextView statusText = (TextView) findViewById(R.id.text_rooted_status);
        statusText.setText(getString(R.string.device_rooted_status, String.valueOf(deviceRooted)));
    }
}
