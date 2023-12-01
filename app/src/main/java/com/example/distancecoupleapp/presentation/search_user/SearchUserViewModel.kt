package com.example.distancecoupleapp.presentation.search_user

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.distancecoupleapp.data.FirebaseManager
import com.example.distancecoupleapp.data.User
import com.example.distancecoupleapp.presentation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SearchUserViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseManager().getFirebaseAuth()
    private var roomId: String? = null

    var searchUserState by mutableStateOf(SearchUserState())
        private set

    init {
        getUserName()
    }

    private fun getUserName() {
        if(auth.currentUser?.displayName!=null)
            searchUserState = searchUserState.copy(name = auth.currentUser?.displayName.toString())
    }

    fun changeSearchUserFieldState(newEmail: String){
        searchUserState = searchUserState.copy(searchUserField = newEmail)

        searchUserState = searchUserState.copy(filteredUserList = searchUserState.userList.filter {
            it.email.contains(newEmail, ignoreCase = true) || it.username.contains(newEmail, ignoreCase = true)
        } as ArrayList<User>)

    }

    fun changeSelectedUserState(selected: Int){
        searchUserState = searchUserState.copy(selectedUser = selected)
    }

    fun connectWithUser(navigateToMainBoardScreen: NavController){
        if(auth.currentUser!= null && auth.currentUser!!.uid!=searchUserState.userList[searchUserState.selectedUser].id){
            roomId = getChatroomId(auth.currentUser!!.uid, searchUserState.userList[searchUserState.selectedUser].id)

            //navigate to main board screen with roomId
            navigateToMainBoardScreen.navigate(Screen.MainBoardScreen.withArgs(roomId?:"Error"))

        }else if(auth.currentUser!!.uid==searchUserState.userList[searchUserState.selectedUser].id)
            Log.e("Connecting with user error", "Current user is you")
        else
            Log.e("Connecting with user error", "Current user is null")
    }

    private fun getChatroomId(userId1: String, userId2: String): String{
        // Compare user ID hashes to determine their order.
        if(userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2
        }
        return userId2+"_"+userId1
    }

    fun signOut(navigateToLoginScreen: () -> Unit){
        auth.signOut()
        navigateToLoginScreen()
    }

    fun getUsersFromDatabase() {
        val userList: ArrayList<User> = ArrayList()
        val postReference = FirebaseManager().getFirebaseDatabaseUserReference()

        val usersListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // for all users in database
                for (userSnapshot in dataSnapshot.children) {
                    // getting one user
                    var user = userSnapshot.getValue(User::class.java)
                    user = user?.copy(id = userSnapshot.key ?: "Error")

                    if (user != null && user.id!=auth.currentUser?.uid) {
                        //adding to list
                        userList.add(user)
                    }
                }
                //when its first time getting users list, filtered users list must load for the first time
                if(searchUserState.userList.isEmpty()){
                    searchUserState = searchUserState.copy(filteredUserList = userList)
                }
                searchUserState = searchUserState.copy(userList = userList)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        postReference.addValueEventListener(usersListener)
    }



}