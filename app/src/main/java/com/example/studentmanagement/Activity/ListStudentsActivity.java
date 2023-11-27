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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.studentmanagement.Adapter.StudentAdapter;
import com.example.studentmanagement.Model.StudentModel;
import com.example.studentmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ListStudentsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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

        String[] iplTeam = {"Name", "Identity", "StdClass", "School Year", "Gender", "Faculty","Major"};
        Spinner spin1 = (Spinner) findViewById(R.id.spinner1);

// Make sure that 'this' is implementing AdapterView.OnItemSelectedListener
//        spin1.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, iplTeam);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] iplTeam = {"name", "id", "stClass", "schoolYear", "gender", "faculty","major"};

                String selectedSpinnerItem = iplTeam[position];
                showSortedBy(selectedSpinnerItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void showSortedBy(String type){
        dbStudentList.collection("Students")
                .orderBy(type)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            studentModelList.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                StudentModel cardModel = document.toObject(StudentModel.class);
                                studentModelList.add(cardModel);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    studentAdapter.notifyDataSetChanged();
                                }
                            });

                            if (studentModelList.isEmpty()) {
                                // Xử lý trường hợp dữ liệu trống
                            }
                        } else {
                            Log.e("FirestoreError", "Error getting documents: ", task.getException());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Hiển thị một thông báo lỗi cho người dùng
                                }
                            });
                        }
                    }
                });
    }
    private void SearchStudent(String name, String id, String stClass,String schoolYear){
        Query query = dbStudentList.collection("Students");

        // Thêm điều kiện nếu giá trị không phải null
        if (!TextUtils.isEmpty(name)) {
            query = query.whereEqualTo("name", name);
        }

        if (!TextUtils.isEmpty(id)) {
            query = query.whereEqualTo("id", id);
        }

        if (!TextUtils.isEmpty(stClass)) {
            query = query.whereEqualTo("stClass", stClass);
        }

        if (!TextUtils.isEmpty(schoolYear)) {
            query = query.whereEqualTo("schoolYear", schoolYear);
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            studentModelList.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                StudentModel cardModel = document.toObject(StudentModel.class);
                                studentModelList.add(cardModel);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    studentAdapter.notifyDataSetChanged();
                                }
                            });

                            if (studentModelList.isEmpty()) {
                                // Xử lý trường hợp không tìm thấy dữ liệu
                            }
                        } else {
                            Log.e("FirestoreError", "Error getting documents: ", task.getException());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Hiển thị một thông báo lỗi cho người dùng
                                }
                            });
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
        // Lấy dữ liệu từ thành phần giao diện người dùng trong Dialog
        EditText inputID = dialog.findViewById(R.id.inputID);
        EditText inputName = dialog.findViewById(R.id.inputName);
        EditText inputClass = dialog.findViewById(R.id.inputClass);
        EditText inputYear = dialog.findViewById(R.id.inputYear);
        Button btnSearch = dialog.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị từ EditText
                SearchStudent(inputName.getText().toString(),inputID.getText().toString(),inputClass.getText().toString(),inputYear.getText().toString());
                // Xử lý dữ liệu (ví dụ: in giá trị lên Log)

                // Đóng Dialog nếu cần
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

