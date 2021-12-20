package com.example.martian.photos.ui

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.martian.R
import com.example.martian.databinding.FragmentPhotoDetailBinding
import com.example.martian.photos.PhotoListViewModel


class PhotoDetailFragment : Fragment() {

    private val photoListViewModel: PhotoListViewModel by activityViewModels()
    private var position = -1
    private lateinit var binding: FragmentPhotoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt("position", -1) ?: -1

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoListViewModel.getPhotos().observe(this) { photos ->
            if (position != -1) {
                val currentPhoto = photos[position]
                val applyDimension = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    300f,
                    binding.root.resources.displayMetrics
                )
                Glide.with(this).load(currentPhoto.imageSrc).override(0, applyDimension.toInt())
                    .into(binding.imageCaptured)

                binding.roverName.text =
                    "This image was captured by rover ${currentPhoto.rover.name}"
                binding.launchDate.text =
                    "Launched on: ${currentPhoto.rover.launchDate}"
                binding.landingDate.text = "Landed on: ${currentPhoto.rover.landingDate}"
                binding.currentStatus.text = "Current Status: ${currentPhoto.rover.status}"

            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            PhotoDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
            }
    }
}