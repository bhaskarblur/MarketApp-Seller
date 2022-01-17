package com.multivendor.marketsellerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.Models.newordModel;
import com.multivendor.marketsellerapp.R;

import java.util.List;

public class earnAdapter extends RecyclerView.Adapter<earnAdapter.viewHolder> {
    private Context mcontext;

    public earnAdapter(Context mcontext, List<com.multivendor.marketsellerapp.Models.earnModel> earnList) {
        this.mcontext = mcontext;
        this.earnList= earnList;
    }

    private List<com.multivendor.marketsellerapp.Models.earnModel> earnList;

    @Override
    public viewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.earn_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(earnAdapter.viewHolder holder, int position) {
        holder.pertxt.setText("Earnings in "+earnList.get(position).getPername());
        holder.earncount.setText(String.valueOf(earnList.get(position).getEarncount()));
    }

    @Override
    public int getItemCount() {
        return earnList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView pertxt;
        TextView earncount;
        public viewHolder(View itemView) {
            super(itemView);
           pertxt=itemView.findViewById(R.id.pertxt);
           earncount=itemView.findViewById(R.id.earncount);
        }
    }
}
