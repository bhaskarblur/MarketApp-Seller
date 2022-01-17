package com.multivendor.marketsellerapp.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.multivendor.marketsellerapp.Models.billModel;
import com.multivendor.marketsellerapp.Repos.billRepo;

import java.util.List;

public class billViewModel extends ViewModel {

    private MutableLiveData<List<billModel.billresult>> pendbillModel;
    private MutableLiveData<List<billModel.billresult>> compbillModel;
    private com.multivendor.marketsellerapp.Repos.billRepo mRepo=new billRepo();

    public void initwork(String storeid) {
        if(pendbillModel!=null) {
            return;
        }
        if(compbillModel!=null) {
            return;
        }
        pendbillModel=mRepo.getInstance().returnpendModel(storeid);
        compbillModel=mRepo.getInstance().returncompModel(storeid);
    }
    public MutableLiveData<List<billModel.billresult>> getPendbillModel() {
        return pendbillModel;
    }

    public MutableLiveData<List<billModel.billresult>> getCompbillModel() {
        return compbillModel;
    }


}
