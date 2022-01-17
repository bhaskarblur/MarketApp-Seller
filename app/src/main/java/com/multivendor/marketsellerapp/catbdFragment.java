package com.multivendor.marketsellerapp;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Adapters.categoriesAdapter;
import com.multivendor.marketsellerapp.Adapters.productitemAdapter;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.multivendor.marketsellerapp.Models.categoriesModel;
import com.multivendor.marketsellerapp.Models.loginresResponse;
import com.multivendor.marketsellerapp.Models.productitemModel;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.databinding.FragmentCatbdBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class catbdFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final int IMAGE_PICK_CODE = 1000;
    final int PERMISSION_CODE = 1001;
    private FragmentCatbdBinding ctbinding;
    private categoriesAdapter categoriesAdapter;
    private productitemAdapter productitemAdapter;
    private com.multivendor.marketsellerapp.ViewModels.catbdViewModel catbdViewModel;
    private SharedPreferences shpref;
    private Uri catimgsel;
    private final api_baseurl baseurl=new api_baseurl();
    String userId;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String base64img = new String();

    public catbdFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static catbdFragment newInstance(String param1, String param2) {
        catbdFragment fragment = new catbdFragment();
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
        ctbinding = FragmentCatbdBinding.inflate(inflater, container, false);
        shpref = getActivity().getSharedPreferences("userlogged", 0);
        String imgURI = shpref.getString("userimage", "");
        userId = shpref.getString("userid", "");

        String catadded = shpref.getString("catadded", "");
        if (catadded.equals("yes")) {
            ctbinding.catshowlay.setVisibility(View.VISIBLE);
            ctbinding.catadderlay.setVisibility(View.INVISIBLE);
        } else {
            ctbinding.catshowlay.setVisibility(View.INVISIBLE);
            ctbinding.catadderlay.setVisibility(View.VISIBLE);
        }
        catbdViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(com.multivendor.marketsellerapp.ViewModels.catbdViewModel.class);
        catbdViewModel.initwork(userId, null);
        catbdViewModel.getCatModel().observe(getActivity(), new Observer<List<categoriesModel>>() {
            @Override
            public void onChanged(List<categoriesModel> categoriesModels) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        categoriesAdapter.notifyDataSetChanged();
                    }
                }, 100);
            }
        });

        catbdViewModel.getAllproductModel().observe(getActivity(), new Observer<List<productitemModel>>() {
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
        loadRecs();
        return ctbinding.getRoot();
    }

    private void loadRecs() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
        llm1.setOrientation(RecyclerView.VERTICAL);

        categoriesAdapter = new categoriesAdapter(getContext(), catbdViewModel.getCatModel().getValue());
        productitemAdapter = new productitemAdapter(getContext(), catbdViewModel.getAllproductModel().getValue());

        ctbinding.prodrec.setLayoutManager(llm);
        ctbinding.prodrec.setAdapter(productitemAdapter);
        ctbinding.catsrc.setLayoutManager(llm1);
        ctbinding.catsrc.setAdapter(categoriesAdapter);
    }

    private void viewfunctions() {
        ctbinding.mpcattxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctbinding.catshowlay.setVisibility(View.VISIBLE);
                ctbinding.myprodlay.setVisibility(View.INVISIBLE);
            }
        });

        ctbinding.mpprodtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctbinding.catshowlay.setVisibility(View.INVISIBLE);
                ctbinding.myprodlay.setVisibility(View.VISIBLE);
            }
        });

        ctbinding.cattxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctbinding.catshowlay.setVisibility(View.VISIBLE);
                ctbinding.myprodlay.setVisibility(View.INVISIBLE);
            }
        });

        ctbinding.prodtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctbinding.catshowlay.setVisibility(View.INVISIBLE);
                ctbinding.myprodlay.setVisibility(View.VISIBLE);
            }
        });

        ctbinding.csaddmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctbinding.catshowlay.setVisibility(View.INVISIBLE);
                ctbinding.catadderlay.setVisibility(View.VISIBLE);
            }
        });

        ctbinding.catdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = shpref.edit();
                editor.putString("catadded", "yes");
                editor.commit();
                ctbinding.catshowlay.setVisibility(View.VISIBLE);
                ctbinding.catadderlay.setVisibility(View.INVISIBLE);
            }
        });

        ctbinding.catimgsel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCropActivity();
            }
        });
        ctbinding.catsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (catimgsel == null) {

                    Toast.makeText(getActivity(), "Please Select Category Image.", Toast.LENGTH_SHORT).show();

                } else if (ctbinding.catnamesel.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Category Name.", Toast.LENGTH_SHORT).show();

                } else {
                    ctbinding.saveprogressbar.setVisibility(View.VISIBLE);
                    ctbinding.catsave.setVisibility(View.INVISIBLE);
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(baseurl.baseurl)
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                    try {
                        if (catimgsel != null) {
                            InputStream is = getActivity().getContentResolver().openInputStream(catimgsel);
                            Bitmap image = BitmapFactory.decodeStream(is);
                            ByteArrayOutputStream by = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG, 50, by);
                            base64img = Base64.encodeToString(by.toByteArray(), Base64.DEFAULT);

                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Call<sellerApiResp.commonresult> call = logregApiInterface.addcategory(userId, ctbinding.catnamesel.getText().toString(), base64img);

                    call.enqueue(new Callback<sellerApiResp.commonresult>() {
                        @Override
                        public void onResponse(Call<sellerApiResp.commonresult> call, Response<sellerApiResp.commonresult> response) {
                            if (!response.isSuccessful()) {

                                Log.d("Code", response.message().toString());
                            }

                            sellerApiResp.commonresult resp = response.body();
                            if (resp.getMessage() != null) {
                                Log.d("message", resp.getMessage());
                            }

                            if (resp.getMessage().equals("Category created successfully.!")) {
                                ctbinding.saveprogressbar.setVisibility(View.GONE);
                                ctbinding.catsave.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "Category Added!", Toast.LENGTH_SHORT).show();
                                ctbinding.catshowlay.setVisibility(View.VISIBLE);
                                ctbinding.catadderlay.setVisibility(View.INVISIBLE);
                                SharedPreferences.Editor editor = shpref.edit();
                                editor.putString("catadded", "yes");
                                editor.commit();
                                catbdFragment df = new catbdFragment();
                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.fade_2, R.anim.fade);
                                transaction.replace(R.id.mainfragment, df);
                                transaction.commit();
                            } else {
                                ctbinding.saveprogressbar.setVisibility(View.GONE);
                                ctbinding.catsave.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "There was an error", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<sellerApiResp.commonresult> call, Throwable t) {
                          Log.d("Failure",t.getMessage());
                            ctbinding.saveprogressbar.setVisibility(View.GONE);
                            ctbinding.catsave.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void startCropActivity() {
        CropImage.activity()
                .start(getContext(), this);

    }
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                catimgsel = result.getUri();

                Picasso.get().load(catimgsel).into(ctbinding.imageView8);
            }

        }
    }
}