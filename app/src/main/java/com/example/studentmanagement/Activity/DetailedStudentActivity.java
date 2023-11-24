package com.example.studentmanagement.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.studentmanagement.Model.StudentModel;
import com.example.studentmanagement.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailedStudentActivity extends AppCompatActivity {

    private Toolbar tbStudentDetails;
    private FirebaseFirestore dbDetailedStudent;
    private StudentModel studentModel;
    private TextView tvDetailedStudentName, tvDetailedStudentId, tvDetailedStudentClass, tvDetailedStudentSchoolYear, tvDetailedBirthday, tvDetailedGender, tvDetailedFaculty, tvDetailedMajor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_student);

        tbStudentDetails = findViewById(R.id.tbStudentDetails);
        setSupportActionBar(tbStudentDetails);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final Object obj = intent.getSerializableExtra("Detailed");

        ImageView ivDetailedStudentAvatar = findViewById(R.id.ivDetailedStudentAvatar);
        tvDetailedStudentName = findViewById(R.id.tvDetailedStudentName);
        tvDetailedStudentId = findViewById(R.id.tvDetailedStudentId);
        tvDetailedStudentClass = findViewById(R.id.tvDetailedStudentClass);
        tvDetailedStudentSchoolYear = findViewById(R.id.tvDetailedStudentSchoolYear);
        tvDetailedBirthday = findViewById(R.id.tvDetailedStudentBirthday);
        tvDetailedGender = findViewById(R.id.tvDetailedStudentGender);
        tvDetailedFaculty = findViewById(R.id.tvDetailedStudentFaculty);
        tvDetailedMajor = findViewById(R.id.tvDetailedStudentMajor);

        dbDetailedStudent = FirebaseFirestore.getInstance();

        if (obj instanceof StudentModel) {
            studentModel = (StudentModel) obj;
        }

        if (studentModel != null) {
            Glide.with(getApplicationContext()).load(studentModel.getImg()).into(ivDetailedStudentAvatar);
            tvDetailedStudentName.setText(studentModel.getName());
            tvDetailedStudentId.setText(studentModel.getId());
            tvDetailedStudentClass.setText(studentModel.getStClass());
            tvDetailedStudentSchoolYear.setText(studentModel.getSchoolYear());
            tvDetailedBirthday.setText(studentModel.getBirthday());
            tvDetailedGender.setText(studentModel.getGender());
            tvDetailedFaculty.setText(studentModel.getFaculty());
            tvDetailedMajor.setText(studentModel.getMajor());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detailed_student, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        if (itemId == R.id.itemEditDetailedStudent) {
            Intent myIntent = new Intent(DetailedStudentActivity.this, EditDetailedStudentActivity.class);
            myIntent.putExtra("StudentModel", studentModel);
            startActivity(myIntent);
        }
        if (itemId == R.id.itemDeleteStudent) {
            showDeleteConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Student");
        builder.setMessage("Are you sure you want to delete this student?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteStudent();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }


    private void deleteStudent() {
        if (studentModel != null) {
            dbDetailedStudent.collection("Students")
                    .document(tvDetailedStudentId.getText().toString().trim())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DetailedStudentActivity.this,
                                    "Student deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("DetailedStudentActivity", "Error deleting student", e);
                            Toast.makeText(DetailedStudentActivity.this,
                                    "Error deleting student", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(DetailedStudentActivity.this,
                    "Student data is null", Toast.LENGTH_SHORT).show();
        }
    }

}
