package com.multivendor.marketsellerapp.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.CustomDialogs.viewitemDialog;
import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.Models.newordModel;
import com.multivendor.marketsellerapp.R;
import com.multivendor.marketsellerapp.act_vieworder;

import java.util.ArrayList;
import java.util.List;

public class newordAdapter extends RecyclerView.Adapter<newordAdapter.viewHolder> {
    private Context mcontext;
    private List<cartModel.singlecartResult> ordlist;
    public newordAdapter(Context mcontext, List<cartModel.singlecartResult> ordlist) {
        this.mcontext = mcontext;
        this.ordlist=ordlist;
    }



    @Override
    public viewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.newordlay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder( newordAdapter.viewHolder holder, int position) {
        holder.buyername.setText(ordlist.get(position).getCustomer_name());
        holder.buyeraddr.setText(ordlist.get(position).getCustomer_address());
        holder.amount.setText("Rs " +ordlist.get(position).getTotal_price());

        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("orderid",ordlist.get(position).getOrder_id());
                bundle.putString("storeid",ordlist.get(position).getStore());
                FragmentActivity activity = (FragmentActivity)(mcontext);
                FragmentManager fm = activity.getSupportFragmentManager();
                com.multivendor.marketsellerapp.CustomDialogs.viewitemDialog viewitemDialog=new viewitemDialog();
                viewitemDialog.setArguments(bundle);
                viewitemDialog.show(fm,"viewitemDialog");
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mcontext, act_vieworder.class);
                intent.putExtra("orderid",ordlist.get(position).getOrder_id());
                intent.putExtra("userid",ordlist.get(position).getStore());
                mcontext.startActivity(intent);
                ((Activity)mcontext).overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordlist.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView buyername;
        TextView buyeraddr;
        TextView amount;
        View items;
        View card;
        public viewHolder(View itemView) {
            super(itemView);
            buyername=itemView.findViewById(R.id.buyername);
            buyeraddr=itemView.findViewById(R.id.buyeraddr);
            amount=itemView.findViewById(R.id.amount);
            items=itemView.findViewById(R.id.itemlay);
            card=itemView.findViewById(R.id.newordcard);
        }
    }
}
