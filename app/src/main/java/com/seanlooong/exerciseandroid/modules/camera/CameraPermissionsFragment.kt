package com.seanlooong.exerciseandroid.modules.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.seanlooong.exerciseandroid.R
import kotlinx.coroutines.launch

private var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

class CameraPermissionsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // add the storage access permission request for Android 9 and below.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val permissionList = PERMISSIONS_REQUIRED.toMutableList()
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            PERMISSIONS_REQUIRED = permissionList.toTypedArray()
        }

        if (!hasPermissions(requireContext())) {
            // Request camera-related permissions
            activityResultLauncher.launch(PERMISSIONS_REQUIRED)
        } else {
            navigateToCamera()
        }
    }

    private fun navigateToCamera() {
        lifecycleScope.launch {
            var isComplete = false
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (!isComplete) {
                    try {
                        Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                            CameraPermissionsFragmentDirections.actionPermissionToCamera())
                    } finally {
                        isComplete = true
                    }
                }
            }
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in PERMISSIONS_REQUIRED && !it.value) {
                    permissionGranted = false
                }
            }
            if (!permissionGranted) {
                Toast.makeText(context, "Permission request denied", Toast.LENGTH_LONG).show()
            } else {
                navigateToCamera()
            }
        }

    companion object {
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}