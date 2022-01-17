package com.multivendor.marketsellerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.multivendor.marketsellerapp.APIWork.ApiWork;
import com.multivendor.marketsellerapp.Adapters.addszAdapter;
import com.multivendor.marketsellerapp.Adapters.reviewAdapter;
import com.multivendor.marketsellerapp.Models.newProductModel;
import com.multivendor.marketsellerapp.databinding.FragmentNewProductBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newProduct#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newProduct extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentNewProductBinding binding;

    private addszAdapter adapter1;
    private addszAdapter adapter2;
    private addszAdapter adapter3;
    private String userid;
    private String product_id;
    private String product_type="normal_product";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    api_baseurl baseurl = new api_baseurl();
    public newProduct() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newProduct.
     */
    // TODO: Rename and change types and number of parameters
    public static newProduct newInstance(String param1, String param2) {
        newProduct fragment = new newProduct();
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
        Bundle bundle=getArguments();
        product_id=bundle.getString("product_id");
        userid=getActivity().getSharedPreferences("userlogged",0).getString("userid","");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentNewProductBinding.inflate(inflater,container,false);
        //getlatlong();
        LoadData();
        viewfuncs();
        return binding.getRoot();
    }

    private void viewfuncs() {
        binding.normalRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    binding.bestdealRadio.setChecked(false);
                    binding.dealdayRadio.setChecked(false);
                    binding.topsellRadio.setChecked(false);
                    product_type="normal_product";
                }
            }
        });

        binding.bestdealRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    binding.normalRadio.setChecked(false);
                    binding.dealdayRadio.setChecked(false);
                    binding.topsellRadio.setChecked(false);
                    product_type="best_deal_product";
                }
            }
        });

        binding.dealdayRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    binding.normalRadio.setChecked(false);
                    binding.bestdealRadio.setChecked(false);
                    binding.topsellRadio.setChecked(false);
                    product_type="deal_day_product";
                }
            }
        });

        binding.topsellRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    binding.normalRadio.setChecked(false);
                    binding.dealdayRadio.setChecked(false);
                    binding.bestdealRadio.setChecked(false);
                    product_type="top_selling_product";
                }
            }
        });
        binding.savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder price = new StringBuilder();
                StringBuilder size = new StringBuilder();
                StringBuilder mrp = new StringBuilder();

                for (int i = 0; i < adapter1.list.size(); i++) {
                    if (adapter1.list.size() > 1) {
                        size.append(adapter1.list.get(i) + ",");
                        price.append(adapter2.list.get(i) + ",");
                        mrp.append(adapter3.list.get(i) + ",");
                    } else {
                        size.append(adapter1.list.get(i));
                        price.append(adapter2.list.get(i));
                        mrp.append(adapter3.list.get(i));
                    }

                    Log.d("size",size.toString());
                }
                Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                        .addConverterFactory(GsonConverterFactory.create()).build();

                ApiWork apiWork = retrofit.create(ApiWork.class);

                Call<newProductModel.productdetailResp> call = apiWork.update_product(userid, product_id,
                        size.toString(),mrp.toString(),price.toString(),product_type);

                call.enqueue(new Callback<newProductModel.productdetailResp>() {
                    @Override
                    public void onResponse(Call<newProductModel.productdetailResp> call, Response<newProductModel.productdetailResp> response) {
                        if(!response.isSuccessful()) {
                            Log.d("error code",String.valueOf(response.code()));
                            return;
                        }
                        if(response.body().getResult()!=null){
                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onFailure(Call<newProductModel.productdetailResp> call, Throwable t) {
                        Log.d("Failure",t.getMessage());
                    }
                });
            }
        });
        binding.backbtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        binding.recsAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter1.list.add("");
                adapter2.list.add("");
                adapter3.list.add("");
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                adapter3.notifyDataSetChanged();

            }
        });

        binding.recsDecr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter1.list.remove(adapter1.list.size() - 1);
                adapter2.list.remove(adapter1.list.size() - 1);
                adapter3.list.remove(adapter1.list.size() - 1);
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                adapter3.notifyDataSetChanged();
            }
        });
    }


    private void LoadData() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl. baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiWork apiWork = retrofit.create(ApiWork.class);

        Call<newProductModel.productdetailResp> call = apiWork.getproduct_details(userid, product_id);

        call.enqueue(new Callback<newProductModel.productdetailResp>() {
            @Override
            public void onResponse(Call<newProductModel.productdetailResp> call, Response<newProductModel.productdetailResp> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }

                newProductModel.productdetailResp productdata = response.body();


                Log.d("message", productdata.getSuccess());

                if (productdata.getResult() != null) {
                    if (productdata.getResult().getProduct_images().size() > 0) {
                        Picasso.get().load(productdata.getResult().getProduct_images().get(0).getImage())
                                .resize(300,300).into(binding.productImg);
                    }

                    if (productdata.getResult().getProduct_variants().size() > 0) {
                        List<String> sizelist=new ArrayList<>();
                        List<String> mrplist=new ArrayList<>();
                        List<String> pricelist=new ArrayList<>();
                        for (int i=0;i<productdata.getResult().getProduct_variants().size();i++){
                            sizelist.add(productdata.getResult().getProduct_variants().get(i).getSize());
                            mrplist.add(productdata.getResult().getProduct_variants().get(i).getPrice());
                            pricelist.add(productdata.getResult().getProduct_variants().get(i).getSelling_price());
                        }
                        adapter1 = new addszAdapter(getContext(), sizelist,"text");
                        adapter2 = new addszAdapter(getContext(), pricelist,"number");
                        adapter3 = new addszAdapter(getContext(), mrplist,"number");

                        LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
                        llm1.setOrientation(RecyclerView.HORIZONTAL);
                        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
                        LinearLayoutManager llm3 = new LinearLayoutManager(getContext());
                        LinearLayoutManager llm4 = new LinearLayoutManager(getContext());
                        llm2.setOrientation(RecyclerView.HORIZONTAL);
                        llm3.setOrientation(RecyclerView.HORIZONTAL);
                        llm4.setOrientation(RecyclerView.HORIZONTAL);

                        binding.sizeRec.setLayoutManager(llm1);
                        binding.sizeRec.setAdapter(adapter1);
                        binding.priceRec.setLayoutManager(llm2);
                        binding.priceRec.setAdapter(adapter2);
                        binding.mrpRec.setLayoutManager(llm3);
                        binding.mrpRec.setAdapter(adapter3);
                    }
                    binding.productname.setText(productdata.getResult().getProduct_name().toString());
                    binding.productdesc.setText(productdata.getResult().getProduct_description().toString());

                }


            }

            @Override
            public void onFailure(Call<newProductModel.productdetailResp> call, Throwable t) {
                Log.d("errorDetail", t.getMessage().toString());
            }
        });

    }
}