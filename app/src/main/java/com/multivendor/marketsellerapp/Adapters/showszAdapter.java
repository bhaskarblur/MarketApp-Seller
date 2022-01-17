package com.multivendor.marketsellerapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.Models.productitemModel;
import com.multivendor.marketsellerapp.R;

import java.util.ArrayList;
import java.util.List;

public class showszAdapter extends RecyclerView.Adapter<showszAdapter.viewHolder> {

    public showszAdapter(Context context, List<productitemModel.sizeandquat> list) {
        this.context = context;
        this.list = list;
    }

    private Context context;
    onbtnclick listener;
    private Integer selecteditem=0;
    public List<productitemModel.sizeandquat> list=new ArrayList<>();
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.showsizeoth_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        if(list!=null) {
            if(!list.get(position).getSize().toString().equals("")) {
                holder.fieldtxt.setText(list.get(position).getSize());
            }
            }

        if(selecteditem==position) {

            holder.fieldtxt.setBackgroundTintList(context.getResources().getColorStateList(R.color.secblue));
        }
        else{
            holder.fieldtxt.setBackgroundTintList(context.getResources().getColorStateList(R.color.secgrey));
        }

    }

    @Override
    public int getItemCount() {
        if(list!=null) {
            return list.size();
        }
        else {
            return 0;
        }
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        Button fieldtxt;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            fieldtxt=itemView.findViewById(R.id.size_btn);
            fieldtxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition()!=RecyclerView.NO_POSITION) {
                        listener.onCLICK(getAdapterPosition());
                        Log.d("size",String.valueOf(getItemCount()));
                        selecteditem=getAdapterPosition();
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
    public interface onbtnclick {
        void onCLICK(int position);
    }
    public void setonbtnclickListener(onbtnclick listener) {
        this.listener=listener;
    }
}