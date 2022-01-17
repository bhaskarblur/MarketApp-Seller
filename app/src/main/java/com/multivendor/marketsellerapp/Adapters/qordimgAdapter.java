package com.multivendor.marketsellerapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.CustomDialogs.zoom_imageDialog;
import com.multivendor.marketsellerapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class qordimgAdapter extends RecyclerView.Adapter<qordimgAdapter.viewHolder> {
    private Context mcontext;
    private List<String> imglist;

    public qordimgAdapter(Context mcontext, List<String> imglist) {
        this.mcontext = mcontext;
        this.imglist = imglist;
    }

    @NonNull

    @Override
    public viewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.qordimg_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(qordimgAdapter.viewHolder holder, int position) {
        Picasso.get().load(imglist.get(position)).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentActivity activity = (FragmentActivity) (mcontext);
                FragmentManager fm = activity.getSupportFragmentManager();
                Bundle bundle=new Bundle();
                bundle.putString("image",imglist.get(position));
                zoom_imageDialog dialog=new zoom_imageDialog();
                dialog.setArguments(bundle);
                dialog.show(fm,"dialog");
            }
        });

    }

    @Override
    public int getItemCount() {
        return imglist.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public viewHolder( View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.qordimg);
        }
    }
}
