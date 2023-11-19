package com.example.distancecoupleapp.common

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference

object FirebaseUtil {
    fun getFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    fun getFirebaseDatabase(): FirebaseDatabase{
        return FirebaseDatabase.getInstance("https://distance-couple-app-default-rtdb.europe-west1.firebasedatabase.app/")
    }

    fun getChatReference(roomId: String, photoId: String): String {
        return "/rooms/$roomId/photos/$photoId/comments/"
    }
}