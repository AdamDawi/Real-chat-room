package com.example.distancecoupleapp.presentation.search_user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distancecoupleapp.presentation.search_user.components.UserItem
import java.time.format.TextStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUserScreen(
    viewModel: SearchUserViewModel,
    navigateToLoginScreen: () -> Unit,
    navigateToMainBoardScreen: NavController
) {
    val state = viewModel.searchUserState
    viewModel.getUsersFromDatabase()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(8.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { viewModel.signOut(navigateToLoginScreen) }) {
                Text(text = "SignOut",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary)
            }
        }
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Your nick: ${state.name}",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp)
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = state.searchUserField,
                onValueChange = { viewModel.changeSearchUserFieldState(it)},
                modifier = Modifier
                    .border(BorderStroke(0.dp, Color.Transparent))
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.primary)
                    .height(52.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = colorScheme.secondary,
                    cursorColor = colorScheme.secondary,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        Modifier.size(30.dp),
                        tint = colorScheme.secondary
                    )
                },
                placeholder = { Text("Search",
                    fontSize = 15.sp
                    ) },
            )

            LazyColumn(modifier = Modifier
                .fillMaxHeight()
                .weight(10f)) {
                items(state.filteredUserList.size){
                    UserItem(viewModel = viewModel, state = state, it, navigateToMainBoardScreen)
                }
            }

        }
    }
}