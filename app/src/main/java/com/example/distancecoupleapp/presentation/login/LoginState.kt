package com.example.distancecoupleapp.presentation.login

data class LoginState(
    var email: String ="",
    var password: String ="",
    var isLogged: Boolean = false,
    var isRegistering: Boolean = true,
    var name: String = "",
    //is loading is true when program checks if user is logged in
    var isLoading: Boolean = false,
    //for color animation between register and log in
    var registerTextAlpha: Float = 1.0f,
    var loginTextAlpha: Float = 0.2f,
)
