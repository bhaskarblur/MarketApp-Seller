package com.multivendor.marketsellerapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Adapters.catproductsAdapter;
import com.multivendor.marketsellerapp.Adapters.productitemAdapter;
import com.multivendor.marketsellerapp.CustomDialogs.productaddDialog;
import com.multivendor.marketsellerapp.Models.productitemModel;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.databinding.FragmentCatProductsBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class catProductsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentCatProductsBinding cpbinding;
    private com.multivendor.marketsellerapp.Adapters.catproductsAdapter productitemAdapter;
    private com.multivendor.marketsellerapp.ViewModels.catbdViewModel catbdViewModel;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences shpref;

    String userId;
    String catname;
    String catid;

    public catProductsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static catProductsFragment newInstance(String param1, String param2) {
        catProductsFragment fragment = new catProductsFragment();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cpbinding = FragmentCatProductsBinding.inflate(inflater, container, false);
        shpref = getActivity().getSharedPreferences("userlogged", 0);
        userId = shpref.getString("userid", "");
        Bundle bundle = getArguments();
        catid = bundle.getString("selectedCategoryid");
        catname = bundle.getString("selectedCategoryname");
        cpbinding.textView42.setText(catname);
        catbdViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(com.multivendor.marketsellerapp.ViewModels.catbdViewModel.class);
        catbdViewModel.initwork(userId, catid);
        catbdViewModel.getProductModel().observe(getActivity(), new Observer<List<productitemModel>>() {
            @Override
            public void onChanged(List<productitemModel> productitemModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        productitemAdapter.notifyDataSetChanged();
                    }
                }, 100);
            }
        });
        viewfunctions();
        loadprodrec();
        return cpbinding.getRoot();

    }

    private void loadprodrec() {
        productitemAdapter = new catproductsAdapter(getContext(), catbdViewModel.getProductModel().getValue(), catname);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        cpbinding.prodrec.setLayoutManager(llm);
        cpbinding.prodrec.setAdapter(productitemAdapter);
    }

    private void viewfunctions() {

        cpbinding.catdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle("Delete?").
                        setMessage("Are you sure you want to delete this category and its products").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                                        .addConverterFactory(GsonConverterFactory.create()).build();
                                LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                                Call<sellerApiResp.commonresult> call = logregApiInterface.delete_category(catid);
                                call.enqueue(new Callback<sellerApiResp.commonresult>() {
                                    @Override
                                    public void onResponse(Call<sellerApiResp.commonresult> call, Response<sellerApiResp.commonresult> response) {
                                        if (!response.isSuccessful()) {

                                            Log.d("Code", response.message().toString());
                                            return;
                                        }

                                        sellerApiResp.commonresult data = response.body();
                                        Log.d("data", data.getMessage());

                                        Toast.makeText(getContext(), "Category Deleted!", Toast.LENGTH_SHORT).show();


                                    }

                                    @Override
                                    public void onFailure(Call<sellerApiResp.commonresult> call, Throwable t) {

                                    }
                                });
                                com.multivendor.marketsellerapp.catbdFragment catProductsFragment = new catbdFragment();
                                FragmentTransaction transaction = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                                transaction.replace(R.id.mainfragment, catProductsFragment);
                                transaction.commit();

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        cpbinding.backbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catbdFragment df = new catbdFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.mainfragment, df);
                transaction.commit();
            }
        });

        cpbinding.pdadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("userid", userId);
                bundle.putString("catid", catid);
                com.multivendor.marketsellerapp.CustomDialogs.productaddDialog productaddDialog = new productaddDialog();
                productaddDialog.setArguments(bundle);
                productaddDialog.setCancelable(false);
                productaddDialog.show(fm, "productaddDialog");
            }
        });

    }
}