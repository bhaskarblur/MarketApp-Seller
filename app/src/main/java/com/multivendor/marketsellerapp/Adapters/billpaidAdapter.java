package com.multivendor.marketsellerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.Models.billModel;
import com.multivendor.marketsellerapp.R;

import java.util.List;

public class billpaidAdapter extends RecyclerView.Adapter<billpaidAdapter.viewHolder> {

    private Context mcontext;
    private List<billModel.billresult> billlist;

    public billpaidAdapter(Context mcontext, List<billModel.billresult> billlist) {
        this.mcontext = mcontext;
        this.billlist = billlist;
    }

    @Override
    public billpaidAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.billpaid_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(billpaidAdapter.viewHolder holder, int position) {
        holder.pertxt.setText(billlist.get(position).getBill_from_date()+" to "+billlist.get(position).getBill_to_date());
        holder.totsal.setText("Rs "+billlist.get(position).getSub_total());
        holder.serchar.setText("Rs "+billlist.get(position).getService_charge());
        holder.gst.setText("Rs "+billlist.get(position).getTax_amount());
        holder.ttpayout.setText("Rs "+billlist.get(position).getTotal_amount());

        holder.getinv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(billlist.get(position).getInvoice()), "application/pdf");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return billlist.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView pertxt;
        TextView getinv;
        TextView totsal;
        TextView serchar;
        TextView gst;
        TextView ttpayout;

        public viewHolder( View itemView) {
            super(itemView);
            pertxt=itemView.findViewById(R.id.perdate);
            getinv=itemView.findViewById(R.id.billgetinv);
            totsal=itemView.findViewById(R.id.biltotsal);
            serchar=itemView.findViewById(R.id.billservchar);
            gst=itemView.findViewById(R.id.billgst);
            ttpayout=itemView.findViewById(R.id.totpayout);
        }
    }
}
