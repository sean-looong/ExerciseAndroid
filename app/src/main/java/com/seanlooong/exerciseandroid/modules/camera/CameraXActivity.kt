package com.seanlooong.exerciseandroid.modules.camera

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.seanlooong.exerciseandroid.databinding.ActivityCameraBinding
import com.seanlooong.exerciseandroid.base.ImmersiveActivity

const val KEY_EVENT_ACTION = "key_event_action"
const val KEY_EVENT_EXTRA = "key_event_extra"
private const val IMMERSIVE_FLAG_TIMEOUT = 500L

class CameraXActivity : ImmersiveActivity() {

    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater);
        setContentView(binding.root)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when(keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA,  keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            } else -> {
                super.onKeyDown(keyCode, event)
            }
        }
    }
}