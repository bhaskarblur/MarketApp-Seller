package com.multivendor.marketsellerapp.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.multivendor.marketsellerapp.Models.quickorderModel;
import com.multivendor.marketsellerapp.Repos.quickordRepo;

import java.util.List;

public class quickordViewModel extends ViewModel {

    private MutableLiveData<List<quickorderModel .quick_products>> ordprodModel;
    private MutableLiveData<List<String>> prodimgModel=new MutableLiveData<>();
    private MutableLiveData<quickorderModel.quickordResult> wholedata=new MutableLiveData<>();

    public MutableLiveData<quickorderModel.quickordResult> getWholedata() {
        return wholedata;
    }

    public MutableLiveData<List<String>> getProdimgModel() {
        return prodimgModel;
    }

    public void setProdimgModel(MutableLiveData<List<String>> prodimgModel) {
        this.prodimgModel = prodimgModel;
    }

    private com.multivendor.marketsellerapp.Repos.quickordRepo quickordRepo=new quickordRepo();

    public MutableLiveData<List<quickorderModel .quick_products>> getOrdprodModel() {
        return ordprodModel;
    }

    public void initwork(String storeid,String quickordid){
        if(ordprodModel!=null) {
            return;
        }
        wholedata=quickordRepo.getInstance().returnwholedata();
        ordprodModel=quickordRepo.getInstance().returnprodModel(storeid,quickordid);
        prodimgModel=quickordRepo.getInstance().returnprodimg();

    }
}
