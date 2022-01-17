package com.multivendor.marketsellerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Adapters.ordproductAdapter;
import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.Models.orderprodModel;
import com.multivendor.marketsellerapp.Models.productitemModel;
import com.multivendor.marketsellerapp.ViewModels.vieworderViewModel;
import com.multivendor.marketsellerapp.databinding.ActivityActVieworderBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class act_vieworder extends AppCompatActivity {
    private ActivityActVieworderBinding vobinding;
    private ordproductAdapter ordproductAdapter;
    private com.multivendor.marketsellerapp.ViewModels.vieworderViewModel vieworderViewModel;
    private List<productitemModel> prodlist = new ArrayList<>();
    private String orderid;
    private String storeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vobinding = ActivityActVieworderBinding.inflate(getLayoutInflater());
        setContentView(vobinding.getRoot());
        this.getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        SharedPreferences sharedPreferences = getSharedPreferences("userlogged", 0);
        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderid");
        storeid = intent.getStringExtra("userid");
        vieworderViewModel = new ViewModelProvider(this).get(com.multivendor.marketsellerapp.ViewModels.vieworderViewModel.class);
        vieworderViewModel.initwork(storeid, orderid);

        vieworderViewModel.getGetordinfo().observe(act_vieworder.this, new Observer<cartModel.singlecartResult>() {
            @Override
            public void onChanged(cartModel.singlecartResult cartResult) {
                vobinding.carttotalprice.setText("₹ " + vieworderViewModel.getGetordinfo().getValue().getSubtotal());
                vobinding.cartdeliverycharge.setText("₹ " + vieworderViewModel.getGetordinfo().getValue().getShipping_charge());
                vobinding.cartgrandtotal.setText("₹ " + vieworderViewModel.getGetordinfo().getValue().getTotal_price());
                vobinding.custname3.setText("Name: " + vieworderViewModel.getGetordinfo().getValue()
                        .getCustomer_name());
                vobinding.custaddr6.setText("Address: " + vieworderViewModel.getGetordinfo().getValue()
                        .getCustomer_address());

                if (vieworderViewModel.getGetordinfo().getValue().getPayment_method() != null) {
                    if (vieworderViewModel.getGetordinfo().getValue().getPayment_method().equals("prepaid")) {
                        if (vieworderViewModel.getGetordinfo().getValue().getTransaction_id() != null) {
                            vobinding.custpaymeth.setText("Payment Method: " + "UPI " + "(Txn:" + vieworderViewModel.getGetordinfo().getValue().getTransaction_id() + ")");
                        } else {
                            vobinding.custpaymeth.setText("Payment Method: " + "UPI");
                        }

                    } else if ((vieworderViewModel.getGetordinfo().getValue().getPayment_method().equals("cod"))) {
                        vobinding.custpaymeth.setText("Payment Method: " + "COD");
                    }
                } else {
                    vobinding.custpaymeth.setText("Payment Method: " + "");
                }
                if (!vieworderViewModel.getGetordinfo().getValue().getStatus().equals("Delivered")) {
                    if (vieworderViewModel.getGetordinfo().getValue().getPayment_method() != null) {
                        if (vieworderViewModel.getGetordinfo().getValue().getPayment_method().equals("prepaid")) {
                            vobinding.custpaystat.setText("Payment Status: " + "Paid");
                        } else if (vieworderViewModel.getGetordinfo().getValue().getPayment_method().equals("cod")) {
                            vobinding.custpaystat.setText("Payment Status: " + "Pending");
                        }
                    } else {
                        vobinding.custpaystat.setText("Payment Status: " + "Pending");
                    }
                } else {
                    vobinding.custpaystat.setText("Payment Status: " + "Paid");
                }

                if (vieworderViewModel.getGetordinfo().getValue().getDelivery_instruction() != null) {
                    vobinding.custaddr5.setText("Instructions: " + vieworderViewModel.getGetordinfo().getValue().getDelivery_instruction());
                }
            }
        });
        vieworderViewModel.getAllproductModel().observe(this, new Observer<List<productitemModel>>() {
            @Override
            public void onChanged(List<productitemModel> productitemModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (productitemModels.size() > 0) {
                            if(vieworderViewModel.getGetordinfo().getValue()!=null) {
                                for (cartModel.productResult selprods : vieworderViewModel.getGetordinfo().getValue().getProducts()) {
                                    for (productitemModel prodmodel : productitemModels) {
                                        if (prodmodel.getProduct_id().equals(selprods.getProduct_id())) {
                                            prodlist.add(prodmodel);
                                        }
                                    }
                                }
                            }
                            loadData();
                        }
                    }

                }, 100);
            }
        });

        viewfunc();
        refreshDataFromServer(storeid, orderid);
    }

    private void refreshDataFromServer(String storeid, String orderid) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<cartModel.singlecartResp> call = logregApiInterface.getsingleorder(storeid, orderid);

        call.enqueue(new Callback<cartModel.singlecartResp>() {
            @Override
            public void onResponse(Call<cartModel.singlecartResp> call, Response<cartModel.singlecartResp> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                cartModel.singlecartResp resp = response.body();

                if (resp.getResult() != null) {
                    if (resp.getResult().getOrderstatus() != null) {
                        if (resp.getResult().getStatus().equals("Pending")) {
                            if (resp.getResult().getPayment_method() != null) {
                                if (resp.getResult().getPayment_method().toString().equals("prepaid")) {
                                    vobinding.ordactbtn.setVisibility(View.VISIBLE);
                                    vobinding.ordrejbtn.setVisibility(View.GONE);
                                } else if (resp.getResult().getPayment_method().toString().equals("cod")) {
                                    vobinding.ordactbtn.setVisibility(View.VISIBLE);
                                    vobinding.ordrejbtn.setVisibility(View.VISIBLE);
                                }
                            } else {
                                vobinding.ordactbtn.setVisibility(View.VISIBLE);
                                vobinding.ordrejbtn.setVisibility(View.GONE);
                            }
                        }
                        for (int i = 0; i < resp.getResult().getOrderstatus().size(); i++) {
                            if (resp.getResult().getOrderstatus().get(i).getStatus().toString().equals("Pending")) {
                                if (resp.getResult().getPayment_method() != null) {
                                    if (resp.getResult().getPayment_method().toString().equals("prepaid")) {
                                        vobinding.ordactbtn.setVisibility(View.VISIBLE);
                                        vobinding.ordrejbtn.setVisibility(View.GONE);
                                    } else if (resp.getResult().getPayment_method().toString().equals("cod")) {
                                        vobinding.ordactbtn.setVisibility(View.VISIBLE);
                                        vobinding.ordrejbtn.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    vobinding.ordactbtn.setVisibility(View.VISIBLE);
                                    vobinding.ordrejbtn.setVisibility(View.GONE);
                                }
                            } else if (resp.getResult().toString().equals("Rejected")) {
                                vobinding.ordactbtn.setVisibility(View.GONE);
                                vobinding.ordrejbtn.setVisibility(View.GONE);
                            }
                            if (resp.getResult().getOrderstatus().get(i).getStatus().toString().equals("Accepted")) {
                                vobinding.ordrejbtn.setVisibility(View.GONE);
                                vobinding.ordactbtn.setVisibility(View.GONE);
                                vobinding.ordprocbtn.setVisibility(View.VISIBLE);
                            }
                            if (resp.getResult().getOrderstatus().get(i).getStatus().toString().equals("Packing")) {
                                vobinding.ordprocbtn.setVisibility(View.GONE);
                                vobinding.ordredship.setVisibility(View.VISIBLE);
                            }
                            if (resp.getResult().getOrderstatus().get(i).getStatus().toString().equals("Ready_To_Ship")) {
                                vobinding.ordredship.setVisibility(View.GONE);
                                vobinding.ordoutdelv.setVisibility(View.VISIBLE);
                            }
                            if (resp.getResult().getOrderstatus().get(i).getStatus().toString().equals("Out_For_Delivery")) {
                                vobinding.ordoutdelv.setVisibility(View.GONE);
                                //  vobinding.orddelv.setVisibility(View.VISIBLE);
                            }
                            if (resp.getResult().getOrderstatus().get(i).getStatus().toString().equals("Delivered")) {
                                vobinding.orddelv.setVisibility(View.GONE);
                                vobinding.orstatustxt.setVisibility(View.VISIBLE);
                                vobinding.orstatustxt.setText("Order Delivered");
                                vobinding.custpaystat.setText("Payment Status: Paid");

                            }
                            if (resp.getResult().getOrderstatus().get(i).getStatus().toString().equals("Cancelled")) {
                                vobinding.orddelv.setVisibility(View.GONE);
                                vobinding.orddelv.setVisibility(View.GONE);
                                vobinding.orstatustxt.setVisibility(View.VISIBLE);
                                vobinding.orstatustxt.setText("Order Cancelled (Reason: " + resp.getResult().getCancel_reason() + ")");
                                vobinding.custpaystat.setText("Payment Status: Paid");

                            }
                        }
                    } else {
                        if (resp.getResult().getPayment_method() != null) {
                            if (resp.getResult().getPayment_method().toString().equals("prepaid")) {
                                vobinding.ordactbtn.setVisibility(View.VISIBLE);
                                vobinding.ordrejbtn.setVisibility(View.GONE);
                            } else if (resp.getResult().getPayment_method().toString().equals("cod")) {
                                vobinding.ordactbtn.setVisibility(View.VISIBLE);
                                vobinding.ordrejbtn.setVisibility(View.VISIBLE);
                            }
                        } else {
                            vobinding.ordactbtn.setVisibility(View.VISIBLE);
                            vobinding.ordrejbtn.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<cartModel.singlecartResp> call, Throwable t) {
                Log.d("failedhere", t.getMessage());
            }
        });
    }

    private void viewfunc() {
        vobinding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vobinding.ordactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updatestatus("Accepted");
            }
        });
        vobinding.ordrejbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updatestatus("Rejected");
            }
        });
        vobinding.ordprocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updatestatus("Packing");
            }
        });
        vobinding.ordredship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updatestatus("Ready_To_Ship");
            }
        });
        vobinding.ordoutdelv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updatestatus("Out_For_Delivery");
            }
        });
        vobinding.orddelv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Updatestatus("Delivered");
            }
        });
    }

    private void Updatestatus(String status) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<cartModel.multcartResp> call = logregApiInterface.update_orderstatus(storeid, orderid, status);

        call.enqueue(new Callback<cartModel.multcartResp>() {
            @Override
            public void onResponse(Call<cartModel.multcartResp> call, Response<cartModel.multcartResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("errorode", String.valueOf(response.code()));
                    return;
                }

                cartModel.multcartResp resp = response.body();

                if (resp.getResult() != null) {
                    Toast.makeText(act_vieworder.this, "Status Updated!", Toast.LENGTH_SHORT).show();
                    refreshDataFromServer(storeid, orderid);
                }
            }

            @Override
            public void onFailure(Call<cartModel.multcartResp> call, Throwable t) {
                Log.d("failure", t.getMessage());
            }
        });
    }

    private void loadData() {

        ordproductAdapter = new ordproductAdapter(this, prodlist, vieworderViewModel.getGetordinfo().
                getValue().getProducts());
        LinearLayoutManager llm = new LinearLayoutManager(this);
        vobinding.orditemsrec.setLayoutManager(llm);
        vobinding.orditemsrec.setAdapter(ordproductAdapter);
    }

    @Override
    public void finish() {
        super.finish();
        this.getViewModelStore().clear();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}