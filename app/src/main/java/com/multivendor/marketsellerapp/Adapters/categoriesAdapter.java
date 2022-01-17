package com.multivendor.marketsellerapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.CustomDialogs.zoom_imageDialog;
import com.multivendor.marketsellerapp.Models.categoriesModel;
import com.multivendor.marketsellerapp.R;
import com.multivendor.marketsellerapp.catProductsFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class categoriesAdapter extends RecyclerView.Adapter<categoriesAdapter.viewHolder> {

    public categoriesAdapter(Context mcontext, List<categoriesModel> catModel) {
        this.mcontext = mcontext;
        this.catModel = catModel;
    }

    private Context mcontext;
    private List<categoriesModel> catModel;
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.cats_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        Picasso.get().load(catModel.get(position).getImage())
                .resize(300,300).transform(new CropCircleTransformation()).into(holder.catimg);
        holder.catname.setText(catModel.get(position).getName());
        holder.catimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity) (mcontext);
                FragmentManager fm = activity.getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("image",catModel.get(position).getImage());
                com.multivendor.marketsellerapp.CustomDialogs.zoom_imageDialog zoom_imageDialog1=new zoom_imageDialog();
                zoom_imageDialog1.setArguments(bundle);
                zoom_imageDialog1.show(fm,"zoom_imagDialog1");
            }
        });
        holder.catcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.multivendor.marketsellerapp.catProductsFragment cpfrag=new catProductsFragment();
                Bundle bundle =new Bundle();
                bundle.putString("selectedCategoryid",catModel.get(position).getId());
                bundle.putString("selectedCategoryname",catModel.get(position).getName());
                cpfrag.setArguments(bundle);
                FragmentTransaction transaction=((AppCompatActivity)mcontext).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_left);
                transaction.replace(R.id.mainfragment,cpfrag);
                transaction.addToBackStack("B");
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return catModel.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView catimg;
        TextView catname;
        View catcard;
        public viewHolder(View itemView) {
            super(itemView);
            catimg=itemView.findViewById(R.id.catimg);
            catname=itemView.findViewById(R.id.catname);
            catcard=itemView.findViewById(R.id.catlay);

        }
    }
}
