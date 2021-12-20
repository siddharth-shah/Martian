package com.example.martian.photos.model

import com.google.gson.annotations.SerializedName

data class Photo(
    val id: Long,
    val sol: Int,
    @SerializedName("img_src") val imageSrc: String,
    @SerializedName("earth_date") val earthDate: String,
    val camera: Camera,
    val rover: Rover
)

data class Rover(
    val id: Long,
    val name: String,
    @SerializedName("landing_date") val landingDate: String,
    @SerializedName("launch_date") val launchDate: String,
    val status: String
)

data class Camera(val id: Long, val name: String, @SerializedName("full_name") val fullName: String)

data class Photos(val photos: List<Photo>)
