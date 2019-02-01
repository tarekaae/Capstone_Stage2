package com.tarek.nanodegree.capstone.fishing.model;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tarek.nanodegree.capstone.fishing.model.pojo.Spot;

import java.util.ArrayList;


/**
 * Created by tarek.abdulkader on 3/4/2018.
 */

public class FirebaseHandler {

    private static FirebaseHandler ourInstance;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private static final String CHILD = "spots";

    public static FirebaseHandler getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new FirebaseHandler(context);
        }
        return ourInstance;
    }

    private FirebaseHandler(Context context) {


        mFirebaseDatabase = FirebaseDatabase.getInstance();
         mDatabaseReference = mFirebaseDatabase.getReference().child(CHILD);
    }

    public void saveTOFirebase(Spot spot) {
        mDatabaseReference.child(spot.getId()+"").setValue(spot);
    }



    public DatabaseReference getDatabaseReference() {
        return mDatabaseReference;
    }
}
