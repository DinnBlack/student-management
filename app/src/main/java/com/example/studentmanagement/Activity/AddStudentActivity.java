package com.example.studentmanagement.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.studentmanagement.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddStudentActivity extends AppCompatActivity {

    private Toolbar tbAddStudent;
    private FirebaseFirestore dbStudentAdd;
    private ImageView ivAddStudentAvatar;
    private TextView tvAddStudentName, tvAddStudentId, tvAddStudentClass, tvAddStudentSchoolYear,
            tvAddStudentBirthday, tvAddStudentGender, tvAddStudentFaculty, tvAddStudentMajor;
    private Button btAddStudentAfterFill;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ActivityResultLauncher<String> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        tbAddStudent = findViewById(R.id.tbAddStudent);
        setSupportActionBar(tbAddStudent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbStudentAdd = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        ivAddStudentAvatar = findViewById(R.id.ivAddStudentAvatar);
        tvAddStudentName = findViewById(R.id.tvAddStudentName);
        tvAddStudentId = findViewById(R.id.tvAddStudentId);
        tvAddStudentClass = findViewById(R.id.tvAddStudentClass);
        tvAddStudentSchoolYear = findViewById(R.id.tvAddStudentSchoolYear);
        tvAddStudentBirthday = findViewById(R.id.tvAddStudentBirthday);
        tvAddStudentGender = findViewById(R.id.tvAddStudentGender);
        tvAddStudentFaculty = findViewById(R.id.tvAddStudentFaculty);
        tvAddStudentMajor = findViewById(R.id.tvAddStudentMajor);
        btAddStudentAfterFill = findViewById(R.id.btAddStudentAfterFill);

        ivAddStudentAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(AddStudentActivity.this)
                        .crop()
                        .maxResultSize(120, 120)
                        .start();
            }
        });

        tvAddStudentBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        btAddStudentAfterFill.setOnClickListener(v -> {
            uploadImageAndData();
        });
    }

    private void openDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                //Showing the picked value in the textView
                tvAddStudentBirthday.setText(String.valueOf(day)+ "/"+String.valueOf(month+1)+ "/"+String.valueOf(year));

            }
        }, 2023, 01, 20);

        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        ivAddStudentAvatar.setImageURI(imageUri);
    }

    private void uploadImageAndData() {
        if (imageUri != null) {
            StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                insertData(downloadUri);
                            } else {
                                Toast.makeText(AddStudentActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddStudentActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertData(Uri downloadUri) {
        Map<String, Object> items = new HashMap<>();
        items.put("name", tvAddStudentName.getText().toString().trim());
        items.put("id", tvAddStudentId.getText().toString().trim());
        items.put("stClass", tvAddStudentClass.getText().toString().trim());
        items.put("schoolYear", tvAddStudentSchoolYear.getText().toString().trim());
        items.put("birthday", tvAddStudentBirthday.getText().toString().trim());
        items.put("gender", tvAddStudentGender.getText().toString().trim());
        items.put("faculty", tvAddStudentFaculty.getText().toString().trim());
        items.put("major", tvAddStudentMajor.getText().toString().trim());
        items.put("img", downloadUri.toString());

        // Add data to Firestore
        dbStudentAdd.collection("Students").document(tvAddStudentId.getText().toString().trim())
                .set(items)
                .addOnCompleteListener(task -> {
                    // Reset UI elements
                    tvAddStudentName.setText("");
                    tvAddStudentId.setText("");
                    tvAddStudentClass.setText("");
                    tvAddStudentSchoolYear.setText("");
                    tvAddStudentBirthday.setText("");
                    tvAddStudentGender.setText("");
                    tvAddStudentFaculty.setText("");
                    tvAddStudentMajor.setText("");
                    ivAddStudentAvatar.setImageDrawable(null);
                    Toast.makeText(AddStudentActivity.this, "Added Student Successfully", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
