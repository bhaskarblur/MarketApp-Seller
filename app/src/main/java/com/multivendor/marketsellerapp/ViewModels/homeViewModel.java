package com.multivendor.marketsellerapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.Models.newProductModel;
import com.multivendor.marketsellerapp.Models.quickorderModel;
import com.multivendor.marketsellerapp.Repos.homeRepo;

import java.util.List;


public class homeViewModel extends ViewModel {
    private MutableLiveData<List<cartModel.singlecartResult>> newordModel;
    private MutableLiveData<List<quickorderModel .quickordResult>> quickordModel;
    private com.multivendor.marketsellerapp.Repos.homeRepo mRepo=new homeRepo();
    private MutableLiveData<newProductModel.homeprodResult> nbyshopmodel;

    public void getlocation(String userid,String lat,String longit,String cityname) {
        if(nbyshopmodel!=null) {
            return;
        }
        nbyshopmodel=mRepo.getInstance().returnnybyshopdata(userid,lat,longit,cityname);
    }
    public void initwork(String userid) {
        if(newordModel!=null) {
            return;
        }
        newordModel=mRepo.getInstance().returnpendordModel(userid);
        quickordModel=mRepo.getInstance().returnquickModel(userid);
    }

    public LiveData<List<cartModel.singlecartResult>> getNewordModel() {
        return newordModel;
    }

    public MutableLiveData<List<quickorderModel.quickordResult>> getQuickordModel() {
        return quickordModel;
    }

    public LiveData<newProductModel.homeprodResult> getnbyshopModel() {
        return nbyshopmodel;
    }

}
