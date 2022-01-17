package com.multivendor.marketsellerapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.multivendor.marketsellerapp.databinding.ActivityOrderplacedactivityBinding;

public class billpaidactivity extends AppCompatActivity {

    private ActivityOrderplacedactivityBinding orbinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orbinding=ActivityOrderplacedactivityBinding.inflate(getLayoutInflater());
        setContentView(orbinding.getRoot());
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        playsound();
        viewfunctions();
    }

    private void playsound() {
        MediaPlayer mediaPlayer=MediaPlayer.create(this, R.raw.gpaypaymsound);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
                mediaPlayer.release();

            }
        });
    }

    private void viewfunctions() {
        orbinding.delvbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(billpaidactivity.this,Mainarea.class));
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);

            }
        });
    }


    @Override
    public void onBackPressed() {
    }
}