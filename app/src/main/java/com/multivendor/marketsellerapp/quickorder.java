package com.multivendor.marketsellerapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.Adapters.homequickordAdapter;
import com.multivendor.marketsellerapp.Adapters.ordlayAdapter;
import com.multivendor.marketsellerapp.Adapters.quickfragAdapter;
import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.Models.quickorderModel;
import com.multivendor.marketsellerapp.ViewModels.homeViewModel;
import com.multivendor.marketsellerapp.ViewModels.ordViewModel;
import com.multivendor.marketsellerapp.databinding.FragmentOrdfragmentBinding;
import com.multivendor.marketsellerapp.databinding.FragmentQuickorderBinding;

import java.util.List;


public class quickorder extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentQuickorderBinding odbinding;
    private com.multivendor.marketsellerapp.Adapters.quickfragAdapter ordlayAdapter;
    private homeViewModel hmviewmodel;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public quickorder() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static quickorder newInstance(String param1, String param2) {
        quickorder fragment = new quickorder();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userlogged",0);
        String userid=sharedPreferences.getString("userid","");
        hmviewmodel=new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(com.multivendor.marketsellerapp.ViewModels.homeViewModel.class);
        hmviewmodel.initwork(userid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        odbinding=FragmentQuickorderBinding.inflate(inflater,container,false);
        hmviewmodel.getQuickordModel().observe(getActivity(), new Observer<List<quickorderModel.quickordResult>>() {
            @Override
            public void onChanged(List<quickorderModel.quickordResult> quickordResults) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(quickordResults.size()>0) {
                            ordlayAdapter.notifyDataSetChanged();
                        }
                    }
                },100);
            }
        });
        viewfunctions();
        loadquickorders();
        return odbinding.getRoot();
    }

    private void loadquickorders() {
        ordlayAdapter=new quickfragAdapter(getContext(),hmviewmodel.getQuickordModel().getValue());
        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        odbinding.acprec.setLayoutManager(llm);
        odbinding.acprec.setAdapter(ordlayAdapter);

    }


    private void viewfunctions() {

        odbinding.acptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odbinding.acptxt.setTextColor(Color.parseColor("#0881E3"));
                odbinding.acplay.setVisibility(View.VISIBLE);

                odbinding.pendtxt.setTextColor(Color.parseColor("#595959"));
                odbinding.pendlay.setVisibility(View.INVISIBLE);

                odbinding.delvtxt.setTextColor(Color.parseColor("#595959"));
                odbinding.delvlay.setVisibility(View.INVISIBLE);

                odbinding.rejtxt.setTextColor(Color.parseColor("#595959"));
                odbinding.rejlay.setVisibility(View.INVISIBLE);


            }
        });

        odbinding.pendtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odbinding.pendtxt.setTextColor(Color.parseColor("#0881E3"));
                odbinding.pendlay.setVisibility(View.VISIBLE);

                odbinding.acptxt.setTextColor(Color.parseColor("#595959"));
                odbinding.acplay.setVisibility(View.INVISIBLE);

                odbinding.delvtxt.setTextColor(Color.parseColor("#595959"));
                odbinding.delvlay.setVisibility(View.INVISIBLE);

                odbinding.rejtxt.setTextColor(Color.parseColor("#595959"));
                odbinding.rejlay.setVisibility(View.INVISIBLE);


            }
        });

        odbinding.delvtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odbinding.delvtxt.setTextColor(Color.parseColor("#0881E3"));
                odbinding.delvlay.setVisibility(View.VISIBLE);

                odbinding.pendtxt.setTextColor(Color.parseColor("#595959"));
                odbinding.pendlay.setVisibility(View.INVISIBLE);

                odbinding.acptxt.setTextColor(Color.parseColor("#595959"));
                odbinding.acplay.setVisibility(View.INVISIBLE);

                odbinding.rejtxt.setTextColor(Color.parseColor("#595959"));
                odbinding.rejlay.setVisibility(View.INVISIBLE);


            }
        });

        odbinding.rejtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odbinding.rejtxt.setTextColor(Color.parseColor("#0881E3"));
                odbinding.rejlay.setVisibility(View.VISIBLE);

                odbinding.pendtxt.setTextColor(Color.parseColor("#595959"));
                odbinding.pendlay.setVisibility(View.INVISIBLE);

                odbinding.delvtxt.setTextColor(Color.parseColor("#595959"));
                odbinding.delvlay.setVisibility(View.INVISIBLE);

                odbinding.acptxt.setTextColor(Color.parseColor("#595959"));
                odbinding.acplay.setVisibility(View.INVISIBLE);


            }
        });

        odbinding.orderstatustext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordFragment df=new ordFragment();
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment,df);
                transaction.commit();


            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getViewModelStore().clear();
    }
}