package com.multivendor.marketsellerapp.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.multivendor.marketsellerapp.R;

import java.util.ArrayList;
import java.util.List;

public class addszAdapter extends RecyclerView.Adapter<addszAdapter.viewHolder> {

    private String inputtype;
    public addszAdapter(Context context, List<String> list,String inputtype) {
        this.context = context;
        this.list = list;
        this.inputtype=inputtype;
    }

    private Context context;
    public List<String> list;
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.addsizeoth_lay,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.fieldtxt.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        EditText fieldtxt;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            fieldtxt=itemView.findViewById(R.id.fieldtxt);

            if(inputtype.equals("number")) {
                fieldtxt.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            else if(inputtype.equals("text")) {
                fieldtxt.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            }
            fieldtxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    list.set(getAdapterPosition(),s.toString());
                }
            });
        }
    }
}
