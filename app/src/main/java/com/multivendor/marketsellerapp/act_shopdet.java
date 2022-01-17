package com.multivendor.marketsellerapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Models.loginresResponse;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.databinding.ActivityActShopdetBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class act_shopdet extends AppCompatActivity {
    private ActivityActShopdetBinding sdbinding;
    private boolean[] selectedCat;
    private ArrayList<Integer> categarray = new ArrayList<>();
    private Boolean registering = false;
    SharedPreferences sharedPreferences;
    private final api_baseurl baseurl=new api_baseurl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdbinding = ActivityActShopdetBinding.inflate(getLayoutInflater());
        setContentView(sdbinding.getRoot());
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        sharedPreferences = getSharedPreferences("userlogged", 0);
        String shopregid = sharedPreferences.getString("shopregistered", "");
        if (shopregid.equals("yes")) {
            startActivity(new Intent(act_shopdet.this, Mainarea.class));
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }
        else if (!shopregid.equals("yes")) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                    .addConverterFactory(GsonConverterFactory.create()).build();

            com.multivendor.marketsellerapp.APIWork.LogregApiInterface logregApiInterface = retrofit.create(
                    com.multivendor.marketsellerapp.APIWork.LogregApiInterface.class);

            String userId = sharedPreferences.getString("userid", "");
            Call<sellerApiResp.sellerinfo> call = logregApiInterface.get_seller_info(userId);

            call.enqueue(new Callback<sellerApiResp.sellerinfo>() {
                @Override
                public void onResponse(Call<sellerApiResp.sellerinfo> call, Response<sellerApiResp.sellerinfo> response) {
                    if (!response.isSuccessful()) {
                        Log.d("Error code", String.valueOf(response.code()));
                        return;
                    }
                    sellerApiResp.sellerinfo resp = response.body();
                    if (resp.getMessage() != null) {
                        Log.d("message", resp.getMessage());
                    }

                    if (resp.getMessage().equals("My store")) {
                        if (resp.getResult().getStore_name() != null && resp.getResult().getCity() != null) {
                            Toast.makeText(act_shopdet.this, "Done", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("shopregistered", "yes");
                            editor.putString("shopname", sdbinding.shopnameet.getText().toString());
                            editor.commit();
                            startActivity(new Intent(act_shopdet.this, Mainarea.class));
                            finish();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        }

                    }
                }

                @Override
                public void onFailure(Call<sellerApiResp.sellerinfo> call, Throwable t) {
                    Toast.makeText(act_shopdet.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        loadfunc();
        viewfunction();
    }

    private void viewfunction() {
        sdbinding.shopdetreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sdbinding.shopnameet.getText().toString().isEmpty()) {
                    sdbinding.shopnameet.setError("Enter Shop Name");
                } else if (sdbinding.shctet.getText().toString().isEmpty()) {
                    sdbinding.shctet.setError("Enter Shop City");
                } else if (sdbinding.statet.getText().toString().isEmpty()) {
                    sdbinding.statet.setError("Enter Shop State");
                } else if (sdbinding.catsptxt.toString().isEmpty() || sdbinding.catsptxt.getText().toString().equals(
                        "Select Categories")) {
                    Toast.makeText(act_shopdet.this, "Please Select Categories", Toast.LENGTH_SHORT).show();
                } else {
                    if (registering.equals(false)) {
                        registering = true;
                        sdbinding.progressBar2.setVisibility(View.VISIBLE);
                        sdbinding.shopdetreg.setVisibility(View.INVISIBLE);
                        String userid = sharedPreferences.getString("userid", "");
                        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                                .addConverterFactory(GsonConverterFactory.create()).build();

                        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                        Call<loginresResponse.shopregister> call = logregApiInterface.shopregister(userid, sdbinding.shopnameet.getText().toString()
                                , sdbinding.shctet.getText().toString(), sdbinding.statet.getText().toString(), sdbinding.catsptxt.getText().toString());

                        call.enqueue(new Callback<loginresResponse.shopregister>() {
                            @Override
                            public void onResponse(Call<loginresResponse.shopregister> call, Response<loginresResponse.shopregister> response) {
                                if (!response.isSuccessful()) {

                                    Log.d("Code", response.message().toString());
                                }

                                loginresResponse.shopregister resp = response.body();

                                if (resp.getResult() != null) {
                                    Log.d("result", resp.getResult());
                                }
                                if (resp.getMessage() != null) {
                                    Log.d("message", resp.getMessage());
                                }

                                if (resp.getMessage().equals("Registration successfully.!")) {
                                    sdbinding.progressBar2.setVisibility(View.GONE);
                                    sdbinding.shopdetreg.setVisibility(View.VISIBLE);
                                    Toast.makeText(act_shopdet.this, "Shop Registered!", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("shopregistered", "yes");
                                    editor.putString("shopname", sdbinding.shopnameet.getText().toString());
                                    editor.commit();
                                    startActivity(new Intent(act_shopdet.this, act_shopsetup.class));
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                } else {
                                    sdbinding.progressBar2.setVisibility(View.GONE);
                                    sdbinding.shopdetreg.setVisibility(View.VISIBLE);
                                    sdbinding.progressBar2.setVisibility(View.VISIBLE);
                                    sdbinding.shopdetreg.setVisibility(View.INVISIBLE);
                                    Toast.makeText(act_shopdet.this, "An error has occured!", Toast.LENGTH_SHORT).show();
                                    registering = false;
                                }
                            }

                            @Override
                            public void onFailure(Call<loginresResponse.shopregister> call, Throwable t) {
                                Toast.makeText(act_shopdet.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                Log.d("cause", t.getCause().toString());
                                registering = false;
                            }
                        });
                    }
                }
            }
        });


    }

    private void loadfunc() {
  String[] orditems = {"Grocery", "Pharmacy","Food","Clothing","Furniture","Toys & Gifts" ,"Cosmetics",
          "Footwear","Fashion Accessories","Home Appliances", "Mobiles & Laptops","Ayurvedic" , "Vegetables"};
//        String[] orditems = {"Grocery", "Vegetables", "Food", "Pharma", "Clothing", "Electronics", "Furniture"};

        selectedCat = new boolean[orditems.length];
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, orditems);
        sdbinding.catsp.setAdapter(arrayAdapter);

        sdbinding.catsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sdbinding.catsptxt.setText(adapterView.getItemAtPosition(i).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        sdbinding.catsptxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(act_shopdet.this)
//                        .setTitle("Select 2 Categories").setCancelable(false).setMultiChoiceItems(orditems, selectedCat, new DialogInterface.OnMultiChoiceClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//
//                                if (isChecked) {
//
//                                    categarray.add(which);
//                                    Collections.sort(categarray);
//
//
//                                } else {
//                                    categarray.remove(which);
//                                    selectedCat[which] = false;
//                                }
//                            }
//                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                StringBuilder stringBuilder = new StringBuilder();
//
//                                for (int j = 0; j < categarray.size(); j++) {
//                                    if (!stringBuilder.toString().contains(orditems[categarray.get(j)])) {
//                                        if (categarray.size() < 3) {
//                                            stringBuilder.append(orditems[categarray.get(j)]);
//                                        }
//                                    }
//                                    if (categarray.size() > 3) {
//                                        stringBuilder.replace(0, stringBuilder.length(), "Select Only 2");
//                                    }
//                                    if (j != categarray.size() - 1) {
//                                        stringBuilder.append(",");
//
//                                    }
//                                }
//                                sdbinding.catsptxt.setText(stringBuilder.toString());
//                            }
//                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                builder.show();
//            }
//        });
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        com.multivendor.marketsellerapp.APIWork.LogregApiInterface logregApiInterface = retrofit.create(
                com.multivendor.marketsellerapp.APIWork.LogregApiInterface.class);

        Call<sellerApiResp.getstate> call = logregApiInterface.getstate();

        call.enqueue(new Callback<sellerApiResp.getstate>() {
            @Override
            public void onResponse(Call<sellerApiResp.getstate> call, Response<sellerApiResp.getstate> response) {
                if (!response.isSuccessful()) {
                    Log.d("error code:", String.valueOf(response.code()));
                    return;
                }

                sellerApiResp.getstate statedata = response.body();
                ArrayList<String> statelist = new ArrayList<>();
                statelist.add("Select State");

                if (statedata.getResult() != null) {
                    for (int i = 0; i < statedata.getResult().size(); i++) {
                        statelist.add(statedata.getResult().get(i).getStatename());
                    }
                    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(act_shopdet.this, R.layout.support_simple_spinner_dropdown_item, statelist);
                    sdbinding.statespin.setAdapter(listAdapter);
                    sdbinding.statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (!parent.getItemAtPosition(position).equals("Select State")) {
                                sdbinding.statet.setText(parent.getItemAtPosition(position).toString());
                                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                                        .addConverterFactory(GsonConverterFactory.create()).build();

                                com.multivendor.marketsellerapp.APIWork.LogregApiInterface logregApiInterface = retrofit.create(
                                        com.multivendor.marketsellerapp.APIWork.LogregApiInterface.class);

                                Call<sellerApiResp.getcity> call = logregApiInterface.getcity(statedata.getResult().get(position - 1).getId());

                                call.enqueue(new Callback<sellerApiResp.getcity>() {
                                    @Override
                                    public void onResponse(Call<sellerApiResp.getcity> call, Response<sellerApiResp.getcity> response) {
                                        if (!response.isSuccessful()) {
                                            Log.d("error code:", String.valueOf(response.code()));
                                            return;
                                        }

                                        sellerApiResp.getcity citydata = response.body();
                                        ArrayList<String> citylist = new ArrayList<>();
                                        citylist.add("Select City");
                                        if (citydata.getResult() != null) {
                                            for (int i = 0; i < citydata.getResult().size(); i++) {
                                                citylist.add(citydata.getResult().get(i).getCity());
                                            }
                                            ArrayAdapter<String> citylistAdapter = new ArrayAdapter<String>(act_shopdet.this, R.layout.support_simple_spinner_dropdown_item,
                                                    citylist);
                                            sdbinding.cityspin.setAdapter(citylistAdapter);
                                            sdbinding.cityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    if (!parent.getItemAtPosition(position).equals("Select City")) {
                                                        sdbinding.shctet.setText(parent.getItemAtPosition(position).toString());
                                                    }
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });

                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<sellerApiResp.getcity> call, Throwable t) {

                                    }
                                });
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<sellerApiResp.getstate> call, Throwable t) {

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}