package com.example.distancecoupleapp.presentation.search_user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distancecoupleapp.common.Constants
import com.example.distancecoupleapp.presentation.search_user.components.UserItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUserScreen(
    viewModel: SearchUserViewModel,
    navController: NavController
) {
    val state = viewModel.searchUserState
    
    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorScheme.background)
        .padding(8.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Text(
                    //take() because of long name
                    text = "Your nick: ${state.name.take(Constants.MAX_SIZE_OF_NAME_APPBAR)}",
                    color = colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = state.searchUserField,
                onValueChange = { viewModel.changeSearchUserFieldState(it)},
                modifier = Modifier
                    .border(BorderStroke(0.dp, Color.Transparent))
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.primary)
                    .height(52.dp)
                    .fillMaxWidth(),
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
            if(state.isLoading){
                Box(modifier = Modifier.fillMaxHeight()
                ){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(3.dp)
                            .align(Alignment.Center),
                        color = colorScheme.secondary
                    )
                }
            }
            else{
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .weight(10f)) {
                    items(
                        state.filteredUserList.size,
//                        key = {
//                            //increase scrolling efficiency
//                            state.filteredUserList[it].id
//                        }
                        ){
                        UserItem(viewModel = viewModel, state = state, it, navController)
                    }
                }
            }
        }
    }
}