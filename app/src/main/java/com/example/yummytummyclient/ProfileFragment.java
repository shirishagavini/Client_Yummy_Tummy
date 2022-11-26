    package com.example.yummytummyclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ProfileFragment extends BaseFragment{

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        AppCompatButton logoutButton = view.findViewById(R.id.logOutButton);
        TextView userName,gmail,phoneNumber,wallet;
        userName = view.findViewById(R.id.userNameTextView);
        gmail = view.findViewById(R.id.emailIdTextView);
        phoneNumber = view.findViewById(R.id.numberTextView);
        currUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("Resturants").document(currUserID)
                .collection("Details").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<ResturantDetails> list = queryDocumentSnapshots.toObjects(ResturantDetails.class);
                        userName.setText(list.get(0).getName());
                        gmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        phoneNumber.setText(list.get(0).getPhone());
                    }
                });

        ImageView myDetails = view.findViewById(R.id.myDetailsArrow);
        myDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myDetails = new MyDetaillsFragment();
                FragmentManager manager = getActivity(). getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout,myDetails);
                transaction.commit();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(getActivity());
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }
}