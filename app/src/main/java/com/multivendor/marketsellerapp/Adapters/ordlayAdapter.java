package com.multivendor.marketsellerapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.Models.cartModel;
import com.multivendor.marketsellerapp.R;
import com.multivendor.marketsellerapp.act_vieworder;

import java.util.List;

public class ordlayAdapter extends RecyclerView.Adapter<ordlayAdapter.viewHolder> {
    public ordlayAdapter(Context mcontext, List<cartModel.singlecartResult> ordlist) {
        this.mcontext = mcontext;
        this.ordlist = ordlist;
    }

    private Context mcontext;
    private List<cartModel.singlecartResult> ordlist;

    @Override
    public ordlayAdapter.viewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.orders_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(ordlayAdapter.viewHolder holder, int position) {
        holder.orderid.setText("Order #"+String.valueOf(ordlist.get(position).getOrder_id()));
        holder.orderdate.setText(String.valueOf(ordlist.get(position).getOrder_date()));
        holder.orderstats.setText("Order Status: "+ordlist.get(position).getStatus());
        holder.orderitemdetail.setText("Order Details: "+ordlist.get(position).getProducts().get(0).getProduct_name()
                +" and "+String.valueOf(ordlist.get(position).getProducts().size()-1)+" other items");

        holder.orderamount.setText("Order Amount: "+"₹ "+String.valueOf(ordlist.get(position).getTotal_price()));

        if(ordlist.get(position).getPayment_method()!=null) {
            if (ordlist.get(position).getPayment_method().equals("prepaid")) {
                if (ordlist.get(position).getTransaction_id() != null) {
                    holder.paymmethod.setText("Payment Method: " + "UPI " + "(Txn:" + ordlist.get(position).getTransaction_id() + ")");
                } else {
                    holder.paymmethod.setText("Payment Method: " + "UPI");
                }

            } else if ((ordlist.get(position).getPayment_method().equals("cod"))) {
                holder.paymmethod.setText("Payment Method: " + "COD");
            }
        }
        else {
            holder.paymmethod.setText("Payment Method: "+"");
        }
        if(!ordlist.get(position).getStatus().equals("Delivered")) {
            if (ordlist.get(position).getPayment_method() != null) {
                if (ordlist.get(position).getPayment_method().equals("prepaid")) {
                    holder.paymstat.setText("Payment Status: " + "Paid");
                } else if (ordlist.get(position).getPayment_method().equals("cod")) {
                    holder.paymstat.setText("Payment Status: " + "Pending");
                }
            } else {
                holder.paymstat.setText("Payment Status: " + "Pending");
            }
        }
        else {
            holder.paymstat.setText("Payment Status: " + "Paid");
        }
        holder.orderlocat.setText(ordlist.get(position).getCustomer_address());

        holder.ordercard.setOnClickListener(new View.OnClickListener() {
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
        TextView orderid;
        TextView orderdate;
        TextView orderstats;
        TextView orderitemdetail;
        TextView orderamount;
        TextView paymmethod;
        TextView paymstat;
        TextView orderlocat;
        View ordercard;
        public viewHolder( View itemView) {
            super(itemView);
            orderid=itemView.findViewById(R.id.ordtickid);
            orderdate=itemView.findViewById(R.id.orddate);
            orderitemdetail=itemView.findViewById(R.id.orditemdet);
            orderstats=itemView.findViewById(R.id.ordstatus);
            orderamount=itemView.findViewById(R.id.ordamount);
            paymmethod=itemView.findViewById(R.id.ordpmmeth);
            paymstat=itemView.findViewById(R.id.ordpmstat);
            orderlocat=itemView.findViewById(R.id.ordlocat);
            ordercard=itemView.findViewById(R.id.ordercard);

        }
    }
}
