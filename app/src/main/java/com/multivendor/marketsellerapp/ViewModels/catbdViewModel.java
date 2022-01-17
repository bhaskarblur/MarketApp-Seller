package com.multivendor.marketsellerapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.multivendor.marketsellerapp.Models.categoriesModel;
import com.multivendor.marketsellerapp.Models.productitemModel;
import com.multivendor.marketsellerapp.Repos.catbdRepo;

import java.util.List;


public class catbdViewModel extends ViewModel {
    private MutableLiveData<List<categoriesModel>> catModel;
    private MutableLiveData<List<productitemModel>> productModel;
    private MutableLiveData<List<productitemModel>> allproductModel;
    private com.multivendor.marketsellerapp.Repos.catbdRepo mRepo=new catbdRepo();



    public void initwork(String userid, String catid) {
        if(catModel!=null) {
            return;
        }
        if(productModel!=null) {
            return;
        }
        allproductModel=mRepo.getInstance().returnallproductitem(userid);
        catModel=mRepo.getInstance().returncatModel(userid);
        if(catid!=null) {
            productModel=mRepo.getInstance().returnproductitem(userid,catid);
        }

    }
    public MutableLiveData<List<categoriesModel>> getCatModel() {
        return catModel;
    }

    public LiveData<List<productitemModel>> getProductModel() {
        return productModel;
    }
    
    public LiveData<List<productitemModel>> getAllproductModel() {
        return allproductModel;
    }




}
