package com.example.studentmanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.studentmanagement.Model.AccountModel;
import com.example.studentmanagement.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailedAccountActivity extends AppCompatActivity {
    private Toolbar tbAccountDetails;
    private FirebaseFirestore dbDetailedAccount;
    private AccountModel accountModel;
    private TextView tvDetailedAccountName, tvDetailedAccountRole, tvDetailedAccountAge, tvDetailedAccountPhoneNumber, tvDetailedAccountStatus;
    private ImageView ivDetailedAccountAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_account);

        tbAccountDetails = findViewById(R.id.tbAccountDetails);
        setSupportActionBar(tbAccountDetails);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final Object obj = intent.getSerializableExtra("Detailed");

        ivDetailedAccountAvatar = findViewById(R.id.ivDetailedAccountAvatar);
        tvDetailedAccountAge = findViewById(R.id.tvDetailedAccountAge);
        tvDetailedAccountPhoneNumber = findViewById(R.id.tvDetailedAccountPhoneNumber);
        tvDetailedAccountStatus = findViewById(R.id.tvDetailedAccountStatus);

        dbDetailedAccount = FirebaseFirestore.getInstance();

        if (obj instanceof AccountModel) {
            accountModel = (AccountModel) obj;
        }

        if (accountModel != null) {
            Glide.with(getApplicationContext()).load(accountModel.getImg()).into(ivDetailedAccountAvatar);

        }
    }
}