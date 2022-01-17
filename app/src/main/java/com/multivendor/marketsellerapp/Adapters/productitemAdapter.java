package com.multivendor.marketsellerapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.multivendor.marketsellerapp.APIWork.LogregApiInterface;
import com.multivendor.marketsellerapp.CustomDialogs.zoom_imageDialog;
import com.multivendor.marketsellerapp.Models.productitemModel;
import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class productitemAdapter extends RecyclerView.Adapter<productitemAdapter.viewHolder> {

    private Context mcontext;
    private List<productitemModel> productmodel;
    private showszAdapter adapter;
    public productitemAdapter(Context mcontext, List<productitemModel> productmodel) {
        this.mcontext = mcontext;
        this.productmodel = productmodel;
    }

    @Override
    public viewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.productitem_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final int radius = 20;
        final int margin = 20;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(productmodel.get(position).getItemimg())
                .resize(200,200).transform(transformation).into(holder.itemimg);
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

        holder.itemname.setText(productmodel.get(position).getItemname());


        if (productmodel.get(position).getSizeandquats() != null) {
            holder.quatavail.setText("Available Qty:" + productmodel.get(position).getSizeandquats().get(0)
                    .getQty());
            holder.itemprice.setText("Price: "+"₹ "+productmodel.get(position)
                    .getSizeandquats().get(0).getSelling_price());
        }
        adapter=new showszAdapter(mcontext,productmodel.get(position).getSizeandquats());
        LinearLayoutManager llm=new LinearLayoutManager(mcontext);
        llm.setOrientation(RecyclerView.HORIZONTAL);
        holder.sizerec.setLayoutManager(llm);
        holder.sizerec.setAdapter(adapter);
        adapter.setonbtnclickListener(new showszAdapter.onbtnclick() {
            @Override
            public void onCLICK(int pos) {
                holder.quatavail.setText("Available Qty.:" + productmodel.get(position).
                        getSizeandquats().get(pos).getQty());

                holder.itemprice.setText("Price: "+"₹ "+productmodel.get(position)
                        .getSizeandquats().get(pos).getSelling_price());
            }
        });
        
        if(productmodel.get(position).getStock_status().equals("out_of_stock")) {
            holder.quatavail.setText("Out Of Stock");
        }

    }

    @Override
    public int getItemCount() {
        return productmodel.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView itemimg;
        TextView itemname;
        TextView itemprice;

        TextView quatavail;
        Button addtrigger;
        View instock;
        View outstock;
        RecyclerView sizerec;

        public viewHolder(View itemView) {
            super(itemView);
            itemimg=itemView.findViewById(R.id.productimg);
            itemname=itemView.findViewById(R.id.productname);
            itemprice=itemView.findViewById(R.id.pricetxt);
            sizerec=itemView.findViewById(R.id.size_rec);
            quatavail=itemView.findViewById(R.id.totalquat);
            instock=itemView.findViewById(R.id.instockbtn);
            outstock=itemView.findViewById(R.id.outstockbtn);

            instock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                    Call<sellerApiResp.addproductinfo> call = logregApiInterface.updatestock(productmodel.get(getAdapterPosition())
                            .getUserid(), productmodel.get(getAdapterPosition()).getProduct_id(),"in_stock");

                    call.enqueue(new Callback<sellerApiResp.addproductinfo>() {
                        @Override
                        public void onResponse(Call<sellerApiResp.addproductinfo> call, Response<sellerApiResp.addproductinfo> response) {
                            if (!response.isSuccessful()) {
                                Log.d("Error code", String.valueOf(response.code()));
                            }
                            sellerApiResp.addproductinfo resp = response.body();

                            Log.d("msg",resp.getMessage());

                            if(resp.getMessage().toString().equals("Stock status updated successfully.!")) {
                                Toast.makeText(mcontext, "Set to In Stock", Toast.LENGTH_SHORT).show();
                                quatavail.setText("Available Qty.:"+productmodel.get(getAdapterPosition()).getSizeandquats()
                                .get(0).getQty());
                            }
                        }

                        @Override
                        public void onFailure(Call<sellerApiResp.addproductinfo> call, Throwable t) {
                            Toast.makeText(mcontext, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

           outstock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://lmartsolutions.com/api/")
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    LogregApiInterface logregApiInterface = retrofit.create(LogregApiInterface.class);

                    Call<sellerApiResp.addproductinfo> call = logregApiInterface.updatestock(productmodel.get(getAdapterPosition())
                            .getUserid(), productmodel.get(getAdapterPosition()).getProduct_id(),"out_of_stock");

                    call.enqueue(new Callback<sellerApiResp.addproductinfo>() {
                        @Override
                        public void onResponse(Call<sellerApiResp.addproductinfo> call, Response<sellerApiResp.addproductinfo> response) {
                            if (!response.isSuccessful()) {
                                Log.d("Error code", String.valueOf(response.code()));
                            }
                            sellerApiResp.addproductinfo resp = response.body();
                            Log.d("msg",resp.getMessage());

                            if(resp.getMessage().toString().equals("Stock status updated successfully.!")) {
                                Toast.makeText(mcontext, "Set to Out Of Stock", Toast.LENGTH_SHORT).show();
                                quatavail.setText("Out Of Stock");
                            }
                        }

                        @Override
                        public void onFailure(Call<sellerApiResp.addproductinfo> call, Throwable t) {
                            Toast.makeText(mcontext, t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
