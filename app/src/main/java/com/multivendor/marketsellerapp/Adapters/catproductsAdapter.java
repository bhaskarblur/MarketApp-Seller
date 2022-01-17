package com.multivendor.marketsellerapp.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.CustomDialogs.productaddDialog;
import com.multivendor.marketsellerapp.CustomDialogs.productupdateDialog;
import com.multivendor.marketsellerapp.CustomDialogs.zoom_imageDialog;
import com.multivendor.marketsellerapp.Models.productitemModel;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.R;
import com.multivendor.marketsellerapp.catProductsFragment;
import com.multivendor.marketsellerapp.catbdFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class catproductsAdapter extends RecyclerView.Adapter<catproductsAdapter.viewHolder> {

    private Context mcontext;
    private List<productitemModel> productmodel;
    private showszAdapter adapter;
    private String catname;

    public catproductsAdapter(Context mcontext, List<productitemModel> productmodel,String catname) {
        this.mcontext = mcontext;
        this.productmodel = productmodel;
        this.catname=catname;

    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.catproducts_lay, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final int radius = 20;
        final int margin = 10;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(productmodel.get(position).getItemimg())
                .resize(200,200).transform(transformation).into(holder.itemimg);
        holder.itemname.setText(productmodel.get(position).getItemname());
        holder.itemimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity) (mcontext);
                FragmentManager fm = activity.getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("image",productmodel.get(position).getItemimg());
                com.multivendor.marketsellerapp.CustomDialogs.zoom_imageDialog zoom_imageDialog1=new zoom_imageDialog();
                zoom_imageDialog1.setArguments(bundle);
                zoom_imageDialog1.show(fm,"zoom_imagDialog1");
            }
        });
        if (productmodel.get(position).getSizeandquats() != null) {
            holder.quatavail.setText("Available Qty:" + productmodel.get(position).getSizeandquats().get(0)
                    .getQty());
            holder.itemprice.setText("Price: "+"₹ "+productmodel.get(position)
                    .getSizeandquats().get(0).getSelling_price());
        }
        adapter = new showszAdapter(mcontext, productmodel.get(position).getSizeandquats());
        adapter.setonbtnclickListener(new showszAdapter.onbtnclick() {
            @Override
            public void onCLICK(int pos) {
                holder.quatavail.setText("Available Qty.:"+productmodel.get(position).
                        getSizeandquats().get(pos).getQty());

                holder.itemprice.setText("Price: "+"₹ "+productmodel.get(position)
                        .getSizeandquats().get(pos).getSelling_price());
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(mcontext);
        llm.setOrientation(RecyclerView.HORIZONTAL);
        holder.sizerec.setLayoutManager(llm);
        holder.sizerec.setAdapter(adapter);
        holder.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userid", productmodel.get(position).getUserid());
                bundle.putString("catid", productmodel.get(position).getCategory_id());
                bundle.putString("prodid", productmodel.get(position).getProduct_id());
                bundle.putInt("position",position);
                FragmentActivity activity = (FragmentActivity) (mcontext);
                FragmentManager fm = activity.getSupportFragmentManager();
                com.multivendor.marketsellerapp.CustomDialogs.productupdateDialog productaddDialog = new productupdateDialog();
                productaddDialog.setArguments(bundle);
                productaddDialog.setCancelable(false);
                productaddDialog.show(fm, "productaddDialog");
            }
        });


        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext).setTitle("Delete?").setMessage(
                        "Are you sure you want to delete this product?"
                ).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                                .addConverterFactory(GsonConverterFactory.create()).build();

                        LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                        Call<sellerApiResp.commonresult> call = logregApiInterface.deleteproduct(productmodel.get(position)
                                .getUserid(), productmodel.get(position).getProduct_id());

                        call.enqueue(new Callback<sellerApiResp.commonresult>() {
                            @Override
                            public void onResponse(Call<sellerApiResp.commonresult> call, Response<sellerApiResp.commonresult> response) {
                                if (!response.isSuccessful()) {
                                    Log.d("Error code", String.valueOf(response.code()));
                                }
                                sellerApiResp.commonresult resp = response.body();


                            }

                            @Override
                            public void onFailure(Call<sellerApiResp.commonresult> call, Throwable t) {

                            }
                        });

                        com.multivendor.marketsellerapp.catbdFragment catProductsFragment = new catbdFragment();

                        FragmentTransaction transaction = ((AppCompatActivity) mcontext).getSupportFragmentManager().beginTransaction();
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
    }

    @Override
    public int getItemCount() {
        return productmodel.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView itemimg;
        TextView itemname;
        TextView itemprice;
        RecyclerView sizerec;
        TextView quatavail;
        Button addtrigger;
        View editbtn;
        View deletebtn;

        public viewHolder(View itemView) {
            super(itemView);
            itemimg = itemView.findViewById(R.id.productimg);
            itemname = itemView.findViewById(R.id.productname);
            itemprice = itemView.findViewById(R.id.pricetxt);
            sizerec = itemView.findViewById(R.id.size_rec);
            quatavail = itemView.findViewById(R.id.totalquat);
            editbtn = itemView.findViewById(R.id.prodedit);
            deletebtn = itemView.findViewById(R.id.proddelete);
        }
    }
}
