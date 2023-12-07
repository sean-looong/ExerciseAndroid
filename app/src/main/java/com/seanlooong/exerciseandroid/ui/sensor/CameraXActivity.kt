package com.seanlooong.exerciseandroid.ui.sensor

import android.os.Bundle
import com.seanlooong.exerciseandroid.databinding.ActivityCameraBinding
import com.seanlooong.exerciseandroid.ui.base.ImmersiveActivity

class CameraXActivity : ImmersiveActivity() {

    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater);
        setContentView(binding.root)
    }
}