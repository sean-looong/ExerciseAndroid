package com.seanlooong.exerciseandroid.modules.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.seanlooong.exerciseandroid.R
import com.seanlooong.exerciseandroid.utils.MediaStoreFile
import java.io.File

class PhotoFragment internal constructor() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ImageView(context)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments ?: return
        val resource = args.getString(FILE_NAME_KEY)?.let { File(it) } ?: R.drawable.ic_photo
        Glide.with(view).load(resource).into(view as ImageView)
    }

    companion object {
        private const val FILE_NAME_KEY = "file_name"

        fun create(mediaStoreFile: MediaStoreFile) = PhotoFragment().apply {
            val image = mediaStoreFile.file
            arguments = Bundle().apply {
                putString(FILE_NAME_KEY, image.absolutePath)
            }
        }
    }
}