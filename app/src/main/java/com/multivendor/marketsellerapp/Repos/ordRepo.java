package com.multivendor.marketsellerapp.Repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.Models.ordersModel;
import com.multivendor.marketsellerapp.api_baseurl;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ordRepo {

    private ordRepo instance;
    private List<cartModel.singlecartResult> acpordList=new ArrayList<>();
    private List<cartModel.singlecartResult> pendordList=new ArrayList<>();
    private List<cartModel.singlecartResult> delvordList=new ArrayList<>();
    private List<cartModel.singlecartResult> rejordList=new ArrayList<>();
    private MutableLiveData<List<cartModel.singlecartResult>> acpordModel=new MutableLiveData<>();
    private MutableLiveData<List<cartModel.singlecartResult>> pendordModel=new MutableLiveData<>();;
    private MutableLiveData<List<cartModel.singlecartResult>> delvordModel=new MutableLiveData<>();;
    private MutableLiveData<List<cartModel.singlecartResult>> rejordModel=new MutableLiveData<>();;
    private final api_baseurl baseurl=new api_baseurl();
    public ordRepo getInstance() {
        if(instance==null) {
            instance=new ordRepo();
        }
         return instance;
    }

    public MutableLiveData<List<cartModel.singlecartResult>> returnacpordModel(String storeid) {
        getacpordFromSource(storeid);
        if(acpordList==null) {
            acpordModel.setValue(null);
        }
        acpordModel.setValue(acpordList);
        return acpordModel;
    }

    private void getacpordFromSource(String storeid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<cartModel.multcartResp> call=logregApiInterface.getorders(storeid,"Accepted");

        call.enqueue(new Callback<cartModel.multcartResp>() {
            @Override
            public void onResponse(Call<cartModel.multcartResp> call, Response<cartModel.multcartResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorcode:",String.valueOf(response.code()));
                    return;
                }

                cartModel.multcartResp resp=response.body();
                if(resp.getResult()!=null) {

                    for(cartModel.singlecartResult data:resp.getResult()) {
                        acpordList.add(data);
                    }
                    acpordModel.setValue(acpordList);
                }
            }

            @Override
            public void onFailure(Call<cartModel.multcartResp> call, Throwable t) {
                Log.d("failure:",t.getMessage());
            }
        });


    }

    public MutableLiveData<List<cartModel.singlecartResult>> returnpendordModel(String storeid) {
        getpendordFromSource(storeid);
        if(pendordList==null) {
            pendordModel.setValue(null);
        }
        pendordModel.setValue(pendordList);
        return pendordModel;
    }

    private void getpendordFromSource(String storeid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<cartModel.multcartResp> call=logregApiInterface.getorders(storeid,"pending");

        call.enqueue(new Callback<cartModel.multcartResp>() {
            @Override
            public void onResponse(Call<cartModel.multcartResp> call, Response<cartModel.multcartResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorcode:",String.valueOf(response.code()));
                    return;
                }

                cartModel.multcartResp resp=response.body();
                if(resp.getResult()!=null) {

                    for(cartModel.singlecartResult data:resp.getResult()) {
                        pendordList.add(data);
                    }
                    pendordModel.setValue(pendordList);
                }
            }

            @Override
            public void onFailure(Call<cartModel.multcartResp> call, Throwable t) {
                Log.d("failure:",t.getMessage());
            }
        });


    }

    public MutableLiveData<List<cartModel.singlecartResult>> returndelvordModel(String storeid) {
        getdelvordFromSource(storeid);
        if(delvordList==null) {
            delvordModel.setValue(null);
        }
        delvordModel.setValue(delvordList);
        return delvordModel;
    }

    private void getdelvordFromSource(String storeid) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl  )
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<cartModel.multcartResp> call=logregApiInterface.getorders(storeid,"delivered");

        call.enqueue(new Callback<cartModel.multcartResp>() {
            @Override
            public void onResponse(Call<cartModel.multcartResp> call, Response<cartModel.multcartResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorcode:",String.valueOf(response.code()));
                    return;
                }

                cartModel.multcartResp resp=response.body();
                if(resp.getResult()!=null) {

                    for(cartModel.singlecartResult data:resp.getResult()) {
                        delvordList.add(data);
                    }
                    delvordModel.setValue(delvordList);
                }
            }

            @Override
            public void onFailure(Call<cartModel.multcartResp> call, Throwable t) {
                Log.d("failure:",t.getMessage());
            }
        });
    }

    public MutableLiveData<List<cartModel.singlecartResult>> returnrejordModel(String storeid) {
        getrejordFromSource(storeid);
        if(rejordList==null) {
            rejordModel.setValue(null);
        }
        rejordModel.setValue(rejordList);
        return rejordModel;
    }

    private void getrejordFromSource(String storeid) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<cartModel.multcartResp> call=logregApiInterface.getorders(storeid,"Rejected");

        call.enqueue(new Callback<cartModel.multcartResp>() {
            @Override
            public void onResponse(Call<cartModel.multcartResp> call, Response<cartModel.multcartResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorcode:",String.valueOf(response.code()));
                    return;
                }

                cartModel.multcartResp resp=response.body();
                if(resp.getResult()!=null) {

                    for(cartModel.singlecartResult data:resp.getResult()) {
                        rejordList.add(data);
                    }
                    rejordModel.setValue(rejordList);
                }
            }

            @Override
            public void onFailure(Call<cartModel.multcartResp> call, Throwable t) {
                Log.d("failure:",t.getMessage());
            }
        });
    }
}
