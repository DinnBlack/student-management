package com.example.studentmanagement.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.studentmanagement.Fragment.HomeFragment;
import com.example.studentmanagement.Fragment.ProfileFragment;
import com.example.studentmanagement.R;

import nl.joery.animatedbottombar.AnimatedBottomBar;

// ...

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment = new HomeFragment();
    private ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnimatedBottomBar bottomBar = findViewById(R.id.botnavBar);
        replaceFragment(new HomeFragment());
        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                if (tab1.getId() == R.id.action_home) {
                    replaceFragment(new HomeFragment());
                } else if (tab1.getId() == R.id.action_profile) {
                    replaceFragment(new ProfileFragment());
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}
