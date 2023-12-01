package com.example.distancecoupleapp.presentation.search_user

import com.example.distancecoupleapp.data.User

data class SearchUserState(
    var name: String = "",
    var comment: String = "",
    var searchUserField: String = "",
    var userList: ArrayList<User> = ArrayList(),
    var emailSearch: String = "",
    var selectedUser: Int = 0,
    var filteredUserList: ArrayList<User> = ArrayList(),
)