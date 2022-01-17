package com.multivendor.marketsellerapp.Repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.Models.orderprodModel;
import com.multivendor.marketsellerapp.Models.productitemModel;
import com.multivendor.marketsellerapp.Models.sellerApiResp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class vieworderRepo {

    private vieworderRepo instance;
    private List<orderprodModel> ordprodList=new ArrayList<>();
    private MutableLiveData<List<orderprodModel>> ordprodModel=new MutableLiveData<>();
    public MutableLiveData<cartModel.singlecartResult> getordinfo=new MutableLiveData<>();
    private List<productitemModel> allproductlist=new ArrayList<>();
    private MutableLiveData<List<productitemModel>> allproductModel=new MutableLiveData<>();
    public vieworderRepo getInstance() {
        if(instance==null) {
            instance=new vieworderRepo();
        }
        return instance;
    }

    public MutableLiveData<List<orderprodModel>> returnordprodModel() {
        getdata();
        if(ordprodList==null) {
            ordprodModel.setValue(null);
        }
        ordprodModel.setValue(ordprodList);
        return ordprodModel;
    }

    public MutableLiveData<cartModel.singlecartResult> returnorderdata(String storeid,String orderid) {
        getOrdInfo(storeid,orderid);
        return getordinfo;
    }

    private void getOrdInfo(String storeid,String orderid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<cartModel.singlecartResp> call=logregApiInterface.getsingleorder(storeid,orderid);

        call.enqueue(new Callback<cartModel.singlecartResp>() {
            @Override
            public void onResponse(Call<cartModel.singlecartResp> call, Response<cartModel.singlecartResp> response) {
                if(!response.isSuccessful()) {
                    return;
                }

                cartModel.singlecartResp resp=response.body();

                if(resp.getResult()!=null)  {
                    getordinfo.setValue(resp.getResult());
                }
            }

            @Override
            public void onFailure(Call<cartModel.singlecartResp> call, Throwable t) {

            }
        });
    }

    private void getdata() {
        ordprodList.add(new orderprodModel("https://www.bigbasket.com/media/uploads/p/xxl/40202281_4-lays-potato-chips-american-style-cream-onion-flavour-best-quality-crunchy.jpg","Red Lays Chips",500,"1kg",15,"ok"));

        ordprodList.add(new orderprodModel("https://www.bigbasket.com/media/uploads/p/xxl/40202281_4-lays-potato-chips-american-style-cream-onion-flavour-best-quality-crunchy.jpg","Red Lays Chips",500,"1kg",15,"ok"));

    }

    public MutableLiveData<List<productitemModel>> returnallproductitem(String userid) {
        getallproductdatafromSource(userid);
        if(allproductlist==null) {
            allproductModel.setValue(null);
        }
        allproductModel.setValue(allproductlist);
        return allproductModel;
    }

    private void getallproductdatafromSource(String userid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        com.multivendor.marketsellerapp.APIWork.LogregApiInterface logregApiInterface=retrofit.create(com.multivendor.marketsellerapp.APIWork.LogregApiInterface.class);

        Call<sellerApiResp.getproduct_info> call=logregApiInterface.getallproducts(userid);

        call.enqueue(new Callback<sellerApiResp.getproduct_info>() {
            @Override
            public void onResponse(Call<sellerApiResp.getproduct_info> call, Response<sellerApiResp.getproduct_info> response) {
                if(!response.isSuccessful()) {
                    Log.d("error code:",String.valueOf(response.code()));
                    return;
                }

                sellerApiResp.getproduct_info data=response.body();
                if(data!=null) {
                    if(data.getMessage()!=null) {
                        Log.d("message",data.getMessage());
                    }

                    if(data.getMessage().equals("My Products")) {
                        for(productitemModel itrdata:data.getResult().getProducts()) {
                            allproductlist.add(itrdata);
                        }
                        allproductModel.setValue(allproductlist);
                    }
                }

            }

            @Override
            public void onFailure(Call<sellerApiResp.getproduct_info> call, Throwable t) {

            }
        });
    }
}
