package com.example.nutritiontracker.camera

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner


// Camera code was references from Google Codelab: Getting Started with CameraX
//https://developer.android.com/codelabs/camerax-getting-started?authuser=6#1
class CameraController(private val activity: ComponentActivity) {

    private val cameraPermissions = arrayOf(Manifest.permission.CAMERA)
    private var pendingPreviewView: PreviewView? = null
    private var pendingLifecycleOwner: LifecycleOwner? = null

    private val activityResultLauncher: ActivityResultLauncher<Array<String>> =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in cameraPermissions && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    activity,
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val previewView = pendingPreviewView
                val lifecycleOwner = pendingLifecycleOwner
                if (previewView != null && lifecycleOwner != null){
                    startCamera(previewView, lifecycleOwner)
                }
            }
        }


    fun requestPermissions(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
        pendingPreviewView = previewView
        pendingLifecycleOwner = lifecycleOwner
        activityResultLauncher.launch(cameraPermissions)
    }

    fun allPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    //TODO: Connect Barcode scanner with
    fun startCamera(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {

        previewView.visibility = PreviewView.VISIBLE

        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(activity))
    }
    //TODO: Implement Barcode scanner from https://developers.google.com/ml-kit/vision/barcode-scanning/android

}

