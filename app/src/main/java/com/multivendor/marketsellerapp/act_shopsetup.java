package com.multivendor.marketsellerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Models.loginresResponse;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.databinding.ActivityActShopsetupBinding;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class act_shopsetup extends AppCompatActivity {
    private ActivityActShopsetupBinding spbinding;
    SharedPreferences sharedPreferences;
    private Boolean updaing=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spbinding=ActivityActShopsetupBinding.inflate(getLayoutInflater());
        setContentView(spbinding.getRoot());
        this.getSupportActionBar().hide();
        sharedPreferences=getSharedPreferences("userlogged",0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        loadData();
        viewfunc();
    }

    private void loadData() {
        String userId=sharedPreferences.getString("userid","");
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        com.multivendor.marketsellerapp.APIWork.LogregApiInterface logregApiInterface=retrofit.create(
                com.multivendor.marketsellerapp.APIWork.LogregApiInterface.class);

        Call<sellerApiResp.sellerinfo> call=logregApiInterface.get_seller_info(userId);

        call.enqueue(new Callback<sellerApiResp.sellerinfo>() {
            @Override
            public void onResponse(Call<sellerApiResp.sellerinfo> call, Response<sellerApiResp.sellerinfo> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }
                sellerApiResp.sellerinfo resp = response.body();
                if(resp.getMessage()!=null) {
                    Log.d("message",resp.getMessage());
                }

                if(resp.getMessage().equals("My store")) {
                    if(resp.getResult().getDelivery_redius()!=null) {
                        spbinding.shopradet.setText(resp.getResult().getDelivery_redius());
                    }

                    if(resp.getResult().min_order_amount!=null) {
                        spbinding.moaet.setText(resp.getResult().getMin_order_amount());
                    }
                    if(resp.getResult().getPan_number()!=null) {
                        spbinding.panet2.setText(resp.getResult().getPan_number());
                    }

                    if(resp.getResult().getGst_number()!=null) {
                        spbinding.gstet.setText(resp.getResult().getGst_number());
                    }
                    if(resp.getResult().getFree_delivery_above()!=null) {
                        spbinding.freedelet.setText(resp.getResult().getFree_delivery_above());
                    }
                    if(resp.getResult().getDelivery_charge()!=null) {
                        spbinding.delvcharet.setText(resp.getResult().getDelivery_charge());
                    }
                    if(resp.getResult().getUpi_id()!=null) {
                        spbinding.upiidet.setText(resp.getResult().getUpi_id());
                    }
                }
            }

            @Override
            public void onFailure(Call<sellerApiResp.sellerinfo> call, Throwable t) {
                Toast.makeText(act_shopsetup.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void viewfunc() {
        spbinding.backbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(act_shopsetup.this,Mainarea.class));
                finish();
            }
        });

        spbinding.shsetupd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spbinding.shopradet.getText().toString().isEmpty()) {
                    spbinding.shopradet.setError("Enter Delivery Radius!");
                }
                else if(spbinding.moaet.getText().toString().isEmpty()) {
                    spbinding.moaet.setError("Enter Minimum Order Amount!");
                }
                else if(spbinding.freedelet.getText().toString().isEmpty()) {
                    spbinding.freedelet.setError("Enter Free Delv. Amount!");
                }
                else if(spbinding.delvcharet.getText().toString().isEmpty()) {
                    spbinding.delvcharet.setError("Enter Delivery Charges!");
                }
                else if(spbinding.upiidet.getText().toString().isEmpty()) {
                    spbinding.upiidet.setError("Enter UPI Id!");
                }
                else {
                    if(updaing.equals(false)) {
                        updaing=true;
                        spbinding.progressBar3.setVisibility(View.VISIBLE);
                        spbinding.shsetupd.setVisibility(View.INVISIBLE);
                        String userid = sharedPreferences.getString("userid", "");
                        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                                .addConverterFactory(GsonConverterFactory.create()).build();

                        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                        Call<loginresResponse.shopsetup> call= logregApiInterface.shopsetup(userid,
                                spbinding.shopradet.getText().toString(),spbinding.moaet
                                        .getText().toString(),spbinding.freedelet.getText().toString(),spbinding.delvcharet.getText().toString(),
                                spbinding.upiidet.getText().toString(),spbinding.panet2.getText().toString(),spbinding.gstet.getText().toString());

                        call.enqueue(new Callback<loginresResponse.shopsetup>() {
                            @Override
                            public void onResponse(Call<loginresResponse.shopsetup> call, Response<loginresResponse.shopsetup> response) {
                                if (!response.isSuccessful()) {
                                    Log.d("Error code", String.valueOf(response.code()));
                                    Toast.makeText(act_shopsetup.this, "Done12", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                loginresResponse.shopsetup shopsetup=response.body();
                                if(shopsetup.getMessage()!=null) {
                                    Log.d("message",shopsetup.getMessage());
                                }
                                if(shopsetup.getMessage()!=null && shopsetup.getMessage().equals("Store setup successfully.!")) {
                                    Toast.makeText(act_shopsetup.this, "Add address. Ignore if already added.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(act_shopsetup.this,profileSettings.class));
                                    Log.d("storesetup","done");
                                    spbinding.progressBar3.setVisibility(View.GONE);
                                    spbinding.shsetupd.setVisibility(View.VISIBLE);
                                    updaing=false;
                                }
                                else {
                                    spbinding.progressBar3.setVisibility(View.GONE);
                                    spbinding.shsetupd.setVisibility(View.VISIBLE);
                                    updaing=false;
                                    Toast.makeText(act_shopsetup.this, "There was an error!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<loginresResponse.shopsetup> call, Throwable t) {
                                updaing=false;
                                spbinding.progressBar3.setVisibility(View.GONE);
                                spbinding.shsetupd.setVisibility(View.VISIBLE);
                                Log.d("Failure",t.getMessage());
                            }
                        });
                    }
                }


            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        if(spbinding.shopradet.getText().toString().isEmpty()) {
            spbinding.shopradet.setError("Enter Delivery Radius!");
        }
        else if(spbinding.moaet.getText().toString().isEmpty()) {
            spbinding.moaet.setError("Enter Minimum Order Amount!");
        }
        else if(spbinding.freedelet.getText().toString().isEmpty()) {
            spbinding.freedelet.setError("Enter Free Delv. Amount!");
        }
        else if(spbinding.delvcharet.getText().toString().isEmpty()) {
            spbinding.delvcharet.setError("Enter Delivery Charges!");
        }
        else if(spbinding.upiidet.getText().toString().isEmpty()) {
            spbinding.upiidet.setError("Enter UPI Id!");
        } else{
        startActivity(new Intent(act_shopsetup.this,Mainarea.class));
        finish();
        }

    }
}