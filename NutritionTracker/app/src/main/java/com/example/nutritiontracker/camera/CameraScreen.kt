package com.example.nutritiontracker.camera

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

//CameraX with Jetpack Compose was referenced from
//https://medium.com/androiddevelopers/getting-started-with-camerax-in-jetpack-compose-781c722ca0c4
//https://christianstowers.medium.com/android-jetpack-compose-camerax-2b7c996474b0
@Composable
fun CameraScreen(cameraController: CameraController){
    val lifecycle: LifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = { previewView }
    )
    if (cameraController.allPermissionsGranted())
        cameraController.startCamera(
            previewView,
            lifecycle)
    else cameraController.requestPermissions(
        previewView,
        lifecycle)
}
