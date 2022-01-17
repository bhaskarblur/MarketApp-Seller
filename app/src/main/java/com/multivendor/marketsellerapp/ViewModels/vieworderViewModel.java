package com.multivendor.marketsellerapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.Models.productitemModel;
import com.multivendor.marketsellerapp.Repos.vieworderRepo;

import java.util.ArrayList;
import java.util.List;

public class vieworderViewModel extends ViewModel {


    public MutableLiveData<cartModel.singlecartResult> getordinfo;
    private MutableLiveData<List<productitemModel>> allproductModel;
    private com.multivendor.marketsellerapp.Repos.vieworderRepo mRepo=new vieworderRepo();

    public void initwork(String storeid,String orderid) {
        if(getordinfo!=null) {
            return;
        }
        getordinfo=mRepo.getInstance().returnorderdata(storeid,orderid);
        allproductModel=mRepo.getInstance().returnallproductitem(storeid);
    }

    public LiveData<List<productitemModel>> getAllproductModel() {
        return allproductModel;
    }

    public LiveData<cartModel.singlecartResult> getGetordinfo() {
        return getordinfo;
    }


}
