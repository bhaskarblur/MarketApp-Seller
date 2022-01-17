package com.multivendor.marketsellerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Adapters.earnAdapter;
import com.multivendor.marketsellerapp.Adapters.homequickordAdapter;
import com.multivendor.marketsellerapp.Adapters.newordAdapter;
import com.multivendor.marketsellerapp.Adapters.reviewAdapter;
import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.Models.earnModel;
import com.multivendor.marketsellerapp.Models.newordModel;
import com.multivendor.marketsellerapp.Models.quickorderModel;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class homeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentHomeBinding hmbinding;
    private com.multivendor.marketsellerapp.Adapters.earnAdapter earnAdapter;
    private com.multivendor.marketsellerapp.Adapters.newordAdapter newordAdapter;
    private com.multivendor.marketsellerapp.Adapters.homequickordAdapter quickordadapter;
    private com.multivendor.marketsellerapp.ViewModels.homeViewModel homeViewModel;
    private SharedPreferences sharedPreferences;
    private List<sellerApiResp.reviewresult> reviewlist = new ArrayList<>();
    private com.multivendor.marketsellerapp.Adapters.reviewAdapter reviewAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String userid;
    private final api_baseurl baseurl=new api_baseurl();


    public homeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        userid = sharedPreferences.getString("userid", "");
        homeViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(com.multivendor.marketsellerapp.ViewModels.homeViewModel.class);
        homeViewModel.initwork(userid);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        hmbinding = FragmentHomeBinding.inflate(inflater, container, false);

        homeViewModel.getQuickordModel().observe(getActivity(), new Observer<List<quickorderModel.quickordResult>>() {
            @Override
            public void onChanged(List<quickorderModel.quickordResult> quickordResults) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        quickordadapter.notifyDataSetChanged();
                        hmbinding.quicordtxt.setText("Quick Orders ("+quickordResults.size()+")");
                    }
                }, 100);
            }
        });

        homeViewModel.getNewordModel().observe(getActivity(), new Observer<List<cartModel.singlecartResult>>() {
            @Override
            public void onChanged(List<cartModel.singlecartResult> cartResults) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newordAdapter.notifyDataSetChanged();
                        hmbinding.newordtxt.setText("New Orders ("+cartResults.size()+")");

                    }
                }, 100);
            }
        });


        sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        String userid = sharedPreferences.getString("userid", "");

        loadShopstats();
        handlequicktips();
        viewfunc();
        loadrecs();
        loadData(userid);
        return hmbinding.getRoot();

    }

    private void handlequicktips() {
        String tip1closed = sharedPreferences.getString("tip1closed", "");
        String tip2closed = sharedPreferences.getString("tip2closed", "");
        String tip3closed = sharedPreferences.getString("tip3closed", "");
        if (tip1closed.equals("yes")) {
            hmbinding.restip.setVisibility(View.GONE);
        }
        if (tip2closed.equals("yes")) {
            hmbinding.prodtip.setVisibility(View.GONE);
        }
        if (tip3closed.equals("yes")) {
            hmbinding.onofftip.setVisibility(View.GONE);
        }
        if (tip1closed.equals("yes") && tip2closed.equals("yes") && tip3closed.equals("yes")) {
            hmbinding.qttxt.setVisibility(View.GONE);
            hmbinding.tipslay.setVisibility(View.GONE);
        }


        SharedPreferences.Editor editor = sharedPreferences.edit();


        hmbinding.closeonofftip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hmbinding.restip.setVisibility(View.GONE);
                editor.putString("tip3closed", "yes");
                editor.apply();
                if (hmbinding.prodtip.getVisibility() == View.GONE && hmbinding.restip.getVisibility() == View.GONE) {
                    hmbinding.qttxt.setVisibility(View.GONE);
                    hmbinding.tipslay.setVisibility(View.GONE);
                }
            }
        });
        hmbinding.closerestip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hmbinding.restip.setVisibility(View.GONE);
//                editor.putString("tip1closed", "yes");
//                editor.apply();
//                if (hmbinding.prodtip.getVisibility() == View.GONE && hmbinding.onofftip.getVisibility() == View.GONE) {
//                    hmbinding.qttxt.setVisibility(View.GONE);
//                    hmbinding.tipslay.setVisibility(View.GONE);
//                }
                Intent sharemsg = new Intent(Intent.ACTION_VIEW);
                try {
//                     Uri uri =  Uri.parse("android.resource://com.multivendor.marketapp/drawable/helpicon");
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");

                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Market App");

                    String shareMessage= "I have create an online shop on MarketApp. To order from my shop download the MarketApp now!" +
                            " \n\nMarketApp lets you shop from your nearby shop online. #SupportLocal \n\n Ab Market hai apke hath main \n\n";

                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.multivendor.marketapp";

                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//                    shareIntent.putExtra(Intent.EXTRA_STREAM,uri );
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


//        });

        hmbinding.closeprodtip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hmbinding.prodtip.setVisibility(View.GONE);
                editor.putString("tip2closed", "yes");
                editor.apply();

                if (hmbinding.restip.getVisibility() == View.GONE && hmbinding.onofftip.getVisibility() == View.GONE) {
                    hmbinding.qttxt.setVisibility(View.GONE);
                    hmbinding.tipslay.setVisibility(View.GONE);
                }
            }
        });
    }


    private void loadData(String userId) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        com.multivendor.marketsellerapp.APIWork.LogregApiInterface logregApiInterface = retrofit.create(
                com.multivendor.marketsellerapp.APIWork.LogregApiInterface.class);

        Call<sellerApiResp.sellerinfo> call = logregApiInterface.get_seller_info(userId);

        call.enqueue(new Callback<sellerApiResp.sellerinfo>() {
            @Override
            public void onResponse(Call<sellerApiResp.sellerinfo> call, Response<sellerApiResp.sellerinfo> response) {
                if (!response.isSuccessful()) {
                    Log.d("Error code", String.valueOf(response.code()));
                    return;
                }
                sellerApiResp.sellerinfo resp = response.body();
                if (resp.getMessage() != null) {
                    Log.d("messagedata", resp.getMessage());
                }

                if (resp.getMessage().equals("My store")) {
                    if (resp.getResult().getName() != null) {
                        hmbinding.textView13.setText("Hello " + resp.getResult().getName() + "!");
                        // Calendar calendar=Calendar.getInstance();
                        // hmbinding.textView12.setText("Date: " +calendar.getTime().getDate()+"/"
                        //  +calendar.getTime().getMonth()+"/"+calendar.getTime().getYear());
                    }

                }
            }

            @Override
            public void onFailure(Call<sellerApiResp.sellerinfo> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadShopstats() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<sellerApiResp.reviewresp> call = logregApiInterface.getreviews(userid);

        call.enqueue(new Callback<sellerApiResp.reviewresp>() {
            @Override
            public void onResponse(Call<sellerApiResp.reviewresp> call, Response<sellerApiResp.reviewresp> response) {
                if (!response.isSuccessful()) {
                    Log.d("errorcode", String.valueOf(response.code()));
                    return;
                }

                sellerApiResp.reviewresp reviewresp = response.body();

                if (reviewresp.getResult() != null) {
                    for (sellerApiResp.reviewresult res : reviewresp.getResult()) {
                        reviewlist.add(res);
                    }
                    reviewAdapter = new reviewAdapter(getActivity(), reviewlist);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    hmbinding.reviewsLay.setLayoutManager(linearLayoutManager);
                    hmbinding.reviewsLay.setAdapter(reviewAdapter);
                }
            }

            @Override
            public void onFailure(Call<sellerApiResp.reviewresp> call, Throwable t) {
                Log.d("Failure68", t.getMessage());
            }
        });

        Call<sellerApiResp.statsresp> call2 = logregApiInterface.getstatsinfo(userid);
        call2.enqueue(new Callback<sellerApiResp.statsresp>() {
            @Override
            public void onResponse(Call<sellerApiResp.statsresp> call, Response<sellerApiResp.statsresp> response) {
                if (!response.isSuccessful()) {
                    Log.d("errorcode", String.valueOf(response.code()));
                    return;
                }

                sellerApiResp.statsresp reviewresp = response.body();

                Log.d("messagedata_here",reviewresp.getMessage());
                if (reviewresp.getResult() != null) {
                    if (reviewresp.getResult().getTotal_received_orders() != null) {
                        hmbinding.pendordcount.setText(reviewresp.getResult().getTotal_received_orders());
                        hmbinding.compretcount.setText(reviewresp.getResult().getTotal_rejected_orders());
                        hmbinding.compordcount.setText(reviewresp.getResult().getTotal_delivered_orders());
                        hmbinding.samplecount.setText(reviewresp.getResult().getTotal_delivered_products());
                        hmbinding.earncount.setText("Rs "+reviewresp.getResult().getTotal_earning());
                    }
                }
            }

            @Override
            public void onFailure(Call<sellerApiResp.statsresp> call, Throwable t) {
                Log.d("Failure_info",t.getMessage());
            }
        });

        Call<sellerApiResp.statusUpdateResp> call3= logregApiInterface.getStore_Status(userid);

        call3.enqueue(new Callback<sellerApiResp.statusUpdateResp>() {
            @Override
            public void onResponse(Call<sellerApiResp.statusUpdateResp> call, Response<sellerApiResp.statusUpdateResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorcode",String.valueOf(response.code()));
                    return;
                }

                sellerApiResp.statusUpdateResp resp=response.body();

                if(resp.getResult()!=null) {
                    if(resp.getResult().getStatus()!=null) {
                        if (resp.getResult().getStatus().equals("Online")) {
                            hmbinding.statbtn.getBackground().setTint(Color.parseColor("#40D914"));
                            hmbinding.stattxt.setText("Online");
                        } else if (resp.getResult().getStatus().equals("Offline")) {
                            hmbinding.statbtn.getBackground().setTint(Color.parseColor("#DC331D"));
                            hmbinding.stattxt.setText("Offline");
                        }
                    }
                    else {
                        hmbinding.statbtn.getBackground().setTint(Color.parseColor("#40D914"));
                        hmbinding.stattxt.setText("Online");
                    }
                }
            }

            @Override
            public void onFailure(Call<sellerApiResp.statusUpdateResp> call, Throwable throwable) {
                Log.d("Failure67",String.valueOf(throwable.getMessage()));
            }
        });
    }

    private void loadrecs() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
        llm1.setOrientation(RecyclerView.VERTICAL);
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        llm2.setOrientation(RecyclerView.VERTICAL);
      //  llm.setReverseLayout(true);
       // llm.setStackFromEnd(true);
        //llm1.setReverseLayout(true);
      // llm1.setStackFromEnd(true);
      //  llm2.setReverseLayout(true);
      //  llm2.setStackFromEnd(true);
        newordAdapter = new newordAdapter(getContext(), homeViewModel.getNewordModel().getValue());
        quickordadapter = new homequickordAdapter(getContext(), homeViewModel.getQuickordModel().getValue());


        hmbinding.newordrec.setLayoutManager(llm);
        hmbinding.newordrec.setAdapter(newordAdapter);

        // change adapter view;


        hmbinding.newordtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hmbinding.newordrec.setAdapter(newordAdapter);
                hmbinding.newordtxt.setTextColor(Color.parseColor("#000000"));
                hmbinding.quicordtxt.setTextColor(Color.parseColor("#757575"));
            }
        });

        hmbinding.quicordtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hmbinding.newordrec.setAdapter(quickordadapter);
                hmbinding.quicordtxt.setTextColor(Color.parseColor("#000000"));
                hmbinding.newordtxt.setTextColor(Color.parseColor("#757575"));
            }
        });

        hmbinding.pendordlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordFragment of = new ordFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment,of);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

        hmbinding.ordretlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordFragment of = new ordFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment,of);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

        hmbinding.ordcomplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordFragment of = new ordFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment,of);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

        hmbinding.sampleboxlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordFragment of = new ordFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment,of);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

    }


    public void updateStatus(String status) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

        Call<sellerApiResp.statusUpdateResp> call = logregApiInterface.updatetStore_Status(userid,status);

        call.enqueue(new Callback<sellerApiResp.statusUpdateResp>() {
            @Override
            public void onResponse(Call<sellerApiResp.statusUpdateResp> call, Response<sellerApiResp.statusUpdateResp> response) {
                if(!response.isSuccessful()) {
                    Log.d("errorcode",String.valueOf(response.code()));
                    return;
                }

                sellerApiResp.statusUpdateResp resp=response.body();

                if(resp.getResult()!=null) {
                    Toast.makeText(getContext(), "Status Updated!", Toast.LENGTH_SHORT).show();
                    if(resp.getResult().getStatus()!=null) {
                        if (resp.getResult().getStatus().equals("Online")) {
                            hmbinding.statbtn.getBackground().setTint(Color.parseColor("#40D914"));
                            hmbinding.stattxt.setText("Online");
                        } else if (resp.getResult().getStatus().equals("Offline")) {
                            hmbinding.statbtn.getBackground().setTint(Color.parseColor("#DC331D"));
                            hmbinding.stattxt.setText("Offline");
                        }
                    }
                    else {
                        hmbinding.statbtn.getBackground().setTint(Color.parseColor("#40D914"));
                        hmbinding.stattxt.setText("Online");
                    }
                }
            }

            @Override
            public void onFailure(Call<sellerApiResp.statusUpdateResp> call, Throwable throwable) {
                Log.d("Failure69",String.valueOf(throwable.getMessage()));
            }
        });
    }
    private void viewfunc() {
        ArrayList<String> orditems = new ArrayList<>();
        orditems.add("Monthly");
        orditems.add("Weekly");
        orditems.add("Yearly");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, orditems);
        hmbinding.myordsp.setAdapter(arrayAdapter);
        hmbinding.earnsp.setAdapter(arrayAdapter);

        hmbinding.notiicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notiFragment df = new notiFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment, df);
                transaction.addToBackStack("A");
                transaction.commit();
            }
        });

        hmbinding.shopstat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hmbinding.stattxt.getText().toString().equals("Online")) {
                    updateStatus("Offline");
                    hmbinding.statbtn.getBackground().setTint(Color.parseColor("#DC331D"));
                    hmbinding.stattxt.setText("Offline");
                } else if (hmbinding.stattxt.getText().toString().equals("Offline")) {
                    updateStatus("Online");
                    hmbinding.statbtn.getBackground().setTint(Color.parseColor("#40D914"));
                    hmbinding.stattxt.setText("Online");
                }

            }
        });

        hmbinding.billpendcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), act_mybills.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        hmbinding.billinvcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), act_mybills.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        hmbinding.billpaidcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), act_mybills.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getViewModelStore().clear();
    }
}