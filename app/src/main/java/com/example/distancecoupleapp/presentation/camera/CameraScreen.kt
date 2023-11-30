package com.example.distancecoupleapp.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.distancecoupleapp.presentation.camera.components.CameraPreview

@Composable
fun CameraScreen(viewModel: CameraViewModel,
    context: Context,
    navController: NavController,
    roomId: String
) {
        //controller must be in composable
        val controller = remember {
            LifecycleCameraController(context).apply {
                setEnabledUseCases(
                    CameraController.IMAGE_CAPTURE
                )
            }
        }

        val state = viewModel.cameraState
        viewModel.changeIconState(controller)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Black)
                .padding(bottom = 150.dp, top = 50.dp)
                .clip(RoundedCornerShape(19.dp))

        ) {
            if(!state.isLoading)
            CameraPreview(
                controller = controller,
                modifier = Modifier
                    .fillMaxSize()
            )else{
                //temporary screen view, which is a photo taken when the button was clicked
                Image(
                    bitmap = state.image?.asImageBitmap()?:Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888).asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                )
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(3.dp)
                        .align(Alignment.Center)
                )
            }
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom,
    ) {

        IconButton(
            onClick = {
                viewModel.changeFlashMode(controller)
            },
            modifier = Modifier
                .size(90.dp)
                .padding(end = 20.dp)
        ) {
            Icon(
                imageVector = state.imageVector?: Icons.Default.FlashOff,
                contentDescription = "Switch flash",
                modifier = Modifier.size(40.dp),
            )
        }

        Box(modifier = Modifier
            .border(7.dp, Color.White, CircleShape)
            .size(90.dp)
            .clickable {
                viewModel.takePhoto(
                    controller = controller,
                    context,
                    roomId,
                    navController
                )
            }
        )

        IconButton(
            onClick = {
                viewModel.changeCameraMode(controller)
            },
            modifier = Modifier
                .size(90.dp)
                .padding(start = 20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Cameraswitch,
                contentDescription = "Switch camera",
                modifier = Modifier.size(40.dp),
            )
        }
    }
}