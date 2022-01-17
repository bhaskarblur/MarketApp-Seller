package com.multivendor.marketsellerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.Models.sellerApiResp;
import com.multivendor.marketsellerapp.R;

import org.w3c.dom.Text;

import java.util.List;

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.viewHolder> {

    private Context mcontext;
    private List<sellerApiResp.reviewresult> reviewList;

    public reviewAdapter(Context mcontext, List<sellerApiResp.reviewresult> reviewList) {
        this.mcontext = mcontext;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.reviews_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.revname.setText(reviewList.get(position).getName());

        holder.revdate.setText(reviewList.get(position).getCreated_at().substring(0,10));
        float rating=Float.parseFloat(reviewList.get(position).getRating());
        holder.revrating.setProgress((int) rating*2);
        holder.revtext.setText(reviewList.get(position).getReview());
        holder.revrating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView revname;
        TextView revdate;
        RatingBar revrating;
        TextView revtext;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            revname=itemView.findViewById(R.id.revname);
            revdate=itemView.findViewById(R.id.revdate);
            revrating=itemView.findViewById(R.id.revratingbar);
            revtext=itemView.findViewById(R.id.revreviewtxt);
        }
    }
}
