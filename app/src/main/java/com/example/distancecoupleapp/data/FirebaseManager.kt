package com.example.distancecoupleapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseManager {

    fun currentUserId(): String?{
        return FirebaseAuth.getInstance().uid
    }
    fun getFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    fun getFirebaseDatabaseReference(): DatabaseReference{
        return FirebaseDatabase.getInstance("https://distance-couple-app-default-rtdb.europe-west1.firebasedatabase.app/").reference
    }

    //getting path to all users
    fun getFirebaseDatabaseUserReference(): DatabaseReference{
        return FirebaseDatabase.getInstance("https://distance-couple-app-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
    }
    //getting path in database to photos in particular room
    fun getFirebaseDatabasePhotosReference(roomId: String): DatabaseReference{
        return FirebaseDatabase.getInstance("https://distance-couple-app-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("rooms").child(roomId).child("photos")
    }
}