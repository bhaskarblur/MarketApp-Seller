package com.multivendor.marketsellerapp.CustomDialogs;

import static android.app.Activity.RESULT_OK;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.Adapters.addszAdapter;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.R;
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

public class productaddDialog extends BottomSheetDialogFragment {
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
    String userid;
    View progbar;
    final int IMAGE_PICK_CODE=1000;
    final int PERMISSION_CODE=1001;
    int SELECT_PICTURE = 202;

    private Uri imguri;
    private addszAdapter adapter1;
    private addszAdapter adapter2;
    private addszAdapter adapter3;
    private addszAdapter adapter4;
    String base64img = new String();


    private Boolean adding=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.productadddialog,null);
        progbar=view.findViewById(R.id.progressBar5);
        save=view.findViewById(R.id.addpdsave);
        cancel=view.findViewById(R.id.addpdcanc);
        prodimg=view.findViewById(R.id.addpdimg);
        prodname=view.findViewById(R.id.addpdname);
        sizerec=view.findViewById(R.id.size_rec);
        pricerec=view.findViewById(R.id.price_rec);
        mrprec=view.findViewById(R.id.mrp_rec);
        qtyrec=view.findViewById(R.id.qty_rec);
        addrecs=view.findViewById(R.id.recs_add);
        remrecs=view.findViewById(R.id.recs_decr);
        viewfunc();
        loadData();
        final int radius = 10;
        final int margin = 10;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(R.drawable.imgsample).transform(transformation).into(prodimg);
        return view;
    }

    private void loadData() {
        Bundle bundle=getArguments();
        if(bundle!=null) {
           userid=bundle.getString("userid");
           catid=bundle.getString("catid");

        }
        List<String> newlist=new ArrayList<>();
        List<String> newlist1=new ArrayList<>();
        List<String> newlist2=new ArrayList<>();
        List<String> newlist3=new ArrayList<>();
        newlist.add("");
        newlist1.add("");
        newlist2.add("");
        newlist3.add("");
        adapter1=new addszAdapter(getContext(),newlist,"text");
        adapter2=new addszAdapter(getContext(),newlist1,"number");
        adapter3=new addszAdapter(getContext(),newlist2,"number");
        adapter4=new addszAdapter(getContext(),newlist3,"number");

        LinearLayoutManager llm1=new LinearLayoutManager(getContext());
        llm1.setOrientation(RecyclerView.HORIZONTAL);
        LinearLayoutManager llm2=new LinearLayoutManager(getContext());
        LinearLayoutManager llm3=new LinearLayoutManager(getContext());
        LinearLayoutManager llm4=new LinearLayoutManager(getContext());
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
                pickImageFromGallery();
//               startCropActivity();
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
                if(adapter1.list.size()>1) {
                    adapter1.list.remove(adapter1.list.size() - 1);
                    adapter2.list.remove(adapter1.list.size() - 1);
                    adapter3.list.remove(adapter1.list.size() - 1);
                    adapter4.list.remove(adapter1.list.size() - 1);
                    adapter1.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                    adapter3.notifyDataSetChanged();
                    adapter4.notifyDataSetChanged();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prodname.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Product Name", Toast.LENGTH_SHORT).show();
                }
                else if(adapter1.list.get(0).toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Size", Toast.LENGTH_SHORT).show();
                }
                else if(adapter3.list.get(0).toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter MRP", Toast.LENGTH_SHORT).show();
                }
                else if(adapter2.list.get(0).toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Price", Toast.LENGTH_SHORT).show();
                }
                else if(adapter4.list.get(0).toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Quantity", Toast.LENGTH_SHORT).show();
                }
                else if(imguri==null) {
                    Toast.makeText(getActivity(), "Select A Product Image", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(adding.equals(false)) {
                        adding=true;
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
                                image.compress(Bitmap.CompressFormat.JPEG, 60, by);
                                base64img = Base64.encodeToString(by.toByteArray(), Base64.DEFAULT);
                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        StringBuilder price=new StringBuilder();
                        StringBuilder size = new StringBuilder();
                        StringBuilder mrp=new StringBuilder();
                        StringBuilder qty=new StringBuilder();

                        for(int i=0;i<adapter1.list.size();i++) {
                            if(adapter1.list.size()>1) {
                                size.append(adapter1.list.get(i)+ ",");
                                price.append(adapter2.list.get(i)+ ",");
                                mrp.append(adapter3.list.get(i)+ ",");
                                qty.append(adapter4.list.get(i)+ ",");
                            }
                            else {
                                size.append(adapter1.list.get(i));
                                price.append(adapter2.list.get(i));
                                mrp.append(adapter3.list.get(i));
                                qty.append(adapter4.list.get(i));
                            }
                        }
                        Call<sellerApiResp.addproductinfo> call=logregApiInterface.addproduct(userid,catid,base64img,
                                prodname.getText().toString(),mrp.toString(),price.toString(),size.toString(),qty.toString());

                        call.enqueue(new Callback<sellerApiResp.addproductinfo>() {
                            @Override
                            public void onResponse(Call<sellerApiResp.addproductinfo> call, Response<sellerApiResp.addproductinfo> response) {
                                if (!response.isSuccessful()) {

                                    Log.d("Code", response.message().toString());
                                    adding=false;
                                    return;
                                }

                                sellerApiResp.addproductinfo addproductinfo=response.body();

                                if(addproductinfo.getMessage()!=null) {
                                    Log.d("msg",addproductinfo.getMessage());
                                }
                                adding=false;
                                if(addproductinfo.getMessage().toString().equals("Product created successfully.!")) {
                                    progbar.setVisibility(View.GONE);
                                    save.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "Product Added!", Toast.LENGTH_SHORT).show();

                                    com.multivendor.marketsellerapp.catbdFragment catProductsFragment = new catbdFragment();

                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                                    transaction.replace(R.id.mainfragment, catProductsFragment);
                                    transaction.commit();
                                    dismiss();

                                }
                                else {
                                    progbar.setVisibility(View.GONE);
                                    save.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "An error occured!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<sellerApiResp.addproductinfo> call, Throwable t) {
                                progbar.setVisibility(View.GONE);
                                save.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                                adding=false;
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
//        startActivityForResult(intent, IMAGE_PICK_CODE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null) {
            Toast.makeText(getActivity(), "Nothing Selected", Toast.LENGTH_SHORT).show();
        }
        else {
            if (resultCode == RESULT_OK) {

                // compare the resultCode with the
                // SELECT_PICTURE constant
                if (requestCode == SELECT_PICTURE) {
                    // Get the url of the image from data
                    Uri selectedImageUri = data.getData();
                    imguri = selectedImageUri;
                    if (null != selectedImageUri) {
                final int radius = 15;
                final int margin = 15;
                final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                Picasso.get().load(imguri).transform(transformation).into(prodimg);

            }
        }
    }
        }
    }
}
