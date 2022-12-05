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

import com.example.yummytummyclient.Adapter.CompeletOrdersAdapter;
import com.example.yummytummyclient.Adapter.IncompeleteOrderAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class OrderCompeletedFragment extends BaseFragment
{

    public OrderCompeletedFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        this segment is made for getting the pending orders of the restaurant and getting it displayed into the recyclerview so that they can be completd by the vendor
        view = inflater.inflate(R.layout.fragment_order_compeleted, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycelerView);
        try{
//            getting the restaurant ID from the firebase
            currUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//          Getting the completed orders from the firebase
            FirebaseFirestore.getInstance().collection("Resturants").document(currUserID).collection("Completed Orders").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<OrderDetails> list = queryDocumentSnapshots.toObjects(OrderDetails.class);
//                            casting the document into the order details class
                            LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
//                            setting up the recyclerview for the completed order so that it can be displayed into the recyclerview
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(manager);
                            CompeletOrdersAdapter adapter = new CompeletOrdersAdapter(list,getActivity());
                            recyclerView.setAdapter(adapter);
                        }
                    });
        }catch (Exception e){
        }
        return view;
    }
}