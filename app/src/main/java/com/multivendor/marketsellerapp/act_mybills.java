package com.multivendor.marketsellerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.multivendor.marketsellerapp.Adapters.billAdapter;
import com.multivendor.marketsellerapp.Adapters.billpaidAdapter;
import com.multivendor.marketsellerapp.Models.billModel;
import com.multivendor.marketsellerapp.R;
import com.multivendor.marketsellerapp.databinding.ActivityActMybillsBinding;

import java.util.List;

public class act_mybills extends AppCompatActivity {
    private ActivityActMybillsBinding mbbinding;
    private com.multivendor.marketsellerapp.ViewModels.billViewModel billViewModel;
    private com.multivendor.marketsellerapp.Adapters.billAdapter billAdapter;
    private com.multivendor.marketsellerapp.Adapters.billpaidAdapter billAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbbinding = ActivityActMybillsBinding.inflate(getLayoutInflater());
        setContentView(mbbinding.getRoot());
        this.getSupportActionBar().hide();
        SharedPreferences sharedPreferences = getSharedPreferences("userlogged", 0);
        String storeid = sharedPreferences.getString("userid", "");
        billViewModel = new ViewModelProvider(this).get(com.multivendor.marketsellerapp.ViewModels.billViewModel.class);
        billViewModel.initwork(storeid);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        billViewModel.getPendbillModel().observe(this, new Observer<List<billModel.billresult>>() {
            @Override
            public void onChanged(List<billModel.billresult> billModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        billAdapter.notifyDataSetChanged();
                    }
                }, 100);
            }
        });

        billViewModel.getCompbillModel().observe(this, new Observer<List<billModel.billresult>>() {
            @Override
            public void onChanged(List<billModel.billresult> billModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        billAdapter2.notifyDataSetChanged();
                    }
                }, 100);
            }
        });

        loaddata();
        viewfunc();
    }

    private void loaddata() {
        billAdapter = new billAdapter(this, billViewModel.getPendbillModel().getValue());
        billAdapter2 = new billpaidAdapter(this, billViewModel.getCompbillModel().getValue());
        LinearLayoutManager llm = new LinearLayoutManager(this);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        mbbinding.pendbilrec.setLayoutManager(llm);
        mbbinding.compbilrec.setLayoutManager(llm2);

        mbbinding.pendbilrec.setAdapter(billAdapter);
        mbbinding.compbilrec.setAdapter(billAdapter2);
    }

    private void viewfunc() {
        mbbinding.billpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbbinding.billpend.setTextColor(Color.parseColor("#0881E3"));
                mbbinding.billcomp.setTextColor(Color.parseColor("#595959"));

                mbbinding.pendlay.setVisibility(View.VISIBLE);
                mbbinding.complay.setVisibility(View.INVISIBLE);
            }
        });

        mbbinding.billcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbbinding.billpend.setTextColor(Color.parseColor("#595959"));
                mbbinding.billcomp.setTextColor(Color.parseColor("#0881E3"));

                mbbinding.pendlay.setVisibility(View.INVISIBLE);
                mbbinding.complay.setVisibility(View.VISIBLE);
            }
        });

        mbbinding.backbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}