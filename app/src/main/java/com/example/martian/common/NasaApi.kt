package com.example.martian.common

import com.example.martian.photos.model.Photo
import com.example.martian.photos.model.Photos
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NasaApi {
    @GET("v1/rovers/{roverName}/photos?sol=1&api_key=DEMO_KEY")
    fun getPhotosByRoverName(@Path("roverName") roverName: String): Call<Photos>

}