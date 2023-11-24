package com.example.studentmanagement.Fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.studentmanagement.Activity.EditMyProfileActivity;
import com.example.studentmanagement.Activity.LoginActivity;
import com.example.studentmanagement.Activity.LoginHistoryMyProfileActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Toolbar tbMyProfile;
    private ImageView ivMyProfileAvatar;
    private TextView tvMyProfileName, tvMyProfileRole, tvMyProfileEmail, tvMyProfilePassword, tvMyProfileBirthday, tvMyProfilePhoneNumber, tvMyProfileStatus;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tbMyProfile = view.findViewById(R.id.tbMyProfile);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tbMyProfile);
        setHasOptionsMenu(true);

        ivMyProfileAvatar = view.findViewById(R.id.ivMyProfileAvatar);
        tvMyProfileName = view.findViewById(R.id.tvMyProfileName);
        tvMyProfileRole = view.findViewById(R.id.tvMyProfileRole);
        tvMyProfileEmail = view.findViewById(R.id.tvMyProfileEmail);
        tvMyProfilePassword = view.findViewById(R.id.tvMyProfilePassword);
        tvMyProfileBirthday = view.findViewById(R.id.tvMyProfileBirthday);
        tvMyProfilePhoneNumber = view.findViewById(R.id.tvMyProfilePhoneNumber);
        tvMyProfileStatus = view.findViewById(R.id.tvMyProfileStatus);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference accountsRef = db.collection("Accounts");

            Query query = accountsRef.whereEqualTo("email", userEmail);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Glide.with(requireContext()).load(document.getString("img")).into(ivMyProfileAvatar);
                            tvMyProfileName.setText(document.getString("name"));
                            tvMyProfileRole.setText(document.getString("role"));
                            tvMyProfileEmail.setText(document.getString("email"));
                            tvMyProfilePassword.setText(document.getString("password"));
                            tvMyProfileBirthday.setText(document.getString("birthday"));
                            tvMyProfilePhoneNumber.setText(document.getString("phoneNumber"));
                            tvMyProfileStatus.setText(document.getString("status"));
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_my_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.itemEditMyProfile) {
            Intent myIntent = new Intent(getContext(), EditMyProfileActivity.class);
            startActivity(myIntent);
            return true;
        }
        if (itemId == R.id.itemSeeLoginHistoryMyProfile) {
            Intent myIntent = new Intent(getContext(), LoginHistoryMyProfileActivity.class);
            startActivity(myIntent);
            return true;
        }
        if (itemId == R.id.itemLogoutMyProfile) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}