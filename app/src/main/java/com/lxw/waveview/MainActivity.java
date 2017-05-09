package com.lxw.waveview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private WaveView mWaveView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWaveView= (WaveView) findViewById(R.id.wave_view);
        mWaveView.startAnimation();
    }
}
