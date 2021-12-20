package com.example.martian.photos

import com.example.martian.photos.model.Photo

interface PhotosListUseCase {
    interface Listener<D, E> {
        fun onSuccess(photos: D)
        fun onFailure(error: E)
    }

    fun getPhotosByRoverName(roverName: String, callback: Listener<List<Photo>, String>)
}