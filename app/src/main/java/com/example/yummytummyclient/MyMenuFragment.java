package com.example.yummytummyclient;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yummytummyclient.Adapter.MyMenuAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MyMenuFragment extends BaseFragment
{

    public MyMenuFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_my_menu, container, false);
        final String[] pincode = new String[1];
//        getting the current user ID of the restaurant
        currUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        AppCompatButton addItem = view.findViewById(R.id.add_an_item);
//        getting the pin codes in which restaurant is serving
        FirebaseFirestore.getInstance().collection("Resturants").document(currUserID).collection("Pincode")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<PincodeClass> pincodeClass = queryDocumentSnapshots.toObjects(PincodeClass.class);
                pincode[0] = pincodeClass.get(0).getPincode();
                try{
                    FirebaseFirestore.getInstance().collection("Pincode").document(pincode[0]).collection("Menu")
                            .document(currUserID).collection(currUserID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            getting the pin codes in which restaurant is serving
                            List<MenuItemClass> menuItems = queryDocumentSnapshots.toObjects(MenuItemClass.class);
//                            casting the menuitem document into the menuitem class
                            RecyclerView menuRecyclerView = view.findViewById(R.id.recycelerView);
//                            setting up the recyclerview for the menu items
                            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            menuRecyclerView.setLayoutManager(manager);
                            menuRecyclerView.setHasFixedSize(true);
//                            creating and initialising the menu adaptor for displaying the menu items in the recycler view
                            MyMenuAdapter adapter = new MyMenuAdapter(menuItems,getActivity());
                            menuRecyclerView.setAdapter(adapter);
                        }
                    });
                }catch (Exception e){

                }

            }
        });
//        listening to add item button in the menu Fragment
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//launching the add item fragment
                Fragment menu = new AddItemFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.framelayout, menu);
                transaction.commit();
            }
        });
        return view;
    }
}