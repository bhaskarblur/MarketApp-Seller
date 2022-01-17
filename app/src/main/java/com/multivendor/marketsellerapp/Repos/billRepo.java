package com.multivendor.marketsellerapp.Repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Models.billModel;
import com.multivendor.marketsellerapp.Models.quickorderModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class billRepo {

    private billRepo instance;
    private List<billModel.billresult> pendlist=new ArrayList<>();
    private MutableLiveData<List<billModel.billresult>> pendModel=new MutableLiveData<>();
    private List<billModel.billresult> complist=new ArrayList<>();
    private MutableLiveData<List<billModel.billresult>> compModel=new MutableLiveData<>();

    public billRepo getInstance() {
        if(instance==null) {
            instance=new billRepo();
        }
        return instance;

    }

    public MutableLiveData<List<billModel.billresult>> returnpendModel(String storeid) {
        getpenData(storeid);
        if(pendlist==null) {
            pendModel.setValue(null);
        }
        pendModel.setValue(pendlist);
        return pendModel;
    }

    public MutableLiveData<List<billModel.billresult>> returncompModel(String storeid) {
        getcompData( storeid);
        if(complist==null) {
            compModel.setValue(null);
        }
        compModel.setValue(complist);
        return compModel;
    }

    private void getcompData(String storeid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<billModel.billListResp> call = logregApiInterface.getbill(storeid,"success");

        call.enqueue(new Callback<billModel.billListResp>() {
            @Override
            public void onResponse(Call<billModel.billListResp> call, Response<billModel.billListResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorcode",String.valueOf(response.code()));
                    return;
                }

                billModel.billListResp resp=response.body();
                Log.d("msg",resp.getMessage());
                if(resp.getResilt()!=null) {
                    for(billModel.billresult data:resp.getResilt()) {
                        complist.add(data);
                    }
                    compModel.setValue(complist);
                }
            }

            @Override
            public void onFailure(Call<billModel.billListResp> call, Throwable throwable) {
                Log.d("failure",throwable.getMessage());
            }
        });
    }


    private void getpenData(String storeid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<billModel.billListResp> call = logregApiInterface.getbill(storeid,"pending");

        call.enqueue(new Callback<billModel.billListResp>() {
            @Override
            public void onResponse(Call<billModel.billListResp> call, Response<billModel.billListResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorcode",String.valueOf(response.code()));
                    return;
                }

                billModel.billListResp resp=response.body();
                Log.d("msg",resp.getMessage());
                if(resp.getResilt()!=null) {
                    for(billModel.billresult data:resp.getResilt()) {
                        pendlist.add(data);
                    }
                    pendModel.setValue(pendlist);
                }
            }

            @Override
            public void onFailure(Call<billModel.billListResp> call, Throwable throwable) {
                Log.d("failure",throwable.getMessage());
            }
        });

    }

}
