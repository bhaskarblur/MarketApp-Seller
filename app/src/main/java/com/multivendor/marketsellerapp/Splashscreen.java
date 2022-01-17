package com.multivendor.marketsellerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));
        }

        handleFirebaseMessageIntent();
    }

    private void handleFirebaseMessageIntent() {

        SharedPreferences shpref = getSharedPreferences("userlogged", 0);
        String userid = shpref.getString("userid", "");

        if (!userid.isEmpty() || !userid.equals("")) {

            if (getIntent() != null) {
                if (getIntent().getStringExtra("noti_type") != null) {
                    String noti_type = getIntent().getStringExtra("noti_type");

                    if (noti_type.equals("order")) {
                        Log.d("noti_type",noti_type);
                        String order_id = getIntent().getStringExtra("order_id");
                        String store_id = getIntent().getStringExtra("store_id");
                        String status= getIntent().getStringExtra("status");
                        Intent openact;
                        if(status.equals("Delivered")) {
                            openact = new Intent(Splashscreen.this,act_vieworder.class);
                        }
                        else {
                            openact = new Intent(Splashscreen.this,act_vieworder.class);
                        }

                        openact.putExtra("orderid", order_id);
                        openact.putExtra("userid", store_id);
                        startActivity(openact);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                    }

                    else if(noti_type.equals("quick_order")) {
                        Log.d("noti_type",noti_type);
                        String order_id = getIntent().getStringExtra("order_id");
                        String store_id = getIntent().getStringExtra("store_id");
                        Intent openact =new Intent(Splashscreen.this,act_quickord.class);
                        openact.putExtra("orderid", order_id);
                        openact.putExtra("storeid", store_id);
                        openact.putExtra("userid", userid);
                        startActivity(openact);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    }
                    else if(noti_type.equals("bill")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(Splashscreen.this, Loginact.class));
                                finish();
                            }
                        }, 1500);
                    }

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Splashscreen.this, Loginact.class));
                            finish();
                        }
                    }, 1500);
                }
            }
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splashscreen.this, Signinact.class));
                    finish();
                }
            }, 1500);
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
    }
}