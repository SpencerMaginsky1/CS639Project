package com.example.nutritiontracker

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat



// Camera code was references from Google Codelab: Getting Started with CameraX
//https://developer.android.com/codelabs/camerax-getting-started?authuser=6#1
class MainActivity : ComponentActivity() {

    // TODO: Factor out camera code into its own class
    private val cameraPermissions = arrayOf(Manifest.permission.CAMERA)

    private lateinit var cameraView: PreviewView
    private lateinit var upcPlaceholder: TextView
    private lateinit var scanBarcodeButton: Button
    private lateinit var barcodeBox: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraView = findViewById(R.id.previewView)
        upcPlaceholder = findViewById(R.id.upcTextView)
        scanBarcodeButton = findViewById(R.id.scanBarcodeButton)
        barcodeBox = findViewById(R.id.barcodeBox)

        cameraView.visibility = PreviewView.GONE
        upcPlaceholder.visibility = TextView.GONE
        barcodeBox.visibility = TextView.GONE

        scanBarcodeButton.setOnClickListener {
            if(allPermissionsGranted()) startCamera()
            else requestPermissions()
        }

    }

    private fun requestPermissions() {
        activityResultLauncher.launch(cameraPermissions)
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in cameraPermissions && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }

    private fun allPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        scanBarcodeButton.visibility = Button.GONE
        cameraView.visibility = PreviewView.VISIBLE
        upcPlaceholder.visibility = TextView.VISIBLE
        barcodeBox.visibility = TextView.VISIBLE

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(cameraView.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

}
