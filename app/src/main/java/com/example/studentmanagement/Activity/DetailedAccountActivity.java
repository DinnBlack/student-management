package com.example.studentmanagement.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    private TextView tvDetailedAccountName, tvDetailedAccountRole, tvDetailedAccountEmail, tvDetailedAccountPassword, tvDetailedAccountBirthday, tvDetailedAccountPhoneNumber, tvDetailedAccountStatus;
    private ImageView ivDetailedAccountAvatar;
    @SuppressLint("MissingInflatedId")
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
        tvDetailedAccountName = findViewById(R.id.tvDetailedAccountName);
        tvDetailedAccountRole = findViewById(R.id.tvDetailedAccountRole);
        tvDetailedAccountEmail = findViewById(R.id.tvDetailedAccountEmail);
        tvDetailedAccountPassword = findViewById(R.id.tvDetailedAccountPassword);
        tvDetailedAccountBirthday = findViewById(R.id.tvDetailedAccountBirthday);
        tvDetailedAccountPhoneNumber = findViewById(R.id.tvDetailedAccountPhoneNumber);
        tvDetailedAccountStatus = findViewById(R.id.tvDetailedAccountStatus);

        dbDetailedAccount = FirebaseFirestore.getInstance();

        if (obj instanceof AccountModel) {
            accountModel = (AccountModel) obj;
        }

        if (accountModel != null) {
            Glide.with(getApplicationContext()).load(accountModel.getImg()).into(ivDetailedAccountAvatar);
            tvDetailedAccountName.setText(accountModel.getName());
            tvDetailedAccountRole.setText(accountModel.getRole());
            tvDetailedAccountEmail.setText(accountModel.getEmail());
            tvDetailedAccountPassword.setText(accountModel.getPassword());
            tvDetailedAccountBirthday.setText(accountModel.getBirthday());
            tvDetailedAccountPhoneNumber.setText(accountModel.getPhoneNumber());
            tvDetailedAccountStatus.setText(accountModel.getStatus());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detailed_account, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        if (itemId == R.id.itemEditDetailedAccount) {
            Intent myIntent = new Intent(DetailedAccountActivity.this, EditDetailedAccountActivity.class);
            myIntent.putExtra("StudentModel", accountModel);
            startActivity(myIntent);
        }
        if (itemId == R.id.itemDeleteAccount) {
            // Delete Account
        }
        return super.onOptionsItemSelected(item);
    }
}