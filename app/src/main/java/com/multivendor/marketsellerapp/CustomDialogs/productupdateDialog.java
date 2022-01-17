package com.multivendor.marketsellerapp.CustomDialogs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Adapters.addszAdapter;
import com.multivendor.marketsellerapp.Models.productitemModel;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.R;
import com.multivendor.marketsellerapp.catProductsFragment;
import com.multivendor.marketsellerapp.catbdFragment;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class productupdateDialog extends BottomSheetDialogFragment {
    Button save;
    Button cancel;
    ImageView prodimg;
    EditText prodname;
    RecyclerView sizerec;
    RecyclerView pricerec;
    RecyclerView mrprec;
    RecyclerView qtyrec;
    Button addrecs;
    Button remrecs;
    String catid;
    String prod_id;
    String userid;
    View progbar;
    final int IMAGE_PICK_CODE = 1000;
    final int PERMISSION_CODE = 1001;
    private Uri imguri;
    private addszAdapter adapter1;
    private addszAdapter adapter2;
    private addszAdapter adapter3;
    private addszAdapter adapter4;
    String base64img = new String();
    private Boolean adding = false;
    private Integer pos;
    private StringBuilder variation = new StringBuilder();
    private com.multivendor.marketsellerapp.ViewModels.catbdViewModel catbdViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.productadddialog, null);
        save = view.findViewById(R.id.addpdsave);
        cancel = view.findViewById(R.id.addpdcanc);
        prodimg = view.findViewById(R.id.addpdimg);
        prodname = view.findViewById(R.id.addpdname);
        sizerec = view.findViewById(R.id.size_rec);
        pricerec = view.findViewById(R.id.price_rec);
        progbar = view.findViewById(R.id.progressBar5);
        mrprec = view.findViewById(R.id.mrp_rec);
        qtyrec = view.findViewById(R.id.qty_rec);
        addrecs = view.findViewById(R.id.recs_add);
        remrecs = view.findViewById(R.id.recs_decr);
        viewfunc();
        loadData();
        return view;
    }



    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            userid = bundle.getString("userid");
            catid = bundle.getString("catid");
            prod_id = bundle.getString("prodid");
            pos = bundle.getInt("position");

            catbdViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(com.multivendor.marketsellerapp.ViewModels.catbdViewModel.class);
            catbdViewModel.initwork(userid, catid);
            catbdViewModel.getProductModel().observe(getActivity(), new Observer<List<productitemModel>>() {
                @Override
                public void onChanged(List<productitemModel> productitemModels) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (productitemModels.size() > 0) {
                                final int radius = 45;
                                final int margin = 45;
                                final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                                Picasso.get().load(productitemModels.get(pos).getItemimg()).transform(transformation).into(prodimg);
                                prodname.setText(productitemModels.get(pos).getItemname());
                                List<String> newlist = new ArrayList<>();
                                List<String> newlist1 = new ArrayList<>();
                                List<String> newlist2 = new ArrayList<>();
                                List<String> newlist3 = new ArrayList<>();

                                for (int i = 0; i < productitemModels.get(pos).getSizeandquats().size(); i++) {
                                    newlist.add(productitemModels.get(pos).getSizeandquats().get(i).getSize());
                                    newlist1.add(productitemModels.get(pos).getSizeandquats().get(i).getSelling_price());
                                    newlist2.add(productitemModels.get(pos).getSizeandquats().get(i).getPrice());
                                    newlist3.add(productitemModels.get(pos).getSizeandquats().get(i).getQty());

                                    variation.append(productitemModels.get(pos)
                                            .getSizeandquats().get(i).getVariation_id() + ",");
                                }

                                adapter1 = new addszAdapter(getContext(), newlist,"text");
                                adapter2 = new addszAdapter(getContext(), newlist1,"number");
                                adapter3 = new addszAdapter(getContext(), newlist2,"number");
                                adapter4 = new addszAdapter(getContext(), newlist3,"number");

                                LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
                                llm1.setOrientation(RecyclerView.HORIZONTAL);
                                LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
                                LinearLayoutManager llm3 = new LinearLayoutManager(getContext());
                                LinearLayoutManager llm4 = new LinearLayoutManager(getContext());
                                llm2.setOrientation(RecyclerView.HORIZONTAL);
                                llm3.setOrientation(RecyclerView.HORIZONTAL);
                                llm4.setOrientation(RecyclerView.HORIZONTAL);

                                sizerec.setLayoutManager(llm1);
                                sizerec.setAdapter(adapter1);
                                pricerec.setLayoutManager(llm2);
                                pricerec.setAdapter(adapter2);
                                mrprec.setLayoutManager(llm3);
                                mrprec.setAdapter(adapter3);
                                qtyrec.setLayoutManager(llm4);
                                qtyrec.setAdapter(adapter4);
                            }
                        }
                    }, 100);
                }
            });
        }


    }

    private void viewfunc() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        prodimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCropActivity();
            }
        });

        addrecs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter1.list.add("");
                adapter2.list.add("");
                adapter3.list.add("");
                adapter4.list.add("");
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                adapter3.notifyDataSetChanged();
                adapter4.notifyDataSetChanged();
            }
        });

        remrecs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter1.list.remove(adapter1.list.size() - 1);
                adapter2.list.remove(adapter1.list.size() - 1);
                adapter3.list.remove(adapter1.list.size() - 1);
                adapter4.list.remove(adapter1.list.size() - 1);
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                adapter3.notifyDataSetChanged();
                adapter4.notifyDataSetChanged();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prodname.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Product Name", Toast.LENGTH_SHORT).show();
                } else if (adapter1.list.get(0).toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Size", Toast.LENGTH_SHORT).show();
                } else if (adapter3.list.get(0).toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter MRP", Toast.LENGTH_SHORT).show();
                } else if (adapter2.list.get(0).toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Price", Toast.LENGTH_SHORT).show();
                } else if (adapter4.list.get(0).toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Quantity", Toast.LENGTH_SHORT).show();
                } else {
                    if (adding.equals(false)) {
                        adding = true;

                        progbar.setVisibility(View.VISIBLE);
                        save.setVisibility(View.INVISIBLE);
                        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                                .addConverterFactory(GsonConverterFactory.create()).build();

                        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                        try {
                            if (imguri != null) {
                                InputStream is = getActivity().getContentResolver().openInputStream(imguri);
                                Bitmap image = BitmapFactory.decodeStream(is);
                                ByteArrayOutputStream by = new ByteArrayOutputStream();
                                image.compress(Bitmap.CompressFormat.JPEG, 50, by);
                                base64img = Base64.encodeToString(by.toByteArray(), Base64.DEFAULT);
                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        StringBuilder price = new StringBuilder();
                        StringBuilder size = new StringBuilder();
                        StringBuilder mrp = new StringBuilder();
                        StringBuilder qty = new StringBuilder();

                        for (int i = 0; i < adapter1.list.size(); i++) {
                            if (adapter1.list.size() > 1) {
                                size.append(adapter1.list.get(i) + ",");
                                price.append(adapter2.list.get(i) + ",");
                                mrp.append(adapter3.list.get(i) + ",");
                                qty.append(adapter4.list.get(i) + ",");
                            } else {
                                size.append(adapter1.list.get(i));
                                price.append(adapter2.list.get(i));
                                mrp.append(adapter3.list.get(i));
                                qty.append(adapter4.list.get(i));
                            }

                            Log.d("size",size.toString());
                            Log.d("variation",variation.toString());
                        }
                        Call<sellerApiResp.addproductinfo> call = logregApiInterface.updateproduct(userid, prod_id, variation.toString(), base64img,
                                prodname.getText().toString(), mrp.toString(), price.toString(), size.toString(), qty.toString());

                        call.enqueue(new Callback<sellerApiResp.addproductinfo>() {
                            @Override
                            public void onResponse(Call<sellerApiResp.addproductinfo> call, Response<sellerApiResp.addproductinfo> response) {
                                if (!response.isSuccessful()) {

                                    Log.d("Code", response.message().toString());
                                    adding = false;
                                    return;
                                }

                                sellerApiResp.addproductinfo addproductinfo = response.body();

                                if (addproductinfo.getMessage() != null) {
                                    Log.d("msg", addproductinfo.getMessage());
                                }
                                adding = false;
                                if (addproductinfo.getMessage().toString().equals("Product updated successfully.!")) {
                                    progbar.setVisibility(View.GONE);
                                    save.setVisibility(View.VISIBLE);

                                    Toast.makeText(getContext(), "Product Updated!", Toast.LENGTH_SHORT).show();
                                    com.multivendor.marketsellerapp.catbdFragment catProductsFragment = new catbdFragment();
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                                    transaction.replace(R.id.mainfragment, catProductsFragment);
                                    transaction.commit();
                                    dismiss();
                                } else {
                                    progbar.setVisibility(View.GONE);
                                    save.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "An error occured!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<sellerApiResp.addproductinfo> call, Throwable t) {
                                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                                adding = false;
                                progbar.setVisibility(View.GONE);
                                save.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                }
            }
        });
    }
    private void startCropActivity() {
        CropImage.activity()
                .start(getContext(), this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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


    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            Toast.makeText(getActivity(), "Nothing Selected", Toast.LENGTH_SHORT).show();
        } else {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imguri=result.getUri();
                final int radius = 45;
                final int margin = 45;
                final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                Picasso.get().load(imguri).transform(transformation).into(prodimg);

            }
        }
    }
}
