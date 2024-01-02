package com.example.distancecoupleapp.presentation.main_board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distancecoupleapp.presentation.main_board.components.AppBarView
import com.example.distancecoupleapp.presentation.main_board.components.PhotoItem
import com.example.distancecoupleapp.presentation.theme.Secondary

@Composable
fun MainBoardScreen(
    viewModel: MainBoardViewModel,
    navController: NavController,
    roomId: String
) {
    val state = viewModel.mainBoardState
    viewModel.getUsersFromDatabase(roomId)
    viewModel.getPhotosFromDatabase(roomId)

    Box(modifier = Modifier.fillMaxSize()
    ) {
        if(state.photoList.size>0){
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(10f)
                ) {
                    item { Spacer(modifier = Modifier.height(40.dp)) }
                    items(state.photoList.size,
                        //increase lazy column efficiency
                        key = {state.photoList[state.photoList.size-1-it].id}
                    ) { index->
                        //first index in photo item is the last from photo list
                        PhotoItem(viewModel = viewModel, state = state, state.photoList.size-1-index, roomId, navController)
                    }
                }

            }
        }else if(state.isLoadingUsers || state.isLoadingPhotos){
            CircularProgressIndicator(
                modifier = Modifier
                    .size(40.dp)
                    .padding(3.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary
            )
        } else
        {
            Text(text = "No posts to display",
                modifier = Modifier.align(Alignment.Center),
                color = Secondary,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
        }
            AppBarView(navController, viewModel)
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            Box(modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
                .border(7.dp, Color.White, CircleShape)
                .clickable {
                    viewModel.navigateToCameraScreen(navController, roomId)
                }
                //for custom ripple effect u must clip and set border shape
                .indication(
                    interactionSource = MutableInteractionSource(),
                    rememberRipple(radius = 50.dp)
                )
                .padding(bottom = 1.dp)
            )
            Spacer(modifier = Modifier
                .height(5.dp))
        }


    }
}