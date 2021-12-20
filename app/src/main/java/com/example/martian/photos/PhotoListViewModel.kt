package com.example.martian.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.martian.photos.model.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val useCase: PhotosListUseCase
) : ViewModel() {

    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Boolean>()
    private val photosLiveData = MutableLiveData<List<Photo>>()

    fun getLoading(): LiveData<Boolean> = loadingLiveData
    fun getError(): LiveData<Boolean> = errorLiveData
    fun getPhotos(): LiveData<List<Photo>> = photosLiveData


    fun getPhotosByRoverName(roverName: String) {
        loadingLiveData.value = true
        errorLiveData.value = false
        useCase.getPhotosByRoverName(roverName, object :
            PhotosListUseCase.Listener<List<Photo>, String> {
            override fun onSuccess(photos: List<Photo>) {
                errorLiveData.value = false
                loadingLiveData.value = false
                photosLiveData.value = photos
            }

            override fun onFailure(error: String) {
                errorLiveData.value = true
                loadingLiveData.value = false
            }

        })

    }

}


sealed class PhotoListViewState {
    object Loading : PhotoListViewState()
    data class Error(val throwable: Throwable) : PhotoListViewState()
    data class Success(val data: Any) : PhotoListViewState()
}

