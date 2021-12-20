package com.example.martian.photos.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.martian.R
import com.example.martian.databinding.FragmentMarsPhotoListBinding
import com.example.martian.databinding.PhotoListItemBinding
import com.example.martian.photos.PhotoListViewModel
import com.example.martian.photos.model.Photo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarsPhotoListFragment : Fragment(), PhotoItemClickListener {

    private val photoListViewModel: PhotoListViewModel by activityViewModels()
    private lateinit var binding: FragmentMarsPhotoListBinding
    private var photosAdapter: PhotosAdapter? = null
    private val currentRover = "curiosity"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarsPhotoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.photosList.layoutManager = LinearLayoutManager(activity!!)
        photosAdapter = PhotosAdapter(this)
        binding.photosList.adapter = photosAdapter

        photoListViewModel.getPhotosByRoverName(currentRover)
        photoListViewModel.getLoading().observe(this) { loading ->
            showLoading(loading)
        }
        photoListViewModel.getError().observe(this) { error ->
            showError(error)
        }
        photoListViewModel.getPhotos().observe(this) { photos ->
            showData(photos)

        }

        binding.errorState.retryButton.setOnClickListener {
            photoListViewModel.getPhotosByRoverName(currentRover)
        }
    }

    private fun showData(photos: List<Photo>?) {
        photos?.let {
            photosAdapter?.addPhotos(photos)
        }
    }

    private fun showError(error: Boolean) {
        binding.errorState.root.visibility = if (error) View.VISIBLE else View.GONE

    }

    private fun showLoading(loading: Boolean) {
        binding.loadingState.root.visibility = if (loading) View.VISIBLE else View.GONE
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            MarsPhotoListFragment().apply {
            }
    }

    class PhotosAdapter(private val photoItemClickListener: PhotoItemClickListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val photos = mutableListOf<Photo>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val binding =
                PhotoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            val photoListItemViewHolder = PhotoListItemViewHolder(binding)
            photoListItemViewHolder.setItemClickListener(photoItemClickListener)
            return photoListItemViewHolder
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is PhotoListItemViewHolder) {
                holder.bind(photos[position])
            }
        }

        override fun getItemCount(): Int {
            return photos.size
        }

        fun addPhotos(photos: List<Photo>) {
            this.photos.addAll(photos)
            notifyDataSetChanged()
        }

        inner class PhotoListItemViewHolder(private val binding: PhotoListItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            private lateinit var photoItemClickListener: PhotoItemClickListener

            init {
                binding.root.setOnClickListener {
                    photoItemClickListener.onPhotoItemClicked(adapterPosition)
                }
            }

            fun bind(photo: Photo) {
                binding.roverName.text = "Rover: ${photo.rover.name}"
                binding.cameraName.text = "Camera: ${photo.camera.fullName}"
                binding.photoDate.text = "Date: ${photo.earthDate}"
                val dimen = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    300f,
                    binding.root.resources.displayMetrics
                )
                Glide.with(binding.root).load(photo.imageSrc)
                    .override(dimen.toInt(), dimen.toInt())
                    .fitCenter()
                    .into(binding.roverPhoto)

            }

            fun setItemClickListener(photoItemClickListener: PhotoItemClickListener) {
                this.photoItemClickListener = photoItemClickListener
            }
        }


    }

    override fun onPhotoItemClicked(position: Int) {
        val photoDetailFragment = PhotoDetailFragment.newInstance(position)
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(R.id.fragment_container, photoDetailFragment)
            ?.addToBackStack(null)
            ?.commit()
    }
}

interface PhotoItemClickListener {
    fun onPhotoItemClicked(position: Int)
}
