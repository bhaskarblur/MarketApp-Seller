package com.multivendor.marketsellerapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.Models.ordersModel;
import com.multivendor.marketsellerapp.Repos.ordRepo;

import java.util.List;

public class ordViewModel extends ViewModel {

    private MutableLiveData<List<cartModel.singlecartResult>> acpordModel;
    private MutableLiveData<List<cartModel.singlecartResult>> pendordModel;
    private MutableLiveData<List<cartModel.singlecartResult>> delvordModel;
    private MutableLiveData<List<cartModel.singlecartResult>> rejordModel;
    private com.multivendor.marketsellerapp.Repos.ordRepo mRepo=new ordRepo();

    public void initwork(String userid) {
        if(acpordModel!=null) {
            return;
        }
        if(pendordModel!=null) {
            return;
        }
        if(delvordModel!=null) {
            return;
        }
        if(rejordModel!=null) {
            return;
        }
        acpordModel=mRepo.getInstance().returnacpordModel(userid);
        pendordModel=mRepo.getInstance().returnpendordModel(userid);
        delvordModel=mRepo.getInstance().returndelvordModel(userid);
        rejordModel=mRepo.getInstance().returnrejordModel(userid);

    }

    public LiveData<List<cartModel.singlecartResult>> getAcpordModel() {
        return acpordModel;
    }

    public LiveData<List<cartModel.singlecartResult>> getPendordModel() {
        return pendordModel;
    }

    public LiveData<List<cartModel.singlecartResult>> getDelvordModel() {
        return delvordModel;
    }

    public LiveData<List<cartModel.singlecartResult>> getRejordModel() {
        return rejordModel;
    }


}
