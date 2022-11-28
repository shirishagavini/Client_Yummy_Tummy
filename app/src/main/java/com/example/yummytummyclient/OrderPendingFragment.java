package com.example.yummytummyclient;

import android.content.Intent;
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

import com.example.yummytummyclient.Adapter.IncompeleteOrderAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class OrderPendingFragment extends BaseFragment{

    public OrderPendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        this is the order pending fragment in which all the orders which has been placed by the customer get displayed to the vendor for the very first time for getting it completed
        view = inflater.inflate(R.layout.fragment_order_pending, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycelerView);
        AppCompatButton saveButton = view.findViewById(R.id.register_again_textView);
        try{
//          getting the current user ID of the restaurant
            currUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

//            getting the documents of the incomplete order from the firebase
            FirebaseFirestore.getInstance().collection("Resturants").document(currUserID).collection("InComplete Orders").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<OrderDetails> list = queryDocumentSnapshots.toObjects(OrderDetails.class);
//                            casting the incomplete order document into the order details class
                            LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
//                          setting up the recyclerview for the incomplete orders to be displayed into the recyclerview of the order pending fragment
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(manager);
                            IncompeleteOrderAdapter adapter = new IncompeleteOrderAdapter(list,getActivity());
                            recyclerView.setAdapter(adapter);
//                          setting up the save button so that the orders from the pending section get transferred to the order completed section
                            saveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final int[] total = new int[1];
                                    for(int i=0;i<list.size();i++){
                                        OrderDetails currEle = list.get(i);
//                                        getting the list of Orders which are mark as checked
                                        if(currEle.getIsComepleted()){
                                            queryDocumentSnapshots.getDocuments().get(i).getReference().delete();
                                            FirebaseFirestore.getInstance().collection("Resturants").document(currUserID).collection("Completed Orders")
                                                    .add(currEle);
//                                            adding the orders into the completed section in the firebase
                                            FirebaseFirestore.getInstance().collection("CustomerDetails").document(currEle.getCurrUserId()).collection("Orders Compeleted")
                                                    .add(currEle);
//                                            adding the orders in to the customers database in the completed section
                                            total[0] = currEle.getPrice()* currEle.getQuantity();
                                        }
                                    }
//                                    getting the restaurant id
                                    currUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    FirebaseFirestore.getInstance().collection("Resturants").document(currUserID).collection("Details")
                                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            total[0] = total[0] + Integer.parseInt(queryDocumentSnapshots.toObjects(ResturantDetails.class).get(0).getWalletBalance());
                                            queryDocumentSnapshots.getDocuments().get(0).getReference().update("walletBalance",""+total[0]).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Fragment Home = new OrderPendingFragment();
                                                    FragmentManager manager = getActivity().getSupportFragmentManager();
                                                    FragmentTransaction transaction = manager.beginTransaction();
                                                    transaction.replace(R.id.framelayout,Home);
                                                    transaction.commit();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
        }catch (Exception e){
        }
        return view;
    }
}