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

    fun getFirebaseDatabaseUserReference(): DatabaseReference{
        return FirebaseDatabase.getInstance("https://distance-couple-app-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
    }
}