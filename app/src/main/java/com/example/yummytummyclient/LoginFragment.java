package com.example.yummytummyclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class LoginFragment extends BaseFragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        Setting up the layout on the fragemnet
        view = inflater.inflate(R.layout.fragment_login, container, false);
        AppCompatButton loginButton;
        loginButton = view.findViewById(R.id.loginButton);
//      Adding a listner for the button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<AuthUI.IdpConfig> authenticationMethods = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build()
                );
//          Setting up the login fragment.
                Intent Login = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(authenticationMethods)
                        .setLogo(R.drawable.restaurant_logo)
                        .setAlwaysShowSignInMethodScreen(true)
                        .build();
//              Setting up the activity
                startActivityForResult(Login,95460);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        if(requestCode == 95460 ){
            if(resultCode == Activity.RESULT_OK){
//              Checking the user has first timmed loggin in or in the second time
                if(currUser.getMetadata().getCreationTimestamp() <= currUser.getMetadata().getLastSignInTimestamp() && currUser.getMetadata().getCreationTimestamp()+5 >= currUser.getMetadata().getLastSignInTimestamp()){
//                  Getting the user id.
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    try{
                        FirebaseFirestore.getInstance().collection("Users").document(currUserID).collection("Details").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                          Setting the  user and its details for filling up the form
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<UserData> details = queryDocumentSnapshots.toObjects(UserData.class);
                                String currUserId = FirebaseAuth.getInstance().getUid();
                                if(details.size() == 1){
//                                  Calling the UserFragment
                                    loadFragment(new UserFormFragment(),false);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e){
//                                setting up the toast fragment
                                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        UserData userData = new UserData("false",userId);
//                        setting up the flag for form fragment and adding it in the firebase
                        FirebaseFirestore.getInstance().collection("Users")
                                .document(userId)
                                .collection("Details")
                                .add(userData)
//                               adding up the user data into the firebase
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
//                                  loading the user fragment on getting success
                                        loadFragment(new UserFormFragment(),false);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }


                }
                else{
                    //                  Getting the user id.
                    currUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseFirestore.getInstance().collection("Users").document(currUserID).collection("Details").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<UserData> details = queryDocumentSnapshots.toObjects(UserData.class);
//                           Setting up the flag for form fragment
                            String currUserId = FirebaseAuth.getInstance().getUid();
                            for(int i=0;i<details.size();i++){
                                if(details.get(i).getUserId().equals(currUserId) && details.get(i).getIsFormFilled().equals("true")){
//                                  Launching the dashboard activity
                                    Intent DashboardActivity = new Intent(getActivity(),DashBoardActivity.class);
                                    DashboardActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(DashboardActivity);
                                }else{
//                                    loading the user Fragment
                                    loadFragment(new UserFormFragment(),false);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e){
//                            on getting Failed loading the user fragment
                            loadFragment(new UserFormFragment(),false);
                        }
                    });
                }
            }
        }
    }
}
