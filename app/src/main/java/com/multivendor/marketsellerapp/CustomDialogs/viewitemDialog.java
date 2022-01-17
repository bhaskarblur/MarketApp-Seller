package com.multivendor.marketsellerapp.CustomDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.Adapters.ordproductAdapter;
import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.Models.orderprodModel;
import com.multivendor.marketsellerapp.Models.productitemModel;
import com.multivendor.marketsellerapp.R;

import java.util.ArrayList;
import java.util.List;

public class viewitemDialog extends AppCompatDialogFragment {
    RecyclerView itemsrec;
    Button ok;
    String id;
    private com.multivendor.marketsellerapp.Adapters.ordproductAdapter ordproductAdapter;
    private com.multivendor.marketsellerapp.ViewModels.vieworderViewModel vieworderViewModel;
    private List<productitemModel> prodlist = new ArrayList<>();
    private String orderid;
    private String storeid;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.viewitemdialog, null);
        builder.setView(view);

        itemsrec = view.findViewById(R.id.itemsrecs);
        ok = view.findViewById(R.id.okbtn);
        Bundle bundle = getArguments();
        orderid = bundle.getString("orderid");
        storeid = bundle.getString("storeid");
        vieworderViewModel = new ViewModelProvider(this).get(com.multivendor.marketsellerapp.ViewModels.vieworderViewModel.class);
        vieworderViewModel.initwork(storeid, orderid);

        vieworderViewModel.getGetordinfo().observe(this, new Observer<cartModel.singlecartResult>() {
            @Override
            public void onChanged(cartModel.singlecartResult cartResult) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 100);
            }
        });
        vieworderViewModel.getAllproductModel().observe(this, new Observer<List<productitemModel>>() {
            @Override
            public void onChanged(List<productitemModel> productitemModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (productitemModels.size() > 0) {

                            for (cartModel.productResult selprods : vieworderViewModel.getGetordinfo().getValue().getProducts()) {
                                for (productitemModel prodmodel : productitemModels) {
                                    // if(prodmodel.getProduct_id().equals(selprods.getProduct_id())) {
                                    prodlist.add(prodmodel);
                                    // }
                                }
                            }
                            loaditems();
                        }
                    }

                }, 100);
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();

    }

    private void loaditems() {

        ordproductAdapter = new ordproductAdapter(getActivity(), prodlist, vieworderViewModel.getGetordinfo().getValue().getProducts());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        itemsrec.setLayoutManager(llm);
        itemsrec.setAdapter(ordproductAdapter);
    }
}

