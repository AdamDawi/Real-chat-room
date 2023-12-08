package com.example.distancecoupleapp.presentation.main_board.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distancecoupleapp.presentation.main_board.MainBoardState
import com.example.distancecoupleapp.presentation.theme.Secondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarView(navController: NavController, state: MainBoardState) {

    CenterAlignedTopAppBar(title = {
        Text(text = "Room with: ${state.user2.username}",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            maxLines = 1
        ) },
        modifier = Modifier
            .padding(12.dp)
            .heightIn(max = 24.dp),
        colors = androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Secondary,
        ),
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Localized description",
                    tint = Secondary
                )
            }
        },
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Default.Person2,
                    contentDescription = "Localized description",
                    tint = Secondary
                )
            }
        }
    )
}