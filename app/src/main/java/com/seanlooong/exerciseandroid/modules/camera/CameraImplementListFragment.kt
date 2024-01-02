package com.seanlooong.exerciseandroid.modules.camera

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.seanlooong.exerciseandroid.databinding.FragmentCameraImplementListBinding

class CameraImplementListFragment : Fragment() {

    private var _binding: FragmentCameraImplementListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraImplementListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 跳转到CameraXActivity
        _binding?.cameraxBasic?.setOnClickListener{
            requireActivity().startActivity(
                    Intent(
                        requireActivity(),
                        CameraXActivity::class.java
                    )
                )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}