package com.example.distancecoupleapp.presentation.login

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel,
                context: Context
                ) {
    val state = viewModel.loginState

    //if(!state.isLogged){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(value = state.email,
                onValueChange = {
                    viewModel.changeEmailState(it)
                },
                label = {
                    Text("Email")
                })

            OutlinedTextField(value = state.password,
                onValueChange ={
                    viewModel.changePasswordState(it)
                },
                visualTransformation = PasswordVisualTransformation(),
                label = {
                    Text("Password")
                })

            Button(onClick = {viewModel.createAccount(context)}) {
                Text("Register")
            }
        }
    //}

}
