package com.multivendor.marketsellerapp.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.Models.quickorderModel;
import com.multivendor.marketsellerapp.R;

import java.util.List;

public class ordprodAdapter extends RecyclerView.Adapter<ordprodAdapter.viewHolder> {

    private Context mcontext;
    public List<quickorderModel .quick_products> prodList;
    onpricechange listener;
    public ordprodAdapter(Context mcontext, List<quickorderModel .quick_products> prodList) {
        this.mcontext = mcontext;
        this.prodList = prodList;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.ordprod_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(ordprodAdapter.viewHolder holder, int position) {

        if(prodList.get(position).getProduct_name()!=null) {
            holder.prodname.setText(prodList.get(position).getProduct_name());
        }
        if(prodList.get(position).getPrice()!=null) {
            holder.prodprice.setText(String.valueOf(prodList.get(position).getPrice()));
        }
        if(prodList.get(position).getQty()!=null) {
            holder.prodquat.setText(String.valueOf(prodList.get(position).getQty()));
        }

        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prodList.size()>1) {
                    prodList.remove(position);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return prodList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        EditText prodname;
        EditText prodquat;
        EditText prodprice;
        View delbtn;
        public viewHolder( View itemView) {
            super(itemView);
            prodname=itemView.findViewById(R.id.ordprodname);
            prodquat=itemView.findViewById(R.id.ordprodquat);
            prodprice=itemView.findViewById(R.id.ordprodprice);
            delbtn=itemView.findViewById(R.id.orddelbtn);

            prodprice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.length()>0) {
                        prodList.get(getAdapterPosition()).setPrice(String.valueOf(editable));
                        listener.onpriceChange(Integer.valueOf(editable.toString()));
                    }
                }
            });
            prodname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    prodList.get(getAdapterPosition()).setProduct_name(String.valueOf(editable));
                }
            });
            prodquat.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    prodList.get(getAdapterPosition()).setQty(String.valueOf(editable));
                }
            });
        }
    }

    public interface onpricechange {
        void onpriceChange(Integer price);

    }

    public void Setonpricechange(onpricechange listener) {
        this.listener=listener;
    }


}
