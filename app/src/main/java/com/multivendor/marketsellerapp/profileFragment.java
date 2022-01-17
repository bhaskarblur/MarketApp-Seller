package com.multivendor.marketsellerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.multivendor.marketsellerapp.CustomDialogs.zoom_imageDialog;
import com.multivendor.marketsellerapp.Loginact;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.MaskTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class profileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentProfileBinding pfbinding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPreferences sharedPreferences;
    private final api_baseurl baseurl=new api_baseurl();
    public profileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
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
        pfbinding = FragmentProfileBinding.inflate(inflater, container, false);

        sharedPreferences = getActivity().getSharedPreferences("userlogged", 0);
        String userid = sharedPreferences.getString("userid", "");
        viewfunctions();
        loadData(userid);
        return pfbinding.getRoot();
    }

    private void loadData(String userId) {
        String imgurl = sharedPreferences.getString("userimage", "data");
        String storename = sharedPreferences.getString("storename", "");
        String address = sharedPreferences.getString("useraddress", "");
        String delvcode = sharedPreferences.getString("deliverycode", "");
        pfbinding.shopimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("image", imgurl);
                zoom_imageDialog zoom_imageDialog1 = new zoom_imageDialog();
                zoom_imageDialog1.setArguments(bundle);
                zoom_imageDialog1.show(getParentFragmentManager(), "zoom_imagDialog1");
            }

        });
        if (imgurl != null) {
            final int radius = 10;
            final int margin = 10;
            final Transformation transformation = new MaskTransformation(getActivity(), R.drawable.rounded_transf);
            if (imgurl != null && !imgurl.equals("data")) {
                Picasso.get().load(imgurl).transform(transformation).fit().into(pfbinding.shopimg);
            }
            else if(imgurl.equals("data")){
                Picasso.get().load(R.drawable.imgsampleround).into(pfbinding.shopimg);
            }
        }
        if (storename != null) {
            pfbinding.shopname.setText(storename);
        }
        if (address != null) {
            pfbinding.shopaddress.setText(address);
        }
        if(delvcode!=null) {
            pfbinding.shopdelvcode.setText("Delivery Code: " + delvcode);
        }
        else {
            pfbinding.shopdelvcode.setText("Delivery Code: ");
        }
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
                    Log.d("message", resp.getMessage());
                }


                SharedPreferences.Editor editor = sharedPreferences.edit();

                if(resp.getResult()!=null) {
                    if (resp.getResult().getImage() != null) {

                        editor.putString("userimage", resp.getResult().getImage());
                        final int radius = 30;
                        final int margin = 30;
                        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                             Picasso.get().load(resp.getResult().getImage()).fit().transform(transformation).into(pfbinding.shopimg);
                        editor.apply();
                    }
                    if (resp.getResult().getStore_name() != null) {
                        editor.putString("storename", resp.getResult().getStore_name());
                        //    pfbinding.shopname.setText(resp.getResult().getStore_name());
                        editor.apply();
                    }
                    if (resp.getResult().getAddress() != null) {
                        editor.putString("useraddress", resp.getResult().getAddress());
                        //     pfbinding.shopaddress.setText(resp.getResult().getAddress());
                        editor.apply();
                    }

                    if (resp.getResult().getDelivery_code() != null) {
                        editor.putString("deliverycode", resp.getResult().getDelivery_code());
                        //     pfbinding.shopaddress.setText(resp.getResult().getAddress());
                        editor.apply();
                    }
                }
            }

            @Override
            public void onFailure(Call<sellerApiResp.sellerinfo> call, Throwable t) {
               Log.d("Failure",t.getMessage());
            }
        });

    }

    private void viewfunctions() {

        pfbinding.helplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("http://wa.me/918765255956"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);


            }
        });


        pfbinding.termscondlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://marketapp.co.in/conditions.html"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        pfbinding.myorderlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ordFragment df=new ordFragment();
//                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
//                transaction.replace(R.id.mainfragment,df);
//                transaction.addToBackStack("A");
//                transaction.commit();
                startActivity(new Intent(getActivity(), profileSettings.class));
            }
        });
                                                        
        pfbinding.quickordlay.setOnClickListener(new View.OnClickListener() {
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
        pfbinding.ratepstxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("\"market://details?id=" + getActivity().getPackageName());
                Intent gotoapp = new Intent(Intent.ACTION_VIEW, uri);
                gotoapp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                gotoapp.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                try {
                    startActivity(gotoapp);
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            }
        });

        pfbinding.sendfeedtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("\"market://details?id=" + getActivity().getPackageName());
                Intent gotoapp = new Intent(Intent.ACTION_VIEW, uri);
                gotoapp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                gotoapp.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                try {
                    startActivity(gotoapp);
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            }
        });
        
        pfbinding.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharemsg = new Intent(Intent.ACTION_VIEW);
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Market App");
                    String shareMessage= "Install Market App Now.\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        pfbinding.textView22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), profileSettings.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                getActivity().finish();
            }
        });

        pfbinding.imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), profileSettings.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                getActivity().finish();
            }

        });

        pfbinding.logouttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("" +
                        "Are you sure, you want to log out?").setTitle("Log Out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(getActivity(), Loginact.class));
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
        pfbinding.shopsetlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), act_shopsetup.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                getActivity().finish();

            }
        });

        pfbinding.textView29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.multivendor.marketdeliveryapp");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        pfbinding.aboutlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.multivendor.marketdeliveryapp");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }


}