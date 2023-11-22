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
import com.example.studentmanagement.Activity.DetailedStudentActivity;
import com.example.studentmanagement.Model.StudentModel;
import com.example.studentmanagement.R;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private Context context;
    private List<StudentModel> list;

    public StudentAdapter(Context context, List<StudentModel> list) {
        this.context = context;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStudentAvatar;
        TextView tvStudentName;
        TextView tvStudentId;
        TextView tvStudentClass;
        TextView tvStudentSchoolYear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStudentAvatar = itemView.findViewById(R.id.ivStudentAvatar);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            tvStudentClass = itemView.findViewById(R.id.tvStudentClass);
            tvStudentSchoolYear = itemView.findViewById(R.id.tvStudentSchoolYear);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitems_student, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImg()).into(holder.ivStudentAvatar);
        holder.tvStudentName.setText(list.get(position).getName());
        holder.tvStudentId.setText(list.get(position).getId());
        holder.tvStudentClass.setText(list.get(position).getStClass());
        holder.tvStudentSchoolYear.setText(list.get(position).getSchoolYear());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, DetailedStudentActivity.class);
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
