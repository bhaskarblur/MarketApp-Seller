package com.multivendor.marketsellerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Adapters.ordprodAdapter;
import com.multivendor.marketsellerapp.Adapters.qordimgAdapter;

import com.multivendor.marketsellerapp.Models.quickorderModel;
import com.multivendor.marketsellerapp.R;
import com.multivendor.marketsellerapp.databinding.ActivityActQuickordBinding;
import com.multivendor.marketsellerapp.ViewModels.quickordViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class act_quickord extends AppCompatActivity {
    private ActivityActQuickordBinding qobinding;
    private ordprodAdapter ordprodAdapter;
    private quickordViewModel quickordViewModel;
    private qordimgAdapter qordimgAdapter;
    List<quickorderModel.quick_products> finalprodlist = new ArrayList<>();
    String storeid;
    String quickorderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qobinding = ActivityActQuickordBinding.inflate(getLayoutInflater());
        setContentView(qobinding.getRoot());
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        Intent intent = getIntent();
        storeid = intent.getStringExtra("storeid");
        quickorderid = intent.getStringExtra("orderid");
        quickordViewModel = new ViewModelProvider(this).get(com.multivendor.marketsellerapp.ViewModels.quickordViewModel.class);
        quickordViewModel.initwork(storeid, quickorderid);
        quickordViewModel.getOrdprodModel().observe(this, new Observer<List<quickorderModel.quick_products>>() {
            @Override
            public void onChanged(List<quickorderModel.quick_products> quick_products) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (quick_products.size() > 0) {
                            finalprodlist = quick_products;
                            ordprodAdapter = new ordprodAdapter(act_quickord.this, finalprodlist);
                            LinearLayoutManager llm = new LinearLayoutManager(act_quickord.this);
                            qobinding.prodlistrec.setLayoutManager(llm);
                            qobinding.prodlistrec.setAdapter(ordprodAdapter);
                            ordprodAdapter.Setonpricechange(new ordprodAdapter.onpricechange() {
                                @Override
                                public void onpriceChange(Integer price) {
                                    Integer totalprice=0;
                                    for(int i=0;i<ordprodAdapter.prodList.size();i++) {
                                        if(ordprodAdapter.prodList.get(i).price!=null) {
                                            totalprice = totalprice + Integer.valueOf(ordprodAdapter.prodList.get(i).getPrice());
                                        }
                                    }
                                    qobinding.carttotalprice.setText(String.valueOf(totalprice));

                                }
                            });
                        }
                    }
                }, 100);
            }
        });
        quickordViewModel.getProdimgModel().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        qordimgAdapter.notifyDataSetChanged();
                    }
                }, 100);
            }
        });
        quickordViewModel.getWholedata().observe(this, new Observer<quickorderModel.quickordResult>() {
            @Override
            public void onChanged(quickorderModel.quickordResult quickordResult) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(quickordResult.getPayment_method()!=null) {
                            if (quickordResult.getPayment_method().equals("prepaid")) {
                                if (quickordResult.getTransaction_id() != null) {
                                    qobinding.custpaymeth.setText("Payment Method: " + "UPI " + "(Txn:" + quickordResult.getTransaction_id() + ")");
                                } else {
                                    qobinding.custpaymeth.setText("Payment Method: " + "UPI");
                                }

                            } else if ((quickordResult.getPayment_method().equals("cod"))) {
                                qobinding.custpaymeth.setText("Payment Method: " + "COD");
                            }
                        }
                        else {
                            qobinding.custpaymeth.setText("Payment Method: "+"COD");
                        }

                        if(quickordResult.getStatus()!=null) {
                            if (!quickordResult.getStatus().equals("Delivered")) {
                                if (quickordResult.getPayment_method() != null) {
                                    if (quickordResult.getPayment_method().equals("prepaid")) {
                                        qobinding.custpaystat.setText("Payment Status: " + "Paid");
                                    } else if (quickordResult.getPayment_method().equals("cod")) {
                                        qobinding.custpaystat.setText("Payment Status: " + "Pending");
                                    }
                                } else {
                                    qobinding.custpaystat.setText("Payment Status: " + "Pending");
                                }
                            } else {
                                qobinding.custpaystat.setText("Payment Status: " + "Pending");
                            }
                        }
                        else {
                            qobinding.custpaystat.setText("Payment Status: " + "Pending");
                        }

                        qobinding.custname.setText("Name: "+quickordResult.getCustomer_name());
                        qobinding.custaddr.setText("Address: "+quickordResult.getCustomer_address());
                        if (quickordResult.getSubtotal() != null) {
                            qobinding.carttotalprice.setText(quickordResult.getSubtotal());
                        }
                        if (quickordResult.getShipping_charge() != null) {
                            qobinding.cartdeliverycharge.setText(quickordResult.getShipping_charge());
                        }
                        if (quickordResult.getTotal_price() != null) {
                            qobinding.cartgrandtotal.setText(quickordResult.getTotal_price());
                        }

                        if (quickordResult.getExpected_delivery() != null) {

                            qobinding.exdelvmin.setText(quickordResult.getExpected_delivery());
                        }
                        if(quickordResult.getOrder_instructions()!=null) {
                            qobinding.orderDesc.setText(quickordResult.getOrder_instructions());
                        }

                        if(quickordResult.getDelivery_instructions()!=null) {
                            qobinding.delvDesc.setText(quickordResult.getDelivery_instructions());
                        }
                        if (quickordResult.getSeller_status() != null) {
                            if (quickordResult.getSeller_status().equals("Accepted")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Wait For Customer To Respond.");

                            } else if (quickordResult.getSeller_status().equals("Rejected")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("You Rejected The Order!");
                            }
                        }

                        if (quickordResult.getUser_status() != null) {
                            if (quickordResult.getUser_status().equals("Accepted")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Order Confirmed!");
                            }
                            else if (quickordResult.getUser_status().equals("Rejected")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Order Rejected By Customer!");
                            }
                        }
                        if (quickordResult.getStatus() != null) {
                            if (quickordResult.getStatus().equals("Delivered")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                 qobinding.ordpackbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Order Delivered.");
                                qobinding.custpaystat.setText("Payment Status: " + "Paid");
                            }
                        }

                        for (int i = 0; i < quickordResult.getOrderstatus().size(); i++) {
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Pending")) {
                                if (quickordResult.getPayment_method() != null) {
                                    if (quickordResult.getPayment_method().toString().equals("prepaid")) {
                                        qobinding.placeorderbtn.setVisibility(View.VISIBLE);
                                        qobinding.ordrejbtn.setVisibility(View.GONE);
                                    } else if (quickordResult.getPayment_method().toString().equals("cod")) {
                                        qobinding.placeorderbtn.setVisibility(View.VISIBLE);
                                        qobinding.ordrejbtn.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    qobinding.placeorderbtn.setVisibility(View.VISIBLE);
                                    qobinding.ordrejbtn.setVisibility(View.GONE);
                                }
                            } else if (quickordResult.toString().equals("Rejected")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                            }
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Accepted")) {
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordpackbtn.setVisibility(View.VISIBLE);
                            }
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Packing")) {
                                qobinding.ordpackbtn.setVisibility(View.GONE);
                                qobinding.ordredpickbtn.setVisibility(View.VISIBLE);
                            }
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Ready_To_Ship")) {
                                qobinding.ordredpickbtn.setVisibility(View.GONE);
                                qobinding.ordoutdelv.setVisibility(View.VISIBLE);
                            }
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Out_For_Delivery")) {
                                qobinding.ordoutdelv.setVisibility(View.GONE);
                                //  vobinding.orddelv.setVisibility(View.VISIBLE);
                            }
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Delivered")) {
                                qobinding.ordoutdelv.setVisibility(View.GONE);
                                qobinding.ordpackbtn.setVisibility(View.GONE);
                                qobinding.custpaystat.setText("Payment Status: Paid");

                            }
                            if (quickordResult.getOrderstatus().get(i).getStatus().toString().equals("Cancelled") ||
                                    quickordResult.getStatus().equals("Cancelled")) {
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Order Cancelled (Reason: "+quickordResult.getCancel_reason()+")");
                            }
                        }
                    }
                }, 100);
            }
        });
        viewfunc();
        loadData();

    }

    private void loadData() {

        qordimgAdapter = new qordimgAdapter(this, quickordViewModel.getProdimgModel().getValue());
        LinearLayoutManager llm1 = new LinearLayoutManager(this);
        llm1.setOrientation(RecyclerView.HORIZONTAL);
        qobinding.prodimgrec.setLayoutManager(llm1);
        qobinding.prodimgrec.setAdapter(qordimgAdapter);

//        if(qobinding.carttotalprice.getText().toString().equals("")){
//        qobinding.cartdeliverycharge.setVisibility(View.INVISIBLE);
//        }
//        else
//            qobinding.cartdeliverycharge.setVisibility(View.VISIBLE);

        qobinding.cartdeliverycharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            if(editable.length()>0) {

                if(qobinding.carttotalprice.getText().toString().equals("")){


                    Toast.makeText(act_quickord.this, "Enter price first", Toast.LENGTH_LONG).show();


                }else{
                    qobinding.cartgrandtotal.setText(String.valueOf(Integer.valueOf(qobinding.carttotalprice.getText().toString()) +
                            Integer.valueOf(editable.toString())));
                }
            }


            }
        });


    }

    private void viewfunc() {

        qobinding.carttotalprice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        qobinding.cartgrandtotal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        qobinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }
        });
        qobinding.prodlistadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordprodAdapter.prodList.add(new quickorderModel.quick_products(null, null, null));
                ordprodAdapter.notifyDataSetChanged();

            }
        });
    
        qobinding.placeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ordprodAdapter.prodList!=null) {
                    if (ordprodAdapter.prodList.get(0).getPrice()==null) {
                        Toast.makeText(act_quickord.this, "Please Enter Price First!", Toast.LENGTH_SHORT).show();
                    } else if (ordprodAdapter.prodList.get(0).getProduct_name()==null) {
                        Toast.makeText(act_quickord.this, "Please Write A Product First!", Toast.LENGTH_SHORT).show();
                    } else if (ordprodAdapter.prodList.get(0).getQty()==null) {
                        Toast.makeText(act_quickord.this, "Please Enter Quantity First!", Toast.LENGTH_SHORT).show();
                    }
                }
                if(qobinding.carttotalprice.getText().toString().isEmpty()) {
                    Toast.makeText(act_quickord.this, "Please Enter Proper Amount!", Toast.LENGTH_SHORT).show();
                }
                else  if(qobinding.cartdeliverycharge.getText().toString().isEmpty()) {
                    Toast.makeText(act_quickord.this, "Please Enter Proper Amount!", Toast.LENGTH_SHORT).show();
                }
                else  if(qobinding.cartgrandtotal.getText().toString().isEmpty()) {
                    Toast.makeText(act_quickord.this, "Please Enter Proper Amount!", Toast.LENGTH_SHORT).show();
                }

                else if(qobinding.exdelvmin.getText().toString().isEmpty()) {
                    Toast.makeText(act_quickord.this, "Please Enter Expected Delivery Time!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                    StringBuilder productname = new StringBuilder();
                    StringBuilder qty = new StringBuilder();
                    StringBuilder price = new StringBuilder();
                    String delivery_time =qobinding.exdelvmin.getText().toString();

                    for (int i = 0; i < ordprodAdapter.prodList.size(); i++) {
                        if (ordprodAdapter.prodList.size() > 1) {
                            if (ordprodAdapter.prodList.get(i).getProduct_name() != null &&
                                    !ordprodAdapter.prodList.get(i).getProduct_name().equals("") &&
                                    ordprodAdapter.prodList.get(i).getQty() != null &&
                                    !ordprodAdapter.prodList.get(i).getQty().equals("")) {
                                productname.append(ordprodAdapter.prodList.get(i).getProduct_name() + ",");
                                qty.append(ordprodAdapter.prodList.get(i).getQty() + ",");
                                price.append(ordprodAdapter.prodList.get(i).getPrice() + ",");
                            }
                        } else {
                            productname.append(ordprodAdapter.prodList.get(i).getProduct_name());
                            qty.append(ordprodAdapter.prodList.get(i).getQty());
                            price.append(ordprodAdapter.prodList.get(i).getPrice());
                        }
                    }

                    Call<quickorderModel.singlequickordResp> call = logregApiInterface.update_quickorder(storeid, quickorderid
                            , "Accepted", productname.toString(), qty.toString(), price.toString(), qobinding.carttotalprice.getText().toString()
                            , qobinding.cartdeliverycharge.getText().toString(), qobinding.cartgrandtotal.getText().toString(), delivery_time);


                    call.enqueue(new Callback<quickorderModel.singlequickordResp>() {
                        @Override
                        public void onResponse(Call<quickorderModel.singlequickordResp> call, Response<quickorderModel.singlequickordResp> response) {
                            if (!response.isSuccessful()) {
                                Log.d("errorcode", String.valueOf(response.code()));
                                return;
                            }

                            quickorderModel.singlequickordResp resp = response.body();

                            if (resp.getResult() != null) {
                                Toast.makeText(act_quickord.this, "Status Updated!", Toast.LENGTH_SHORT).show();
                                qobinding.placeorderbtn.setVisibility(View.GONE);
                                qobinding.ordrejbtn.setVisibility(View.GONE);
                                qobinding.orstatustxt.setVisibility(View.VISIBLE);
                                qobinding.orstatustxt.setText("Wait For User To Respond.");
                            }
                            else {
                                Toast.makeText(act_quickord.this, "There Was An Error, Please Try Again!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<quickorderModel.singlequickordResp> call, Throwable throwable) {
                            Log.d("Failure", throwable.getMessage());
                        }
                    });
                }
            }
        });

        qobinding.ordrejbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                Call<quickorderModel.singlequickordResp> call = logregApiInterface.update_quickorder(storeid ,quickorderid
                        , "Rejected",null,null,null,null,null,null
                ,null);


                call.enqueue(new Callback<quickorderModel.singlequickordResp>() {
                    @Override
                    public void onResponse(Call<quickorderModel.singlequickordResp> call, Response<quickorderModel.singlequickordResp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("errorcode", String.valueOf(response.code()));
                            return;
                        }

                        quickorderModel.singlequickordResp resp = response.body();

                        if (resp.getResult() != null) {
                            Toast.makeText(act_quickord.this, "Status Updated!", Toast.LENGTH_SHORT).show();
                            qobinding.placeorderbtn.setVisibility(View.GONE);
                            qobinding.ordrejbtn.setVisibility(View.GONE);
                            qobinding.orstatustxt.setVisibility(View.VISIBLE);
                            qobinding.orstatustxt.setText("Order Rejected By You!");
                        }

                        else {
                            Toast.makeText(act_quickord.this, "There Was An Error, Please Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<quickorderModel.singlequickordResp> call, Throwable throwable) {
                        Log.d("Failure",throwable.getMessage());
                    }
                });
            }
        });

        qobinding.ordpackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                Call<quickorderModel.singlequickordResp> call = logregApiInterface.finalupdate_quickorder(storeid ,quickorderid
                        , "Packing");


                call.enqueue(new Callback<quickorderModel.singlequickordResp>() {
                    @Override
                    public void onResponse(Call<quickorderModel.singlequickordResp> call, Response<quickorderModel.singlequickordResp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("errorcode", String.valueOf(response.code()));
                            return;
                        }

                        quickorderModel.singlequickordResp resp = response.body();

                        if (resp.getResult() != null) {
                            Toast.makeText(act_quickord.this, "Status Updated!", Toast.LENGTH_SHORT).show();
                            qobinding.placeorderbtn.setVisibility(View.GONE);
                            qobinding.ordrejbtn.setVisibility(View.GONE);
                            qobinding.ordredpickbtn.setVisibility(View.VISIBLE);
                        }

                        else {
                            Toast.makeText(act_quickord.this, "There Was An Error, Please Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<quickorderModel.singlequickordResp> call, Throwable throwable) {
                        Log.d("Failure",throwable.getMessage());
                    }
                });
            }
        });

        qobinding.ordredpickbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                Call<quickorderModel.singlequickordResp> call = logregApiInterface.finalupdate_quickorder(storeid ,quickorderid
                        , "Ready_To_Ship");


                call.enqueue(new Callback<quickorderModel.singlequickordResp>() {
                    @Override
                    public void onResponse(Call<quickorderModel.singlequickordResp> call, Response<quickorderModel.singlequickordResp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("errorcode", String.valueOf(response.code()));
                            return;
                        }

                        quickorderModel.singlequickordResp resp = response.body();

                        if (resp.getResult() != null) {
                            Toast.makeText(act_quickord.this, "Status Updated!", Toast.LENGTH_SHORT).show();
                            qobinding.ordredpickbtn.setVisibility(View.GONE);
                            qobinding.ordoutdelv.setVisibility(View.VISIBLE);
                        }

                        else {
                            Toast.makeText(act_quickord.this, "There Was An Error, Please Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<quickorderModel.singlequickordResp> call, Throwable throwable) {
                        Log.d("Failure",throwable.getMessage());
                    }
                });
            }
        });

        qobinding.ordoutdelv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                        .addConverterFactory(GsonConverterFactory.create()).build();

                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                Call<quickorderModel.singlequickordResp> call = logregApiInterface.finalupdate_quickorder(storeid ,quickorderid
                        , "Out_For_Delivery");

                call.enqueue(new Callback<quickorderModel.singlequickordResp>() {
                    @Override
                    public void onResponse(Call<quickorderModel.singlequickordResp> call, Response<quickorderModel.singlequickordResp> response) {
                        if (!response.isSuccessful()) {
                            Log.d("errorcode", String.valueOf(response.code()));
                            return;
                        }

                        quickorderModel.singlequickordResp resp = response.body();

                        if (resp.getResult() != null) {
                            Toast.makeText(act_quickord.this, "Status Updated!", Toast.LENGTH_SHORT).show();
                            qobinding.placeorderbtn.setVisibility(View.GONE);
                            qobinding.ordoutdelv.setVisibility(View.GONE);
                            qobinding.ordpackbtn.setVisibility(View.GONE);
                            qobinding.orstatustxt.setVisibility(View.VISIBLE);
                            qobinding.orstatustxt.setText("Order Out For Delivery!");
                        }

                        else {
                            Toast.makeText(act_quickord.this, "There Was An Error, Please Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<quickorderModel.singlequickordResp> call, Throwable throwable) {
                        Log.d("Failure",throwable.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        this.getViewModelStore().clear();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}