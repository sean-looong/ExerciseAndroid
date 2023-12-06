package com.seanlooong.exerciseandroid.ui.sensor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.seanlooong.exerciseandroid.databinding.ActivityCameraBinding

class CameraXActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater);
        setContentView(binding.root)
    }
}