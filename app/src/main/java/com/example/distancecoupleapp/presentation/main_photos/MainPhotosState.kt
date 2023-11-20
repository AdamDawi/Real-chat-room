package com.example.distancecoupleapp.presentation.main_photos

import com.example.distancecoupleapp.data.User

data class MainPhotosState(
    var name: String = "",
    var comment: String = "",
    var secondUserEmail: String = "",
    var userList: ArrayList<User> = ArrayList(),
    var emailSearch: String = "",
    var selectedUser: Int = 0
)