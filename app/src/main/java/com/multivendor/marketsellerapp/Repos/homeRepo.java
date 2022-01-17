package com.multivendor.marketsellerapp.Repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.APIWork.ApiWork;
import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.Models.newProductModel;
import com.multivendor.marketsellerapp.Models.quickorderModel;
import com.multivendor.marketsellerapp.api_baseurl;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class homeRepo {

    private homeRepo instance;
    private List<quickorderModel.quickordResult> quickordList = new ArrayList<>();
    private MutableLiveData<List<quickorderModel.quickordResult>> quickordModel = new MutableLiveData<>();
    private List<cartModel.singlecartResult> pendordList = new ArrayList<>();
    private MutableLiveData<newProductModel.homeprodResult> nyshopdata=new MutableLiveData<>();
    private MutableLiveData<List<cartModel.singlecartResult>> pendordModel = new MutableLiveData<>();
    private api_baseurl baseurl=new api_baseurl();
    public homeRepo getInstance() {
        if (instance == null) {
            instance = new homeRepo();
        }
        return instance;
    }

    public MutableLiveData<List<cartModel.singlecartResult>> returnpendordModel(String storeid) {
        getpendordFromSource(storeid);
        if (pendordList == null) {
            pendordModel.setValue(null);
        }
        pendordModel.setValue(pendordList);
        return pendordModel;
    }

    private void getpendordFromSource(String storeid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<cartModel.multcartResp> call = logregApiInterface.getorders(storeid, "pending");

        call.enqueue(new Callback<cartModel.multcartResp>() {
            @Override
            public void onResponse(Call<cartModel.multcartResp> call, Response<cartModel.multcartResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("errorcode:", String.valueOf(response.code()));
                    return;
                }

                cartModel.multcartResp resp = response.body();
                if (resp.getResult() != null) {

                    for (cartModel.singlecartResult data : resp.getResult()) {
                        pendordList.add(data);
                    }
                    pendordModel.setValue(pendordList);
                }
            }

            @Override
            public void onFailure(Call<cartModel.multcartResp> call, Throwable t) {
                Log.d("failure11:", t.getMessage());
            }
        });


    }

    public MutableLiveData<newProductModel.homeprodResult> returnnybyshopdata(String userid, String lat, String longit, String cityname) {
        getnbyshopsdatafromSource(userid,lat,longit,cityname);
        return nyshopdata;
    }

    private void getnbyshopsdatafromSource(String userid,String lat,String longit,String cityname) {
        Log.d("latandlong",lat+","+longit);
        Log.d("city_name",cityname);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork= retrofit.create(ApiWork.class);

        Call<newProductModel.homeprodResp> call=apiWork.getallproducts(userid,lat,longit,cityname);

        call.enqueue(new Callback<newProductModel.homeprodResp>() {
            @Override
            public void onResponse(Call<newProductModel.homeprodResp> call, Response<newProductModel.homeprodResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                newProductModel.homeprodResp storedata = response.body();




                if(storedata.getResult()!=null) {
                    Log.d("message12",storedata.getSuccess());
                    nyshopdata.setValue(storedata.getResult());
                }

            }

            @Override
            public void onFailure(Call<newProductModel.homeprodResp> call, Throwable t) {
                Log.d("errorshops",t.getMessage().toString());
            }
        });

    }

    public MutableLiveData<List<quickorderModel.quickordResult>> returnquickModel(String storeid) {
        getquickordFromQuick(storeid);
        if(quickordList!=null) {
            quickordModel.setValue(null);
        }
        quickordModel.setValue(quickordList);
        return quickordModel;
    }

    private void getquickordFromQuick(String storeid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<quickorderModel.quickordResp> call = logregApiInterface.get_quickorders(storeid);

        call.enqueue(new Callback<quickorderModel.quickordResp>() {
            @Override
            public void onResponse(Call<quickorderModel.quickordResp> call, Response<quickorderModel.quickordResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorcode",String.valueOf(response.code()));
                    return;
                }

                quickorderModel.quickordResp resp=response.body();
                if(resp.getResult()!=null) {
                    for(quickorderModel.quickordResult quickdata:resp.getResult()) {
                        quickordList.add(quickdata);
                    }
                    quickordModel.setValue(quickordList);
                }
            }

            @Override
            public void onFailure(Call<quickorderModel.quickordResp> call, Throwable throwable) {
                Log.d("failure11:", throwable.getMessage());
            }
        });


        Call<quickorderModel.quickordResp> call1 = logregApiInterface.get_nextquickorders(storeid);

        call1.enqueue(new Callback<quickorderModel.quickordResp>() {
            @Override
            public void onResponse(Call<quickorderModel.quickordResp> call, Response<quickorderModel.quickordResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorcode",String.valueOf(response.code()));
                    return;
                }

                quickorderModel.quickordResp resp=response.body();
                if(resp.getResult()!=null) {
                    for(quickorderModel.quickordResult quickdata:resp.getResult()) {
                        quickordList.add(quickdata);
                    }
                    quickordModel.setValue(quickordList);
                }
            }

            @Override
            public void onFailure(Call<quickorderModel.quickordResp> call, Throwable throwable) {
                Log.d("failure12:", throwable.getMessage());
            }
        });
    }

}
