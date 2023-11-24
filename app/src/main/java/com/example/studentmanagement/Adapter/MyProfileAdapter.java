package com.example.studentmanagement.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studentmanagement.Model.AccountModel;
import com.example.studentmanagement.R;

import java.util.List;

public class MyProfileAdapter extends RecyclerView.Adapter<MyProfileAdapter.ViewHolder> {

    private Context context;
    private List<AccountModel> list;

    public MyProfileAdapter(Context context, List<AccountModel> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivMyProfileAvatar;
        TextView tvMyProfileEmail, tvMyProfileName, tvMyProfileRole, tvMyProfilePassword, tvMyProfileBirthday, tvMyProfilePhoneNumber, tvMyProfileStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMyProfileAvatar = itemView.findViewById(R.id.ivMyProfileAvatar);
            tvMyProfileName = itemView.findViewById(R.id.tvMyProfileName);
            tvMyProfileRole = itemView.findViewById(R.id.tvMyProfileRole);
            tvMyProfileEmail = itemView.findViewById(R.id.tvMyProfileEmail);
            tvMyProfilePassword = itemView.findViewById(R.id.tvMyProfilePassword);
            tvMyProfileBirthday = itemView.findViewById(R.id.tvMyProfileBirthday);
            tvMyProfilePhoneNumber = itemView.findViewById(R.id.tvMyProfilePhoneNumber);
            tvMyProfileStatus = itemView.findViewById(R.id.tvMyProfileStatus);
        }
    }

    @NonNull
    @Override
    public MyProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyProfileAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg()).into(holder.ivMyProfileAvatar);
        holder.tvMyProfileName.setText(list.get(position).getName());
        holder.tvMyProfileRole.setText(list.get(position).getRole());
        holder.tvMyProfileEmail.setText(list.get(position).getEmail());
        holder.tvMyProfilePassword.setText(list.get(position).getPassword());
        holder.tvMyProfileBirthday.setText(list.get(position).getBirthday());
        holder.tvMyProfilePhoneNumber.setText(list.get(position).getPhoneNumber());
        holder.tvMyProfileStatus.setText(list.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
