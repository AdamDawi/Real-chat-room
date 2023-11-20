package com.example.distancecoupleapp.presentation.main_board

import androidx.lifecycle.ViewModel
import com.example.distancecoupleapp.data.Comment
import com.example.distancecoupleapp.data.FirebaseManager
import com.example.distancecoupleapp.data.Photo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class MainBoardViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()
    private val database: DatabaseReference = FirebaseManager().getFirebaseDatabaseReference()


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
        }
    }

    fun addPhoto(imageUrl: String, description: String, roomId: String) {
        val currentUser = auth.currentUser
        val ownerId = currentUser?.uid

        if (ownerId != null) {
            val photoId = database.child("photos").push().key

            val photo = Photo(imageUrl, ownerId, description,  emptyMap())
            val photoValues = photo.toMap()

            val childUpdates = HashMap<String, Any>()
            childUpdates["/rooms/$roomId/photos/$photoId"] = photoValues

            database.updateChildren(childUpdates)
        }
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
            "comments" to comments,
        )
    }
}