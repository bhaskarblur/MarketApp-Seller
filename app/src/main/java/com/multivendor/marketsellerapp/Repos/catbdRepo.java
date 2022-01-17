package com.multivendor.marketsellerapp.Repos;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.multivendor.marketsellerapp.Models.categoriesModel;
import com.multivendor.marketsellerapp.Models.loginresResponse;
import com.multivendor.marketsellerapp.Models.productitemModel;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.api_baseurl;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class catbdRepo {
    private catbdRepo instance;
    private List<categoriesModel> catlist=new ArrayList<>();
    private List<productitemModel> productlist=new ArrayList<>();
    private MutableLiveData<List<productitemModel>> productModel=new MutableLiveData<>();
    private MutableLiveData<List<categoriesModel>> cattModel=new MutableLiveData<>();
    private List<productitemModel> allproductlist=new ArrayList<>();
    private MutableLiveData<List<productitemModel>> allproductModel=new MutableLiveData<>();
    private final api_baseurl baseurl=new api_baseurl();
    public catbdRepo getInstance() {
        if(instance==null) {
            instance=new catbdRepo();
        }
        return instance;
    }

    public MutableLiveData<List<categoriesModel>> returncatModel(String userid) {
        getcatdatafromSource(userid);
        if(catlist==null) {
            cattModel.setValue(null);
        }
        cattModel.setValue(catlist);
        return cattModel;
    }

    public MutableLiveData<List<productitemModel>> returnproductitem(String userid,String catid) {
        getproductdatafromSource(userid,catid);
        if(productlist==null) {
            productModel.setValue(null);
        }
        productModel.setValue(productlist);
        return productModel;
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
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

    private void getproductdatafromSource(String userid,String catid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        com.multivendor.marketsellerapp.APIWork.LogregApiInterface logregApiInterface=retrofit.create(com.multivendor.marketsellerapp.APIWork.LogregApiInterface.class);

        Call<sellerApiResp.getproduct_info> call=logregApiInterface.getqueproducts(userid,catid);

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
                            productlist.add(itrdata);
                        }
                        productModel.setValue(productlist);
                    }
                }

            }

            @Override
            public void onFailure(Call<sellerApiResp.getproduct_info> call, Throwable t) {

            }
        });
    }

    private void getcatdatafromSource(String userid) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        com.multivendor.marketsellerapp.APIWork.LogregApiInterface logregApiInterface=retrofit.create(com.multivendor.marketsellerapp.APIWork.LogregApiInterface.class);

        Call<sellerApiResp.get_category> call=logregApiInterface.get_category(userid);

        call.enqueue(new Callback<sellerApiResp.get_category>() {
            @Override
            public void onResponse(Call<sellerApiResp.get_category> call, Response<sellerApiResp.get_category> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                sellerApiResp.get_category resp = response.body();

                if(resp.getMessage()!=null) {
                    Log.d("message",resp.getMessage());
                }
                if(resp.getMessage().equals("All Category.!")) {
                    if(resp.getResult()!=null) {
                        for(int i=0;i<resp.getResult().size();i++) {
                            catlist.add(resp.getResult().get(i));
                        }
                        cattModel.setValue(catlist);
                    }

                }
            }

            @Override
            public void onFailure(Call<sellerApiResp.get_category> call, Throwable t) {
                Log.d("error",t.getMessage());
            }
        });
    }

}
