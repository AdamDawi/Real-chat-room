package com.example.distancecoupleapp.presentation.main_board

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.distancecoupleapp.data.FirebaseManager
import com.example.distancecoupleapp.data.Photo
import com.example.distancecoupleapp.data.User
import com.example.distancecoupleapp.presentation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class MainBoardViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()

    var mainBoardState by mutableStateOf(MainBoardState())
        private set

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
                        //adding all photos to list without the currently logged in user
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
    //check which user is current user
    fun checkRoomWithUser(): String{
        if(auth.currentUser!=null){
            return if(auth.currentUser!!.uid==mainBoardState.user1.id) mainBoardState.user2.username
            else mainBoardState.user1.username
        }
        return ""
    }

    fun getUsersFromDatabase(roomId: String) {
        val userList: ArrayList<User> = ArrayList()
        val postReference = FirebaseManager().getFirebaseDatabaseUserReference()
        //retrieve users ids from room id
        val usersId = roomId.split('_')

        val usersListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // for all users in database
                for (userSnapshot in dataSnapshot.children) {
                    // getting one user
                    var user = userSnapshot.getValue(User::class.java)
                    user = user?.copy(id = userSnapshot.key ?: "Error")

                    if (user != null && usersId.contains(user.id)) {
                        //adding to list
                        userList.add(user)
                    }
                }
                mainBoardState = mainBoardState.copy(user1 = userList[0])
                mainBoardState = mainBoardState.copy(user2 = userList[1])

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(usersListener)
    }

    fun navigateToCommentScreen(navController: NavController, roomId: String, photoId: String){
        navController.navigate(Screen.CommentsScreen.withArgs(roomId, photoId))
    }

    fun navigateToCameraScreen(navController: NavController, roomId: String){
        navController.navigate(Screen.CameraScreen.withArgs(roomId))
    }

    @SuppressLint("SimpleDateFormat")
    fun convertMillisToReadableDateTime(millis: Long): String {
        val currentDate = Date()
        val photoDate = Date(millis)

        //calculate difference between photo timestamp and current time
        val diffInMillis = currentDate.time - photoDate.time
        val diffInSeconds = diffInMillis / 1000
        val diffInMinutes = diffInSeconds / 60
        val diffInHours = diffInMinutes / 60
        val diffInDays = diffInHours / 24
        val diffInWeeks = diffInDays / 7
        val diffInMonths = diffInDays / 30

        return when {
            diffInMonths > 12 -> SimpleDateFormat("dd-MM-yyyy").format(photoDate)
            diffInMonths > 1 -> "$diffInMonths months ago"
            diffInMonths == 1L -> "$diffInMonths month ago"
            diffInWeeks > 1 -> "$diffInWeeks weeks ago"
            diffInWeeks == 1L -> "$diffInWeeks week ago"
            diffInDays > 1 -> "$diffInDays days ago"
            diffInDays == 1L -> "Yesterday"
            diffInHours > 1 -> "$diffInHours hours ago"
            diffInHours == 1L -> "$diffInHours hour ago"
            diffInMinutes > 1 -> "$diffInMinutes minutes ago"
            diffInMinutes == 1L -> "$diffInMinutes minute ago"
            else -> "Just now"
        }
    }

    fun getUserNameById(id: String): String{
        return if(mainBoardState.user1.id==id) mainBoardState.user1.username else mainBoardState.user2.username
    }



}