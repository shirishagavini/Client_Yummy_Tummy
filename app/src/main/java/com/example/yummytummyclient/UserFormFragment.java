package com.example.yummytummyclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFormFragment extends BaseFragment {


    public UserFormFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
//        this is the fragment is responsible for getting the information from the restaurant owner and putting it into the database
        view = inflater.inflate(R.layout.fragment_user_form, container, false);
        TextView nametextView,pincodetextView,phoneNumbertextView,addresstextView;
        AppCompatButton submitButton = view.findViewById(R.id.submitButton);

        nametextView = view.findViewById(R.id.namedittext);
        phoneNumbertextView = view.findViewById(R.id.phoneEdittext);
        addresstextView = view.findViewById(R.id.stateEdittext);
        pincodetextView = view.findViewById(R.id.pincodeEdittext);

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String name,phone,pincode,address,state,landmark;
//              getting the data from the edittext and converting them into the string
                name = nametextView.getText().toString();
                phone = phoneNumbertextView.getText().toString();
                address = addresstextView.getText().toString();
                pincode = pincodetextView.getText().toString();
//                checking if the field provided are not empty
                if(!name.isEmpty() && !phone.isEmpty() && !pincode.isEmpty() && !address.isEmpty()){
                    currUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Map<String,String> pincodeMap = new HashMap<>();
                    pincodeMap.put("pincode",pincode);
//                    setting up the pin code of the restaurant in which it would be serving
                    FirebaseFirestore.getInstance().collection("Resturants").document(currUserID).collection("ImcompleteOrders");
                    FirebaseFirestore.getInstance().collection("Resturants").document(currUserID).collection("Pincode").add(pincodeMap);
                    FirebaseFirestore.getInstance().collection("Resturants").document(currUserID).collection("CompleteOrders");
                    FirebaseFirestore.getInstance().collection("Pincode").document(pincode).collection("Menu").document(currUserID).collection(currUserID);

                    FirebaseFirestore.getInstance().collection("Pincode").document(pincode).collection("Resturant Names")
                            .document(currUserID).set(new ResClientDetails(name,currUserID,4.5f,Integer.parseInt(pincode)));
                    FirebaseFirestore.getInstance().collection("AllResturants").document(currUserID).set(new ResClientDetails(name,currUserID,4.5f,Integer.parseInt(pincode)));
                    FirebaseFirestore.getInstance()
                            .collection("Resturants")
                            .document(currUserID)
                            .collection("Details")
                            .add(new ResturantDetails(name,phone,address))
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    FirebaseFirestore.getInstance().collection("Users").document(currUserID).collection("Details").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<UserData> details = queryDocumentSnapshots.toObjects(UserData.class);
                                            for(int i=0;i<details.size();i++){
//                                                updating the respond details as form field when the form is actually paid by the vendor
                                                queryDocumentSnapshots.getDocuments().get(i).getReference().update("isFormFilled",""+true);
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e){
                                            Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });
//                                    setting up the toast message and launching the dashboard activity on Success
                                    Toast.makeText(getActivity(),"Data Saved",Toast.LENGTH_SHORT).show();
                                    Intent DashboardActivity = new Intent(getActivity(),DashBoardActivity.class);
                                    DashboardActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(DashboardActivity);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e){
                            Toast.makeText(getActivity(),"Unable to process",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }
}