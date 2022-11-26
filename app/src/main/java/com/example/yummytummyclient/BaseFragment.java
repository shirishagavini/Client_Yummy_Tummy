package com.example.yummytummyclient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;

public class BaseFragment extends Fragment {
// This is the base fragment where all variables and methods are declared for the generic use of the Fragments.
    protected View view;
    protected String currUserID;
    protected FirebaseUser currUser;

//  This is the load fragment for laoding the fragments in  the main framelayout.
    protected void loadFragment(androidx.fragment.app.Fragment fragment, boolean add) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainframeLayout, fragment);
        if (add) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

}