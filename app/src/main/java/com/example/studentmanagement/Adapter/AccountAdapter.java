package com.example.studentmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studentmanagement.Activity.DetailedAccountActivity;
import com.example.studentmanagement.Model.AccountModel;
import com.example.studentmanagement.R;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder>{

    private Context context;
    private List<AccountModel> list;

    public AccountAdapter(Context context, List<AccountModel> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivAccountAvatar, btMoreActions;
        TextView tvAccountName, tvAccountAge, tvAccountPhoneNumber, tvAccountStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAccountAvatar = itemView.findViewById(R.id.ivAccountAvatar);
            tvAccountName = itemView.findViewById(R.id.tvAccountName);
            tvAccountAge = itemView.findViewById(R.id.tvAccountAge);
            tvAccountPhoneNumber = itemView.findViewById(R.id.tvAccountPhoneNumber);
            tvAccountStatus = itemView.findViewById(R.id.tvAccountStatus);
        }

    }



    @NonNull
    @Override
    public AccountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitems_account, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg()).into(holder.ivAccountAvatar);
        holder.tvAccountName.setText(list.get(position).getName());
        holder.tvAccountAge.setText(list.get(position).getAge());
        holder.tvAccountPhoneNumber.setText(list.get(position).getPhoneNumber());
        holder.tvAccountStatus.setText(list.get(position).getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, DetailedAccountActivity.class);
                myIntent.putExtra("Detailed", list.get(position));
                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
