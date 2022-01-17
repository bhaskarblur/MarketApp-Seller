package com.multivendor.marketsellerapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.Adapters.ordlayAdapter;
import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.ViewModels.ordViewModel;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multivendor.marketsellerapp.databinding.FragmentOrdfragmentBinding;

import java.util.List;


public class ordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentOrdfragmentBinding odbinding;
    private com.multivendor.marketsellerapp.Adapters.ordlayAdapter ordlayAdapter;
    private com.multivendor.marketsellerapp.Adapters.ordlayAdapter ordlayAdapter2;
    private com.multivendor.marketsellerapp.Adapters.ordlayAdapter ordlayAdapter3;
    private com.multivendor.marketsellerapp.Adapters.ordlayAdapter ordlayAdapter4;
    private ordViewModel ordViewModel;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ordFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ordFragment newInstance(String param1, String param2) {
        ordFragment fragment = new ordFragment();
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
        ordViewModel=new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(com.multivendor.marketsellerapp.ViewModels.ordViewModel.class);
        ordViewModel.initwork(userid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        odbinding=FragmentOrdfragmentBinding.inflate(inflater,container,false);
        ordViewModel.getAcpordModel().observe(getActivity(), new Observer<List<cartModel.singlecartResult>>() {
            @Override
            public void onChanged(List<cartModel.singlecartResult> ordersModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ordlayAdapter.notifyDataSetChanged();
                    }
                },100);
            }
        });
        ordViewModel.getPendordModel().observe(getActivity(), new Observer<List<cartModel.singlecartResult>>() {
            @Override
            public void onChanged(List<cartModel.singlecartResult> ordersModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ordlayAdapter2.notifyDataSetChanged();
                    }
                },100);
            }
        });
        ordViewModel.getDelvordModel().observe(getActivity(), new Observer<List<cartModel.singlecartResult>>() {
            @Override
            public void onChanged(List<cartModel.singlecartResult> ordersModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ordlayAdapter3.notifyDataSetChanged();
                    }
                },100);
            }
        });
        ordViewModel.getRejordModel().observe(getActivity(), new Observer<List<cartModel.singlecartResult>>() {
            @Override
            public void onChanged(List<cartModel.singlecartResult> ordersModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ordlayAdapter4.notifyDataSetChanged();
                    }
                },100);
            }
        });
        viewfunctions();
        loadallrec();
        return odbinding.getRoot();
    }

    private void loadallrec() {
        LinearLayoutManager llm=new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        LinearLayoutManager llm1=new LinearLayoutManager(getContext());
        llm1.setOrientation(RecyclerView.VERTICAL);
        llm1.setReverseLayout(true);
        llm1.setStackFromEnd(true);
        LinearLayoutManager llm2=new LinearLayoutManager(getContext());
        llm2.setOrientation(RecyclerView.VERTICAL);
        llm2.setReverseLayout(true);
        llm2.setStackFromEnd(true);
        LinearLayoutManager llm3=new LinearLayoutManager(getContext());
        llm3.setOrientation(RecyclerView.VERTICAL);
        llm3.setReverseLayout(true);
        llm3.setStackFromEnd(true);

        ordlayAdapter=new ordlayAdapter(getActivity(),ordViewModel.getAcpordModel().getValue());
        ordlayAdapter2=new ordlayAdapter(getActivity(),ordViewModel.getPendordModel().getValue());
        ordlayAdapter3=new ordlayAdapter(getActivity(),ordViewModel.getDelvordModel().getValue());
        ordlayAdapter4=new ordlayAdapter(getActivity(),ordViewModel.getRejordModel().getValue());

        odbinding.acprec.setLayoutManager(llm);
        odbinding.acprec.setAdapter(ordlayAdapter);

        odbinding.pendrec.setLayoutManager(llm1);
        odbinding.pendrec.setAdapter(ordlayAdapter2);

        odbinding.delvrec.setLayoutManager(llm2);
        odbinding.delvrec.setAdapter(ordlayAdapter3);

        odbinding.rejrec.setLayoutManager(llm3);
        odbinding.rejrec.setAdapter(ordlayAdapter4);
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

        odbinding.quickordertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickorder df=new quickorder();
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment,df);
                transaction.addToBackStack("A");
                transaction.commit();
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
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getViewModelStore().clear();
    }
}