package com.example.distancecoupleapp.presentation.login

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.distancecoupleapp.presentation.login.components.LoginColumn
import com.example.distancecoupleapp.presentation.login.components.RegisterColumn


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel,
                context: Context,
                navigateToMainPhotosScreen: () -> Unit
) {
    val state = viewModel.loginState

    if(state.isLogged)
    {
        //anytime user is logged navigate to main photos screen
        navigateToMainPhotosScreen()
        //prevents from navigating to the main photos screen again
        viewModel.changeIsLoggedState(false)
        viewModel.changeIsLoadingState(false)
    }

    if(state.isLoading){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Box(modifier = Modifier.fillMaxHeight()
            ){
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(3.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Register",
                    Modifier.clickable { viewModel.changeIsRegisteringState(true)},
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(10.dp))

                Box(modifier = Modifier
                    .height(20.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = "Log in",
                    Modifier.clickable { viewModel.changeIsRegisteringState(false)},
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Column(modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(value = state.email,
                    onValueChange = {
                        viewModel.changeEmailState(it)
                    },
                    label = {
                        Text("Email")
                    })

                if(state.isRegistering){
                    RegisterColumn(viewModel = viewModel, state = state, context = context)
                }else {
                    LoginColumn(viewModel = viewModel, state = state, context = context)
                }
            }

        }
    }



}
