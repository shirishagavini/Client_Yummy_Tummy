package com.example.yummytummyclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SplashFragment extends BaseFragment {


    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//       this is a Splash fragment which appears as the app loads
        view =  inflater.inflate(R.layout.fragment_splash, container, false);
        new Thread(){
            public void run(){
                try{
//                    Sleeping the system for loading
                    sleep(5000);
                }catch(Exception e){
                }
                finally {
                    if(FirebaseAuth.getInstance().getCurrentUser() != null){
                        currUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                        getting the user details and checking whether the form is filled or not
                        FirebaseFirestore.getInstance().collection("Users").document(currUserID).collection("Details").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<UserData> details = queryDocumentSnapshots.toObjects(UserData.class);

                                String currUserId = FirebaseAuth.getInstance().getUid();
                                for(int i=0;i<details.size();i++){
                                    if(details.get(i).getUserId().equals(currUserId) && details.get(i).getUserType().equals("customer")){
//                                      checking if the customer has sign in in the client app
                                        Toast.makeText(getActivity(),"You are not eligible for this application",Toast.LENGTH_SHORT);
                                        break;

                                    }

                                    if(details.get(i).getUserId().equals(currUserId) && details.get(i).getIsFormFilled().equals("true")){
//                                        launching the dashboard activity if the form is filled
                                        Intent DashboardActivity = new Intent(getActivity(),DashBoardActivity.class);
                                        DashboardActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(DashboardActivity);

                                    }else{
//                                        launching the userform fragment if the form is not filled
                                        loadFragment(new UserFormFragment(),false);
                                    }
                                }

                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e){
                                //Error Msg created
                                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
//                       launching the login fragment if the user is new to the app
                        loadFragment(new LoginFragment(),false);
                    }
                }
            }
        }.start();

        return view;
    }
}