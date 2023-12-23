package com.example.distancecoupleapp.presentation.comments.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.distancecoupleapp.common.Constants
import com.example.distancecoupleapp.presentation.comments.CommentsState
import com.example.distancecoupleapp.presentation.comments.CommentsViewModel

@Composable
fun CommentItem(
    viewModel: CommentsViewModel,
    state: CommentsState,
    it: Int
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        //image state
        val painter = rememberAsyncImagePainter(viewModel.getUsersProfilesPictures(state.commentsList[it].user))
        if(viewModel.getUsersProfilesPictures(state.commentsList[it].user).isNotEmpty()) {
            Box(modifier = Modifier
                .fillMaxHeight()
                .height(40.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
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
                            .size(30.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Spacer(modifier = Modifier.width(5.dp))
        }
        else if(painter.state is AsyncImagePainter.State.Error || viewModel.getUsersProfilesPictures(state.commentsList[it].user).isEmpty()){
            Icon(Icons.Default.AccountCircle,
                contentDescription = "Account icon",
                modifier = Modifier
                    .fillMaxHeight()
                    .height(45.dp)
                    .aspectRatio(1f),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.width(1.dp))
        }
        Column(verticalArrangement = Arrangement.Center) {
            Row{
                Text(
                    //take() because of long name
                    text = viewModel.getUserNameById(state.commentsList[it].user).take(Constants.MAX_SIZE_OF_NAME_ITEM),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = viewModel.convertMillisToReadableDateTime(state.commentsList[it].timestamp),
                    color = Gray,
                    maxLines = 1
                )
            }
            Text(text = state.commentsList[it].text,
                color = MaterialTheme.colorScheme.secondary
            )
        }

    }

    }
