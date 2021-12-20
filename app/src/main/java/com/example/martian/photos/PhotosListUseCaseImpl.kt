package com.example.martian.photos

import com.example.martian.common.NasaApi
import com.example.martian.photos.model.Photo
import com.example.martian.photos.model.Photos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PhotosListUseCaseImpl @Inject constructor(
    private val nasaApi: NasaApi
) : PhotosListUseCase {
    override fun getPhotosByRoverName(
        roverName: String,
        callback: PhotosListUseCase.Listener<List<Photo>, String>
    ) {
        nasaApi.getPhotosByRoverName(roverName).enqueue(wrapCallback(callback))
    }

    private fun wrapCallback(callback: PhotosListUseCase.Listener<List<Photo>, String>) =
        object : Callback<Photos> {
            override fun onResponse(call: Call<Photos>, response: Response<Photos>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it.photos)
                    }
                } else {
                    callback.onFailure("Something went wrong")
                }
            }

            override fun onFailure(call: Call<Photos>, t: Throwable) {
                callback.onFailure(t.message ?: "Something went wrong")
            }

        }

}
