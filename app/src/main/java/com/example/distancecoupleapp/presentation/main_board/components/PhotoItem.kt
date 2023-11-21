package com.example.distancecoupleapp.presentation.main_board.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.distancecoupleapp.presentation.main_board.MainBoardState
import com.example.distancecoupleapp.presentation.main_board.MainBoardViewModel

@Composable
fun PhotoItem(
    viewModel: MainBoardViewModel,
    state:MainBoardState,
    it: Int,
    roomId:String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .height(40.dp)
                    .aspectRatio(1f)
            )
            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Text(text = state.photoList[it].owner)
                Text(text = viewModel.convertMillisToDateTime(state.photoList[it].timestamp).toString())
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
        ){
            Image(painter = rememberAsyncImagePainter("https://www.themealdb.com//images//media//meals//xqrwyr1511133646.jpg"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp)))
        }
        Text(text = state.photoList[it].description)
        Text(text = "Add a comment...",
            color = Color.LightGray,
            modifier = Modifier.clickable {
                viewModel.addComment(state.photoList[it].id, "nice", roomId)
            })
        Spacer(modifier = Modifier.height(12.dp))
    }
}