package com.example.distancecoupleapp.presentation.login

data class LoginState(
    var email: String ="",
    var password: String ="",
    var isLogged: Boolean = false
)
