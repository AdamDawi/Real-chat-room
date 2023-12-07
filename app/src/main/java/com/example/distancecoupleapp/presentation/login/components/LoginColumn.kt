package com.example.distancecoupleapp.presentation.login.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.distancecoupleapp.presentation.login.LoginState
import com.example.distancecoupleapp.presentation.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginColumn(
    viewModel: LoginViewModel,
    state: LoginState,
    context: Context
) {
    OutlinedTextField(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        value = state.password,
        onValueChange ={
            viewModel.changePasswordState(it)
        },
        visualTransformation = PasswordVisualTransformation(),
        label = {
            Text("Password")
        },

        )
    Button(
        modifier = Modifier.padding(top = 8.dp),
        onClick = {
            viewModel.signIn(context) //navigate to next screen if sign in is success
        }) {
        Text("Log in",
            color = MaterialTheme.colorScheme.secondary)
    }
}