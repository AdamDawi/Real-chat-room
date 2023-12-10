package com.example.distancecoupleapp.presentation.search_user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.distancecoupleapp.presentation.search_user.SearchUserState
import com.example.distancecoupleapp.presentation.search_user.SearchUserViewModel
import com.example.distancecoupleapp.presentation.theme.Grey

@Composable
fun UserItem(
    viewModel: SearchUserViewModel,
    state: SearchUserState,
    index: Int,
    navController: NavController
) {
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier
        .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountCircle,
                    contentDescription = "User icon",
                    Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(3.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(text = state.filteredUserList[index].email,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = state.filteredUserList[index].username,
                        color = Grey
                    )
                }
            }

            Button(onClick = { viewModel.connectWithUser(navController, index)}
                ) {
                Text(text = "Connect",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }


        }
    }
}