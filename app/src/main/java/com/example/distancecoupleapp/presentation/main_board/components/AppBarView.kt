package com.example.distancecoupleapp.presentation.main_board.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distancecoupleapp.common.Constants
import com.example.distancecoupleapp.presentation.Screen
import com.example.distancecoupleapp.presentation.main_board.MainBoardViewModel
import com.example.distancecoupleapp.presentation.theme.Secondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarView(navController: NavController, viewModel: MainBoardViewModel) {

    CenterAlignedTopAppBar(title = {
        //take() because of long name
        Text(text = "Room with: ${viewModel.checkRoomWithUser().take(Constants.MAX_SIZE_OF_NAME_APPBAR)}",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        ) },
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .heightIn(max = 34.dp)
            .height(34.dp),
        colors = androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Secondary,
        ),
        navigationIcon = {
            IconButton(onClick = {
                    viewModel.backButton(navController) }) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Go to search user screen",
                    tint = Secondary,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(Screen.UserScreen.route) }) {
                Icon(
                    imageVector = Icons.Default.Person2,
                    contentDescription = "Go to account screen",
                    tint = Secondary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    )
}