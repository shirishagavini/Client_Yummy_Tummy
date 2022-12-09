package com.example.yummytummyclient;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;

public class AddItemFragment extends BaseFragment
{

    public AddItemFragment() {

    }

    private final int PICK_IMAGE_REQUEST = 25;
    private Uri filePath = null;
    private ImageView Image;
    private TextView itemIdTextView,itemPriceTextView,itemNameTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_add_item, container, false);

         itemIdTextView = view.findViewById(R.id.namedittext);
         itemNameTextView = view.findViewById(R.id.phoneEdittext);
         itemPriceTextView = view.findViewById(R.id.pincodeEdittext);
         AppCompatButton addButton = view.findViewById(R.id.submitButton);
         AppCompatButton uploadPicButton = view.findViewById(R.id.addPicButton);
         Image = view.findViewById(R.id.foodImageView);

        uploadPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!itemIdTextView.getText().toString().isEmpty() && !itemNameTextView.getText().toString().isEmpty() && !itemPriceTextView.getText().toString().isEmpty() ){
                    uploadImage();
                }
            }
        });
         return view;
    }


    private void uploadImage()
    {
        if (filePath != null) {


            ProgressDialog progressDialog
                    = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();



            StorageReference ref =  FirebaseStorage.getInstance().getReference()
                    .child("Product Images")
                    .child(itemIdTextView.getText().toString()+System.currentTimeMillis()+".jpeg");


            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    getProfileImageUrl(ref);

                                    progressDialog.dismiss();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

//                            // Error, Image not uploaded
//                            progressDialog.dismiss();
//                            Toast
//                                    .makeText(MainActivity.this,
//                                            "Failed " + e.getMessage(),
//                                            Toast.LENGTH_SHORT)
//                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }else{
            Toast.makeText(getContext(), "Please add a picture", Toast.LENGTH_SHORT).show();
        }
    }


    protected void getProfileImageUrl(StorageReference reference){
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e("manan","Stage 5");
                        String FoodUrl = uri.toString();

                        currUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseFirestore.getInstance().collection("Resturants").document(currUserID).collection("Pincode")
                                //                           getting the pincode in which the resturant is serving
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        //                          Casting the documents into PincodeClass.class
                                        List<PincodeClass> pincodeClass = queryDocumentSnapshots.toObjects(PincodeClass.class);
                                        for(int i=0;i<pincodeClass.size();i++){
                                            String currPincode = pincodeClass.get(i).getPincode();

                                            FirebaseFirestore.getInstance().collection("Pincode").document(currPincode).collection("Menu").document(currUserID).collection(currUserID)
                                                    //                                       adding the menu item into the firebase of the desired the resturant
                                                    .add(new MenuItemClass(itemNameTextView.getText().toString(),itemIdTextView.getText().toString(),Integer.parseInt(itemPriceTextView.getText().toString()),FoodUrl))
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            //                                              Starting the menufragment to refresh the menu item.
                                                            Fragment menu = new MyMenuFragment();
                                                            FragmentManager manager = getActivity().getSupportFragmentManager();
                                                            FragmentTransaction transaction = manager.beginTransaction();
                                                            transaction.replace(R.id.framelayout, menu);
                                                            transaction.commit();
                                                        }
                                                    });

                                        }
                                    }
                                });

                        Log.e("status manan",uri.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),filePath);
                Image.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}