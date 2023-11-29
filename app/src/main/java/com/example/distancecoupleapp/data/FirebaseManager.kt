package com.example.distancecoupleapp.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage

class FirebaseManager {

    fun getFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    fun getFirebaseDatabaseReference(): DatabaseReference{
        return FirebaseDatabase.getInstance("https://distance-couple-app-default-rtdb.europe-west1.firebasedatabase.app/").reference
    }

    fun getFirebaseStoreReference(): StorageReference {
        return Firebase.storage.reference
    }

    //getting path to all users in database
    fun getFirebaseDatabaseUserReference(): DatabaseReference{
        return FirebaseDatabase.getInstance("https://distance-couple-app-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
    }
    //getting path in database to photos in particular room
    fun getFirebaseDatabasePhotosReference(roomId: String): DatabaseReference{
        return FirebaseDatabase.getInstance("https://distance-couple-app-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("rooms").child(roomId).child("photos")
    }
    //getting path in database to comments in particular photo
    fun getFirebaseDatabaseCommentsReference(roomId: String, photoId: String): DatabaseReference{
        return FirebaseDatabase.getInstance("https://distance-couple-app-default-rtdb.europe-west1.firebasedatabase.app/").reference.child("rooms").child(roomId).child("photos").child(photoId).child("comments")
    }

}