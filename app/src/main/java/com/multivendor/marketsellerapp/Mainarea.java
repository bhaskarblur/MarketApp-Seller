package com.multivendor.marketsellerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Models.billModel;
import com.multivendor.marketsellerapp.Models.loginresResponse;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.ViewModels.billViewModel;
import com.multivendor.marketsellerapp.databinding.ActivityMainareaBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Mainarea extends AppCompatActivity {

    ActivityMainareaBinding mabidning;
    private Integer backclicks=0;
    final int PERMISSION_CODE=1001;
    private SharedPreferences sharedPreferences;
    private final api_baseurl baseurl=new api_baseurl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mabidning=ActivityMainareaBinding.inflate(getLayoutInflater());
        setContentView(mabidning.getRoot());
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        bottombarholder();
        handleshopsetup();
    }

    private void handleshopsetup() {

        mabidning.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim0 = AnimationUtils.loadAnimation(Mainarea.this, R.anim.slide_in_up);
                mabidning.billnotlay.setAnimation(anim0);
                mabidning.billnotlay.setVisibility(View.INVISIBLE);

            }
        });
        mabidning.paybillbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Mainarea.this,act_mybills.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });
        mabidning.pfcancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim0 = AnimationUtils.loadAnimation(Mainarea.this, R.anim.slide_in_up);
                mabidning.pfnotstlay.setAnimation(anim0);
                mabidning.pfnotstlay.setVisibility(View.INVISIBLE);

            }
        });
        mabidning.pfsetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Mainarea.this,profileSettings.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });

        mabidning.shopcancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim0 = AnimationUtils.loadAnimation(Mainarea.this, R.anim.slide_in_up);
                mabidning.shopnotstlay.setAnimation(anim0);
                mabidning.shopnotstlay.setVisibility(View.INVISIBLE);

            }
        });
        mabidning.shopsetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Mainarea.this,act_shopsetup.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });
        sharedPreferences = getSharedPreferences("userlogged", 0);
        String userid = sharedPreferences.getString("userid", "");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<sellerApiResp.sellerinfo> call = logregApiInterface.get_seller_info(userid);

        call.enqueue(new Callback<sellerApiResp.sellerinfo>() {
            @Override
            public void onResponse(Call<sellerApiResp.sellerinfo> call, Response<sellerApiResp.sellerinfo> response) {
                if(!response.isSuccessful()) {
                    Log.d("error code:",String.valueOf(response.code()));
                    return;
                }

                sellerApiResp.sellerinfo resp=response.body();

                if(     resp.getResult().getName()!=null &&
                        resp.getResult().getStore_name()!=null
                        && resp.getResult().getAddress()!=null && !resp.getResult().getName().toString().equals(" ")
                        && !resp.getResult().getAddress().toString().equals(" ")) {

                    mabidning.pfnotstlay.setVisibility(View.GONE);
                }
                else {
                    mabidning.pfnotstlay.setVisibility(View.VISIBLE);
                    Animation anim0 = AnimationUtils.loadAnimation(Mainarea.this, R.anim.slide_out_up);
                    mabidning.pfnotstlay.setAnimation(anim0);
                }

                if(resp.getResult().getDelivery_redius()!=null && resp.getResult().getDelivery_charge()!=null &&
                        resp.getResult().getUpi_id()!=null && resp.getResult().getFree_delivery_above()!=null
                        && resp.getResult().getMin_order_amount()!=null) {

                    mabidning.shopnotstlay.setVisibility(View.GONE);
                }
                else {
                    mabidning.shopnotstlay.setVisibility(View.VISIBLE);
                    Animation anim0 = AnimationUtils.loadAnimation(Mainarea.this, R.anim.slide_out_up);
                    mabidning.shopnotstlay.setAnimation(anim0);
                }
            }

            @Override
            public void onFailure(Call<sellerApiResp.sellerinfo> call, Throwable t) {

            }
        });


        billViewModel vm=new ViewModelProvider(this).get(billViewModel.class);
        vm.initwork(userid);
        vm.getPendbillModel().observe(this, new Observer<List<billModel.billresult>>() {
            @Override
            public void onChanged(List<billModel.billresult> billresults) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(billresults.size()>0) {
                            mabidning.billnotlay.setVisibility(View.VISIBLE);
                        }
                        else {
                            mabidning.billnotlay.setVisibility(View.INVISIBLE);
                        }
                    }
                },500);
            }
        });

    }

    private void bottombarholder() {
        homeFragment df=new homeFragment();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_fast_2,R.anim.fade);
        transaction.replace(R.id.mainfragment,df);
        transaction.commit();
        mabidning.homelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(R.drawable.bottomhomeicon).into(mabidning.homeselectedicon);
                mabidning.hometxt.setTextColor(Color.parseColor("#0881E3"));

                Picasso.get().load(R.drawable.bottomordersicon).into(mabidning.orderselectedicon);
                mabidning.ordertext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomcarticon).into(mabidning.cartselectedicon);
                mabidning.carttext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomprofileicon).into(mabidning.profileselectedicon);
                mabidning.profiletext.setTextColor(Color.parseColor("#929292"));

                mabidning.homeselector.setVisibility(View.VISIBLE);
                mabidning.orderselector.setVisibility(View.INVISIBLE);
                mabidning.cartselector.setVisibility(View.INVISIBLE);
                mabidning.profileselector.setVisibility(View.INVISIBLE);
               homeFragment df=new homeFragment();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_fast_2,R.anim.fade);
               transaction.replace(R.id.mainfragment,df);
                transaction.commit();
            }
        });

        mabidning.orderlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(R.drawable.bottomhomenotselected).into(mabidning.homeselectedicon);
                mabidning.hometxt.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomorderselected).into(mabidning.orderselectedicon);
                mabidning.ordertext.setTextColor(Color.parseColor("#0881E3"));

                Picasso.get().load(R.drawable.bottomcarticon).into(mabidning.cartselectedicon);
                mabidning.carttext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomprofileicon).into(mabidning.profileselectedicon);
                mabidning.profiletext.setTextColor(Color.parseColor("#929292"));


                mabidning.homeselector.setVisibility(View.INVISIBLE);
                mabidning.orderselector.setVisibility(View.VISIBLE);
                mabidning.cartselector.setVisibility(View.INVISIBLE);
                mabidning.profileselector.setVisibility(View.INVISIBLE);
                ordFragment df=new ordFragment();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_fast_2,R.anim.fade);
                transaction.replace(R.id.mainfragment,df);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

        mabidning.profiellay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(R.drawable.bottomhomenotselected).into(mabidning.homeselectedicon);
                mabidning.hometxt.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomordersicon).into(mabidning.orderselectedicon);
                mabidning.ordertext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomcarticon).into(mabidning.cartselectedicon);
                mabidning.carttext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomprofileselected).into(mabidning.profileselectedicon);
                mabidning.profiletext.setTextColor(Color.parseColor("#0881E3"));


                mabidning.homeselector.setVisibility(View.INVISIBLE);
                mabidning.orderselector.setVisibility(View.INVISIBLE);
                mabidning.cartselector.setVisibility(View.INVISIBLE);
                mabidning.profileselector.setVisibility(View.VISIBLE);
               profileFragment df=new profileFragment();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_fast_2,R.anim.fade);
                transaction.replace(R.id.mainfragment,df);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

        mabidning.cartlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(R.drawable.bottomhomenotselected).into(mabidning.homeselectedicon);
                mabidning.hometxt.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomordersicon).into(mabidning.orderselectedicon);
                mabidning.ordertext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomcartselected).into(mabidning.cartselectedicon);
                mabidning.carttext.setTextColor(Color.parseColor("#0881E3"));

                Picasso.get().load(R.drawable.bottomprofileicon).into(mabidning.profileselectedicon);
                mabidning.profiletext.setTextColor(Color.parseColor("#929292"));
                mabidning.homeselector.setVisibility(View.INVISIBLE);
                mabidning.orderselector.setVisibility(View.INVISIBLE);
                mabidning.cartselector.setVisibility(View.VISIBLE);
                mabidning.profileselector.setVisibility(View.INVISIBLE);
               newCatalog df=new newCatalog();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_fast_2,R.anim.fade);
                transaction.replace(R.id.mainfragment,df);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()!=0) {
            getSupportFragmentManager().popBackStack();
            if(getSupportFragmentManager().getBackStackEntryCount()<2) {
                Picasso.get().load(R.drawable.bottomhomeicon).into(mabidning.homeselectedicon);
                mabidning.hometxt.setTextColor(Color.parseColor("#0881E3"));

                Picasso.get().load(R.drawable.bottomordersicon).into(mabidning.orderselectedicon);
                mabidning.ordertext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomcarticon).into(mabidning.cartselectedicon);
                mabidning.carttext.setTextColor(Color.parseColor("#929292"));

                Picasso.get().load(R.drawable.bottomprofileicon).into(mabidning.profileselectedicon);
                mabidning.profiletext.setTextColor(Color.parseColor("#929292"));

                mabidning.homeselector.setVisibility(View.VISIBLE);
                mabidning.orderselector.setVisibility(View.INVISIBLE);
                mabidning.cartselector.setVisibility(View.INVISIBLE);
                mabidning.profileselector.setVisibility(View.INVISIBLE);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            }
        }
        else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this).
                    setTitle("Exit?").setMessage("Do you want to exit the app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}