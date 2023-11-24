package com.example.studentmanagement.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.studentmanagement.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddAccountActivity extends AppCompatActivity {

    private Toolbar tbAddAccount;
    private FirebaseFirestore dbAccountAdd;
    private ImageView ivAddAccountAvatar;
    private Button btAddAccountAfterFill;
    private TextView tvAddAccountEmail, tvAddAccountPassword, tvAddAccountName, tvAddAccountBirthday, tvAddAccountPhoneNumber, tvAddAccountRole;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        tbAddAccount = findViewById(R.id.tbAddAccount);
        setSupportActionBar(tbAddAccount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        dbAccountAdd = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        ivAddAccountAvatar = findViewById(R.id.ivAddAccountAvatar);
        tvAddAccountEmail = findViewById(R.id.tvAddAccountEmail);
        tvAddAccountPassword = findViewById(R.id.tvAddAccountPassword);
        tvAddAccountName = findViewById(R.id.tvAddAccountName);
        tvAddAccountBirthday = findViewById(R.id.tvAddAccountBirthday);
        tvAddAccountPhoneNumber = findViewById(R.id.tvAddAccountPhoneNumber);
        tvAddAccountRole = findViewById(R.id.tvAddAccountRole);
        btAddAccountAfterFill = findViewById(R.id.btAddAccountAfterFill);

        ivAddAccountAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(AddAccountActivity.this)
                        .crop()
                        .maxResultSize(120, 120)
                        .start();
            }
        });

        btAddAccountAfterFill.setOnClickListener(v -> {

            String email = tvAddAccountEmail.getText().toString().trim();
            String password = tvAddAccountPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                tvAddAccountEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                tvAddAccountPassword.setError("Password is required");
                return;
            }
            if (password.length() < 6) {
                tvAddAccountPassword.setError("Password must be >= 6 characters");
                return;
            }

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            String userEmail = currentUser.getEmail();
            String[] currentEmail = {""};
            String[] currentPassword = {""};

            if (currentUser != null) {


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference accountsRef = db.collection("Accounts");
                Query query = accountsRef.whereEqualTo("email", userEmail);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                currentEmail[0] = document.getString("email");
                                currentPassword[0] = document.getString("password");
                            }
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            uploadImageAndData();
                            mAuth.signInWithEmailAndPassword(currentEmail[0], currentPassword[0])
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> signInTask) {
                                            if (signInTask.isSuccessful()) {
                                            } else {
                                            }
                                        }
                                    });
                        } else {
                        }
                    });


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        ivAddAccountAvatar.setImageURI(imageUri);
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
                                Toast.makeText(AddAccountActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddAccountActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertData(Uri downloadUri) {
        Map<String, Object> items = new HashMap<>();
        items.put("email", tvAddAccountEmail.getText().toString().trim());
        items.put("password", tvAddAccountPassword.getText().toString().trim());
        items.put("name", tvAddAccountName.getText().toString().trim());
        items.put("birthday", tvAddAccountBirthday.getText().toString().trim());
        items.put("phoneNumber", tvAddAccountPhoneNumber.getText().toString().trim());
        items.put("role", tvAddAccountRole.getText().toString().trim());
        items.put("status", "Nomal");
        items.put("img", downloadUri.toString());

        dbAccountAdd.collection("Accounts").document(tvAddAccountEmail.getText().toString().trim())
                .set(items)
                .addOnCompleteListener(task -> {
                    Toast.makeText(AddAccountActivity.this, "Added Account Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    finish();
                });
    }
}
