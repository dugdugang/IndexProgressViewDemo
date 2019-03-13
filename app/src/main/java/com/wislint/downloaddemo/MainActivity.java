package com.wislint.downloaddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.wislint.downloaddemo.progress.IndexProgressView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final IndexProgressView progressView = findViewById(R.id.index);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressView.setProgress(progress);
                    }
                });
                Log.i("M-TAG", "run: "+progress);
                progress++;
                if (progress>100){
                    timer.cancel();
                }
            }
        }, 0,10);

        SeekBar seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressView.setProgress(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
