package com.example.studentmanagement.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.Adapter.AccountAdapter;
import com.example.studentmanagement.Model.AccountModel;
import com.example.studentmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListAccountsActivity extends AppCompatActivity {

    Toolbar tbAccountsList;
    RecyclerView rcAccountsList;
    AccountAdapter accountAdapter;
    List<AccountModel> accountModelList;
    FirebaseFirestore dbAccountList;
    ImageView btAddAccount;
    String[] currentRole = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_accounts);
        tbAccountsList = findViewById(R.id.tbAccountsList);
        rcAccountsList = findViewById(R.id.rcAccountsList);
        setSupportActionBar(tbAccountsList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbAccountsList.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String userEmail = currentUser.getEmail();

        if (currentUser != null) {


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference accountsRef = db.collection("Accounts");
            Query query = accountsRef.whereEqualTo("email", userEmail);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            currentRole[0] = document.getString("role");
                        }
                        checkRole(currentRole[0]);
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        btAddAccount = findViewById(R.id.btAddAccount);
        btAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ListAccountsActivity.this, AddAccountActivity.class);
                startActivity(myIntent);
            }
        });

        dbAccountList = FirebaseFirestore.getInstance();
        rcAccountsList.setLayoutManager(new GridLayoutManager(ListAccountsActivity.this, 1));
        accountModelList = new ArrayList<>();
        accountAdapter = new AccountAdapter(ListAccountsActivity.this, accountModelList);
        rcAccountsList.setAdapter(accountAdapter);
        dbAccountList.collection("Accounts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AccountModel cardModel = document.toObject(AccountModel.class);
                                accountModelList.add(cardModel);
                            }
                            accountAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void checkRole(String role) {
        if (role.equals("Admin")) {
            btAddAccount.setVisibility(View.VISIBLE);
        } else {
            btAddAccount.setVisibility(View.INVISIBLE);
        }
    }
}