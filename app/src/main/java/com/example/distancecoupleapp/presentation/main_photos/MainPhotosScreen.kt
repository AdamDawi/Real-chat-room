package com.example.distancecoupleapp.presentation.main_photos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPhotosScreen(
    viewModel: MainPhotosViewModel,
    navigateToLoginScreen: () -> Unit
) {
    val state = viewModel.mainPhotosState
    viewModel.getUserName()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { viewModel.signOut(navigateToLoginScreen) }) {
                Text(text = "SignOut")
            }
        }
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = state.name)
            OutlinedTextField(value = state.secondUserEmail, onValueChange = { viewModel.changeSecondUserEmail(it)})
            Button(onClick = { viewModel.connectWithUser()}) {
                Text(text = "Connect with user")
            }
        }
    }
}