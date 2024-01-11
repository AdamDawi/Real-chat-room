package com.example.distancecoupleapp.presentation.search_user.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
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
            val painter = rememberAsyncImagePainter(state.filteredUserList[index].picture)

            Row(verticalAlignment = Alignment.CenterVertically) {
                if(state.filteredUserList[index].picture.isNotEmpty()) {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .height(50.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .padding(start = 5.dp)
                    ){
                        //display circular progress indicator when image is loading or an error occurs
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(1f)
                                .clip(CircleShape),
                            painter = painter,
                            contentDescription = "profile picture",
                            contentScale = ContentScale.Crop
                        )
                        if (painter.state is AsyncImagePainter.State.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(25.dp)
                                    .align(Alignment.Center),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }
                else if(painter.state is AsyncImagePainter.State.Error || state.filteredUserList[index].picture.isEmpty()){
                    Icon(Icons.Default.AccountCircle,
                        contentDescription = "Account icon",
                        modifier = Modifier
                            .fillMaxHeight()
                            .height(55.dp)
                            .aspectRatio(1f),

                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Spacer(modifier = Modifier.width(3.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(text = state.filteredUserList[index].email,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(150.dp)
                    )
                    Text(text = state.filteredUserList[index].username,
                        color = Grey,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(150.dp)
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