package com.example.yummytummyclient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MyDetaillsFragment extends BaseFragment {
    protected EditText mYuserName, mYphoneuNumber, mYpincode, mYstate, mYaddress, mYlandmark;
    public MyDetaillsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        this fragment is meant for getting the restaurant details of the restaurant
        view =  inflater.inflate(R.layout.fragment_my_detaills, container, false);
        mYuserName = view.findViewById(R.id.namedittext);
        mYphoneuNumber = view.findViewById(R.id.phoneEdittext);
        mYpincode = view.findViewById(R.id.pincodeEdittext);
        mYstate = view.findViewById(R.id.stateEdittext);
        mYaddress = view.findViewById(R.id.stateEdittext);


//      getting the user ID of the resturant
        currUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("Resturants").document(currUserID).collection("Details").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//getting the restaurant details from the firebase
                List<ResturantDetails> details = queryDocumentSnapshots.toObjects(ResturantDetails.class);
//                casting the document into restaurant details class
                ResturantDetails Details = details.get(0);
                mYuserName.setText(Details.getName());
                mYphoneuNumber.setText(Details.getPhone());
                mYaddress.setText(Details.getAddress());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e){

            }
        });



        return view;
    }
}