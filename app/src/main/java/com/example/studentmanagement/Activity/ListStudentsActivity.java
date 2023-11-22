package com.example.studentmanagement.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Make sure to use the correct import statement
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.studentmanagement.Adapter.StudentAdapter;
import com.example.studentmanagement.Model.StudentModel;
import com.example.studentmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListStudentsActivity extends AppCompatActivity {

    Toolbar tbStudentsList;
    ImageView btFilter;
    ImageView btAddStudent;
    LinearLayout layoutStudentList;
    RecyclerView rcStudentsList;
    StudentAdapter studentAdapter;
    List<StudentModel> studentModelList;
    FirebaseFirestore dbStudentList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_students);

        layoutStudentList = findViewById(R.id.layoutStudentList);
        tbStudentsList = findViewById(R.id.tbStudentsList);
        btFilter = findViewById(R.id.btFilter);
        btAddStudent = findViewById(R.id.btAddStudent);
        setSupportActionBar(tbStudentsList); // Use setSupportActionBar instead of setActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tbStudentsList.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        btAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ListStudentsActivity.this, AddStudentActivity.class);
                startActivity(myIntent);
            }
        });

        rcStudentsList = findViewById(R.id.rcStudentsList);
        dbStudentList = FirebaseFirestore.getInstance();
        rcStudentsList.setLayoutManager(new GridLayoutManager(ListStudentsActivity.this, 1));
        studentModelList = new ArrayList<>();
        studentAdapter = new StudentAdapter(ListStudentsActivity.this, studentModelList);
        rcStudentsList.setAdapter(studentAdapter);
        dbStudentList.collection("Students")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                StudentModel cardModel = document.toObject(StudentModel.class);
                                studentModelList.add(cardModel);
                            }
                            studentAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_filter_studentslist);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}
