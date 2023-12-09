package com.example.distancecoupleapp.presentation.login

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.distancecoupleapp.presentation.login.components.LoginColumn
import com.example.distancecoupleapp.presentation.login.components.RegisterColumn
import com.example.distancecoupleapp.presentation.theme.Primary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel,
                context: Context,
                navigateToMainBoardScreen: () -> Unit,
                navController: NavController
) {
    val state = viewModel.loginState

    if(state.isLogged)
    {
        //pop login screen because we don't need that when person is log in
        navController.popBackStack()
        //anytime user is logged navigate to main photos screen
        navigateToMainBoardScreen()
        //prevents from navigating to the main photos screen again
        viewModel.changeIsLoggedState(false)
    }
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
                    color = if(state.isRegistering) MaterialTheme.colorScheme.secondary else Primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(10.dp))

                Box(modifier = Modifier
                    .height(22.dp)
                    .width(2.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = "Log in",
                    Modifier.clickable { viewModel.changeIsRegisteringState(false)},
                    color = if(state.isRegistering) Primary else MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            }
            if(state.isLoading){
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
            }else {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(value = state.email,
                        onValueChange = {
                            viewModel.changeEmailState(it)
                        },
                        modifier = Modifier
                            .border(BorderStroke(0.dp, Color.Transparent))
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .fillMaxWidth(),
                        placeholder = { Text("Email") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.secondary,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                    if (state.isRegistering) {
                        RegisterColumn(viewModel = viewModel, state = state, context = context)
                    } else {
                            LoginColumn(viewModel = viewModel, state = state, context = context)
                    }
                }
            }


    }



}
