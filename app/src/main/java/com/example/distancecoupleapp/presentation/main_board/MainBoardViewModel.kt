package com.example.distancecoupleapp.presentation.main_board

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.distancecoupleapp.data.Comment
import com.example.distancecoupleapp.data.FirebaseManager
import com.example.distancecoupleapp.data.Photo
import com.example.distancecoupleapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.Date

class MainBoardViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()
    private val database: DatabaseReference = FirebaseManager().getFirebaseDatabaseReference()

    var mainBoardState by mutableStateOf(MainBoardState())
        private set

    fun addComment(photoId: String, text: String, roomId: String) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val commentId = database.child("comments").push().key

            val comment = Comment(userId, text, System.currentTimeMillis())
            val commentValues = comment.toMap()

            val childUpdates = HashMap<String, Any>()
            childUpdates["/rooms/$roomId/photos/$photoId/comments/$commentId"] = commentValues

            database.updateChildren(childUpdates)
        }else{
            Log.e("Add comment Error", "current user is null")
        }
    }

    fun getPhotosFromDatabase(roomId: String) {
        val photoList: ArrayList<Photo> = ArrayList()
        val postReference = FirebaseManager().getFirebaseDatabasePhotosReference(roomId)

        val photosListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // for all photos in particular room
                for (photoSnapshot in dataSnapshot.children) {
                    // getting one user
                    var photo = photoSnapshot.getValue(Photo::class.java)
                    photo = photo?.copy(id = photoSnapshot.key ?: "Error")

                    if (photo != null && photo.id!=auth.currentUser?.uid) {
                        //adding to list
                        photoList.add(photo)
                    }
                }
                mainBoardState = mainBoardState.copy(photoList = photoList)

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(photosListener)
    }

    fun addPhoto(imageUrl: String, description: String, roomId: String) {
        val currentUser = auth.currentUser
        val ownerId = currentUser?.uid

        if (ownerId != null) {
            val photoId = database.child("photos").push().key

            val photo = Photo(imageUrl, ownerId, description, photoId?:"Error", System.currentTimeMillis())
            val photoValues = photo.toMap()

            val childUpdates = HashMap<String, Any>()
            childUpdates["/rooms/$roomId/photos/$photoId"] = photoValues

            database.updateChildren(childUpdates)
        }
        getPhotosFromDatabase(roomId)
    }

    fun convertMillisToDateTime(millis: Long): Date {
        return Date(millis)
    }

    private fun Comment.toMap(): Map<String, Any> {
        return mapOf(
            "user" to user,
            "text" to text,
            "timestamp" to timestamp
        )
    }

    private fun Photo.toMap(): Map<String, Any> {
        return mapOf(
            "imageUrl" to imageUrl,
            "owner" to owner,
            "description" to description,
            "id" to id,
            "timestamp" to timestamp
        )
    }
}