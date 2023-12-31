package com.example.distancecoupleapp.presentation.search_user

import com.example.distancecoupleapp.data.User

data class SearchUserState(
    //logged user name
    var name: String = "",
    //text from search field on the screen
    var searchUserField: String = "",
    //stores user list from database
    var userList: ArrayList<User> = ArrayList(),
    //stores filtered user list which is displayed on the screen
    var filteredUserList: ArrayList<User> = ArrayList(),
    //stores state if list loading
    var isLoading: Boolean = false
)