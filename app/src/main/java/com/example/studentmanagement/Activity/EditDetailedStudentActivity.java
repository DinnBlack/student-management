package com.example.studentmanagement.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.studentmanagement.Model.StudentModel;
import com.example.studentmanagement.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class EditDetailedStudentActivity extends AppCompatActivity {

    private Toolbar tbEditDetailedStudent;
    private ImageView ivEditDetailedStudentAvatar, btSaveEditDetailedStudent;
    private EditText etEditDetailedStudentName, etEditDetailedStudentId, etEditDetailedStudentClass,
            etEditDetailedStudentSchoolYear, etEditDetailedStudentBirthday, etEditDetailedStudentGender,
            etEditDetailedStudentFaculty, etEditDetailedStudentMajor;
    private FirebaseFirestore dbEditDetailedStudent;
    private StudentModel studentModel;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ActivityResultLauncher<String> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detailed_student);

        tbEditDetailedStudent = findViewById(R.id.tbEditDetailedStudent);
        setSupportActionBar(tbEditDetailedStudent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Intent intent = getIntent();
        if (intent.hasExtra("StudentModel")) {
            studentModel = (StudentModel) intent.getSerializableExtra("StudentModel");
        }

        ivEditDetailedStudentAvatar = findViewById(R.id.ivEditDetailedStudentAvatar);
        etEditDetailedStudentName = findViewById(R.id.etEditDetailedStudentName);
        etEditDetailedStudentId = findViewById(R.id.etEditDetailedStudentId);
        etEditDetailedStudentClass = findViewById(R.id.etEditDetailedStudentClass);
        etEditDetailedStudentSchoolYear = findViewById(R.id.etEditDetailedStudentSchoolYear);
        etEditDetailedStudentBirthday = findViewById(R.id.etEditDetailedStudentBirthday);
        etEditDetailedStudentGender = findViewById(R.id.etEditDetailedStudentGender);
        etEditDetailedStudentFaculty = findViewById(R.id.etEditDetailedStudentFaculty);
        etEditDetailedStudentMajor = findViewById(R.id.etEditDetailedStudentMajor);
        btSaveEditDetailedStudent = findViewById(R.id.btSaveEditDetailedStudent);

        dbEditDetailedStudent = FirebaseFirestore.getInstance();

        if (studentModel != null) {
            // Set the values to EditText fields
            etEditDetailedStudentName.setText(studentModel.getName());
            etEditDetailedStudentId.setText(studentModel.getId());
            etEditDetailedStudentClass.setText(studentModel.getStClass());
            etEditDetailedStudentSchoolYear.setText(studentModel.getSchoolYear());
            etEditDetailedStudentBirthday.setText(studentModel.getBirthday());
            etEditDetailedStudentGender.setText(studentModel.getGender());
            etEditDetailedStudentFaculty.setText(studentModel.getFaculty());
            etEditDetailedStudentMajor.setText(studentModel.getMajor());

            // Load the image using Glide
            Glide.with(getApplicationContext()).load(studentModel.getImg()).into(ivEditDetailedStudentAvatar);
        }

        ivEditDetailedStudentAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(EditDetailedStudentActivity.this)
                        .crop()
                        .maxResultSize(120, 120)
                        .start();
            }
        });

        btSaveEditDetailedStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageAndData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        ivEditDetailedStudentAvatar.setImageURI(imageUri);
    }

    private void uploadImageAndData() {
        if (imageUri != null) {
            StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                updateData(downloadUri);
                            } else {
                                Toast.makeText(EditDetailedStudentActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditDetailedStudentActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            updateDataNoImage();
        }
    }

    private void updateData(Uri downloadUri) {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", etEditDetailedStudentName.getText().toString().trim());
        updatedData.put("id", etEditDetailedStudentId.getText().toString().trim());
        updatedData.put("stClass", etEditDetailedStudentClass.getText().toString().trim());
        updatedData.put("schoolYear", etEditDetailedStudentSchoolYear.getText().toString().trim());
        updatedData.put("birthday", etEditDetailedStudentBirthday.getText().toString().trim());
        updatedData.put("gender", etEditDetailedStudentGender.getText().toString().trim());
        updatedData.put("faculty", etEditDetailedStudentFaculty.getText().toString().trim());
        updatedData.put("major", etEditDetailedStudentMajor.getText().toString().trim());
        updatedData.put("img", downloadUri.toString());

        dbEditDetailedStudent.collection("Students")
                .whereEqualTo("id", etEditDetailedStudentId.getText().toString().trim())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            dbEditDetailedStudent.collection("Students")
                                    .document(documentId)
                                    .update(updatedData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(EditDetailedStudentActivity.this, "Updated Detailed Student Successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EditDetailedStudentActivity.this, "Error updating student details", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(EditDetailedStudentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateDataNoImage() {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", etEditDetailedStudentName.getText().toString().trim());
        updatedData.put("id", etEditDetailedStudentId.getText().toString().trim());
        updatedData.put("stClass", etEditDetailedStudentClass.getText().toString().trim());
        updatedData.put("schoolYear", etEditDetailedStudentSchoolYear.getText().toString().trim());
        updatedData.put("birthday", etEditDetailedStudentBirthday.getText().toString().trim());
        updatedData.put("gender", etEditDetailedStudentGender.getText().toString().trim());
        updatedData.put("faculty", etEditDetailedStudentFaculty.getText().toString().trim());
        updatedData.put("major", etEditDetailedStudentMajor.getText().toString().trim());

        dbEditDetailedStudent.collection("Students")
                .whereEqualTo("id", etEditDetailedStudentId.getText().toString().trim())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            dbEditDetailedStudent.collection("Students")
                                    .document(documentId)
                                    .update(updatedData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(EditDetailedStudentActivity.this, "Updated Detailed Student Successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EditDetailedStudentActivity.this, "Error updating student details", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(EditDetailedStudentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
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
