package com.example.distancecoupleapp.presentation.search_user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.distancecoupleapp.presentation.search_user.SearchUserState
import com.example.distancecoupleapp.presentation.search_user.SearchUserViewModel

@Composable
fun UserItem(
    viewModel: SearchUserViewModel,
    state: SearchUserState,
    it: Int
) {
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .background(
            if (state.selectedUser == it) Color.Green
            else Color.DarkGray
        )
        .clickable {
            viewModel.changeSelectedUserState(it)
        }
        .padding(10.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)){
            Icon(Icons.Default.AccountCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = state.filteredUserList[it].email)
            Spacer(modifier = Modifier.width(2.dp))
            Text(text = "(${state.filteredUserList[it].username})")
        }
    }
}