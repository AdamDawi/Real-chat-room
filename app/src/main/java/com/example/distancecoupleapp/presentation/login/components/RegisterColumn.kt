package com.example.distancecoupleapp.presentation.login.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.distancecoupleapp.presentation.login.LoginState
import com.example.distancecoupleapp.presentation.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterColumn(
    viewModel: LoginViewModel,
    state: LoginState,
    context: Context
) {
    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(value = state.name,
        modifier = Modifier
            .border(BorderStroke(0.dp, Color.Transparent))
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth(),
        onValueChange ={
            viewModel.changeNameState(it)
        },
        placeholder = { Text("Username") },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.secondary,
            cursorColor = MaterialTheme.colorScheme.secondary,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Go
        ),
        keyboardActions = KeyboardActions(
            onGo = {
                viewModel.createAccount(context) //navigate to next screen if sign in is success
            }
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
    OutlinedTextField(value = state.password,
        modifier = Modifier
            .border(BorderStroke(0.dp, Color.Transparent))
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth(),
        onValueChange ={
            viewModel.changePasswordState(it)
        },
        visualTransformation = PasswordVisualTransformation(),
        placeholder = { Text("Password") },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.secondary,
            cursorColor = MaterialTheme.colorScheme.secondary,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Go
        ),
        keyboardActions = KeyboardActions(
            onGo = {
                viewModel.createAccount(context) //navigate to next screen if sign in is success
            }
        )
        )

    Button(
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxWidth()
            .height(52.dp),
        onClick = {
            viewModel.createAccount(context)
        }
    ) {
        Text("Register",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 16.sp)
    }
}